package com.example.krazynews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.krazynews.adapter.SliderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
//  <<,
public class MainActivity extends AppCompatActivity {
    Dialog myDialog;
    private TextView marquee;
    private String id;
    private ViewPager2 viewPager2;
    private List<SliderItem> sliderItems;
    private ViewPropertyAnimator animate;
    private boolean flag;
    private String URL_NEWS = "https://www.krazyfox.in/krazynews/api/post.php";
    //variable declaration for poll card
    private boolean pollFlag;
    private LinearLayout first, second, third;
    private ConstraintLayout firstClickColour,secondClickColour,thirdClickColour;
    private TextView firstOptionPercentage, secondOptionPercentage, thirdOptionPercentage, submit;
    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myDialog = new Dialog(this);

        marquee = findViewById(R.id.floatingText);
        marquee.setSelected(true);
        ImageView imageView = findViewById(R.id.dashboard_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this,DashBoard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        viewPager2 = findViewById(R.id.viewPager);
        sliderItems = new ArrayList<>();
        getData();

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(0));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.75f + r * 0.25f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);

        View inflatedView = getLayoutInflater().inflate(R.layout.slide_item_container, null);
    }

    private void getData(){

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_NEWS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                for (int i = 1; i < response.length(); i++) {
                    id = Integer.toString(i);
                    try {
                        JSONObject jsonObject = response.optJSONObject(id);
                        SliderItem sliderItem = new SliderItem();
                        sliderItem.setImage(R.drawable.image1);
                        sliderItem.setTitle(jsonObject.getString("title").toString());
                        sliderItem.setText(R.string.news1);
                        sliderItems.add(sliderItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
                    viewPager2.setClipToPadding(false);
                    viewPager2.setClipChildren(false);
                    viewPager2.setOffscreenPageLimit(3);
                    viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_NEWS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        SliderItem sliderItem = new SliderItem();
                        sliderItem.setImage(R.drawable.image1);
                        sliderItem.setTitle(jsonObject.getString("title").toString());
                        sliderItem.setText(R.string.news1);
                        sliderItems.add(sliderItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
                    viewPager2.setClipToPadding(false);
                    viewPager2.setClipChildren(false);
                    viewPager2.setOffscreenPageLimit(3);
                    viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("tag", "onErrorResponse: " + error.getMessage());
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void ShowNewsPopUp(View v){
        myDialog.setContentView(R.layout.activity_news_pop_up);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFade;
        myDialog.getWindow().setGravity(Gravity.BOTTOM);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void ShowPoll(View v){
        myDialog.setContentView(R.layout.poll_layout);
        myDialog.getWindow().setGravity(Gravity.CENTER);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFade;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        pollFlag = false;

        submit = myDialog.findViewById(R.id.submit);

        first = myDialog.findViewById(R.id.first);
        second = myDialog.findViewById(R.id.second);
        third = myDialog.findViewById(R.id.third);

        firstClickColour = myDialog.findViewById(R.id.firstClickColour);
        secondClickColour = myDialog.findViewById(R.id.secondClickColour);
        thirdClickColour = myDialog.findViewById(R.id.thirdClickColour);

        firstOptionPercentage = myDialog.findViewById(R.id.firstOptionPercentage);
        secondOptionPercentage = myDialog.findViewById(R.id.secondOptionPercentage);
        thirdOptionPercentage = myDialog.findViewById(R.id.thirdOptionPercentage);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pollFlag){
                    first.setBackgroundResource(R.drawable.poll_selected_stroke);
                    pollFlag = true;
                    firstClickColour.getBackground().setLevel(6000);
                    firstOptionPercentage.setText("60%");
                    firstOptionPercentage.setVisibility(View.VISIBLE);

                    secondClickColour.getBackground().setLevel(1000);
                    secondOptionPercentage.setText("10%");
                    secondOptionPercentage.setVisibility(View.VISIBLE);

                    thirdClickColour.getBackground().setLevel(3000);
                    thirdOptionPercentage.setText("30%");
                    thirdOptionPercentage.setVisibility(View.VISIBLE);
                }
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pollFlag){
                    pollFlag = true;
                    second.setBackgroundResource(R.drawable.poll_selected_stroke);
                    GradientDrawable drawable = (GradientDrawable)second.getBackground();
                    drawable.setStroke(10, 0xFFFD676C);

                    firstClickColour.getBackground().setLevel(6000);
                    firstOptionPercentage.setText("60%");
                    firstOptionPercentage.setVisibility(View.VISIBLE);

                    secondClickColour.getBackground().setLevel(1000);
                    secondOptionPercentage.setText("10%");
                    secondOptionPercentage.setVisibility(View.VISIBLE);

                    thirdClickColour.getBackground().setLevel(3000);
                    thirdOptionPercentage.setText("30%");
                    thirdOptionPercentage.setVisibility(View.VISIBLE);
                }
            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pollFlag){
                    pollFlag = true;
                    third.setBackgroundResource(R.drawable.poll_selected_stroke);
                    firstClickColour.getBackground().setLevel(6000);
                    firstOptionPercentage.setText("60%");
                    firstOptionPercentage.setVisibility(View.VISIBLE);

                    secondClickColour.getBackground().setLevel(1000);
                    secondOptionPercentage.setText("10%");
                    secondOptionPercentage.setVisibility(View.VISIBLE);

                    thirdClickColour.getBackground().setLevel(3000);
                    thirdOptionPercentage.setText("30%");
                    thirdOptionPercentage.setVisibility(View.VISIBLE);
                }
            }
        });

        myDialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.hide();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
