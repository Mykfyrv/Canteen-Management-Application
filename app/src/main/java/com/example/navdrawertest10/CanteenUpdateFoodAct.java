package com.example.navdrawertest10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CanteenUpdateFoodAct extends AppCompatActivity {

    ImageView imageView;
    private TextView uFoodname;
    private String uId, Ufoodid, Ufoodquantity, Uprice, Uimage;
    private Button updatefood;
    private EditText ufoodprice, ufoodquantity;
    private ProgressBar uprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canteen_update_food_act);

        imageView = (ImageView) findViewById(R.id.UpdateimageView);
        updatefood = (Button) findViewById(R.id.update_btn);
        uFoodname = (TextView) findViewById(R.id.UpdatefoodName);
        ufoodprice = (EditText) findViewById(R.id.UpdatefoodPrice);
        ufoodquantity = (EditText) findViewById(R.id.UpdatefoodQuan);
        uprogressBar = (ProgressBar) findViewById(R.id.uprogressBar);
        Bundle foodbundle = getIntent().getExtras();

        if(foodbundle != null) {
            uId = foodbundle.getString("uId");
            Ufoodid = foodbundle.getString("foodId");
            Ufoodquantity = foodbundle.getString("foodQuantity");
            Uprice = foodbundle.getString("foodPrice");
            Uimage = foodbundle.getString("imageUrl");
            uFoodname.setText(uId);
            ufoodprice.setText(Uprice);
            ufoodquantity.setText(Ufoodquantity);
            Glide.with(CanteenUpdateFoodAct.this).load(Uimage).into(imageView);


            updatefood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String foodname = uFoodname.getText().toString().trim();
                    String foodprice = ufoodprice.getText().toString().trim();
                    String foodquantity = ufoodquantity.getText().toString().trim();
                    Bundle foodbundle1 = getIntent().getExtras();
                    if(TextUtils.isEmpty(foodprice) && TextUtils.isEmpty(foodquantity)){
                        ufoodprice.setError("This Field Required.");
                        ufoodquantity.setError("This Field Required.");
                        return;
                    }
                    if(TextUtils.isEmpty(foodprice)){
                        ufoodprice.setError("This Field Required.");
                        return;
                    }
                    if(TextUtils.isEmpty(foodquantity)){
                        ufoodquantity.setError("This Field Required.");
                        return;
                    }
                    if(foodbundle1 != null){
                        String id = Ufoodid;
                        String image = Uimage;
                        uprogressBar.setVisibility(View.VISIBLE);
                        updatefoodToFire(id, foodname, foodprice, foodquantity, image);
                    }else {
                        Toast.makeText(CanteenUpdateFoodAct.this, "Error! " , Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }
    private void updatefoodToFire(String id, String foodn, String foodp, String foodq , String fimage){
        HashMap<String, Object> list = new HashMap<>();
        list.put("foodId", id);
        list.put("foodName", foodn);
        list.put("foodPrice", foodp);
        list.put("foodQuantity", foodq);
        list.put("imageUrl", fimage);
        FirebaseDatabase.getInstance().getReference("A-Food List").child(id).setValue(list);
        startActivity(new Intent(getApplicationContext(),ZCanteenDrawer_MainAct.class));




    }
}