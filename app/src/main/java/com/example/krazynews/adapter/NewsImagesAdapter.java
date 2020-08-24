package com.example.krazynews.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.krazynews.MainActivity;
import com.example.krazynews.NewsLink;
import com.example.krazynews.R;
import com.example.krazynews.SliderItem;
import com.like.LikeButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class NewsImagesAdapter extends RecyclerView.Adapter<NewsImagesAdapter.NewsViewHolder> {
    private Context c;
    private String newsImage;
    private ArrayList<String> newsImages = new ArrayList<>();
    private ArrayList<String> newsUrls = new ArrayList<>();

    public NewsImagesAdapter(Context c,ArrayList<String> newsImages,ArrayList<String> newsUrls) {
        this.c = c;
        this.newsImages = newsImages;
        this.newsUrls = newsUrls;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_images, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, final int position) {
        holder.setImageView(newsImages.get(position));
        holder.newsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c.getApplicationContext(), NewsLink.class);
//                Log.d("newsUrl", "onClickAdapter: "+newsUrls.get(position));
                intent.putExtra("newsUrl",newsUrls.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                c.getApplicationContext().startActivity(intent);
//                Activity activity = (Activity) c;
//                activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsImages.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        private ImageView newsImage;
        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.newsPublisherImage);
        }

        void setImageView(String url){
            Glide.with(c).load(url).into(newsImage);
        }
    }
}
