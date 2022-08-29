package com.example.navdrawertest10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CustomerNotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ModelNotification> list;
    private MyAdapter_Customer_Notification adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.customer_notif_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerviewSales);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new MyAdapter_Customer_Notification(CustomerNotificationFragment.this.getActivity(), list);
        recyclerView.setAdapter(adapter);

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SessionManagement sessionManagement = new SessionManagement(CustomerNotificationFragment.this.getContext());
        String userLogin = sessionManagement.getSession();
        Query root = FirebaseDatabase.getInstance().getReference().child("G-Notification").child(date).child(userLogin).orderByChild("currentTime");

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelNotification model = dataSnapshot.getValue(ModelNotification.class);
                    list.add(model);
                }
//                Collections.reverse(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
