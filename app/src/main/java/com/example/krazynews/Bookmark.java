package com.example.krazynews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.krazynews.adapter.BookmarkAdapter;
import com.example.krazynews.signin_siginup.BookmarkItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Bookmark extends AppCompatActivity {

    private BookmarkAdapter bookmarkAdapter;
    private ArrayList<BookmarkItem> arrayList;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private String userEmail;
    private ImageView backImage;
    private String URL_IMAGE = "https://www.krazyfox.in/krazynews";
    private String URL_GET_BOOKMARK = "https://www.krazyfox.in/krazynews/api/fetchbookmark.php";
    private SharedPreferences preferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);

        preferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);
        userEmail = preferences.getString("UserEmail","");
        arrayList = new ArrayList<>();
        backImage = findViewById(R.id.back_icon);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Bookmark.this,DashBoard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });
        recyclerView = findViewById(R.id.bookmark_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        getBookmarkData();
    }

    public void getBookmarkData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_BOOKMARK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String error ="";
                        Log.i("response Bookmark",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            error = jsonObject.getString("error");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(error.equals("No record found"))
                        {
                            Toast.makeText(getApplicationContext(), "No bookmark found!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    BookmarkItem bookmarkItem = new BookmarkItem();

                                    bookmarkItem.setId(jsonObject.getString("postid"));
                                    bookmarkItem.setImage(jsonObject.getString("image"));
                                    bookmarkItem.setTitle(jsonObject.getString("title"));
                                    bookmarkItem.setNewsLink(jsonObject.getString("url"));

                                    arrayList.add(bookmarkItem);
                                }

                                bookmarkAdapter = new BookmarkAdapter(getApplicationContext(),arrayList);
                                recyclerView.setAdapter(bookmarkAdapter);
                            } catch (JSONException e){
                                e.printStackTrace();
                                Log.d("error", e.toString());
                                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
//                            progressDialog.dismiss();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Something went wrong!" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",userEmail);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Bookmark.this,DashBoard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}