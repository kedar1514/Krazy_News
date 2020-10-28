package com.example.krazynews.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.krazynews.CommentItem;
import com.example.krazynews.CustomLinearLayoutManager;
import com.example.krazynews.R;
import com.example.krazynews.signin_siginup.ChooseTopicSignIn;
import com.example.krazynews.signin_siginup.SignIn;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsHolder> {
    String URL_LIKE_DISLIKE = "https://www.krazyfox.in/krazynews/api/likedislike.php";
    LayoutInflater inflater;
    List<CommentItem> commentItems;
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    public CommentsAdapter(Context context, List<CommentItem> commentItems) {
        this.inflater = LayoutInflater.from(context);
        this.commentItems = commentItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.comment_container,parent,false);
        Log.i("holderSize", String.valueOf(commentItems.size()));
        return new CommentsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentsHolder holder, final int position) {
        preferences = context.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
        final String email = preferences.getString("UserEmail","");
        holder.userName.setText(commentItems.get(position).getUname());
        holder.comment.setText(commentItems.get(position).getComment());
        holder.cLike.setText(commentItems.get(position).getClike());
        holder.cDislike.setText(commentItems.get(position).getCdislike());

        final CommentsHolder temp = holder;
        if(commentItems.get(position).getUlike().equals("1"))
        {
            holder.likeButton.setLiked(true);
        }else if(commentItems.get(position).getUdislike().equals("1"))
        {
            holder.dislikeButton.setLiked(true);
        }
        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            int lk = Integer.parseInt(commentItems.get(position).getClike().trim());
            int dlk = Integer.parseInt(commentItems.get(position).getCdislike().trim());
            @Override
            public void liked(LikeButton likeButton) {
                if(commentItems.get(position).getUdislike().equals("1"))
                {
                    likeDislike("1",commentItems.get(position).getId().trim().toString(),email);
                    likeDislike("4",commentItems.get(position).getId().trim().toString(),email);
                    holder.cLike.setText(String.valueOf(lk+1).toString());
                    holder.cDislike.setText(String.valueOf(dlk-1).toString());
                }
                else{
                    likeDislike("1",commentItems.get(position).getId().trim().toString(),email);
                    holder.cLike.setText(String.valueOf(lk+1).toString());
                    holder.cDislike.setText(String.valueOf(dlk).toString());
                }
                holder.dislikeButton.setLiked(false);
                holder.likeButton.setLiked(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeDislike("2",commentItems.get(position).getId().trim().toString(),email);
                holder.cLike.setText(String.valueOf(lk).toString());
            }
        });

        holder.dislikeButton.setOnLikeListener(new OnLikeListener() {
            int lk = Integer.parseInt(commentItems.get(position).getClike().trim());
            int dlk = Integer.parseInt(commentItems.get(position).getCdislike().trim());
            @Override
            public void liked(LikeButton disLikeButton) {
                if(commentItems.get(position).getUlike().equals("1"))
                {
                    likeDislike("3",commentItems.get(position).getId().trim().toString(),email);
                    likeDislike("2",commentItems.get(position).getId().trim().toString(),email);
                    holder.likeButton.setLiked(false);
                    holder.cLike.setText(String.valueOf(lk-1).toString());
                    holder.cDislike.setText(String.valueOf(dlk+1).toString());
                }else{
                    likeDislike("3",commentItems.get(position).getId().trim().toString(),email);
                    holder.cLike.setText(String.valueOf(lk).toString());
                    holder.cDislike.setText(String.valueOf(dlk+1).toString());
                }
                holder.dislikeButton.setLiked(true);
                holder.likeButton.setLiked(false);
            }

            @Override
            public void unLiked(LikeButton disLikeButton) {
                likeDislike("4",commentItems.get(position).getId().trim().toString(),email);
                holder.cDislike.setText(String.valueOf(dlk).toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }

    class CommentsHolder extends RecyclerView.ViewHolder{
        private TextView userName, comment, cLike, cDislike;
        private LikeButton likeButton, dislikeButton;
        public CommentsHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            comment = itemView.findViewById(R.id.userComment);
            cLike = itemView.findViewById(R.id.like_count);
            cDislike = itemView.findViewById(R.id.dislike_count);
            likeButton = itemView.findViewById(R.id.like_comment);
            dislikeButton = itemView.findViewById(R.id.dislike_comment);
        }
    }

    public void likeDislike(final String like, final String commentid, final String email){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LIKE_DISLIKE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("likeresult",response.toString());
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();

                            if(success.equals("1"))
                            {
                                Toast.makeText(context,"Success", Toast.LENGTH_SHORT).show();
                            }
                            else if(success.equals("0")){
//                                Toast.makeText(context,"Something went wrong", Toast.LENGTH_SHORT).show();
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
                params.put("like",like);
                params.put("commentid",commentid);
                params.put("email",email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
