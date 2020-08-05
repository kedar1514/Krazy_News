package com.example.krazynews.signin_siginup;

import android.view.View;
import android.widget.ImageView;

import com.like.LikeButton;

public class BookmarkItem {
    private String id,image,title,newsLink;

    public BookmarkItem() { }

    public BookmarkItem(String id, String image, String title, String newsLink) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.newsLink = newsLink;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getNewsLink() { return newsLink; }
    public void setNewsLink(String newsLink) { this.newsLink = newsLink; }
}
