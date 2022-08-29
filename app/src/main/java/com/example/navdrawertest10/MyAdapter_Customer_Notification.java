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

public class MyAdapter_Customer_Notification extends RecyclerView.Adapter<MyAdapter_Customer_Notification.MyViewHolder> {

    private ArrayList<ModelNotification> mList;
    private Context context;

    public MyAdapter_Customer_Notification(Context context, ArrayList<ModelNotification> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemlist_customer_notification, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelNotification model = mList.get(position);
        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);
        holder.orderStatus.setText(model.getOrderStatus());
        holder.notificationSen.setText(model.getNotificationSen());
        holder.dateTime.setText(model.getDateTime());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView orderStatus, dateTime, notificationSen;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.m_image);
            orderStatus = itemView.findViewById(R.id.salesFoodName);
            notificationSen = itemView.findViewById(R.id.salesOrder);
            dateTime = itemView.findViewById(R.id.salesTotal);

        }
    }

}
