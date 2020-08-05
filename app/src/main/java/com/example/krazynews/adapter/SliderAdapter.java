package com.example.krazynews.adapter;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.krazynews.MainActivity;
import com.example.krazynews.Profile;
import com.example.krazynews.R;
import com.example.krazynews.SliderItem;
import com.example.krazynews.signin_siginup.ChooseTopicSignIn;
import com.example.krazynews.signin_siginup.SignIn;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

//  <<,
public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    //variable declaration for poll card
    private boolean pollFlag;
    private boolean firstClick=false,secondClick=false,thirdClick=false;
    private LinearLayout first, second, third;
    private ConstraintLayout firstClickColour,secondClickColour,thirdClickColour;
    private TextView firstOptionPercentage, secondOptionPercentage, thirdOptionPercentage, submit;
    private TextView firstOption, secondOption, thirdOption, pollQuestion;
    private Dialog myDialog;

    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;
    private Context c, popContext;
    private String flagPoll;
    private String URL_POLL = "https://www.krazyfox.in/krazynews/api/poll.php";
    private String URL_BOOKMARK = "https://www.krazyfox.in/krazynews/api/bookmark.php";
    private String URL_LIKE = "https://www.krazyfox.in/krazynews/api/like.php";
    private String id, email, url, image, title, flag;
    private SharedPreferences preferences;
    public SliderAdapter(Context c,List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
        this.c = c;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        popContext = parent.getContext();
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final SliderViewHolder holder, final int position) {

        id = sliderItems.get(position).getId();

        holder.setImageView(sliderItems.get(position));
        holder.setTextView(sliderItems.get(position));
        holder.setTitleView(sliderItems.get(position));
        holder.setNews_byView(sliderItems.get(position));
        holder.setNewsTimeView(sliderItems.get(position));
        if(sliderItems.get(position).getPollQuestion().equals(""))
        {
            holder.poll.setVisibility(View.GONE);
        }
        else
        {
            holder.poll.setVisibility(View.VISIBLE);
            holder.poll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowPoll(holder, position);
                }
            });
        }

        if(sliderItems.size()-1 == position) {
            holder.shimmer.setVisibility(View.VISIBLE);
        }
        else {
            holder.shimmer.setVisibility(View.GONE);
        }

        holder.bookmark.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton bookmark) {
                preferences = c.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
                id = sliderItems.get(position).getId();
                email = preferences.getString("UserEmail","");
                url = "http/dkjhssjhfsd";
                image = sliderItems.get(position).getImage();
                title = sliderItems.get(position).getTitle();
                flag = "1";
                bookmarkNews();
            }

            @Override
            public void unLiked(LikeButton bookmark) {
                preferences = c.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
                id = sliderItems.get(position).getId();
                email = preferences.getString("UserEmail","");
                url = "http/dkjhssjhfsd";
                image = sliderItems.get(position).getImage();
                title = sliderItems.get(position).getTitle();
                flag="2";
                bookmarkNews();
            }
        });

        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                String id = sliderItems.get(position).getId();
                like(id,"1");
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                String id = sliderItems.get(position).getId();
                like(id,"2");
            }
        });

        holder.shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_TEXT, sliderItems.get(holder.getAdapterPosition()).getTitle() + " (@ Krazyfox.in ) \n" + sliderItems.get(holder.getAdapterPosition()).getNews_link());
                intent.setType("text/plain");
                c.startActivity(Intent.createChooser(intent, "Send To"));
            }
        });
    }
    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{

        //private CardView cardView;
        private ImageView imageView, shareView;
        private TextView textView,titleView,news_byView,newsTimeView;
        private View shimmer;
        private LikeButton bookmark, likeButton;
        private ConstraintLayout poll;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            //cardView = itemView.findViewById(R.id.cardView);
            imageView = itemView.findViewById(R.id.news_image);
            textView = itemView.findViewById(R.id.news_text);
            titleView = itemView.findViewById(R.id.news_title);
            news_byView = itemView.findViewById(R.id.news_by);
            newsTimeView = itemView.findViewById(R.id.news_time);
            shareView = itemView.findViewById(R.id.share);
            shimmer = (View) itemView.findViewById(R.id.shimmer_loading);
            bookmark = itemView.findViewById(R.id.bookmark);
            likeButton = itemView.findViewById(R.id.like_button);
            poll = itemView.findViewById(R.id.poll);
        }

        void setImageView(SliderItem sliderItem){
            Glide.with(c).load(sliderItem.getImage()).into(imageView);
        }

        void setTextView(SliderItem sliderItem){
            textView.setText(sliderItem.getText());
        }

        void setTitleView(SliderItem sliderItem){
            titleView.setText(sliderItem.getTitle());
        }

        void setNews_byView(SliderItem sliderItem){
            news_byView.setText(sliderItem.getNews_by());
        }

        void setNewsTimeView(SliderItem sliderItem) { newsTimeView.setText(sliderItem.getNews_time());}
    }

    public void bookmarkNews(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BOOKMARK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();
                            if(success.equals("1"))
                            {
                                Toast.makeText(c,"Bookmark", Toast.LENGTH_SHORT).show();
                            }
                            else if(success.equals("0")){
                                Toast.makeText(c,"Bookmark Error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(c,"Bookmark Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(c, "Bookmark Error!" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id.trim().toString());
                params.put("email",email.trim().toString());
                params.put("url",url.trim().toString());
                params.put("title",title.trim().toString());
                params.put("image",image.trim().toString());
                params.put("status",flag.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);
        Log.i("flagResponse", "["+requestQueue+"]");
    }

    public void like(final String id, final String flag){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LIKE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();
                            if(success.equals("1")){
                                Toast.makeText(c,"Liked", Toast.LENGTH_SHORT).show();
                            }
                            else if(success.equals("0")){
                                Toast.makeText(c,"Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(c,"Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(c, "Error!" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("flag",flag);
                return params;
            }
        };
//        Log.d("stringrequest", "["+stringRequest+"]");
        RequestQueue requestQueue = Volley.newRequestQueue(c);
//        Log.d("stringrequest", "["+requestQueue+"]");
        requestQueue.add(stringRequest);
    }

    public void ShowPoll(@NonNull final SliderViewHolder holder, final int position){
        myDialog = new Dialog(popContext);
        myDialog.setContentView(R.layout.poll_layout);
        myDialog.getWindow().setGravity(Gravity.CENTER);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFade;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pollFlag = false;

        submit = myDialog.findViewById(R.id.submit);

        pollQuestion = myDialog.findViewById(R.id.pollQuestion);
        pollQuestion.setText(sliderItems.get(position).getPollQuestion());

        first = myDialog.findViewById(R.id.first);
        second = myDialog.findViewById(R.id.second);
        third = myDialog.findViewById(R.id.third);

        firstOption = myDialog.findViewById(R.id.firstOption);
        secondOption = myDialog.findViewById(R.id.secondOption);
        thirdOption = myDialog.findViewById(R.id.thirdOption);

        firstClickColour = myDialog.findViewById(R.id.firstClickColour);
        secondClickColour = myDialog.findViewById(R.id.secondClickColour);
        thirdClickColour = myDialog.findViewById(R.id.thirdClickColour);

        firstOptionPercentage = myDialog.findViewById(R.id.firstOptionPercentage);
        secondOptionPercentage = myDialog.findViewById(R.id.secondOptionPercentage);
        thirdOptionPercentage = myDialog.findViewById(R.id.thirdOptionPercentage);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pollFlag){
                    first.setBackgroundResource(R.drawable.poll_selected_stroke);
                    pollFlag = true;
                    int yes = Integer.parseInt(sliderItems.get(position).getYes());
                    int no = Integer.parseInt(sliderItems.get(position).getNo())+1;
                    int maybe = Integer.parseInt(sliderItems.get(position).getMaybe());
                    yes = ((100*yes)/(yes+no+maybe));
                    no = ((100*no)/(yes+no+maybe));
                    maybe = ((100*maybe)/(yes+no+maybe));

                    firstClickColour.getBackground().setLevel(yes);
                    yes=100*100;
                    firstOptionPercentage.setText(Integer.toString(yes)+"%");
                    firstOptionPercentage.setVisibility(View.VISIBLE);

                    secondClickColour.getBackground().setLevel(no);
                    no=100*100;
                    secondOptionPercentage.setText(Integer.toString(no)+"%");
                    secondOptionPercentage.setVisibility(View.VISIBLE);

                    thirdClickColour.getBackground().setLevel(maybe);
                    maybe=100*100;
                    thirdOptionPercentage.setText(Integer.toString(maybe)+"%");
                    thirdOptionPercentage.setVisibility(View.VISIBLE);

                    flagPoll = "1";
                }
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pollFlag){
                    pollFlag = true;
                    second.setBackgroundResource(R.drawable.poll_selected_stroke);
                    GradientDrawable drawable = (GradientDrawable)second.getBackground();
                    drawable.setStroke(10, 0xFFFD676C);

                    int yes = Integer.parseInt(sliderItems.get(position).getYes());
                    int no = Integer.parseInt(sliderItems.get(position).getNo())+1;
                    int maybe = Integer.parseInt(sliderItems.get(position).getMaybe());
                    yes = ((100*yes)/(yes+no+maybe));
                    no = ((100*no)/(yes+no+maybe));
                    maybe = ((100*maybe)/(yes+no+maybe));

                    firstClickColour.getBackground().setLevel(yes);
                    yes=100*100;
                    firstOptionPercentage.setText(Integer.toString(yes)+"%");
                    firstOptionPercentage.setVisibility(View.VISIBLE);

                    secondClickColour.getBackground().setLevel(no);
                    no=100*100;
                    secondOptionPercentage.setText(Integer.toString(no)+"%");
                    secondOptionPercentage.setVisibility(View.VISIBLE);

                    thirdClickColour.getBackground().setLevel(maybe);
                    maybe=100*100;
                    thirdOptionPercentage.setText(Integer.toString(maybe)+"%");
                    thirdOptionPercentage.setVisibility(View.VISIBLE);

                    flagPoll = "2";
                }
            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pollFlag){
                    pollFlag = true;
                    int yes = Integer.parseInt(sliderItems.get(position).getYes());
                    int no = Integer.parseInt(sliderItems.get(position).getNo());
                    int maybe = Integer.parseInt(sliderItems.get(position).getMaybe())+1;
                    yes = ((100*yes)/(yes+no+maybe));
                    no = ((100*no)/(yes+no+maybe));
                    maybe = ((100*maybe)/(yes+no+maybe));

                    firstClickColour.getBackground().setLevel(yes);
                    yes=100*100;
                    firstOptionPercentage.setText(Integer.toString(yes)+"%");
                    firstOptionPercentage.setVisibility(View.VISIBLE);

                    secondClickColour.getBackground().setLevel(no);
                    no=100*100;
                    secondOptionPercentage.setText(Integer.toString(no)+"%");
                    secondOptionPercentage.setVisibility(View.VISIBLE);

                    thirdClickColour.getBackground().setLevel(maybe);
                    maybe=100*100;
                    thirdOptionPercentage.setText(Integer.toString(maybe)+"%");
                    thirdOptionPercentage.setVisibility(View.VISIBLE);

                    flagPoll = "3";
                }
            }
        });

        myDialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.hide();
                holder.poll.setVisibility(View.GONE);
                pollAnswer();
            }
        });
    }

    public void pollAnswer(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POLL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();
                            if(success.equals("1"))
                            {
                                Toast.makeText(c,"Deleted", Toast.LENGTH_SHORT).show();

                            }
                            else if(success.equals("0")){
                                Toast.makeText(c,"Something Went Wrong!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(c,"Something Went Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(c, "Something Went Wrong!" + error.toString(), Toast.LENGTH_LONG).show();
                        Log.i("flagResponse", "["+error.toString()+"]");
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("flag",flagPoll);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);
    }
}
