package com.example.navdrawertest10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.Map;

public class AdminSalesFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<ModelRecord> list;
    private MyAdapter_Canteen_Sales adapter;
    private TextView dateX, order, sales;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_sales_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateX = (TextView)view.findViewById(R.id.salesDate);
        order = (TextView)view.findViewById(R.id.salesTotalOrder);
        sales = (TextView)view.findViewById(R.id.salesTotalSale);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerviewSales);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new MyAdapter_Canteen_Sales(AdminSalesFragment.this.getActivity(), list);
        recyclerView.setAdapter(adapter);


        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Query root = FirebaseDatabase.getInstance().getReference().child("H-Record").child(date);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelRecord model = dataSnapshot.getValue(ModelRecord.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalSale = 0;
                int totalOrder = 0;
                for (DataSnapshot ds : snapshot.getChildren()){
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object saleX = map.get("todayTotal");
                    Object orderX = map.get("todayTotalOrder");
                    int pValueSale = Integer.parseInt(String.valueOf(saleX));
                    int pValueOrder = Integer.parseInt(String.valueOf(orderX));
                    totalSale += pValueSale;
                    totalOrder += pValueOrder;
                    sales.setText(String.valueOf(totalSale));
                    order.setText(String.valueOf(totalOrder));
                    dateX.setText(date);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        }); //Total order price.

    }
}
