package com.example.krazynews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.krazynews.signin_siginup.CityInput;
import com.example.krazynews.signin_siginup.SignIn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    // covid status
    private TextView worldDailyConfirmCases, worldTotalConfirmCases, worldTotalActiveCases, worldDailyRecoverCases, worldTotalRecoverCases;
    private TextView worldDailyDeathCases, worldTotalDeathCases;
    private TextView countryDailyConfirmCases, countryTotalConfirmCases, countryTotalActiveCases, countryDailyRecoverCases, countryTotalRecoverCases;
    private TextView countryDailyDeathCases, countryTotalDeathCases;
    private Button covidCountButton;
    private LinearLayout covidCountLayout;
    private boolean covidCountVisibilty;
    private Spinner spinner;
    private ArrayList<String> countryList;
    private ArrayList<CountryCovidData> countryCovidDataList;
    private String URL_WORLD_COVID = "https://corona.lmao.ninja/v3/covid-19/all";
    private String URL_COUNTRY_COVID = "https://corona.lmao.ninja/v3/covid-19/countries";
    //covid status
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

        worldDailyConfirmCases = findViewById(R.id.world_daily_confirm_cases);
        worldTotalConfirmCases = findViewById(R.id.world_total_confirm_cases);
        worldTotalActiveCases = findViewById(R.id.world_total_active_cases);
        worldDailyRecoverCases = findViewById(R.id.world_daily_recover_cases);
        worldTotalRecoverCases = findViewById(R.id.world_total_recover_cases);
        worldDailyDeathCases = findViewById(R.id.world_daily_death_cases);
        worldTotalDeathCases = findViewById(R.id.world_total_death_cases);

        countryDailyConfirmCases = findViewById(R.id.country_daily_confirm_cases);
        countryTotalConfirmCases = findViewById(R.id.country_total_confirm_cases);
        countryTotalActiveCases = findViewById(R.id.country_total_active_cases);
        countryDailyRecoverCases = findViewById(R.id.country_daily_recover_cases);
        countryTotalRecoverCases = findViewById(R.id.country_total_recover_cases);
        countryDailyDeathCases = findViewById(R.id.country_daily_death_cases);
        countryTotalDeathCases = findViewById(R.id.country_total_death_cases);

        covidCountButton = findViewById(R.id.covid_count_button);
        covidCountLayout = findViewById(R.id.covid_counts_layout);
        covidCountLayout.setVisibility(View.GONE);
        covidCountVisibilty = true;
        spinner = findViewById(R.id.country_spinner);
        countryList = new ArrayList<>();
        countryList.add("India");
        countryCovidDataList = new ArrayList<>();
        getCovidData();

        spinner.setAdapter(new ArrayAdapter<>(DashBoard.this,
                android.R.layout.simple_spinner_dropdown_item,countryList));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    countryTotalConfirmCases.setText(countryCovidDataList.get(93).getCases());
                    countryDailyConfirmCases.setText("+"+countryCovidDataList.get(93).getTodayCases());

                    countryTotalRecoverCases.setText(countryCovidDataList.get(93).getRecovered());
                    countryDailyRecoverCases.setText("+"+countryCovidDataList.get(93).getTodayRecovered());

                    countryTotalDeathCases.setText(countryCovidDataList.get(93).getDeaths());
                    countryDailyDeathCases.setText("+"+countryCovidDataList.get(93).getTodayDeaths());

                    countryTotalActiveCases.setText(countryCovidDataList.get(93).getActive());
                }
                else{
                    countryTotalConfirmCases.setText(countryCovidDataList.get(position-1).getCases());
                    countryDailyConfirmCases.setText("+"+countryCovidDataList.get(position-1).getTodayCases());

                    countryTotalRecoverCases.setText(countryCovidDataList.get(position-1).getRecovered());
                    countryDailyRecoverCases.setText("+"+countryCovidDataList.get(position-1).getTodayRecovered());

                    countryTotalDeathCases.setText(countryCovidDataList.get(position-1).getDeaths());
                    countryDailyDeathCases.setText("+"+countryCovidDataList.get(position-1).getTodayDeaths());

                    countryTotalActiveCases.setText(countryCovidDataList.get(position-1).getActive());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        covidCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(networkState())
                {
                    if(covidCountVisibilty) {
                        Transition transition = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            transition = new Slide((Gravity.BOTTOM));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            transition.setDuration(1000);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            transition.addTarget(R.id.covid_counts_layout);
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition((ViewGroup) covidCountLayout.getParent(), transition);
                        }
                        covidCountLayout.setVisibility(View.VISIBLE);
                        covidCountVisibilty = false;
                        covidCountButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    }else{
                        Transition transition = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            transition = new Slide((Gravity.BOTTOM));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            transition.setDuration(1000);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            transition.addTarget(R.id.covid_counts_layout);
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition((ViewGroup) covidCountLayout.getParent(), transition);
                        }
                        covidCountLayout.setVisibility(View.GONE);
                        covidCountVisibilty = true;
                        covidCountButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    }
                }
            }
        });

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
        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection())
                {
                    Intent intent =  new Intent(DashBoard.this,OtherNewsAcivity.class);
                    intent.putExtra("url",Constants.Base_Url+"/api/");
                    intent.putExtra("from","trending");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_right);
                    finish();
                }
            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkConnection())
                {
                    if(isLoggedin())
                    {
                        Intent intent =  new Intent(DashBoard.this,OtherNewsAcivity.class);
                        intent.putExtra("url",Constants.Base_Url+"/api/fetchbookmark.php");
                        intent.putExtra("from","bookmark");
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_right);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please Sign-In to use this feature ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        unread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection())
                {
                    if(isLoggedin())
                    {
                        Intent intent =  new Intent(DashBoard.this,OtherNewsAcivity.class);
                        intent.putExtra("url",Constants.Base_Url+"/api/");
                        intent.putExtra("from","unread");
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_right);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please Sign-In to use this feature ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

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

    void getCovidData() {
        JsonObjectRequest jsonObjectRequestWorld = new JsonObjectRequest(Request.Method.GET, URL_WORLD_COVID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            worldTotalConfirmCases.setText(response.getString("cases"));
                            worldDailyConfirmCases.setText("+"+response.getString("todayCases"));

                            worldTotalRecoverCases.setText(response.getString("recovered"));
                            worldDailyRecoverCases.setText("+"+response.getString("todayRecovered"));

                            worldTotalDeathCases.setText(response.getString("deaths"));
                            worldDailyDeathCases.setText("+"+response.getString("todayDeaths"));

                            worldTotalActiveCases.setText(response.getString("active"));
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashBoard.this,  error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        int socketTimeWorld = 70000;
        RetryPolicy retryPolicyWorld = new DefaultRetryPolicy(socketTimeWorld,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequestWorld.setRetryPolicy(retryPolicyWorld);
        RequestQueue requestQueueWorld = Volley.newRequestQueue(DashBoard.this);
        requestQueueWorld.add(jsonObjectRequestWorld);

        JsonArrayRequest jsonArrayRequestCountry = new JsonArrayRequest(Request.Method.GET, URL_COUNTRY_COVID, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray jsonArray = response;
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            try {
                                countryList.add(jsonArray.getJSONObject(i).getString("country"));
                                CountryCovidData countryCovidData =
                                        new CountryCovidData(jsonArray.getJSONObject(i).getString("cases"),
                                                jsonArray.getJSONObject(i).getString("todayCases"),
                                                jsonArray.getJSONObject(i).getString("deaths"),
                                                jsonArray.getJSONObject(i).getString("todayDeaths"),
                                                jsonArray.getJSONObject(i).getString("recovered"),
                                                jsonArray.getJSONObject(i).getString("todayRecovered"),
                                                jsonArray.getJSONObject(i).getString("active"));
                                countryCovidDataList.add(countryCovidData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashBoard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        int socketTimeCountry = 70000;
        RetryPolicy retryPolicyCountry = new DefaultRetryPolicy(socketTimeCountry,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonArrayRequestCountry.setRetryPolicy(retryPolicyCountry);
        RequestQueue requestQueueCountry = Volley.newRequestQueue(DashBoard.this);
        requestQueueCountry.add(jsonArrayRequestCountry);
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

    public boolean networkState() {
        if(checkConnection()){
            return true;
        }
        else{
            return false;
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
                    if(checkConnection()){
                        getCovidData();
                        networkDialog.hide();
                    }
                }
            });
            networkDialog.show();
            return false;
        } else {
            networkDialog.hide();
            return true;
        }
    }
    @Override
    public void onBackPressed() {
//        editor.putString("allNews","false");
//        editor.putString("topNews","false");
//        editor.putString("trendingNews","false");
//        editor.apply();
//        Intent intent = new Intent(DashBoard.this,MainActivity.class);
//        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}
