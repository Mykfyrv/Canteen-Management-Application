package com.example.navdrawertest10;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MyAdapter_Canteen_Menu extends RecyclerView.Adapter<MyAdapter_Canteen_Menu.MyViewHolder> {

    private ArrayList<ModelFood> mList;
    private Context context;

    public MyAdapter_Canteen_Menu(Context context, ArrayList<ModelFood> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemlist_canteen_menu, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelFood model = mList. get(position);
        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);
        holder.foodName.setText(model.getFoodName());
        holder.foodPrice.setText(model.getFoodPrice());
        holder.foodQuantity.setText(model.getFoodQuantity());
        holder.imageUrl.setText(model.getImageUrl());
        holder.foodId.setText(model.getFoodId());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView foodName, foodPrice, foodQuantity, imageUrl, foodId;
        CheckBox checkBox;
        Long currentTime;
        Button editMenu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.m_image);
            foodName = itemView.findViewById(R.id.m_name);
            foodPrice = itemView.findViewById(R.id.m_price);
            imageUrl = itemView.findViewById(R.id.m_url);
            foodId = itemView.findViewById(R.id.id_holder);
            foodQuantity = itemView.findViewById(R.id.m_orderno);
            checkBox = itemView.findViewById(R.id.checkBoxDele);
            editMenu = itemView.findViewById(R.id.Editmbutton);
            foodId.setVisibility(View.INVISIBLE);
            currentTime = System.currentTimeMillis();

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference root = db.getReference().child("B-Posted Food(Holder)").child(date); //Order Holder + session name
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(foodName.getText().toString()).getValue() != null){
                        checkBox.setChecked(true);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            editMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle menubundle = new Bundle();
                    menubundle.putString("uId", foodName.getText().toString());
                    menubundle.putString("foodId", foodId.getText().toString());
                    menubundle.putString("foodPrice", foodPrice.getText().toString());
                    menubundle.putString("foodQuantity", foodQuantity.getText().toString());
                    menubundle.putString("imageUrl", imageUrl.getText().toString());
                    Intent intent = new Intent(view.getContext(), CanteenUpdateMenuAct.class);
                    intent.putExtras(menubundle);
                    view.getContext().startActivity(intent);
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()){
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference root = db.getReference().child("B-Posted Food(Holder)").child(date);
                        HashMap<String, String> userMap = new HashMap<>();
                        userMap.put("foodId", foodId.getText().toString());
                        userMap.put("foodName", foodName.getText().toString());
                        userMap.put("foodPrice", foodPrice.getText().toString());
                        userMap.put("foodQuantity", foodQuantity.getText().toString());
                        userMap.put("imageUrl", imageUrl.getText().toString());
                        root.child(foodName.getText().toString()).setValue(userMap);
                    } else {
                        String deleteKey = foodName.getText().toString();
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference root = db.getReference().child("B-Posted Food(Holder)").child(date).child(deleteKey);
                        root.removeValue();
                    }
                }
            });


        }
    }

}
