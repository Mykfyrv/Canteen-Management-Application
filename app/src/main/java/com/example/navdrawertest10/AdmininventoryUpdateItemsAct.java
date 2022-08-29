package com.example.navdrawertest10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdmininventoryUpdateItemsAct extends AppCompatActivity {

    private TextView uItemname;
    private String uId, Uitemname, Uitemquantity;
    private Button updateItem, cancel;
    private EditText uItemquantity;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar uprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admininventory_update_items_act);

        updateItem = (Button) findViewById(R.id.update_item_to_inventory);
        cancel = (Button) findViewById(R.id.updateCancel);
        uItemname = (TextView) findViewById(R.id.updateItemName);
        uItemquantity = (EditText) findViewById(R.id.updateItemQuantity);
        uprogressBar = (ProgressBar) findViewById(R.id.update_invent_progress);
        Bundle inventbundle = getIntent().getExtras();
        if(inventbundle != null) {
            uId = inventbundle.getString("uId");
            Uitemname = inventbundle.getString("uItemname");
            Uitemquantity = inventbundle.getString("uItemquantity");
            uItemname.setText(Uitemname);
            uItemquantity.setText(Uitemquantity);
            updateItem.setEnabled(false);

            uItemquantity.addTextChangedListener(textWatcher);

            updateItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String itemname = uItemname.getText().toString().trim();
                    String itemquantity = uItemquantity.getText().toString().trim();
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                    Bundle inventbundle1 = getIntent().getExtras();
                    if(TextUtils.isEmpty(itemquantity)){
                        uItemquantity.setError("This Field Required.");
                        return;
                    }
                    if(inventbundle1 != null){
                        String id = uId;
                        uprogressBar.setVisibility(View.VISIBLE);
                        updateItemToFirestore(id, itemname, itemquantity, date);
                    }else {
                        Toast.makeText(AdmininventoryUpdateItemsAct.this, "Error! " , Toast.LENGTH_SHORT).show();
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
    }
    private void updateItemToFirestore(String id, String itemname, String itemquantity, String date){
        db.collection("inventory").document(itemname).update("id", id,"itemquantity", itemquantity, "date", date).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AdmininventoryUpdateItemsAct.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),ZAdminInventoryDrawer_MainAct.class));
                }else{
                    Toast.makeText(AdmininventoryUpdateItemsAct.this, "Error : " +task.getException(), Toast.LENGTH_SHORT).show();
                    uprogressBar.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdmininventoryUpdateItemsAct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String quant = uItemquantity.getText().toString().trim();

            updateItem.setEnabled(!quant.equals(Uitemquantity));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



}