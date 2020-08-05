package com.example.krazynews.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.example.krazynews.R;
import com.example.krazynews.signin_siginup.BookmarkItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.MyViewHolder> {


    private List<BookmarkItem> arrayList;
    private String URL_BOOKMARK_DELETE = "https://www.krazyfox.in/krazynews/api/bookmark.php";
    private String id, email, url, image, title, flag="2";
    private boolean deleteResult;
    private SharedPreferences preferences;
    Context context;

    public BookmarkAdapter(Context context,List<BookmarkItem> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.bookmark_list_item,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.set_title(arrayList.get(position));
        holder.set_image(arrayList.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = arrayList.get(position).getId();
                preferences = context.getSharedPreferences("PREFERENCE",Context.MODE_PRIVATE);
                email = preferences.getString("UserEmail", "");
                url = arrayList.get(position).getNewsLink();
                image = arrayList.get(position).getImage();
                title = arrayList.get(position).getTitle();
                if(bookmarkDelete()) {
                    arrayList.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView newsImage;
        Button delete;
        public MyViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.bookmark_news_title);
            newsImage = (ImageView) view.findViewById(R.id.bookmark_news_image);
            delete = view.findViewById(R.id.bookmark_delete);
        }

        public void set_title(BookmarkItem bookmarkItem) { title.setText(bookmarkItem.getTitle());}

        public void set_image(BookmarkItem bookmarkItem) { Glide.with(context).load(bookmarkItem.getImage()).into(newsImage);}
    }

    public boolean bookmarkDelete(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BOOKMARK_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success").toString();
                            if(success.equals("1"))
                            {
                                Toast.makeText(context,"Deleted", Toast.LENGTH_SHORT).show();
                                deleteResult = true;
                            }
                            else if(success.equals("0")){
                                Toast.makeText(context,"Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                deleteResult= false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,"Something Went Wrong!", Toast.LENGTH_SHORT).show();
                            deleteResult = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Something Went Wrong!" + error.toString(), Toast.LENGTH_LONG).show();
                        deleteResult = false;
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id.trim().toString());
                params.put("email",email.trim().toString());
                params.put("url",url.trim().toString());
                params.put("title",title.trim().toString());
                params.put("image",image.trim().toString());
                params.put("status",flag.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        Log.i("flagResponse", "["+requestQueue+"]");
        return deleteResult;
    }
}
