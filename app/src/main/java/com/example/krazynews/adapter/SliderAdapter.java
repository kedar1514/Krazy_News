package com.example.krazynews.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    public SliderAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
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
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImageView(sliderItems.get(position));
        holder.setTextView(sliderItems.get(position));
        holder.setTitleView(sliderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{

        //private CardView cardView;
        private ImageView imageView;
        private TextView textView;
        private TextView titleView;
        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            //cardView = itemView.findViewById(R.id.cardView);
            imageView = itemView.findViewById(R.id.news_image);
            textView = itemView.findViewById(R.id.news_text);
            titleView = itemView.findViewById(R.id.news_title);
        }

        void setImageView(SliderItem sliderItem){
            imageView.setImageResource(sliderItem.getImage());
        }

        void setTextView(SliderItem sliderItem){
            textView.setText(sliderItem.getText());
        }

        void setTitleView(SliderItem sliderItem){
            titleView.setText(sliderItem.getTitle());
        }

//        void setCardView(SliderItem sliderItem){
//            cardView.set
//        }

    }
}
