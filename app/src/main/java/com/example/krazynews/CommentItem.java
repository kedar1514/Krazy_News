package com.example.krazynews;

import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;

import java.util.ArrayList;

public class CommentItem {
    private String id, newsid, uname, email, comment, clike, cdislike, status, ulike, udislike, delete;

    public CommentItem(){}
    public CommentItem(String id, String newsid, String uname, String email, String comment, String clike, String cdislike, String status, String ulike, String udislike, String delete) {
        this.id = id;
        this.newsid = newsid;
        this.uname = uname;
        this.email = email;
        this.comment = comment;
        this.clike = clike;
        this.cdislike = cdislike;
        this.status = status;
        this.ulike = ulike;
        this.udislike = udislike;
        this.delete = delete;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNewsid() { return newsid; }
    public void setNewsid(String newsid) { this.newsid = newsid; }

    public String getUname() { return uname; }
    public void setUname(String uname) { this.uname = uname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getClike() { return clike; }
    public void setClike(String clike) { this.clike = clike; }

    public String getCdislike() { return cdislike; }
    public void setCdislike(String cdislike) { this.cdislike = cdislike; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUlike() { return ulike; }
    public void setUlike(String ulike) { this.ulike = ulike; }

    public String getUdislike() { return udislike; }
    public void setUdislike(String udislike) { this.udislike = udislike; }

    public String getDelete() { return delete; }
    public void setDelete(String delete) { this.delete = delete; }
}
