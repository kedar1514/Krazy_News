package com.example.krazynews;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
//  <<,
public class DashBoard extends Activity {
    private ImageView check, my_feed, all_news, top_news, trending, bookmark, unread;
    private SharedPreferences.Editor editor;
    private  SharedPreferences preferences;
    private String userEmail;
    private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6, linearLayout7, linearLayout8, linearLayout9;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        my_feed = findViewById(R.id.my_feed);
        all_news = findViewById(R.id.all_news);
        top_news = findViewById(R.id.top_news);
        trending = findViewById(R.id.trending);
        bookmark = findViewById(R.id.bookmark);
        unread = findViewById(R.id.unread);

        all_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("allNews","true");
                editor.putString("topNews","false");
                editor.putString("trendingNews","false");
                editor.apply();
                startActivity(new Intent(DashBoard.this,MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        my_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("allNews","false");
                editor.putString("topNews","false");
                editor.putString("trendingNews","false");
                editor.apply();
                startActivity(new Intent(DashBoard.this,MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
//
//        top_news.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        trending.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isLoggedin())
                {
                    Intent intent =  new Intent(DashBoard.this,Bookmark.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_right);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Sign-In to use this feature ", Toast.LENGTH_SHORT).show();
                }

            }
        });
//
//        unread.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        ImageView imageView = findViewById(R.id.aero_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("allNews","false");
                editor.putString("topNews","false");
                editor.putString("trendingNews","false");
                editor.apply();
                Intent intent = new Intent(DashBoard.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        ImageView userProfile = findViewById(R.id.account_icon);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoard.this,Profile.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        preferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);
        editor = preferences.edit();

        checkTopic();

        linearLayout1 = findViewById(R.id.linear_layout1);
        linearLayout2 = findViewById(R.id.linear_layout2);
        linearLayout3 = findViewById(R.id.linear_layout3);
        linearLayout4 = findViewById(R.id.linear_layout4);
        linearLayout5 = findViewById(R.id.linear_layout5);
        linearLayout6 = findViewById(R.id.linear_layout6);
        linearLayout7 = findViewById(R.id.linear_layout7);
        linearLayout8 = findViewById(R.id.linear_layout8);
        linearLayout9 = findViewById(R.id.linear_layout9);

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check1);
                String string = preferences.getString("corona","");
                if(string.equals("visible"))
                {
                    check.setVisibility(View.GONE);
                    editor.putString("corona","gone");
                    editor.apply();
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("corona","visible");
                    editor.apply();
                }
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check2);
                String string = preferences.getString("politics","");
                if(string.equals("visible"))
                {
                    check.setVisibility(View.GONE);
                    editor.putString("politics","gone");
                    editor.apply();
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("politics","visible");
                    editor.apply();
                }
            }
        });

        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check3);
                String string = preferences.getString("startup","");
                if(string.equals("visible"))
                {
                    check.setVisibility(View.GONE);
                    editor.putString("startup","gone");
                    editor.apply();
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("startup","visible");
                    editor.apply();
                }
            }
        });

        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check4);
                String string = preferences.getString("india","");
                if(string.equals("visible"))
                {
                    check.setVisibility(View.GONE);
                    editor.putString("india","gone");
                    editor.apply();
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("india","visible");
                    editor.apply();
                }
            }
        });

        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check5);
                String string = preferences.getString("sports","");
                if(string.equals("visible"))
                {
                    check.setVisibility(View.GONE);
                    editor.putString("sports","gone");
                    editor.apply();
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("sports","visible");
                    editor.apply();
                }
            }
        });

        linearLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check6);
                String string = preferences.getString("bollywood","");
                if(string.equals("visible"))
                {
                    check.setVisibility(View.GONE);
                    editor.putString("bollywood","gone");
                    editor.apply();
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("bollywood","visible");
                    editor.apply();
                }
            }
        });

        linearLayout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check7);
                String string = preferences.getString("business","");
                if(string.equals("visible"))
                {
                    check.setVisibility(View.GONE);
                    editor.putString("business","gone");
                    editor.apply();
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("business","visible");
                    editor.apply();
                }
            }
        });

        linearLayout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check8);
                String string = preferences.getString("technology","");
                if(string.equals("visible"))
                {
                    check.setVisibility(View.GONE);
                    editor.putString("technology","gone");
                    editor.apply();
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("technology","visible");
                    editor.apply();
                }
            }
        });

        linearLayout9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = findViewById(R.id.check9);
                String string = preferences.getString("international","");
                if(string.equals("visible"))
                {
                    check.setVisibility(View.GONE);
                    editor.putString("international","gone");
                    editor.apply();
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("international","visible");
                    editor.apply();
                }
            }
        });

//        LinearLayout trending = findViewById(R.id.trending);
//        trending.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(DashBoard.this,Trending.class));
//            }
//        });

//        LinearLayout bookmark = findViewById(R.id.bookmark);
//        bookmark.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(DashBoard.this,Bookmark.class));
//            }
//        });
    }

    public void checkTopic(){
        String corona = preferences.getString("corona","");
        String politics = preferences.getString("politics","");
        String startup = preferences.getString("startup","");
        String india = preferences.getString("india","");
        String sports = preferences.getString("sports","");
        String bollywood = preferences.getString("bollywood","");
        String business = preferences.getString("business","");
        String technology = preferences.getString("technology","");
        String international = preferences.getString("international","");

        if(corona.equals("visible"))
        {
            check = findViewById(R.id.check1);
            check.setVisibility(View.VISIBLE);
        }

        if(politics.equals("visible"))
        {
            check = findViewById(R.id.check2);
            check.setVisibility(View.VISIBLE);
        }

        if(startup.equals("visible"))
        {
            check = findViewById(R.id.check3);
            check.setVisibility(View.VISIBLE);
        }

        if(india.equals("visible"))
        {
            check = findViewById(R.id.check4);
            check.setVisibility(View.VISIBLE);
        }

        if(sports.equals("visible"))
        {
            check = findViewById(R.id.check5);
            check.setVisibility(View.VISIBLE);
        }

        if(bollywood.equals("visible"))
        {
            check = findViewById(R.id.check6);
            check.setVisibility(View.VISIBLE);
        }

        if(business.equals("visible"))
        {
            check = findViewById(R.id.check7);
            check.setVisibility(View.VISIBLE);
        }

        if(technology.equals("visible"))
        {
            check = findViewById(R.id.check8);
            check.setVisibility(View.VISIBLE);
        }

        if(international.equals("visible"))
        {
            check = findViewById(R.id.check9);
            check.setVisibility(View.VISIBLE);
        }
    }

    public boolean isLoggedin()
    {
        String loggedIn = preferences.getString("Login","");
        if(loggedIn.equals("YES"))
        {
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public void onBackPressed() {
        editor.putString("allNews","false");
        editor.putString("topNews","false");
        editor.putString("trendingNews","false");
        editor.apply();
        Intent intent = new Intent(DashBoard.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }
}
