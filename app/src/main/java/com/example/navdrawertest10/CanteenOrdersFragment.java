package com.example.navdrawertest10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class CanteenOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ModelFinalOrder> list;
    private MyAdapter_Canteen_Orders adapter;
    private Button refresh;
    private TextView pending, ready, confirm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.canteen_orders_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pending = (TextView)view.findViewById(R.id.orderPend);
        ready = (TextView)view.findViewById(R.id.orderReady);
        confirm = (TextView)view.findViewById(R.id.orderConfirm);
        refresh = (Button)view.findViewById(R.id.orderRefresh);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerviewOrders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new MyAdapter_Canteen_Orders(CanteenOrdersFragment.this.getActivity(), list);
        recyclerView.setAdapter(adapter);

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Query root = FirebaseDatabase.getInstance().getReference().child("E-Canteen Orders").child(date).orderByChild("currentTime");

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelFinalOrder model = dataSnapshot.getValue(ModelFinalOrder.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
//                Collections.reverse(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null){
                    refresh.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference().child("E-Canteen Orders").child(date)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long totalPendingOrder = 0;
                        totalPendingOrder = snapshot.getChildrenCount();
                        pending.setText(String.valueOf(totalPendingOrder));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }); //pending orders.

        FirebaseDatabase.getInstance().getReference().child("F-Canteen Orders(Ready)").child(date)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long totalOrderReady = 0;
                        totalOrderReady = snapshot.getChildrenCount();
                        ready.setText(String.valueOf(totalOrderReady));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }); //orders ready.

        FirebaseDatabase.getInstance().getReference().child("F-Canteen Orders(Confirm)").child(date)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long totalOrderConfirm = 0;
                        totalOrderConfirm = snapshot.getChildrenCount();
                        confirm.setText(String.valueOf(totalOrderConfirm));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }); //orders confirm.

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("F-Canteen Orders(Confirm)").child(date)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()){
                                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                                    Object orderId = map.get("orderId");
                                    FirebaseDatabase.getInstance().getReference("E-Canteen Orders").child(date).child(orderId.toString()).removeValue();
                                    FirebaseDatabase.getInstance().getReference("F-Canteen Orders(Ready)").child(date).child(orderId.toString()).removeValue();
                                    FirebaseDatabase.getInstance().getReference("F-Canteen Orders(Confirm)").child(date).child(orderId.toString()).removeValue();
                                    Fragment fragment = new CanteenOrdersFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
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
