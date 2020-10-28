package com.example.krazynews.adapter;


import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.krazynews.CommentItem;
import com.example.krazynews.CustomLinearLayoutManager;
import com.example.krazynews.NewsImage;
import com.example.krazynews.NewsLink;
import com.example.krazynews.R;
import com.example.krazynews.SliderItem;
import com.example.krazynews.signin_siginup.RegistrationName;
import com.example.krazynews.signin_siginup.SignIn;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//  <<,
public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    public List<CommentItem> commentItems;
    public CommentsAdapter commentsAdapter;
    //variable declaration for poll card
    private boolean pollFlag;
    private boolean firstClick=false,secondClick=false,thirdClick=false;
    private LinearLayout first, second, third;
    private ConstraintLayout firstClickColour,secondClickColour,thirdClickColour;
    private TextView firstOptionPercentage, secondOptionPercentage, thirdOptionPercentage, submit;
    private TextView firstOption, secondOption, thirdOption, pollQuestion;
    private Dialog myDialog;

    // newsLink
    private ArrayList<String> newsImages = new ArrayList<>();
    private ArrayList<String> newsUrls = new ArrayList<>();

    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;
    private Context c, popContext;
    private String flagPoll;
    private String URL_ADD = "https://www.krazyfox.in/krazynews/api/addviews.php";
    private String URL_POLL = "https://www.krazyfox.in/krazynews/api/poll.php";
    private String URL_BOOKMARK = "https://www.krazyfox.in/krazynews/api/bookmark.php";
    private String URL_LIKE = "https://www.krazyfox.in/krazynews/api/like.php";
    private String URL_FETCH_COMMENT = "https://www.krazyfox.in/krazynews/api/fetchcomment.php";
    private String URL_SEND_COMMENT = "https://www.krazyfox.in/krazynews/api/comment.php";
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
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SliderViewHolder holder, final int position) {
        id = sliderItems.get(position).getId();
        if(sliderItems.get(position).getIsAdd().equals("1"))
        {
            holder.setAdd(sliderItems.get(position));
            holder.addImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(c.getApplicationContext(), NewsLink.class);
                    intent.putExtra("newsUrl",sliderItems.get(position).getAddUrl());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    c.getApplicationContext().startActivity(intent);
                    incrementAddView(sliderItems.get(position).getAddID(),sliderItems.get(position).getAddValue());
                }
            });
        }else{
            holder.setNews();
            holder.setImageView(sliderItems.get(position));
            holder.setTextView(sliderItems.get(position));
            holder.setTitleView(sliderItems.get(position));
            holder.setNews_byView(sliderItems.get(position));
            holder.setNewsTimeView(sliderItems.get(position));
            Log.d("bookmarkStatus", sliderItems.get(position).getBookmark());
            if(sliderItems.get(position).getBookmark().equals("1")) {
                holder.bookmark.setLiked(true);
            }else{
                holder.bookmark.setLiked(false);
            }

            if(sliderItems.get(position).getLike().equals("1")) {
                holder.likeButton.setLiked(true);
            }else{
                holder.likeButton.setLiked(false);
            }

            if(sliderItems.get(position).getPollQuestion().equals(""))
            {
                holder.poll.setVisibility(View.GONE);
            }
            else
            {
                if(isLoggedin())
                {

                }
                if(sliderItems.get(position).getPoll().equals("1")) {
                    holder.poll.setVisibility(View.GONE);
                }else{
                    holder.poll.setVisibility(View.VISIBLE);
                }
                holder.poll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(isLoggedin())
                        {
                            preferences = c.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
                            email = preferences.getString("UserEmail","");
                            ShowPoll(holder, position,email);
                        }
                        else {
                            loginAlert();
                        }
                    }
                });
            }

            if(sliderItems.size()-1 == position) {
                holder.shimmer.setVisibility(View.VISIBLE);
            }
            else {
                holder.shimmer.setVisibility(View.GONE);
            }

            holder.newsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newsImages = sliderItems.get(position).getNewsImages();
                    newsUrls = sliderItems.get(position).getNewsUrls();
                    ShowNewsPopUp();
                }
            });

            holder.bookmark.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton bookmark) {

                    if(isLoggedin())
                    {
                        preferences = c.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
                        id = sliderItems.get(position).getId();
                        email = preferences.getString("UserEmail","");
                        url = sliderItems.get(position).getNewsUrls().get(0);
                        image = sliderItems.get(position).getImage();
                        title = sliderItems.get(position).getTitle();
                        flag = "1";
                        bookmarkNews();
                    }
                    else {
                        holder.bookmark.setLiked(false);
                        loginAlert();
                    }
                }

                @Override
                public void unLiked(LikeButton bookmark) {
                    preferences = c.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
                    id = sliderItems.get(position).getId();
                    email = preferences.getString("UserEmail","");
                    url = sliderItems.get(position).getNewsUrls().get(0);
                    image = sliderItems.get(position).getImage();
                    title = sliderItems.get(position).getTitle();
                    flag="2";
                    bookmarkNews();
                }
            });

            holder.likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    if(isLoggedin())
                    {
                        preferences = c.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
                        email = preferences.getString("UserEmail","");
                        String id = sliderItems.get(position).getId();
                        like(id,"1",email);
                    }
                    else {
                        holder.likeButton.setLiked(false);
                        loginAlert();
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    preferences = c.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
                    email = preferences.getString("UserEmail","");
                    String id = sliderItems.get(position).getId();
                    like(id,"2",email);
                }
            });

            holder.shareView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra(Intent.EXTRA_TEXT, sliderItems.get(position).getTitle() + " (@ Krazyfox.in ) \n");
                    intent.setType("text/plain");
                    c.startActivity(Intent.createChooser(intent, "Send To"));
                }
            });

            holder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isLoggedin())
                    {
                        preferences = c.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
                        id = sliderItems.get(position).getId();
                        email = preferences.getString("UserEmail","");

//                        fetchComments(id,email);

                        Transition transition = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            transition = new Slide((Gravity.BOTTOM));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            transition.setDuration(450);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            transition.addTarget(R.id.random);
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition((ViewGroup) holder.random.getParent(), transition);
                        }
                        holder.random.setVisibility(View.VISIBLE);
                        String postId = sliderItems.get(position).getId();
                        loadComments(postId,email,holder.recyclerView,holder.noCommentFound,holder.commentsProgressBar);

                    }
                    else {
                        loginAlert();
                    }
                }
            });

            holder.hideComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Transition transition = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        transition = new Slide((Gravity.BOTTOM));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        transition.setDuration(450);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        transition.addTarget(R.id.random);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition((ViewGroup) holder.random.getParent(), transition);
                    }
                    holder.random.setVisibility(View.GONE);
                }
            });

            holder.sendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.commentInput.getText().toString().trim().isEmpty())
                    {
                        Toast.makeText(c,"Please comment something.", Toast.LENGTH_SHORT).show();
                    }else{
                        String postId = sliderItems.get(position).getId();
                        String name = preferences.getString("UserName","");
                        String email = preferences.getString("UserEmail","");
                        String comment = holder.commentInput.getText().toString().trim();
                        String action = "add";

                        sendComment(postId,name,email,comment,action);
                        holder.random.setVisibility(View.GONE);
                    }
                }
            });
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(holder.titleView,"news_title");
                    pairs[1] = new Pair<View, String>(holder.imageView,"news_image");
                    ActivityOptions options = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation((Activity)c,pairs);
                    }

                    Bitmap bmp = ((BitmapDrawable)holder.imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 10, stream);
                    byte[] byteArray = stream.toByteArray();
                    Intent intent = new Intent(c, NewsImage.class);
                    intent.putExtra("picture", byteArray);
                    intent.putExtra("text", sliderItems.get(position).getTitle());
                    c.startActivity(intent,options.toBundle());
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{

        //private CardView cardView;
        private ImageView imageView, shareView, newsView, addImage, comment, hideComment, sendComment;
        private TextView textView,titleView,news_byView,newsTimeView, noCommentFound;
        private View shimmer;
        private View random;
        private LikeButton bookmark, likeButton;
        private ConstraintLayout poll;
        private CardView addContainer;
        private LinearLayout newsContainer;
        private EditText commentInput;
        private RecyclerView recyclerView;
        private ProgressBar commentsProgressBar;
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
            newsView = itemView.findViewById(R.id.news);
            comment = itemView.findViewById(R.id.comment);
            hideComment = itemView.findViewById(R.id.hideComment);
            //add options
            addContainer = itemView.findViewById(R.id.add_container);
            newsContainer = itemView.findViewById(R.id.news_container);
            addImage = itemView.findViewById(R.id.add_image);

            random = itemView.findViewById(R.id.random);

            sendComment = itemView.findViewById(R.id.sendComment);
            commentInput = itemView.findViewById(R.id.commentInput);
            recyclerView = itemView.findViewById(R.id.commentsRecyclerview);
            noCommentFound = itemView.findViewById(R.id.noCommentsFound);
            commentsProgressBar = itemView.findViewById(R.id.commentsProgressbar);
        }

        void setAdd(SliderItem sliderItem){
            addContainer.setVisibility(View.VISIBLE);
            newsContainer.setVisibility(View.GONE);
            Glide.with(c).load(sliderItem.getAddImage()).into(addImage);
        }

        void setNews(){
            addContainer.setVisibility(View.GONE);
            newsContainer.setVisibility(View.VISIBLE);
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

    public void like(final String id, final String flag, final String email){
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
                params.put("email",email);
                return params;
            }
        };
