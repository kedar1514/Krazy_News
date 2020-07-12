package com.example.krazynews;

import androidx.cardview.widget.CardView;

public class SliderItem {
    private int image;
    private int text;
    private String title;

    public SliderItem() {}

    public SliderItem(int image, int text, String title)
    {
        this.image = image;
        this.text = text;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }
}
