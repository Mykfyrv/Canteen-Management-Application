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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyAdapter_Canteen_Orders extends RecyclerView.Adapter<MyAdapter_Canteen_Orders.MyViewHolder> {

    private ArrayList<ModelFinalOrder> mList;
    private Context context;

    public MyAdapter_Canteen_Orders(Context context, ArrayList<ModelFinalOrder> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemlist_canteen_order, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelFinalOrder model = mList. get(position);
        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);
        holder.foodName.setText(model.getFoodName());
        holder.customerName.setText(model.getCustomerName());
        holder.orderNum.setText(model.getOrderId());
        holder.foodPrice.setText(model.getTotalPrice());
        holder.foodQuantity.setText(model.getFoodQuantity());
        holder.tableNo.setText(model.getTableNo());
        holder.date.setText(model.getDateOrder());
        holder.imageUrl.setText(model.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView foodName, customerName, orderNum,foodQuantity, foodPrice, tableNo, date, imageUrl;
        Button btnReady, btnConfirm;
        Long currentTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.m_image);
            customerName = itemView.findViewById(R.id.textName);
            foodName = itemView.findViewById(R.id.textFName);
            orderNum = itemView.findViewById(R.id.textOrder);
            foodPrice = itemView.findViewById(R.id.textPrice);
            foodQuantity =itemView.findViewById(R.id.textQuantity);
            tableNo = itemView.findViewById(R.id.textTableNo);
            imageUrl = itemView.findViewById(R.id.textImageUrl);
            date = itemView.findViewById(R.id.textDate);
            btnReady = itemView.findViewById(R.id.btnReady);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);

            btnConfirm.setEnabled(false);
            currentTime = System.currentTimeMillis();

            SessionManagement sessionManagement = new SessionManagement(itemView.getContext());
            String userLogin = sessionManagement.getSession();
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference root = db.getReference().child("F-Canteen Orders(Ready)").child(date);
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(orderNum.getText().toString()).getValue() != null){
                        btnReady.setEnabled(false);
                        btnConfirm.setEnabled(true);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            FirebaseDatabase dbX = FirebaseDatabase.getInstance();
            DatabaseReference rootX = dbX.getReference().child("F-Canteen Orders(Confirm)").child(date);
            rootX.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(orderNum.getText().toString()).getValue() != null){
                        btnReady.setEnabled(false);
                        btnConfirm.setEnabled(false);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


            btnReady.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currentDateTimeString = java.text.DateFormat.getTimeInstance().format(new Date());
                    String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    db.getReference().child("B-Posted Food").child(todayDate).child(foodName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                                Object Serving = map.get("foodQuantity");
                                if (Serving.equals("0")) {
                                    db.getReference().child("E-Canteen Orders").child(date).child(orderNum.getText().toString()).removeValue();
                                    Toast.makeText(view.getContext(), "Out Of Stock", Toast.LENGTH_SHORT).show();
                                    Fragment fragment = new CanteenOrdersFragment();
                                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                    HashMap<String, Object> userMap = new HashMap<>();
                                    userMap.put("status", "Cancelled");
                                    FirebaseDatabase.getInstance().getReference("E-Customer Orders").child(date).child(customerName.getText().toString())
                                            .child(orderNum.getText().toString()).updateChildren(userMap);
                                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                                    DatabaseReference root1 = db.getReference().child("G-Notification").child(date).child(customerName.getText().toString());
                                    HashMap<String, String> userMap1 = new HashMap<>();
                                    userMap1.put("imageUrl", imageUrl.getText().toString());
                                    userMap1.put("orderStatus", "Cancelled");
                                    userMap1.put("notificationSen", "Order: " + orderNum.getText().toString() + " has been Cancelled");
                                    userMap1.put("dateTime", date + " & " + currentDateTimeString);
                                    userMap1.put("currentTime", currentTime.toString());
                                    root1.child(orderNum.getText().toString()).setValue(userMap1);

                                } else {
                                    HashMap<String, Object> userMap = new HashMap<>();
                                    userMap.put("status", "Ready");
                                    FirebaseDatabase.getInstance().getReference("E-Customer Orders").child(date).child(customerName.getText().toString())
                                            .child(orderNum.getText().toString()).updateChildren(userMap);
                                    btnReady.setEnabled(false);
                                    btnConfirm.setEnabled(true);


                                    FirebaseDatabase.getInstance().getReference("E-Customer Orders").child(date).child(customerName.getText().toString()).child(orderNum.getText().toString())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    FirebaseDatabase.getInstance().getReference("F-Canteen Orders(Ready)").child(date).child(orderNum.getText().toString())
                                                            .setValue(snapshot.getValue());
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                    //notification ready.
                                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                                    DatabaseReference root1 = db.getReference().child("G-Notification").child(date).child(customerName.getText().toString());
                                    HashMap<String, String> userMap1 = new HashMap<>();
                                    userMap1.put("imageUrl", imageUrl.getText().toString());
                                    userMap1.put("orderStatus", "Order Confirmed & Prepared");
                                    userMap1.put("notificationSen", "Order: " + orderNum.getText().toString() + " has been Confirmed and Prepared");
                                    userMap1.put("dateTime", date + " & " + currentDateTimeString);
                                    userMap1.put("currentTime", currentTime.toString());
                                    root1.child(orderNum.getText().toString()).setValue(userMap1);
                                }

                            } else {
                                db.getReference().child("E-Canteen Orders").child(date).child(orderNum.getText().toString()).removeValue();
                                Toast.makeText(view.getContext(), "Out Of Stock", Toast.LENGTH_SHORT).show();
                                Fragment fragment = new CanteenOrdersFragment();
                                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("status", "Cancelled");
                                FirebaseDatabase.getInstance().getReference("E-Customer Orders").child(date).child(customerName.getText().toString())
                                        .child(orderNum.getText().toString()).updateChildren(userMap);
                                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference root1 = db.getReference().child("G-Notification").child(date).child(customerName.getText().toString());
                                HashMap<String, String> userMap1 = new HashMap<>();
                                userMap1.put("imageUrl", imageUrl.getText().toString());
                                userMap1.put("orderStatus", "Cancelled");
                                userMap1.put("notificationSen", "Order: " + orderNum.getText().toString() + " has been Cancelled");
                                userMap1.put("dateTime", date + " & " + currentDateTimeString);
                                userMap1.put("currentTime", currentTime.toString());
                                root1.child(orderNum.getText().toString()).setValue(userMap1);
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currentDateTimeString = java.text.DateFormat.getTimeInstance().format(new Date());
                    String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    db.getReference().child("B-Posted Food").child(todayDate).child(foodName.getText().toString().trim())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                                    Object Serving = map.get("foodQuantity");
                                    int tServing = Integer.parseInt(String.valueOf(Serving));
                                    String Quantity = foodQuantity.getText().toString();
                                    int tQuantity = Integer.parseInt(String.valueOf(Quantity));
                                    Integer newServing = (tServing - tQuantity);
                                    HashMap<String, Object> sMap = new HashMap<>();
                                    sMap.put("foodQuantity", newServing.toString());
                                    FirebaseDatabase.getInstance().getReference("B-Posted Food").child(todayDate).child(foodName.getText().toString()).updateChildren(sMap);
                                    DatabaseReference root = db.getReference().child("H-Record").child(todayDate).child(foodName.getText().toString().trim());
                                    root.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            //update record.
                                            Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                                            Object todayTotal = map.get("todayTotal");
                                            Object todayTotalOrder = map.get("todayTotalOrder");
                                            int tTotalValue = Integer.parseInt(String.valueOf(todayTotal));
                                            int tTotalOrderValue = Integer.parseInt(String.valueOf(todayTotalOrder));
                                            String fPriceX = foodPrice.getText().toString();
                                            int fPrice = Integer.parseInt(String.valueOf(fPriceX));
                                            String TQuantity = foodQuantity.getText().toString();
                                            int ttQuantity = Integer.parseInt(String.valueOf(TQuantity));
                                            Integer newTotal = (tTotalValue + fPrice);
                                            Integer newTotalOrder = (tTotalOrderValue + ttQuantity);
                                            HashMap<String, Object> userMap = new HashMap<>();
                                            userMap.put("todayTotal", newTotal.toString());
                                            userMap.put("todayTotalOrder", newTotalOrder.toString());
                                            FirebaseDatabase.getInstance().getReference("H-Record").child(todayDate).child(foodName.getText().toString()).updateChildren(userMap);
                                            //disable confirm button.
                                            FirebaseDatabase.getInstance().getReference("F-Canteen Orders(Ready)").child(date).child(orderNum.getText().toString())
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            FirebaseDatabase.getInstance().getReference("F-Canteen Orders(Confirm)").child(date).child(orderNum.getText().toString())
                                                                    .setValue(snapshot.getValue());
                                                            HashMap<String, Object> userMap = new HashMap<>();
                                                            userMap.put("status", "Delivered");
                                                            FirebaseDatabase.getInstance().getReference("E-Customer Orders").child(date).child(customerName.getText().toString())
                                                                    .child(orderNum.getText().toString()).updateChildren(userMap);
//                                                            FirebaseDatabase.getInstance().getReference("E-Canteen Orders").child(date).child(orderNum.getText().toString()).removeValue();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                            btnConfirm.setEnabled(false);

                                            //notification confirmed.
                                            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                                            DatabaseReference root1 = db.getReference().child("G-Notification").child(date).child(customerName.getText().toString());
                                            HashMap<String, String> userMap1 = new HashMap<>();
                                            userMap1.put("imageUrl", imageUrl.getText().toString());
                                            userMap1.put("orderStatus", "Deliver");
                                            userMap1.put("notificationSen", "Order: " + orderNum.getText().toString() + " will be Deliver at Table No: " + tableNo.getText().toString());
                                            userMap1.put("dateTime", date + " & " + currentDateTimeString);
                                            userMap1.put("currentTime", currentTime.toString());
                                            root1.child(orderNum.getText().toString() + "x").setValue(userMap1);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    }); //Total order price.
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
