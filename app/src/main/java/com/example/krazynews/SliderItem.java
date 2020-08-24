package com.example.krazynews;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;

import com.like.LikeButton;

import org.json.JSONArray;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;

public class SliderItem {
    private String id,image,text,title,news_by,news_time, news_link, pollQuestion,yes,no,maybe;
    private JSONArray publisherImage;
    private ImageView share;
    private View shimmerLoading;
    private LikeButton bookmark, likeButton;
    private ArrayList<String> newsImages = new ArrayList<>();
    private ArrayList<String> newsUrls = new ArrayList<>();

    public SliderItem() {}

    public SliderItem(String id, String image, String text, String title, String news_time, String news_link, LikeButton bookmark, LikeButton likeButton)
    {
        this.id = id;
        this.image = image;
        this.text = text;
        this.title = title;
        this.news_time = news_time;
        this.news_link = news_link;
        this.bookmark = bookmark;
        this.likeButton = likeButton;
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

    public void setId(String id) { this.id = id;}
    public String getId(){return id;}

    public void setPollQuestion(String pollQuestion){ this.pollQuestion = pollQuestion;};
    public String getPollQuestion(){return pollQuestion;}

    public String getYes() { return yes; }
    public void setYes(String yes) { this.yes = yes; }

    public String getNo() { return no; }
    public void setNo(String no) { this.no = no; }

    public String getMaybe() { return maybe; }
    public void setMaybe(String maybe) { this.maybe = maybe; }

    public ArrayList<String> getNewsImages() { return newsImages; }
    public void setNewsImages(ArrayList<String> newsImages) { this.newsImages = newsImages; }

    public ArrayList<String> getNewsUrls() { return newsUrls; }
    public void setNewsUrls(ArrayList<String> newsUrls) { this.newsUrls = newsUrls; }
}
