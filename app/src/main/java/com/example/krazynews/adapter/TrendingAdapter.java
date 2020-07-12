package com.example.krazynews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.krazynews.R;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.MyViewHolder> {


    private ArrayList<HashMap<String,String>> arrayList;
    Context context;
    int counter = 1;

    public TrendingAdapter(Context context,ArrayList<HashMap<String,String>> arrayList){
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trending_list_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String state_name = arrayList.get(position).get("");
        final String confirm_cases = arrayList.get(position).get("");

        holder.state_nameTv.setText(state_name);
        holder.confirm_casesTv.setText(confirm_cases);
        holder.counterTv.setText(String.valueOf(counter ++));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView state_nameTv, confirm_casesTv, counterTv;
        public MyViewHolder(View view){
            super(view);
//
//            this.state_nameTv = (TextView) view.findViewById(R.id.state_nameTv);
//            this.confirm_casesTv = (TextView) view.findViewById(R.id.confirmTv);
//            this.counterTv = (TextView) view.findViewById(R.id.counter);
        }

    }
}
