package com.example.navdrawertest10;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CanteenUpdateMenuAct extends AppCompatActivity {
    ImageView imageView;
    private TextView uMenuname, umenuprice ;
    private String umenuId, Umenuid, Umenuquantity, Umenuprice, Umenuimage;
    private Button updatefood;
    private EditText  umenuquantity;
    private ProgressBar uprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canteen_update_menu_act);

        imageView = (ImageView) findViewById(R.id.UpdatemimageView);
        updatefood = (Button) findViewById(R.id.updatemenu_btn);
        uMenuname = (TextView) findViewById(R.id.UpdatemenuName);
        umenuprice = (TextView) findViewById(R.id.UpdatemenuPrice);
        umenuquantity = (EditText) findViewById(R.id.UpdatemenuQuan);
        uprogressBar = (ProgressBar) findViewById(R.id.umprogressBar);
        Bundle menubundle = getIntent().getExtras();

        if(menubundle != null) {
            umenuId = menubundle.getString("uId");
            Umenuid = menubundle.getString("foodId");
            Umenuquantity = menubundle.getString("foodQuantity");
            Umenuprice = menubundle.getString("foodPrice");
            Umenuimage = menubundle.getString("imageUrl");
            uMenuname.setText(umenuId);
            umenuprice.setText(Umenuprice);
            umenuquantity.setText(Umenuquantity);
            Glide.with(CanteenUpdateMenuAct.this).load(Umenuimage).into(imageView);


            updatefood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String menuname = uMenuname.getText().toString().trim();
                    String menuprice = umenuprice.getText().toString().trim();
                    String menuquantity = umenuquantity.getText().toString().trim();
                    Bundle menubundle1 = getIntent().getExtras();
                    if(TextUtils.isEmpty(menuquantity)){
                        umenuquantity.setError("This Field Required.");
                        return;
                    }
                    if(menubundle1 != null){
                        String id = Umenuid;
                        String image = Umenuimage;
                        uprogressBar.setVisibility(View.VISIBLE);
                        updatefoodToFire(id, menuname, menuprice, menuquantity, image);
                    }else {
                        Toast.makeText(CanteenUpdateMenuAct.this, "Error! " , Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }
    private void updatefoodToFire(String id, String menun, String menup, String menuq , String mimage){
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        HashMap<String, Object> list = new HashMap<>();
        list.put("foodId", id);
        list.put("foodName", menun);
        list.put("foodPrice", menup);
        list.put("foodQuantity", menuq);
        list.put("imageUrl", mimage);
        FirebaseDatabase.getInstance().getReference("B-Posted Food").child(date).child(menun).setValue(list);
        startActivity(new Intent(getApplicationContext(),ZCanteenDrawer_MainAct.class));




    }
}