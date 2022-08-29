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
import com.bumptech.glide.load.HttpException;

import java.util.ArrayList;

public class MyAdapter_Customer_My_Orders extends RecyclerView.Adapter<MyAdapter_Customer_My_Orders.MyViewHolder> {

    private ArrayList <ModelFinalOrder> mList;
    private Context context;

    public MyAdapter_Customer_My_Orders(Context context, ArrayList<ModelFinalOrder> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemlist_customer_my_order, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelFinalOrder model = mList.get(position);
        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);
        holder.orderDate.setText(model.getDateOrder());
        holder.orderId.setText(model.getOrderId());
        holder.orderPrice.setText(model.getTotalPrice());
        holder.orderQuantity.setText(model.getFoodQuantity());
        holder.tableNo.setText(model.getTableNo());
        holder.orderStatus.setText(model.getStatus());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView orderId, orderPrice, orderDate, orderStatus, orderQuantity, tableNo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.m_image);
            orderId = itemView.findViewById(R.id.textOrder);
            orderPrice = itemView.findViewById(R.id.textPrice);
            orderQuantity = itemView.findViewById(R.id.textQ);
            orderDate = itemView.findViewById(R.id.textDate);
            orderStatus = itemView.findViewById(R.id.textStatus);
            tableNo = itemView.findViewById(R.id.textTableNo);
        }
    }


}
