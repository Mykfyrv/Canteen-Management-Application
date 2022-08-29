package com.example.navdrawertest10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter_Canteen_Sales extends RecyclerView.Adapter<MyAdapter_Canteen_Sales.MyViewHolder> {

    private ArrayList<ModelRecord> mList;
    private Context context;

    public MyAdapter_Canteen_Sales(Context context, ArrayList<ModelRecord> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemlist_canteen_sales, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelRecord model = mList.get(position);
        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);
        holder.foodName.setText(model.getFoodName());
        holder.todayTotal.setText(model.getTodayTotal());
        holder.totalOrder.setText(model.getTodayTotalOrder());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView foodName, todayTotal, totalOrder;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.m_image);
            foodName = itemView.findViewById(R.id.salesFoodName);
            todayTotal = itemView.findViewById(R.id.salesOrder);
            totalOrder = itemView.findViewById(R.id.salesTotal);
        }
    }


}
