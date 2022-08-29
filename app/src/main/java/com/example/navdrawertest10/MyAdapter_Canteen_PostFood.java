package com.example.navdrawertest10;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter_Canteen_PostFood extends RecyclerView.Adapter<MyAdapter_Canteen_PostFood.MyViewHolder> {

    private ArrayList<ModelFood> mList;
    private Context context;

    public MyAdapter_Canteen_PostFood(Context context, ArrayList<ModelFood> mList){

        this.context = context;
        this.mList = mList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemlist_canteen_posted, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelFood model = mList. get(position);
        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);
        holder.foodName.setText(model.getFoodName());
        holder.foodPrice.setText(model.getFoodPrice());
        holder.foodQuantity.setText(model.getFoodQuantity());
        holder.foodUrl.setText(model.getImageUrl());
        holder.foodId.setText(model.getFoodId());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        ImageView imageView;
        TextView foodName, foodPrice, foodQuantity, foodUrl, foodId;
        Button postBtn, EditFood, DeleteFood;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.m_image);
            foodName = itemView.findViewById(R.id.m_name);
            foodPrice = itemView.findViewById(R.id.m_price);
            foodUrl = itemView.findViewById(R.id.m_url);
            foodId = itemView.findViewById(R.id.id_holder);
            foodQuantity = itemView.findViewById(R.id.m_orderno);
            postBtn = itemView.findViewById(R.id.button);
            EditFood = itemView.findViewById(R.id.Editabutton);
            DeleteFood = itemView.findViewById(R.id.Deletebutton);

            foodId.setVisibility(View.INVISIBLE);
            foodUrl.setVisibility(View.INVISIBLE);

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference root = db.getReference().child("B-Posted Food");
            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(foodId.getText().toString()).getValue() != null){
                        postBtn.setEnabled(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            EditFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle foodbundle = new Bundle();
                    foodbundle.putString("uId", foodName.getText().toString());
                    foodbundle.putString("foodId", foodId.getText().toString());
                    foodbundle.putString("foodPrice", foodPrice.getText().toString());
                    foodbundle.putString("foodQuantity", foodQuantity.getText().toString());
                    foodbundle.putString("imageUrl", foodUrl.getText().toString());
                    Intent intent = new Intent(view.getContext(), CanteenUpdateFoodAct.class);
                    intent.putExtras(foodbundle);
                    view.getContext().startActivity(intent);
                }
            });

            DeleteFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference("A-Food List").child(foodName.getText().toString()).removeValue();
                    Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new CanteenPostFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

                }
            });

            postBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference root = db.getReference().child("B-Posted Food").child(date);
                    root.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(foodName.getText().toString()).getValue() != null){
                                Toast.makeText(view.getContext(), "Already Posted", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String key = UUID.randomUUID().toString().replace("-","").substring(0,5);
                                HashMap<String, String> userMap = new HashMap<>();
                                userMap.put("foodId", key);
                                userMap.put("foodName", foodName.getText().toString());
                                userMap.put("foodPrice", foodPrice.getText().toString());
                                userMap.put("foodQuantity", foodQuantity.getText().toString());
                                userMap.put("imageUrl", foodUrl.getText().toString());
                                root.child(foodName.getText().toString()).setValue(userMap);

                                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                HashMap<String, Object> userMapX = new HashMap<>();
                                userMapX.put("foodName", foodName.getText().toString());
                                userMapX.put("todayTotal", "0");
                                userMapX.put("todayTotalOrder", "0");
                                userMapX.put("imageUrl", foodUrl.getText().toString());
                                FirebaseDatabase.getInstance().getReference().child("H-Record").child(date).child(foodName.getText().toString()).setValue(userMapX);
                                HashMap<String, Object> userMapZ = new HashMap<>();
                                userMapZ.put("date", date);
                                FirebaseDatabase.getInstance().getReference().child("I-Sales Record").child(date).setValue(userMapZ);
                                Toast.makeText(view.getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }
    }

}
