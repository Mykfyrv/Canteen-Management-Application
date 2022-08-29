package com.example.navdrawertest10;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CanteenPostFragment extends Fragment {

    private Button btnAdd;
    private RecyclerView recyclerView;
    private ArrayList<ModelFood> list;
    private MyAdapter_Canteen_PostFood adapter;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("A-Food List");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.canteen_post_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAdd = (Button)view.findViewById(R.id.btnAdd);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerviewSales);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new MyAdapter_Canteen_PostFood(CanteenPostFragment.this.getActivity(), list);
        recyclerView.setAdapter(adapter);


        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                     ModelFood model = dataSnapshot.getValue(ModelFood.class);
                     list.add(model);
                 }
                 adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CanteenPostFragment.this.getActivity(), CanteenUploadFoodAct.class);
                startActivity(intent);
            }
        });

    }
}
