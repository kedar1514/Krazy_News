package com.example.krazynews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.krazynews.adapter.OtherNewsSliderAdapter;
import com.example.krazynews.adapter.SliderAdapter;
import com.example.krazynews.signin_siginup.ChooseTopicSignIn;
import com.example.krazynews.signin_siginup.SignIn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherNewsAcivity extends AppCompatActivity {

    private String URL_NEWS, From, email, lang;
    private String URL_IMAGE = Constants.Base_Url+"";
    private String URL_VIEW = Constants.Base_Url+"/api/views.php";
    private String URL_USER_VIEW = Constants.Base_Url+"/api/userview.php";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private TextView toolbarName;
    private Dialog fullDialog;
    private ViewPager2 viewPager2;
    private boolean toolbarFlag;
    private List<SliderItem> sliderItems;
    private ImageView backButton;

    private int page = 1, count = 40, itemNum = 0;
    private int mx = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_news_acivity);

        Intent intent = getIntent();
        URL_NEWS = intent.getStringExtra("url");
        From = intent.getStringExtra("from");
        Toolbar toolbar = (Toolbar) findViewById(R.id.otherToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        toolbarFlag = true;
        toolbarName = findViewById(R.id.otherToolbar_name);
        backButton = findViewById(R.id.othersBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OtherNewsAcivity.this,DashBoard.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });
        preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        email = preferences.getString("UserEmail","");
        if(preferences.getString("lang","").equals("hindi")) {
            lang = "hindi";
        }
        else{
            lang = "english";
        }
        fullDialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        if(From.equals("bookmark")) {
            fullDialog.setContentView(R.layout.bookmark_splash_screen);
            fullDialog.show();
            toolbarName.setText("Bookmark");
            getBookmark();
        }else if(From.equals("topNews")) {
            toolbarName.setText("Top News");
//            getNews(From);
        }else if(From.equals("unread")) {
            toolbarName.setText("Unread");
            fullDialog.setContentView(R.layout.bookmark_splash_screen);
            fullDialog.show();
            getNews(page,count,From);
        }else if(From.equals("trending")) {
            toolbarName.setText("Trending");
            fullDialog.setContentView(R.layout.trending_splash_screen);
            fullDialog.show();
            getNews(page,count,From);
        }


        viewPager2 = findViewById(R.id.otherViewPager);
        sliderItems = new ArrayList<>();
        ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position>mx)
                {
                    if(sliderItems.get(position).getIsAdd().equals("0") && sliderItems.get(position).getView().equals("0"))
                    {
                        incrementView(sliderItems.get(position).getId());
                        if(isLoggedin())
                        {
                            incrementUserView(sliderItems.get(position).getId(),email);
                        }
                    }
                    mx = position;
                }
                if (position + 1 == sliderItems.size()) {
                    if(From.equals("bookmark"))
                    {

                    }
                    else{
                        getNews(++page, count,From);
                    }
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
        compositePageTransformer.addTransformer(new MarginPageTransformer(250));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.75f + r * 0.25f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
    }

    private void getBookmark(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_NEWS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.i("bookmarkresponse", "["+response+"]");
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONArray jsonArray = new JSONArray(response);
                            parseResult(jsonArray);
                        } catch (JSONException e) {
                            Log.i("bookmarkresponse", "["+response+"]");
                            Toast.makeText(OtherNewsAcivity.this,"No Bookmark found",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OtherNewsAcivity.this, DashBoard.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            finish();
                            fullDialog.hide();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OtherNewsAcivity.this, "Login Error!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getNews(int page, int count,String From) {
//        if (!checkConnection()) {
//            return;
//        }

        //fetching news
        Call<String> call = null;
        if(From.equals("topNews")) {

        }else if(From.equals("unread")) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL_NEWS)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            UnreadInterface unreadInterface = retrofit.create(UnreadInterface.class);
            call = unreadInterface.STRING_CALL(page, count,email, lang, "");
        }else if(From.equals("trending")) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL_NEWS)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            TrendingInterface trendingInterface = retrofit.create(TrendingInterface.class);
            call = trendingInterface.STRING_CALL(page, count,email, lang, "");
        }
        Log.d("unreadUrl", call.request().url().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                Log.d("tagconvertstr", "["+response+"]");
                Log.d("responseBody", "[" + response.body() + "]");
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Log.d("responseSuccesful", "[" + response.isSuccessful() + "]");
                        String title = jsonObject.getString("title");
                        Log.d("tagconvertstr", "[" + title + "]");
                        parseResult(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("jsonError", "[" + e + "]");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void parseResult(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                SliderItem sliderItem = new SliderItem();
                Log.d("Api Data", "parseResult: "+jsonObject);
                if(jsonObject.getString("add").equals("1"))
                {
                    sliderItem.setAddID(jsonObject.getString("id"));
                    sliderItem.setAddValue(jsonObject.getString("value"));
                    sliderItem.setAddName(jsonObject.getString("name"));
                    sliderItem.setAddStatus(jsonObject.getString("status"));
                    sliderItem.setAddUrl(jsonObject.getString("url"));
                    sliderItem.setAddImage(URL_IMAGE + jsonObject.getString("displaypicture").substring(2));
                    sliderItem.setIsAdd("1");
                }
                else{

                    sliderItem.setImage(URL_IMAGE + jsonObject.getString("displaypicture").substring(2));
                    String lang = preferences.getString("lang", "");

                    if (lang.equals("hindi")) {
                        if (jsonObject.getString("hindititle").equals("")) {
                            sliderItem.setTitle(jsonObject.getString("title"));
                            sliderItem.setText(jsonObject.getString("description"));
                        } else {
                            sliderItem.setTitle(jsonObject.getString("hindititle"));
                            sliderItem.setText(jsonObject.getString("hindidescription"));
                        }
                    } else {
                        sliderItem.setTitle(jsonObject.getString("title"));
                        sliderItem.setText(jsonObject.getString("description"));
                    }

                    ArrayList<String> newsImages = new ArrayList<>();
                    ArrayList<String> newsUrls = new ArrayList<>();
                    String[] urls = jsonObject.getString("publisherURL").split(",", -2);
                    for (String a : urls) {
                        newsUrls.add(a);
                    }
                    sliderItem.setNewsUrls(newsUrls);
                    JSONArray publisherImages = jsonObject.getJSONArray("publisherImages");
                    for (int j = 0; j < publisherImages.length(); j++) {
                        newsImages.add(URL_IMAGE + publisherImages.getString(j).substring(2));
                    }
                    sliderItem.setNewsImages(newsImages);

                    sliderItem.setNews_by("by/" + jsonObject.getString("author"));
                    sliderItem.setId(jsonObject.getString("id").toString());
                    sliderItem.setPollQuestion(jsonObject.getString("poleQuestion"));
                    sliderItem.setYes(jsonObject.getString("poleYes"));
                    sliderItem.setNo(jsonObject.getString("poleNo"));
                    sliderItem.setMaybe(jsonObject.getString("poleMaybe"));
                    sliderItem.setIsAdd("0");

                    Log.d("bookmarkValue", jsonObject.getString("bookmark"));
                    Log.d("likeValue", jsonObject.getString("like"));
                    sliderItem.setBookmark(jsonObject.getString("bookmark"));
                    sliderItem.setLike(jsonObject.getString("like"));
                    sliderItem.setView(jsonObject.getString("view"));
                    sliderItem.setPoll(jsonObject.getString("poll"));

                    Calendar calendar = Calendar.getInstance();
                    String[] currDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime()).split("/", -1);
                    String newsDate = jsonObject.getString("date");
//                  Log.d("newsUrl Main",  newsDate.substring(8,10)+"parseResult: "+currDate/*.substring(1,3)*/);
                    if (currDate[1].equals(newsDate.substring(8, 10))) {
                        sliderItem.setNews_time(jsonObject.getString("time"));
                    } else {
//                    Log.d("newsUrl Main",  newsDate.substring(5,7));
                        String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                        sliderItem.setNews_time(newsDate.substring(8, 10) + " " + month[Integer.parseInt(newsDate.substring(5, 7)) - 1]);
                    }
                }
                sliderItems.add(sliderItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        viewPager2.setAdapter(new OtherNewsSliderAdapter(this, sliderItems, viewPager2, From));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(5);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager2.setCurrentItem(itemNum - 1, false);
        itemNum += 50;
        fullDialog.hide();
    }

    public void hideShowBar(View view) {
        if (toolbarFlag) {
            getSupportActionBar().show();
            toolbarFlag = false;
        } else {
            getSupportActionBar().hide();
            toolbarFlag = true;
        }
    }

    public void incrementView(final String id) {
//        final String id = sliderItems.get(viewPager2.getCurrentItem()).getId();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_VIEW,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("view", "[" + response + "]");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();
                            if(success.equals("true")){
//                                Toast.makeText(getApplicationContext(),"incremented View", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"incremented View failed", Toast.LENGTH_LONG).show();
                            }
                            //Toast.makeText(GetEmail.this, "Register Success!", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OtherNewsAcivity.this, "View Error!" + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OtherNewsAcivity.this, "View Error!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void incrementUserView(final String id, final String email){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_USER_VIEW,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("userview", "onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();
                            if(success.equals("true"))
                            {
//                                Toast.makeText(MainActivity.this,"Viewd", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(OtherNewsAcivity.this,"Something Went Wrong!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OtherNewsAcivity.this,"Something Went Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OtherNewsAcivity.this, "Something Went Wrong!" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("email",email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean isLoggedin() {
        preferences = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
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
        super.onBackPressed();
        startActivity(new Intent(OtherNewsAcivity.this,DashBoard.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}