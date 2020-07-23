package com.example.krazynews.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.krazynews.MainActivity;
import com.example.krazynews.R;
import com.example.krazynews.SliderItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

//  <<,
public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;
    private Context c;

    public SliderAdapter(Context c,List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
        this.c = c;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final SliderViewHolder holder, int position) {
        holder.setImageView(sliderItems.get(position));
        holder.setTextView(sliderItems.get(position));
        holder.setTitleView(sliderItems.get(position));
        holder.setNews_byView(sliderItems.get(position));
        holder.setNewsTimeView(sliderItems.get(position));
//        final String title = sliderItems.get(position).getTitle();
//        final String newsUrl = sliderItems.get(position).getNews_link();
        holder.shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_SUBJECT, sliderItems.get(holder.getAdapterPosition()).getTitle());
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

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            //cardView = itemView.findViewById(R.id.cardView);
            imageView = itemView.findViewById(R.id.news_image);
            textView = itemView.findViewById(R.id.news_text);
            titleView = itemView.findViewById(R.id.news_title);
            news_byView = itemView.findViewById(R.id.news_by);
            newsTimeView = itemView.findViewById(R.id.news_time);
            shareView = itemView.findViewById(R.id.share);
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
}
