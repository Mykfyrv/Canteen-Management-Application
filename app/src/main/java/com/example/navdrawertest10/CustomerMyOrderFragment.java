package com.example.navdrawertest10;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
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
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class CustomerMyOrderFragment extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<ModelFinalOrder> list;
    private MyAdapter_Customer_My_Orders adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.customer_my_order_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerviewSales);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new MyAdapter_Customer_My_Orders(CustomerMyOrderFragment.this.getActivity(), list);
        recyclerView.setAdapter(adapter);

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel channel=new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
//            NotificationManager manager =CustomerMyOrderFragment.this.getContext().getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SessionManagement sessionManagement = new SessionManagement(CustomerMyOrderFragment.this.getContext());
        String userLogin = sessionManagement.getSession();
        Query root = FirebaseDatabase.getInstance().getReference().child("E-Customer Orders").child(date).child(userLogin).orderByChild("currentTime");

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelFinalOrder model = dataSnapshot.getValue(ModelFinalOrder.class);
                    list.add(model);
//                    if(model.getStatus().equals("Ready")){
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(CustomerMyOrderFragment.this.getContext(), "My Notification" )
//                                .setSmallIcon(R.drawable.ic_logout)
//                                .setContentTitle(model.getOrderId())
//                                .setContentText(model.getStatus())
//                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(CustomerMyOrderFragment.this.getContext());
//                        managerCompat.notify(1,builder.build());
//                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
