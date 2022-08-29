package com.example.navdrawertest10;

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

public class CustomerCartFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ModelOrder> list;
    private MyAdapter_Customer_Cart adapter;
    private Button btnCheckOut, btnDelete, btnConfirm;
    private CheckBox checkBoxDel;
    private View splitter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.customer_cart_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCheckOut = (Button)view.findViewById(R.id.btnCheckout);
        btnDelete = (Button)view.findViewById(R.id.btnDel);
        btnConfirm = (Button)view.findViewById(R.id.btnCnfrm);
        checkBoxDel = (CheckBox)view.findViewById(R.id.checkBoxDel);
        splitter = (View)view.findViewById(R.id.cartViewSplitter);
        showCart();

        recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerviewSales);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new MyAdapter_Customer_Cart(CustomerCartFragment.this.getContext(), list);
        recyclerView.setAdapter(adapter);

        btnCheckOut.setVisibility(View.INVISIBLE);
        btnDelete.setEnabled(false);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SessionManagement sessionManagement = new SessionManagement(CustomerCartFragment.this.getContext());
        String userLogin = sessionManagement.getSession();
        Query root = FirebaseDatabase.getInstance().getReference().child("C-Order").child(date).child(userLogin).orderByChild("currentTime");
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelOrder model = dataSnapshot.getValue(ModelOrder.class);
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
                    btnCheckOut.setVisibility(View.INVISIBLE);
                    btnDelete.setVisibility(View.INVISIBLE);
                    btnConfirm.setVisibility(View.INVISIBLE);
                    checkBoxDel.setVisibility(View.INVISIBLE);
                    splitter.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });





        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query root2 = FirebaseDatabase.getInstance().getReference().child("D-Order Holder").child(date).child(userLogin).orderByChild("currentTime");
                root2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null){
                            btnCheckOut.setVisibility(View.VISIBLE);
                            btnConfirm.setVisibility(View.INVISIBLE);
                            checkBoxDel.setEnabled(false);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference root3 = FirebaseDatabase.getInstance().getReference().child("D-Order Holder").child(date).child(userLogin);
                root3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() != null){
                            Fragment fragment = new CustomerCheckOutFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        } else {
                            Toast.makeText(CustomerCartFragment.this.getContext(), "Please Select Order", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new CustomerCartFragment();
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

        checkBoxDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxDel.isChecked()){
                    btnDelete.setEnabled(true);
                    btnCheckOut.setEnabled(false);
                    btnConfirm.setEnabled(false);
                } else {
                    btnDelete.setEnabled(false);
                    btnCheckOut.setEnabled(true);
                    btnConfirm.setEnabled(true);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object orderId = map.get("orderId");
                            Object foodId = map.get("foodId");
                            FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin).child(orderId.toString()).removeValue();
                            FirebaseDatabase.getInstance().getReference("C-Order").child(date).child(userLogin).child(foodId.toString()).removeValue();
                            Toast.makeText(CustomerCartFragment.this.getContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new CustomerCartFragment();
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
    public void showCart(){


    }
}
