package com.example.navdrawertest10;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class MyAdapter_Customer_Menu extends RecyclerView.Adapter<MyAdapter_Customer_Menu.MyViewHolder> {

    private ArrayList<ModelFood> mList;
    private Context context;

    public MyAdapter_Customer_Menu(Context context, ArrayList<ModelFood> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemlist_customer_menu, parent, false);
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
        Button addToCartBtn;
        Long currentTime;
        Object food;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.m_image);
            foodName = itemView.findViewById(R.id.m_name);
            foodPrice = itemView.findViewById(R.id.m_price);
            foodUrl = itemView.findViewById(R.id.m_url);
            foodId = itemView.findViewById(R.id.id_holder);
            foodQuantity = itemView.findViewById(R.id.m_orderno);
            addToCartBtn = itemView.findViewById(R.id.button);
            currentTime = System.currentTimeMillis();
            foodId.setVisibility(View.INVISIBLE);

            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String key = UUID.randomUUID().toString().replace("-","").substring(0,5);
                    SessionManagement sessionManagement = new SessionManagement(view.getContext());
                    String userLogin = sessionManagement.getSession();
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    db.getReference().child("C-Order").child(date).child(userLogin).child(foodId.getText().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue()!= null) {
                                Map<String, Object> list = (Map<String, Object>) snapshot.getValue();
                                Object type = list.get("foodName");
                                    if (type.equals(foodName.getText().toString())) {
                                                    FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.getText().toString())
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            Map<String, Object> lap = (Map<String, Object>) snapshot.getValue();
                                                            Object Quant = lap.get("foodQuantity");
                                                            Object orderId = lap.get("orderId");
                                                            int tQuant = Integer.parseInt(String.valueOf(Quant));
                                                            Integer newQuant = tQuant + 1;
                                                            if (tQuant >= Integer.parseInt(String.valueOf(foodQuantity.getText().toString()))) {
                                                                Toast.makeText(view.getContext(), "Added the maximum serving", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                HashMap<String, Object> qMap = new HashMap<>();
                                                                qMap.put("foodQuantity", newQuant.toString());
                                                                FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.getText().toString()).updateChildren(qMap);
                                                                FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin).child(orderId.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if (snapshot.getValue() != null) {
                                                                            HashMap<String, Object> qMaps = new HashMap<>();
                                                                            qMaps.put("foodQuantity", newQuant.toString());
                                                                            FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin).child(orderId.toString()).updateChildren(qMaps);
                                                                            Toast.makeText(view.getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            Toast.makeText(view.getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });

                                                            }
                                                        }


                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });


                                    } else {
                                          db.getReference().child("B-Posted Food").child(date).child(foodName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                                                Object Serving = map.get("foodQuantity");
                                                if (!Serving.equals("0")) {
                                                    HashMap<String, String> userMap = new HashMap<>();
                                                    userMap.put("orderId", key);
                                                    userMap.put("customerName", userLogin);
                                                    userMap.put("foodId", foodId.getText().toString());
                                                    userMap.put("foodName", foodName.getText().toString());
                                                    userMap.put("foodPrice", foodPrice.getText().toString());
                                                    userMap.put("foodQuantity", "1");
                                                    userMap.put("imageUrl", foodUrl.getText().toString());
                                                    userMap.put("currentTime", currentTime.toString());
                                                    FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.getText().toString())
                                                            .setValue(userMap);
                                                    Toast.makeText(view.getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(view.getContext(), "Out Of Stock", Toast.LENGTH_SHORT).show();
                                                }
                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                            }else{
                                db.getReference().child("B-Posted Food").child(date).child(foodName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                                        Object Serving = map.get("foodQuantity");
                                        if (!Serving.equals("0")) {
                                            HashMap<String, String> userMap = new HashMap<>();
                                            userMap.put("orderId", key);
                                            userMap.put("customerName", userLogin);
                                            userMap.put("foodId", foodId.getText().toString());
                                            userMap.put("foodName", foodName.getText().toString());
                                            userMap.put("foodPrice", foodPrice.getText().toString());
                                            userMap.put("foodQuantity", "1");
                                            userMap.put("imageUrl", foodUrl.getText().toString());
                                            userMap.put("currentTime", currentTime.toString());
                                            FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.getText().toString())
                                                    .setValue(userMap);
                                            Toast.makeText(view.getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(view.getContext(), "Out Of Stock", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
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
