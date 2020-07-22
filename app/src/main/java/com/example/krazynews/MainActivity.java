package com.example.krazynews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
    private boolean categories_flag;
    private SharedPreferences.Editor editor;
    private  SharedPreferences preferences;
    private Dialog myDialog, fullDialog;
    private TextView marquee, news_text, toolbar_name;
    private String id, all_news;
    private ViewPager2 viewPager2;
    private List<SliderItem> sliderItems;
    private ViewPropertyAnimator animate;
    private boolean flag;
    private String URL_NEWS = "https://krazynews.000webhostapp.com/api/post.php";
    private String URL_IMAGE = "https://krazynews.000webhostapp.com", temp_str;
    private ProgressBar progressBar;
    //variable declaration for poll card
    private boolean pollFlag;
    private LinearLayout first, second, third;
    private ConstraintLayout firstClickColour,secondClickColour,thirdClickColour;
    private TextView firstOptionPercentage, secondOptionPercentage, thirdOptionPercentage, submit;
    private int page=1, count=10, itemNum=0;
    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);
        editor = preferences.edit();
        all_news = preferences.getString("allNews","");
        myDialog = new Dialog(this);
        fullDialog = new Dialog(this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        fullDialog.setContentView(R.layout.activity_loading_acitvity);
        fullDialog.show();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_name = findViewById(R.id.toolbar_name);

        if(all_news.equals("true"))
        {
            toolbar_name.setText("All News");
        }
        else{

        }

        progressBar = findViewById(R.id.news_loading);
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
        getData(page, count);
        ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position+1 == sliderItems.size()){
                    progressBar.setVisibility(View.VISIBLE);
                    getData(++page,count);
                }
                else{
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.e("Selected_Page", String.valueOf(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                //Log.e("Selected_State", String.valueOf(state));
            }
        };
        viewPager2.registerOnPageChangeCallback(pageChangeCallback);
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
    }

    private void getData(int page, int count){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://krazynews.000webhostapp.com/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        MainInterface mainInterface = retrofit.create(MainInterface.class);
        Call<String> call = mainInterface.STRING_CALL(page,count);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("tagconvertstr", "["+response+"]");
                if(response.isSuccessful() && response.body() != null){
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        parseResult(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void parseResult(JSONArray jsonArray) {
        categories_flag = false;
        if (preferences.getString("corona", "").equals("visible") || preferences.getString("politics", "").equals("visible") ||
                preferences.getString("startup", "").equals("visible") || preferences.getString("india", "").equals("visible") ||
                preferences.getString("sports", "").equals("visible") || preferences.getString("bollywood", "").equals("visible") ||
                preferences.getString("business", "").equals("visible") || preferences.getString("technology", "").equals("visible") ||
                preferences.getString("international", "").equals("visible")) {
            categories_flag = true;
        }

        if (categories_flag == false || all_news.equals("true")) {
            toolbar_name.setText("All News");
        }
        fullDialog.hide();
        progressBar.setVisibility(View.GONE);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                SliderItem sliderItem = new SliderItem();
                sliderItem.setImage(URL_IMAGE + jsonObject.getString("displaypicture").substring(2));
                sliderItem.setTitle(jsonObject.getString("title").toString());
                sliderItem.setText(jsonObject.getString("description").toString());
                sliderItem.setNews_link(jsonObject.getString("publisherURL").toString());
//                        sliderItem.setNews_by(jsonObject.getString("author").toString());
                sliderItem.setNews_by("by Rudra Ghodke");
//                        sliderItems.add(sliderItem);

                String categories = jsonObject.getString("categories").toString();

                if (categories_flag == false || all_news.equals("true")) {
                    sliderItems.add(sliderItem);
                } else if (categories_flag) {
                    switch (categories) {
                        case "Corona":
                            if (preferences.getString("corona", "").equals("visible")) {
                                sliderItems.add(sliderItem);
                            }
                            break;

                        case "Politics":
                            if (preferences.getString("politics", "").equals("visible")) {
                                sliderItems.add(sliderItem);
                            }
                            break;

                        case "Startups":
                            if (preferences.getString("startup", "").equals("visible")) {
                                sliderItems.add(sliderItem);
                            }
                            break;

                        case "India":
                            if (preferences.getString("india", "").equals("visible")) {
                                sliderItems.add(sliderItem);
                            }
                            break;

                        case "soirts":
                            if (preferences.getString("sports", "").equals("visible")) {
                                sliderItems.add(sliderItem);
                            }
                            break;

                        case "Bollywood":
                            if (preferences.getString("bollywood", "").equals("visible")) {
                                sliderItems.add(sliderItem);
                            }
                            break;

                        case "Business":
                            if (preferences.getString("business", "").equals("visible")) {
                                sliderItems.add(sliderItem);
                            }
                            break;

                        case "Technology":
                            if (preferences.getString("technology", "").equals("visible")) {
                                sliderItems.add(sliderItem);
                            }
                            break;

                        case "International":
                            if (preferences.getString("international", "").equals("visible")) {
                                sliderItems.add(sliderItem);
                            }
                            break;

                        case "Entertainment":
                            if (preferences.getString("entertainment", "").equals("visible")) {
                                sliderItems.add(sliderItem);
                            }
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            viewPager2.setAdapter(new SliderAdapter(getApplicationContext(), sliderItems, viewPager2));
            viewPager2.setClipToPadding(false);
            viewPager2.setClipChildren(false);
            viewPager2.setOffscreenPageLimit(3);
            viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        }
        viewPager2.setCurrentItem(itemNum-1,false);
        itemNum += 10;
    }
//    private void getData(int page, int count){
//        categories_flag = false;
//        if(preferences.getString("corona","").equals("visible")||preferences.getString("politics","").equals("visible")||
//                preferences.getString("startup","").equals("visible")||preferences.getString("india","").equals("visible")||
//                preferences.getString("sports","").equals("visible")||preferences.getString("bollywood","").equals("visible")||
//                preferences.getString("business","").equals("visible")||preferences.getString("technology","").equals("visible")||
//                preferences.getString("international","").equals("visible")){
//            categories_flag=true;
//        }
//
//        if(categories_flag==false || all_news.equals("true"))
//        {
//            toolbar_name.setText("All News");
//        }
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_NEWS, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                fullDialog.hide();
//                for (int i = response.length() - 1; i >= 0; i--) {
//                    try {
//                        JSONObject jsonObject = response.getJSONObject(i);
//                        SliderItem sliderItem = new SliderItem();
//                        temp_str = jsonObject.getString("displaypicture").toString();
//                        sliderItem.setImage(URL_IMAGE + temp_str.substring(2));
//                        sliderItem.setTitle(jsonObject.getString("title").toString());
//                        sliderItem.setText(jsonObject.getString("description").toString());
//                        sliderItem.setNews_link(jsonObject.getString("publisherURL").toString());
////                        sliderItem.setNews_by(jsonObject.getString("author").toString());
//                        sliderItem.setNews_by("by Rudra Ghodke");
////                        sliderItems.add(sliderItem);
//
//                        String categories = jsonObject.getString("categories").toString();
//
//                        if(categories_flag==false || all_news.equals("true"))
//                        {
//                            sliderItems.add(sliderItem);
//                        }
//                        else if(categories_flag){
//                            switch(categories)
//                            {
//                                case "Corona":  if(preferences.getString("corona","").equals("visible"))
//                                                {
//                                                    sliderItems.add(sliderItem);
//                                                }
//                                                break;
//
//                                case "Politics":if(preferences.getString("politics","").equals("visible"))
//                                                {
//                                                    sliderItems.add(sliderItem);
//                                                }
//                                                break;
//
//                                case "Startups":if(preferences.getString("startup","").equals("visible"))
//                                                {
//                                                    sliderItems.add(sliderItem);
//                                                }
//                                                break;
//
//                                case "India":   if(preferences.getString("india","").equals("visible"))
//                                                {
//                                                    sliderItems.add(sliderItem);
//                                                }
//                                                break;
//
//                                case "soirts":  if(preferences.getString("sports","").equals("visible"))
//                                                {
//                                                    sliderItems.add(sliderItem);
//                                                }
//                                                break;
//
//                                case "Bollywood":if(preferences.getString("bollywood","").equals("visible"))
//                                                {
//                                                    sliderItems.add(sliderItem);
//                                                }
//                                                break;
//
//                                case "Business":if(preferences.getString("business","").equals("visible"))
//                                                {
//                                                    sliderItems.add(sliderItem);
//                                                }
//                                                break;
//
//                                case "Technology":  if(preferences.getString("technology","").equals("visible"))
//                                                    {
//                                                        sliderItems.add(sliderItem);
//                                                    }
//                                                    break;
//
//                                case "International":if(preferences.getString("international","").equals("visible"))
//                                                    {
//                                                        sliderItems.add(sliderItem);
//                                                    }
//                                                    break;
//
//                                case "Entertainment":if(preferences.getString("entertainment","").equals("visible"))
//                                                    {
//                                                        sliderItems.add(sliderItem);
//                                                    }
//                                                    break;
//                            }
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    viewPager2.setAdapter(new SliderAdapter(getApplicationContext(),sliderItems, viewPager2));
//                    viewPager2.setClipToPadding(false);
//                    viewPager2.setClipChildren(false);
//                    viewPager2.setOffscreenPageLimit(3);
//                    viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
//                }
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("tag", "onErrorResponse: " + error.getMessage());
//                    }
//                });
//        queue.add(jsonArrayRequest);
//    }

    public void ShowNewsPopUp(View v){
        myDialog.setContentView(R.layout.activity_news_pop_up);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFade;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = myDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.START;
        myDialog.show();

        ImageView imageView = myDialog.findViewById(R.id.source1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewsLink.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                myDialog.hide();
            }
        });
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

    public void toolbarPopUp(View view){
        myDialog = new Dialog(this, R.style.Theme_Dialog);
        myDialog.setContentView(R.layout.activity_tool_bar);
        myDialog.getWindow().setGravity(Gravity.TOP);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.ToolbarTheme;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        ImageView imageView = myDialog.findViewById(R.id.dashboard_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this,DashBoard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
//  <<,