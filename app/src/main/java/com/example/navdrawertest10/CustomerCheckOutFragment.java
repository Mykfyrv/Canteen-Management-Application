package com.example.navdrawertest10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CustomerCheckOutFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ModelOrder> list;
    private MyAdapter_Customer_Checkout adapter;
    private TextView totalAmount, totalAmountText;
    private Spinner spinner;
    private Button btnPlaceHolder;
    private Long currentTime;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.customer_check_out_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        totalAmount = (TextView)view.findViewById(R.id.totalAmount);
        totalAmountText = (TextView)view.findViewById(R.id.textViewTotal);
        spinner = (Spinner)view.findViewById(R.id.spinner);
        btnPlaceHolder = (Button)view.findViewById(R.id.btnPlaceOrder);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerviewSales);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new MyAdapter_Customer_Checkout(CustomerCheckOutFragment.this.getActivity(), list);
        recyclerView.setAdapter(adapter);
        currentTime = System.currentTimeMillis();

        SessionManagement sessionManagement = new SessionManagement(CustomerCheckOutFragment.this.getContext());
        String userLogin = sessionManagement.getSession();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Query root = FirebaseDatabase.getInstance().getReference().child("D-Order Holder").child(date).child(userLogin).orderByChild("currentTime");

        totalAmount.setVisibility(View.INVISIBLE);
        totalAmountText.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        btnPlaceHolder.setVisibility(View.INVISIBLE);

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
                if (snapshot.exists()){
                    totalAmount.setVisibility(View.VISIBLE);
                    totalAmountText.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    btnPlaceHolder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); //Invisible UI

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;
                for (DataSnapshot ds : snapshot.getChildren()){
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object price = map.get("foodPrice");
                    Object quant = map.get("foodQuantity");
                    int fQuant = Integer.parseInt(String.valueOf(quant));
                    int pValue = Integer.parseInt(String.valueOf(price));
                    int Amount = fQuant * pValue;
                    sum += Amount;
                    totalAmount.setText(String.valueOf(sum));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        }); //Total order price.


        ArrayAdapter<CharSequence> tableNoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.TableNo, android.R.layout.simple_spinner_item);
        tableNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(tableNoAdapter);
        spinner.setPrompt("Select Table No.");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String choice = adapterView.getItemAtPosition(i).toString();
                    btnPlaceHolder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!choice.equals("NA")) {
                            FirebaseDatabase.getInstance().getReference("D-Order Holder").child(date).child(userLogin)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                Map<String, Object> map = (Map<String, Object>) ds.getValue();
                                                //get values.
                                                Object customerName = map.get("customerName");
                                                Object foodId = map.get("foodId");
                                                Object foodName = map.get("foodName");
                                                Object foodPrice = map.get("foodPrice");
                                                Object foodQuantity = map.get("foodQuantity");
                                                Object imageUrl = map.get("imageUrl");
                                                Object orderId = map.get("orderId");
                                                int fQuant = Integer.parseInt(String.valueOf(foodQuantity));
                                                int pValue = Integer.parseInt(String.valueOf(foodPrice));
                                                Integer sumT = fQuant *pValue;

                                                //store.
                                                HashMap<String, Object> userMap = new HashMap<>();
                                                userMap.put("orderId", orderId);
                                                userMap.put("customerName", customerName);
                                                userMap.put("foodId", foodId);
                                                userMap.put("foodName", foodName);
                                                userMap.put("foodPrice", foodPrice);
                                                userMap.put("foodQuantity", foodQuantity);
                                                userMap.put("totalPrice", sumT.toString());
                                                userMap.put("imageUrl", imageUrl);
                                                userMap.put("dateOrder", date);
                                                userMap.put("tableNo", choice);
                                                userMap.put("status", "-");
                                                userMap.put("currentTime", currentTime.toString());
                                                //input.
                                                FirebaseDatabase.getInstance().getReference("E-Customer Orders").child(date).child(userLogin).child(orderId.toString()).setValue(userMap);
                                                FirebaseDatabase.getInstance().getReference("E-Canteen Orders").child(date).child(orderId.toString()).updateChildren(userMap);
                                                //delete 1.
                                                FirebaseDatabase db1 = FirebaseDatabase.getInstance();
                                                DatabaseReference root1 = db1.getReference().child("C-Order").child(date).child(userLogin).child(foodId.toString());
                                                root1.removeValue();
                                                //delete 2.
                                                FirebaseDatabase db2 = FirebaseDatabase.getInstance();
                                                DatabaseReference root2 = db2.getReference().child("D-Order Holder").child(date).child(userLogin).child(orderId.toString());
                                                root2.removeValue();
                                                //message.
                                                Toast.makeText(CustomerCheckOutFragment.this.getContext(), "Order Success", Toast.LENGTH_SHORT).show();
                                            }
                                            Fragment fragment = new CustomerMyOrderFragment();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                        }
                            else{
                                Toast.makeText(CustomerCheckOutFragment.this.getContext(),"Invalid Table Number",Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner.setSelection(0);
            }
        });
    }
}


