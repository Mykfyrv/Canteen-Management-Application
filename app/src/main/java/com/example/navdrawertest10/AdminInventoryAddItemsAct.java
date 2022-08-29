package com.example.navdrawertest10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class AdminInventoryAddItemsAct extends AppCompatActivity {
    private EditText itemName, itemQuantity;
    private Button AddItem, cancel;
    private ProgressBar addProgress;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admininventory_add_items_act);

        itemName = (EditText)  findViewById(R.id.enterItemName);
        itemQuantity =(EditText) findViewById(R.id.enterItemQuantity);
        AddItem = (Button) findViewById(R.id.add_item_to_inventory);
        cancel = (Button) findViewById(R.id.Additemcancel);
        addProgress =(ProgressBar) findViewById(R.id.add_invent_progress);

        AddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String itemname = itemName.getText().toString().trim();
                String itemquantity = itemQuantity.getText().toString().trim();
                String key = UUID.randomUUID().toString().replace("-","").substring(0,5);

                if(TextUtils.isEmpty(itemname) && TextUtils.isEmpty(itemquantity)){
                    itemName.setError("This Field Required.");
                    itemQuantity.setError("This Field Required.");
                    return;
                }

                else if(TextUtils.isEmpty(itemname)){
                    itemName.setError("This Field Required.");
                    return;
                }
                else if(TextUtils.isEmpty(itemquantity)) {
                    itemQuantity.setError("This Field Required.");
                    return;
                }
                else {
                    addProgress.setVisibility(View.VISIBLE);
                    addItemToDb(key ,itemname,itemquantity,Date);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ZAdminInventoryDrawer_MainAct.class));
            }
        });
    }
    private void addItemToDb(String id,  String itemname, String itemquantity, String date){
        db.collection("inventory").document(itemname).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Toast.makeText(AdminInventoryAddItemsAct.this, "Item already exists", Toast.LENGTH_SHORT).show();
                    addProgress.setVisibility(View.GONE);
                }else{
                    HashMap<String , Object> map = new HashMap<>();
                    map.put("id" , id);
                    map.put("itemname", itemname);
                    map.put("itemquantity", itemquantity);
                    map.put("date", date);
                    db.collection("inventory").document(itemname).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AdminInventoryAddItemsAct.this, "Item Added", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),ZAdminInventoryDrawer_MainAct.class));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminInventoryAddItemsAct.this, "Failed !!", Toast.LENGTH_SHORT).show();
                            addProgress.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

    }





}