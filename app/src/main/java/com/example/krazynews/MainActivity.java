package com.example.krazynews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.krazynews.adapter.SliderAdapter;
import com.example.krazynews.signin_siginup.GetEmail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private  SharedPreferences preferences;
    private Dialog myDialog, fullDialog;
    private TextView marquee, news_text, toolbar_name;
    private String id, all_news;
    private ViewPager2 viewPager2;
    private List<SliderItem> sliderItems;
    private ViewPropertyAnimator animate;
    private View shimer_home_page;
    private String notificationType;
    private boolean flag;
    private String URL_VIEW = "https://www.krazyfox.in/krazynews/api/views.php";
    private String URL_IMAGE = "https://www.krazyfox.in/krazynews", temp_str;
    private String language;
    private ProgressBar progressBar;
    private int page=1, count=50, itemNum=0;
    private int mx=-1;
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
        toolbarFlag = true;
        //firebase notification code

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("AppNotification","AppNotification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        if(preferences.getString("Login"," ").equals("YES"))
        {
            notificationType = "signUser";
        }
        else{
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
        //firebase notification code

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_name = findViewById(R.id.toolbar_name);

        if(all_news.equals("true"))
        {
            toolbar_name.setText("All News");
        }
        else{

        }

        shimer_home_page = findViewById(R.id.shimmer_loading);
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
        viewPager2.getCurrentItem();
        ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position>mx)
                {
                    mx = position;
                    incrementView(sliderItems.get(position).getId());
                }
                if(position+1 == sliderItems.size()){
                    getData(++page,count);
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

//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.newsRefresh);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
////                page = 1;
////                count = 20;
//                getData(++page,count);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    private void getData(int page, int count){
        if(!checkConnection())
        {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.krazyfox.in/krazynews/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        MainInterface mainInterface = retrofit.create(MainInterface.class);
        Call<String> call = mainInterface.STRING_CALL(page,count);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Log.d("tagconvertstr", "["+response+"]");
                Log.d("responseBody", "["+response.body()+"]");
                if(response.isSuccessful() && response.body() != null){
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Log.d("responseSuccesful", "["+response.isSuccessful()+"]");
                        String title = jsonObject.getString("title");
                        Log.d("tagconvertstr", "["+title+"]");
                        parseResult(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("jsonError", "["+e+"]");
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

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                SliderItem sliderItem = new SliderItem();
                sliderItem.setImage(URL_IMAGE + jsonObject.getString("displaypicture").substring(2));
                String lang = preferences.getString("lang", "");
                if(lang.equals("hindi"))
                {
                    if(jsonObject.getString("hindititle").equals(""))
                    {
                        continue;
                    }else {
                        sliderItem.setTitle(jsonObject.getString("hindititle"));
                        sliderItem.setText(jsonObject.getString("hindidescription"));
                    }
                }
                else
                {
                    sliderItem.setTitle(jsonObject.getString("title"));
                    sliderItem.setText(jsonObject.getString("description"));
                }
                ArrayList<String> newsImages = new ArrayList<>();
                ArrayList<String> newsUrls = new ArrayList<>();
                String[] urls =  jsonObject.getString("publisherURL").split(",",-2);
                for (String a:urls)
                {
//                    Log.d("newsUrl Main", "parseResult: "+a);
                    newsUrls.add(a);
                }
                sliderItem.setNewsUrls(newsUrls);

                JSONArray publisherImages =  jsonObject.getJSONArray("publisherImages");
                for (int j=0;j<publisherImages.length();j++)
                {
                    newsImages.add(URL_IMAGE + publisherImages.getString(j).substring(2));
                }
                sliderItem.setNewsImages(newsImages);

                sliderItem.setNews_by("by/"+jsonObject.getString("author"));
                sliderItem.setId(jsonObject.getString("id").toString());
                sliderItem.setPollQuestion(jsonObject.getString("poleQuestion"));
                sliderItem.setYes(jsonObject.getString("poleYes"));
                sliderItem.setNo(jsonObject.getString("poleNo"));
                sliderItem.setMaybe(jsonObject.getString("poleMaybe"));
                Calendar calendar = Calendar.getInstance();
                String[] currDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime()).split("/",-1);
                String newsDate = jsonObject.getString("date");
//                Log.d("newsUrl Main",  newsDate.substring(8,10)+"parseResult: "+currDate/*.substring(1,3)*/);
                if(currDate[1].equals(newsDate.substring(8,10))){
                    sliderItem.setNews_time(jsonObject.getString("time"));
                }
                else {
//                    Log.d("newsUrl Main",  newsDate.substring(5,7));
                    String [] month = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
                    sliderItem.setNews_time(newsDate.substring(8,10)+"/"+month[Integer.parseInt(newsDate.substring(5,7))-1]);
                }
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

                        case "Sports":
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
        itemNum += 50;
    }

    public void hideShowBar(View view){
        if(toolbarFlag){
            getSupportActionBar().show();
            toolbarFlag = false;
        }
        else{
            getSupportActionBar().hide();
            toolbarFlag = true;
        }
    }

    public boolean checkConnection(){
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

        if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){


            btnTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullDialog.show();
                    networkDialog.hide();
                    getData(page,count);
                }
            });
            fullDialog.hide();
            networkDialog.show();
            return false;
        }else{
            networkDialog.hide();
            return true;
        }
    }

    public void incrementView(final String id){
//        final String id = sliderItems.get(viewPager2.getCurrentItem()).getId();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_VIEW,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("view", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
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
}
//  <<,