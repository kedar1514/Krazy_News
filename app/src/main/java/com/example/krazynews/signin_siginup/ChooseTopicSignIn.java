package com.example.krazynews.signin_siginup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.krazynews.MainActivity;
import com.example.krazynews.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChooseTopicSignIn extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private  SharedPreferences preferences;
    private Button next;
    private String name, city, user_email, password;
    private Integer topicCount;
    private ImageView check;
    private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6, linearLayout7, linearLayout8, linearLayout9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_topic_sign_in);
        Intent i = getIntent();
        name = i.getStringExtra("name");
        city = i.getStringExtra("city");
        user_email = i.getStringExtra("email");
        password = i.getStringExtra("password");
        next = findViewById(R.id.next);
        topicCount = 0; // topic count for counting the selected topics and enabling next button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseTopicSignIn.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        if(topicCount < 2){
            next.setBackgroundResource(R.drawable.disabled_button);
            next.setEnabled(false);
        }else{
            next.setBackgroundResource(R.drawable.buttons);
            next.setEnabled(true);
        }

        preferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("corona","gone");
        editor.putString("politics","gone");
        editor.putString("startup","gone");
        editor.putString("india","gone");
        editor.putString("sports","gone");
        editor.putString("bollywood","gone");
        editor.putString("business","gone");
        editor.putString("technology","gone");
        editor.putString("international","gone");
        editor.apply();
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

                    topicCount--;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("corona","visible");
                    editor.apply();

                    topicCount++;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
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

                    topicCount--;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("politics","visible");
                    editor.apply();

                    topicCount++;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
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

                    topicCount--;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("startup","visible");
                    editor.apply();

                    topicCount++;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
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

                    topicCount--;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("india","visible");
                    editor.apply();

                    topicCount++;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
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

                    topicCount--;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("sports","visible");
                    editor.apply();

                    topicCount++;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
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

                    topicCount--;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("bollywood","visible");
                    editor.apply();

                    topicCount++;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
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

                    topicCount--;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("business","visible");
                    editor.apply();

                    topicCount++;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
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

                    topicCount--;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("technology","visible");
                    editor.apply();

                    topicCount++;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
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

                    topicCount--;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
                }
                else
                {
                    check.setVisibility(View.VISIBLE);
                    editor.putString("international","visible");
                    editor.apply();

                    topicCount++;

                    if(topicCount < 2){
                        next.setBackgroundResource(R.drawable.disabled_button);
                        next.setEnabled(false);
                    }else{
                        next.setBackgroundResource(R.drawable.buttons);
                        next.setEnabled(true);
                    }
                }
            }
        });
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
}
