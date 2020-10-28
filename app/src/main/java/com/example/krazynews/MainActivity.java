package com.example.krazynews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.Fade;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.Slide;
import android.transition.Transition;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.krazynews.adapter.SliderAdapter;
import com.example.krazynews.signin_siginup.GetEmail;
import com.example.krazynews.signin_siginup.RegistrationName;
import com.example.krazynews.signin_siginup.SignIn;
import com.example.krazynews.signin_siginup.SignInSignUp;
import com.example.krazynews.signin_siginup.VerifyOtp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//  <<,
public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean categories_flag, toolbarFlag;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private Dialog myDialog, fullDialog;
    private TextView marquee, news_text, toolbar_name;
    private String id, all_news;
    private ViewPager2 viewPager2;
    private Toolbar toolbar;
    private List<SliderItem> sliderItems;
    private ViewPropertyAnimator animate;
    private View shimer_home_page;
    private String notificationType;
    private boolean flag;
    private String URL_USER_VIEW = "https://www.krazyfox.in/krazynews/api/userview.php";
    private String URL_VIEW = "https://www.krazyfox.in/krazynews/api/views.php";
    private String URL_IMAGE = "https://www.krazyfox.in/krazynews";
    private String URL_TOKEN = "https://www.krazyfox.in/krazynews/app/token.php", token;

    private String language;
    private ProgressBar progressBar;
    private int page = 1, count = 40, itemNum = 0;
    private String email, lang, category;
    private int mx = -1;
    private long day1 = 172800000;
    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = AppMessagingService.getToken(getApplicationContext());
        sendToken(token);
        Log.d("acutal token", token);
        preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        editor = preferences.edit();
        email = preferences.getString("UserEmail","");
        Intent mainIntent = getIntent();
        String notificationsNewsId = mainIntent.getStringExtra("notificationsNewsId");
        if(notificationsNewsId == null) {

        }else{
            if(email.isEmpty()) {
                addNotificationNews(notificationsNewsId,"dummy@gmail.com");
            }else{
                addNotificationNews(notificationsNewsId,email);
            }
        }

        if (!preferences.getString("Login","").equals("YES")) {
            long time = preferences.getLong("pastLoginDecisionTime", 0);
            if (time < System.currentTimeMillis() - 86400000) {
                loginAlert();
                editor.putLong("pastLoginDecisionTime", System.currentTimeMillis()).commit();
            }
            editor.apply();
        }

        if (!preferences.getString("Rated","").equals("YES")) {
            long time = preferences.getLong("pastRatedDecisionTime", 0);
            if (time < System.currentTimeMillis() - 259200000) {
                rateAlert();
                editor.putLong("pastRatedDecisionTime", System.currentTimeMillis()).commit();
            }
            editor.apply();
        }

        all_news = preferences.getString("allNews", "");
        myDialog = new Dialog(this);
        fullDialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        fullDialog.setContentView(R.layout.activity_loading_acitvity);
        fullDialog.show();

        if(preferences.getString("Notifications","").equals("DontShow")) {
            editor = preferences.edit();
            editor.putString("Notifications","DontShow");
            editor.apply();
        }
        else if(preferences.getString("Notifications","").equals("Show")){
            editor = preferences.edit();
            editor.putString("Notifications","Show");
            editor.apply();
        }else{
            editor = preferences.edit();
            editor.putString("Notifications","Show");
            editor.apply();
        }//Checking default value of notifications string

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("AppNotification", "AppNotification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        if (preferences.getString("Login", " ").equals("YES")) {
            notificationType = "signUser";
        } else {
            notificationType = "guestUser";
        }
        FirebaseMessaging.getInstance().subscribeToTopic(notificationType)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "success";
                        if (!task.isSuccessful()) {
                            msg = "failes";
                        }
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_name = findViewById(R.id.toolbar_name);

        if (all_news.equals("true")) {
            toolbar_name.setText("All News");
        } else {

        }
        if(preferences.getString("lang","").equals("hindi")) {
            lang = "hindi";
        }
        else{
            lang = "english";
        }

        shimer_home_page = findViewById(R.id.shimmer_loading);
        ImageView imageView = findViewById(R.id.dashboard_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DashBoard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                finish();
            }
        });

        viewPager2 = findViewById(R.id.viewPager);
        sliderItems = new ArrayList<>();
        getData(page, count);
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
                    getData(++page, count);
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

    private void getData(int page, int count) {
        if (!checkConnection()) {
            return;
        }

         //email, lang, category;
        if(email.isEmpty())
        {
            email = "null";
        }

        category = "";
        if (preferences.getString("corona", "").equals("visible"))
        {
            category = category + "Corona,";
        }
        if (preferences.getString("politics", "").equals("visible"))
        {
            category = category + "Politics,";
        }
        if (preferences.getString("startup", "").equals("visible"))
        {
            category = category + "Startup,";
        }
        if (preferences.getString("india", "").equals("visible"))
        {
            category = category + "India,";
        }
        if (preferences.getString("sports", "").equals("visible"))
        {
            category = category + "Sports,";
        }
        if (preferences.getString("bollywood", "").equals("visible"))
        {
            category = category + "Bollywood,";
        }
        if (preferences.getString("business", "").equals("visible"))
        {
            category = category + "Business,";
        }
        if (preferences.getString("technology", "").equals("visible"))
        {
            category = category + "Technology,";
        }
        if (preferences.getString("international", "").equals("visible"))
        {
            category = category + "International,";
        }
        //fetching news
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.krazyfox.in/krazynews/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        MainInterface mainInterface = retrofit.create(MainInterface.class);
//        Log.d("email", "getData: "+email);
        Call<String> call = mainInterface.STRING_CALL(page, count,email, lang, category);
        Log.d("urlCall", "getData: "+call.request().url());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
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

                    sliderItem.setNews_by("by " + jsonObject.getString("author"));
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
        viewPager2.setAdapter(new SliderAdapter(MainActivity.this, sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(5);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager2.setCurrentItem(itemNum - 1, false);
        itemNum += 40;
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

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        final Dialog networkDialog = new Dialog(this);
        networkDialog.setContentView(R.layout.network_dialog);
        networkDialog.setCanceledOnTouchOutside(false);
        networkDialog.setCancelable(false);
//        networkDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        networkDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        networkDialog.getWindow().getAttributes().windowAnimations =
                android.R.style.Animation_Dialog;

        Button btnTryAgain = networkDialog.findViewById(R.id.try_again);

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {


            btnTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullDialog.show();
                    networkDialog.hide();
                    getData(page, count);
                }
            });
            fullDialog.hide();
            networkDialog.show();
            return false;
        } else {
            networkDialog.hide();
            return true;
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
                            Toast.makeText(MainActivity.this, "View Error!" + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "View Error!" + error.toString(), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void sendToken(final String token) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TOKEN,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("token_file_response", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();
//                            String success = "1";
                            if(success.equals("1")){
//                                Toast.makeText(MainActivity.this, "Token Save", Toast.LENGTH_LONG).show();
                            }
                            else{
//                                Toast.makeText(MainActivity.this, "Token not saved", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("token json exception", e.toString());
                            Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("jsonError",error.toString());
                        Log.d("token error response", error.toString());
                        Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token",token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean isLoggedin() {
        preferences = getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
        String loggedIn = preferences.getString("Login","");
        if(loggedIn.equals("YES"))
        {
            return true;
        }
        else{
            return false;
        }
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
                                Toast.makeText(MainActivity.this,"Something Went Wrong!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,"Something Went Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something Went Wrong!" + error.toString(), Toast.LENGTH_LONG).show();
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

    public void addNotificationNews(String notificationsNewsId, String email){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.krazyfox.in/krazynews/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        ExtraNewsInterface extraNewsInterface = retrofit.create(ExtraNewsInterface.class);
//        Log.d("email", "getData: "+email);
        Call<String> call = extraNewsInterface.STRING_CALL(notificationsNewsId, email);
//        Log.d("urlCall", "getData: "+call.request().url());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Log.d("tagconvertstr", "["+response+"]");
                Log.d("responseBody", "[" + response.body() + "]");
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
//                        Log.d("responseSuccesful", "[" + response.isSuccessful() + "]");
//                        String title = jsonObject.getString("title");
//                        Log.d("tagconvertstr", "[" + title + "]");
                        SliderItem sliderItem = new SliderItem();
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

                        sliderItem.setNews_by("by " + jsonObject.getString("author"));
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
                        sliderItems.add(sliderItem);
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

    public void loginAlert(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        startActivity(new Intent(MainActivity.this, SignIn.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        startActivity(new Intent(MainActivity.this, RegistrationName.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(Html.fromHtml("<font color='#FD676C'>Login OR SignUp</font>"));
        builder.setMessage("You really seem to like this app, "
                + "since you are not logged-in"
                + " you are missing some amazing feature.")
                .setPositiveButton(Html.fromHtml("<font color='#FD676C'>Sign In</font>"), dialogClickListener)
                .setNegativeButton(Html.fromHtml("<font color='#FD676C'>Sign Up</font>"), dialogClickListener)
                .setNeutralButton(Html.fromHtml("<font color='#FD676C'>No, Thanks </font>"), dialogClickListener).show();
    }

    public void rateAlert(){
        preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        editor = preferences.edit();

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        startActivity(new Intent(MainActivity.this, SignIn.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(Html.fromHtml("<font color='#FD676C'>Rate this app</font>"));
        builder.setMessage("You really seem to like this app, "
                + "rate this app")
                .setPositiveButton(Html.fromHtml("<font color='#FD676C'>Rate Now</font>"), dialogClickListener)
                .setNeutralButton(Html.fromHtml("<font color='#FD676C'>Later</font>"), dialogClickListener).show();
    }
}
//  <<,