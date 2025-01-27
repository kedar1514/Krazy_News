package com.example.krazynews;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;

import com.like.LikeButton;

import org.json.JSONArray;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;

public class SliderItem {
    private String id,image,text,title,news_by,news_time, pollQuestion,yes,no,maybe;
    private JSONArray publisherImage;
    private ImageView share;
    //add Section
    private String addImage, addUrl,isAdd,addID,addName,addValue,addStatus;
    private String bookmark, like,view,poll;
    private View shimmerLoading;
    private ArrayList<String> newsImages = new ArrayList<>();
    private ArrayList<String> newsUrls = new ArrayList<>();

    public SliderItem() {}

    public SliderItem(String id, String image, String text, String title, String news_time)
    {
        this.id = id;
        this.image = image;
        this.text = text;
        this.title = title;
        this.news_time = news_time;
    }

    public String getAddName() { return addName; }
    public void setAddName(String addName) { this.addName = addName; }

    public String getAddValue() { return addValue; }
    public void setAddValue(String addValue) { this.addValue = addValue; }

    public String getAddStatus() { return addStatus; }
    public void setAddStatus(String addStatus) { this.addStatus = addStatus; }

    public String getAddID() { return addID; }
    public void setAddID(String addID) { this.addID = addID; }

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

    public String getAddImage() { return addImage;}
    public void setAddImage(String addImage) { this.addImage = addImage;}

    public String getAddUrl() { return addUrl;}
    public void setAddUrl(String addUrl) { this.addUrl = addUrl;}

    public String getIsAdd() { return isAdd; }
    public void setIsAdd(String isAdd) { this.isAdd = isAdd; }

    public String getBookmark() { return bookmark; }
    public void setBookmark(String bookmark) { this.bookmark = bookmark; }

    public String getLike() { return like; }
    public void setLike(String like) { this.like = like; }

    public String getView() { return view; }
    public void setView(String view) { this.view = view; }

    public String getPoll() { return poll; }
    public void setPoll(String poll) { this.poll = poll; }
}