//        Log.d("stringrequest", "["+stringRequest+"]");
        RequestQueue requestQueue = Volley.newRequestQueue(c);
//        Log.d("stringrequest", "["+requestQueue+"]");
        requestQueue.add(stringRequest);
    }

    public void ShowPoll(@NonNull final SliderViewHolder holder, final int position, final String email){
        myDialog = new Dialog(popContext);
        myDialog.setContentView(R.layout.poll_layout);
        myDialog.getWindow().setGravity(Gravity.CENTER);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFade;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pollFlag = false;
        id = sliderItems.get(position).getId();
        submit = myDialog.findViewById(R.id.submit);

        pollQuestion = myDialog.findViewById(R.id.pollQuestion);
        pollQuestion.setText(sliderItems.get(position).getPollQuestion());

        first = myDialog.findViewById(R.id.first);
        second = myDialog.findViewById(R.id.second);
        third = myDialog.findViewById(R.id.third);

        first.setBackgroundResource(R.drawable.poll_stroke);
        second.setBackgroundResource(R.drawable.poll_stroke);
        third.setBackgroundResource(R.drawable.poll_stroke);

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

                    firstOptionPercentage.setText(Integer.toString(yes)+"%");
                    yes=yes*100;
                    firstClickColour.getBackground().setLevel(yes);
                    firstOptionPercentage.setVisibility(View.VISIBLE);

                    secondOptionPercentage.setText(Integer.toString(no)+"%");
                    no=no*100;
                    secondClickColour.getBackground().setLevel(no);
                    secondOptionPercentage.setVisibility(View.VISIBLE);

                    thirdOptionPercentage.setText(Integer.toString(maybe)+"%");
                    maybe=maybe*100;
                    thirdClickColour.getBackground().setLevel(maybe);
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
//                    GradientDrawable drawable = (GradientDrawable)second.getBackground();
//                    drawable.setStroke(10, 0xFFFD676C);

                    int yes = Integer.parseInt(sliderItems.get(position).getYes());
                    int no = Integer.parseInt(sliderItems.get(position).getNo())+1;
                    int maybe = Integer.parseInt(sliderItems.get(position).getMaybe());
                    yes = ((100*yes)/(yes+no+maybe));
                    no = ((100*no)/(yes+no+maybe));
                    maybe = ((100*maybe)/(yes+no+maybe));

                    firstOptionPercentage.setText(Integer.toString(yes)+"%");
                    yes=yes*100;
                    firstClickColour.getBackground().setLevel(yes);
                    firstOptionPercentage.setVisibility(View.VISIBLE);

                    secondOptionPercentage.setText(Integer.toString(no)+"%");
                    no=no*100;
                    secondClickColour.getBackground().setLevel(no);
                    secondOptionPercentage.setVisibility(View.VISIBLE);

                    thirdOptionPercentage.setText(Integer.toString(maybe)+"%");
                    maybe=maybe*100;
                    thirdClickColour.getBackground().setLevel(maybe);
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
                    third.setBackgroundResource(R.drawable.poll_selected_stroke);
//                    GradientDrawable drawable = (GradientDrawable)second.getBackground();
//                    drawable.setStroke(10, 0xFFFD676C);

                    int yes = Integer.parseInt(sliderItems.get(position).getYes());
                    int no = Integer.parseInt(sliderItems.get(position).getNo());
                    int maybe = Integer.parseInt(sliderItems.get(position).getMaybe())+1;
                    yes = ((100*yes)/(yes+no+maybe));
                    no = ((100*no)/(yes+no+maybe));
                    maybe = ((100*maybe)/(yes+no+maybe));
                    firstOptionPercentage.setText(Integer.toString(yes)+"%");
                    yes=yes*100;
                    firstClickColour.getBackground().setLevel(yes);
                    firstOptionPercentage.setVisibility(View.VISIBLE);

                    secondOptionPercentage.setText(Integer.toString(no)+"%");
                    no=no*100;
                    secondClickColour.getBackground().setLevel(no);
                    secondOptionPercentage.setVisibility(View.VISIBLE);

                    thirdOptionPercentage.setText(Integer.toString(maybe)+"%");
                    maybe=maybe*100;
                    thirdClickColour.getBackground().setLevel(maybe);
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
                pollAnswer(email);
            }
        });
    }

    public void pollAnswer(final String email){
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
                params.put("email",email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);
    }

    public void ShowNewsPopUp(){

        myDialog = new Dialog(popContext);
        myDialog.setContentView(R.layout.activity_news_pop_up);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFade;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = myDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.START;

        LinearLayoutManager layoutManager = new LinearLayoutManager(c.getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = myDialog.findViewById(R.id.newsLinkRecycler);
        recyclerView.setLayoutManager(layoutManager);
        NewsImagesAdapter adapter= new NewsImagesAdapter(c.getApplicationContext(),newsImages,newsUrls);
        recyclerView.setAdapter(adapter);
        myDialog.show();
    }
    public boolean isLoggedin() {
        preferences = c.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
        String loggedIn = preferences.getString("Login","");
        if(loggedIn.equals("YES"))
        {
            return true;
        }
        else{
            return false;
        }
    }

    public void incrementAddView(final String id, final String value){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();
                            if(success.equals("true"))
                            {
//                                Toast.makeText(c,"incremented view", Toast.LENGTH_SHORT).show();

                            }
                            else{
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
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("value",value);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);
    }

    public void loginAlert(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Activity activity = (Activity)c;
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        c.startActivity(new Intent(c, SignIn.class));
                        activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        c.startActivity(new Intent(c, RegistrationName.class));
                        activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(Html.fromHtml("<font color='#FD676C'>Login OR SignUp</font>"));
        builder.setMessage(Html.fromHtml("Please Login to use <b>Comment</b>, <b>Bookmark</b>,"
                + " <b>Poll</b> and <b>Like</b> features of our app"))
                .setPositiveButton(Html.fromHtml("<font color='#FD676C'>Sign In</font>"), dialogClickListener)
                .setNegativeButton(Html.fromHtml("<font color='#FD676C'>Sign Up</font>"), dialogClickListener)
                .setNeutralButton(Html.fromHtml("<font color='#FD676C'>No, Thanks </font>"), dialogClickListener).show();
    }

    public void loadComments(final String postId, final String email, final RecyclerView recyclerView, final TextView noCommentsFound,
                             final ProgressBar progressBar){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FETCH_COMMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("nocommetnrespone", response);
                        if(response.equals("{\"error\":\"No record found\"}")) {
                            noCommentsFound.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }else{
                            try {
                                commentItems = new ArrayList<>();
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    CommentItem commentItem = new CommentItem();

                                    commentItem.setId(jsonObject.getString("id"));
                                    commentItem.setNewsid(jsonObject.getString("newsid"));
                                    commentItem.setUname(jsonObject.getString("uname"));
                                    commentItem.setEmail(jsonObject.getString("email"));
                                    commentItem.setComment(jsonObject.getString("comment"));
                                    commentItem.setClike(jsonObject.getString("clike"));
                                    commentItem.setCdislike(jsonObject.getString("cdislike"));
                                    commentItem.setStatus(jsonObject.getString("status"));
                                    commentItem.setUlike(jsonObject.getString("ulike"));
                                    commentItem.setUdislike(jsonObject.getString("udislike"));
                                    commentItem.setDelete(jsonObject.getString("delete"));

                                    commentItems.add(commentItem);
                                }

                                commentsAdapter = new CommentsAdapter(c,commentItems);
                                recyclerView.setAdapter(commentsAdapter);
                                recyclerView.setLayoutManager(new CustomLinearLayoutManager(c));
                                recyclerView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Log.i("funcommentsize", commentItems.get(0).getComment());
                                Log.i("funcommentsize", String.valueOf(commentItems.size()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("postid",postId);
                params.put("email",email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);
    }

    public void sendComment(final String postId, final String name, final String email, final String comment, final String action){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEND_COMMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagconvertstr", "["+response+"]");
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();
                            if(success.equals("1")) {
                                Toast.makeText(c,"Comment added succesfully",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(c,"Something went wrong!",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("postid",postId);
                params.put("name",name);
                params.put("email",email);
                params.put("comment",comment);
                params.put("commentid","0");
                params.put("action",action);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);
    }
}
