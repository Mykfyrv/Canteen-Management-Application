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

public class MyAdapter_Customer_Checkout extends RecyclerView.Adapter<MyAdapter_Customer_Checkout.MyViewHolder> {

    private ArrayList <ModelOrder> mList;
    private Context context;

    public MyAdapter_Customer_Checkout(Context context, ArrayList<ModelOrder> mList){
        this.context = context;
        this.mList = mList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemlist_customer_checkout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelOrder model = mList. get(position);
        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);
        holder.foodName.setText(model.getFoodName());
        holder.foodPrice.setText(model.getFoodPrice());
        holder.foodOrderNo.setText(model.getOrderId());
        holder.foodQuantity.setText(model.getFoodQuantity());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView foodName, foodPrice, foodOrderNo, foodQuantity ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.m_image);
            foodName = itemView.findViewById(R.id.m_name);
            foodPrice = itemView.findViewById(R.id.m_price);
            foodOrderNo = itemView.findViewById(R.id.m_orderno);
            foodQuantity = itemView.findViewById(R.id.m_quantity);

        }
    }

}
