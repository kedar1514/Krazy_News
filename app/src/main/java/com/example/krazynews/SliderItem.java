package com.example.krazynews;

import android.media.Image;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

public class SliderItem {
    private String image,text,title,news_by,news_time, news_link;
    private ImageView share;

    public SliderItem() {}

    public SliderItem(String image, String text, String title, String news_time, String news_link)
    {
        this.image = image;
        this.text = text;
        this.title = title;
        this.news_time = news_time;
        this.news_link = news_link;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getNews_by() {
        return news_by;
    }
    public void setNews_by(String news_by) {
        this.news_by = news_by;
    }

    public void setNews_time(String news_time) { this.news_time = news_time; }
    public String getNews_time() { return news_time; }

    public void setNews_link(String news_link) { this.news_link = news_link; }
    public String getNews_link() { return news_link; }
}
