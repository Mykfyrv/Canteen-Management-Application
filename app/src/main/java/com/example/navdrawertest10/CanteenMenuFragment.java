package com.example.navdrawertest10;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
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

public class CanteenMenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ModelFood> list;
    private MyAdapter_Canteen_Menu adapter;
    private Button btnDelete, btnConfirm;
    private CheckBox checkBoxDel;
    private View viewSplit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.canteen_menu_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDelete = (Button)view.findViewById(R.id.btnDelete);
        btnConfirm = (Button)view.findViewById(R.id.menuConfirm);
        checkBoxDel = (CheckBox)view.findViewById(R.id.checkBoxDelete);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerviewSales);
        viewSplit = (View)view.findViewById(R.id.view6);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new MyAdapter_Canteen_Menu(CanteenMenuFragment.this.getActivity(), list);
        recyclerView.setAdapter(adapter);

        btnConfirm.setVisibility(View.INVISIBLE);
        btnDelete.setEnabled(false);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Query root = FirebaseDatabase.getInstance().getReference().child("B-Posted Food").child(date).orderByChild("foodName");

        root.addListenerForSingleValueEvent(new ValueEventListener() {
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

        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null){
                    btnDelete.setVisibility(View.INVISIBLE);
                    checkBoxDel.setVisibility(View.INVISIBLE);
                    viewSplit.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        checkBoxDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxDel.isChecked()){
                    btnDelete.setEnabled(true);
                } else {
                    btnDelete.setEnabled(false);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("B-Posted Food(Holder)").child(date)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue() != null){
                                    btnDelete.setVisibility(View.INVISIBLE);
                                    btnConfirm.setVisibility(View.VISIBLE);
                                } else{
                                    Toast.makeText(CanteenMenuFragment.this.getContext(), "Please Select Menu", Toast.LENGTH_SHORT).show();
                                    Fragment fragment = new CanteenMenuFragment();
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
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("B-Posted Food(Holder)").child(date).
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.getValue() != null){
                                    for (DataSnapshot ds : snapshot.getChildren()){
                                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                                        Object foodName = map.get("foodName");
                                        FirebaseDatabase.getInstance().getReference("B-Posted Food(Holder)").child(date).child(foodName.toString()).removeValue();
                                        FirebaseDatabase.getInstance().getReference("B-Posted Food").child(date).child(foodName.toString()).removeValue();
                                        FirebaseDatabase.getInstance().getReference("C-Order").child(date).removeValue().equals(foodName);
                                        FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).removeValue().equals(foodName);
                                        Fragment fragment = new CanteenMenuFragment();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

                                    }
                                } else {
                                    Toast.makeText(CanteenMenuFragment.this.getContext(), "Please Select Menu", Toast.LENGTH_SHORT).show();
                                    Fragment fragment = new CanteenMenuFragment();
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
