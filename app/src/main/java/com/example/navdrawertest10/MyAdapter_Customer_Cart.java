package com.example.navdrawertest10;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class MyAdapter_Customer_Cart extends RecyclerView.Adapter<MyAdapter_Customer_Cart.MyViewHolder> {

    private ArrayList<ModelOrder> mList;
    private Context context;

    public MyAdapter_Customer_Cart(Context context, ArrayList<ModelOrder> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_customer_cart, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelOrder model = mList. get(position);
        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);
        holder.foodName.setText(model.getFoodName());
        holder.foodPrice.setText(model.getFoodPrice());
        holder.foodQuantity.setText(model.getFoodQuantity());
        holder.foodUrl.setText(model.getImageUrl());
        holder.foodId.setText(model.getFoodId());
        holder.orderId.setText(model.getOrderId());
        holder.customerName.setText(model.getCustomerName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView foodName, foodPrice, foodUrl, foodId, orderId, customerName;
        EditText foodQuantity;
        CheckBox checkBox;
        TextView btnPlus,btnMinus;
        Long currentTime;

       public MyViewHolder(@NonNull View itemView) {
           super(itemView);

           imageView = itemView.findViewById(R.id.m_image);
           orderId = itemView.findViewById(R.id.idCustomer_holder);
           customerName = itemView.findViewById(R.id.nameCustomer_holder);
           foodId = itemView.findViewById(R.id.id_holder);
           foodName = itemView.findViewById(R.id.m_name);
           foodPrice = itemView.findViewById(R.id.m_price);
           foodUrl = itemView.findViewById(R.id.m_url);
           foodQuantity = itemView.findViewById(R.id.m_orderno);
           checkBox = itemView.findViewById(R.id.checkBox);
           btnPlus = itemView.findViewById(R.id.txtAdd);
           btnMinus = itemView.findViewById(R.id.txtMinus);
           currentTime = System.currentTimeMillis();
           SessionManagement sessionManagement = new SessionManagement(itemView.getContext());
           String userLogin = sessionManagement.getSession();
           String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

           FirebaseDatabase db = FirebaseDatabase.getInstance();
           DatabaseReference root = db.getReference().child("D-Order Holder").child(date).child(userLogin); //Order Holder + session name
           root.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.child(orderId.getText().toString()).getValue() != null){
                       checkBox.setChecked(true);
                   }
               }
               @Override
               public void onCancelled(@NonNull DatabaseError error) {
               }
           });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()){
                        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        db.getReference().child("B-Posted Food").child(todayDate).child(foodName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                                    Object Serving = map.get("foodQuantity");
                                    int serve = Integer.parseInt(String.valueOf(Serving));
                                    db.getReference().child("C-Order").child(todayDate).child(customerName.getText().toString()).child(foodId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Map<String, Object> current = (Map<String, Object>) snapshot.getValue();
                                            Object Quant = current.get("foodQuantity");
                                            int quant = Integer.parseInt(String.valueOf(Quant));

                                            if(Serving.equals("0")) {
                                                db.getReference().child("C-Order").child(todayDate).child(customerName.getText().toString()).child(foodId.getText().toString()).removeValue();
                                                Toast.makeText(view.getContext(), "Out Of Stock", Toast.LENGTH_SHORT).show();
                                                Fragment fragment = new CustomerCartFragment();
                                                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                            }
                                            else if((serve - quant) < 0 ){
                                                Toast.makeText(view.getContext(), "Order is too many", Toast.LENGTH_SHORT).show();
                                                checkBox.setChecked(false);

                                            }else{
                                                DatabaseReference root = db.getReference().child("D-Order Holder").child(date).child(userLogin);
                                                HashMap<String, String> userMap = new HashMap<>();
                                                userMap.put("orderId", orderId.getText().toString());
                                                userMap.put("customerName", customerName.getText().toString());
                                                userMap.put("foodId", foodId.getText().toString());
                                                userMap.put("foodName", foodName.getText().toString());
                                                userMap.put("foodPrice", foodPrice.getText().toString());
                                                userMap.put("foodQuantity", foodQuantity.getText().toString());
                                                userMap.put("imageUrl", foodUrl.getText().toString());
                                                userMap.put("currentTime", currentTime.toString());
                                                root.child(orderId.getText().toString()).setValue(userMap);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        String deleteKey = orderId.getText().toString();
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference root = db.getReference().child("D-Order Holder").child(date).child(userLogin).child(deleteKey);
                        root.removeValue();
                    }
                }
            });
            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference("B-Posted Food").child(date).child(foodName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Map<String, Object> nam = (Map<String, Object>) snapshot.getValue();
                            Object fcount = nam.get("foodQuantity");
                            int tCount = Integer.parseInt(String.valueOf(fcount));
                            int textQuant = Integer.parseInt(String.valueOf(foodQuantity.getText().toString()));
                            if (textQuant >= tCount){
                                Toast.makeText(view.getContext(),"Already reach maximum" ,Toast.LENGTH_SHORT).show();
                            }
                            else{
                                FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.getText().toString())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Map<String, Object> lap = (Map<String, Object>) snapshot.getValue();
                                                Object Quant = lap.get("foodQuantity");
                                                int tQuant = Integer.parseInt(String.valueOf(Quant));
                                                Integer newQuant = tQuant + 1;
                                                HashMap<String, Object> qMap = new HashMap<>();
                                                qMap.put("foodQuantity", newQuant.toString());
                                                FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.getText().toString()).updateChildren(qMap);
                                                FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin).child(orderId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.getValue() != null){
                                                            HashMap<String, Object> qMaps = new HashMap<>();
                                                            qMaps.put("foodQuantity", newQuant.toString());
                                                            FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin).child(orderId.getText().toString()).updateChildren(qMaps);
                                                            Fragment fragment = new CustomerCartFragment();
                                                            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                                            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                                        }else {
                                                            Fragment fragment = new CustomerCartFragment();
                                                            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                                            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

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
            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.getText().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Map<String, Object> lap = (Map<String, Object>) snapshot.getValue();
                                    Object Quant = lap.get("foodQuantity");
                                    int tQuant = Integer.parseInt(String.valueOf(Quant));
                                    Integer newQuant = tQuant - 1;
                                    if (newQuant.equals(0)) {
                                        FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.getText().toString()).removeValue();
                                        FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin).child(orderId.getText().toString()).removeValue();
                                        Fragment fragment = new CustomerCartFragment();
                                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

                                    } else {
                                        HashMap<String, Object> qMap = new HashMap<>();
                                        qMap.put("foodQuantity", newQuant.toString());
                                        FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.getText().toString()).updateChildren(qMap);
                                        FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin).child(orderId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.getValue() != null){
                                                    HashMap<String, Object> qMaps = new HashMap<>();
                                                    qMaps.put("foodQuantity", newQuant.toString());
                                                    FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin).child(orderId.getText().toString()).updateChildren(qMaps);
                                                    Fragment fragment = new CustomerCartFragment();
                                                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                                }else {
                                                    Fragment fragment = new CustomerCartFragment();
                                                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
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
           foodQuantity.setOnKeyListener(new View.OnKeyListener() {
               @Override
               public boolean onKey(View view, int i, KeyEvent keyEvent) {
                   if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                       String orderCode = orderId.getText().toString().trim();
                       String quant = foodQuantity.getText().toString().trim();
                       String  fName = foodName.getText().toString().trim();
                       FirebaseDatabase.getInstance().getReference("B-Posted Food").child(date).child(fName).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               Map<String, Object> nam = (Map<String, Object>) snapshot.getValue();
                               Object fcount = nam.get("foodQuantity");
                               int tCount = Integer.parseInt(String.valueOf(fcount));
                               int textQuant = Integer.parseInt(String.valueOf(quant));
                               if (textQuant > tCount){
                                   Toast.makeText(view.getContext(),"The maximum Quantity is " + tCount ,Toast.LENGTH_SHORT).show();
                                   String fquant = String.valueOf(tCount);
                                   foodQuantity.setText(fquant);
                                   FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.getText().toString())
                                           .addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                   for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                           HashMap<String, Object> qMap = new HashMap<>();
                                                           qMap.put("foodQuantity", fquant);
                                                           FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.getText().toString()).updateChildren(qMap);
                                                           FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin).child(orderCode).addListenerForSingleValueEvent(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                   if(snapshot.getValue() != null){
                                                                       HashMap<String, Object> qMaps = new HashMap<>();
                                                                       qMaps.put("foodQuantity", fquant);
                                                                       FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin).child(orderCode).updateChildren(qMaps);
                                                                       Fragment fragment = new CustomerCartFragment();
                                                                       FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                                                       fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                                                   }else {
                                                                       Fragment fragment = new CustomerCartFragment();
                                                                       FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                                                       fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
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

                               }else {
                                   FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.getText().toString())
                                           .addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                   for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                       Map<String, Object> lap = (Map<String, Object>) snapshot.getValue();
                                                       Object Quant = lap.get("foodQuantity");
                                                       if (quant.equals("") || quant.equals("0") || quant.equals("00") || quant.equals("000")) {
                                                           foodQuantity.setText(Quant.toString());
                                                       } else {
                                                           HashMap<String, Object> qMap = new HashMap<>();
                                                           qMap.put("foodQuantity", quant);
                                                           FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.getText().toString()).updateChildren(qMap);
                                                           FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin).child(orderCode).addListenerForSingleValueEvent(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                   if(snapshot.getValue() != null){
                                                                       HashMap<String, Object> qMaps = new HashMap<>();
                                                                       qMaps.put("foodQuantity", quant);
                                                                       FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin).child(orderCode).updateChildren(qMaps);
                                                                       Fragment fragment = new CustomerCartFragment();
                                                                       FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                                                       fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                                                   }else {
                                                                       Fragment fragment = new CustomerCartFragment();
                                                                       FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                                                       fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                                                   }
                                                               }

                                                               @Override
                                                               public void onCancelled(@NonNull DatabaseError error) {

                                                               }
                                                           });



                                                       }
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


                       return true;
                   }
                   return false;
               }
           });
       }
   }


}
