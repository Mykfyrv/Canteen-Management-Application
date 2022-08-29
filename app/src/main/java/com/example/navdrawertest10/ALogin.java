package com.example.navdrawertest10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

public class ALogin extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button Login;
    private MyAdapter_Admin_Accounts adapter;
    private ArrayList<ModelAccounts> arrayList = new ArrayList<>();
    private FirebaseFirestore firestore =FirebaseFirestore.getInstance();
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> map = new ArrayList<>();

    @Override
    public void onBackPressed() {
        SessionManagement sessionManagement = new SessionManagement(ALogin.this);
        String userID = sessionManagement.getSession();
        if(userID.equals("-1")){
            Intent intent = new Intent(ALogin.this, ALogin.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SessionManagement sessionManagement = new SessionManagement(ALogin.this);
        String userID = sessionManagement.getSession();

        if(!userID.equals("-1")){
            switch (userID) {
                case "ADMIN-ACCOUNTS": {
                    Intent intent = new Intent(ALogin.this, ZAdminDrawer_MainAct.class);
                    startActivity(intent);
                    break;
                }
                case "ADMIN-INVENTORY": {
                    Intent intent = new Intent(ALogin.this, ZAdminInventoryDrawer_MainAct.class);
                    startActivity(intent);
                    break;
                }
                case "ADMIN-CANTEEN": {
                    Intent intent = new Intent(ALogin.this, ZCanteenDrawer_MainAct.class);
                    startActivity(intent);
                    break;
                }
                default: {
                    Intent intent = new Intent(ALogin.this, ZCustomerDrawer_MainAct.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alogin);

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Login = (Button)findViewById(R.id.btnLogin);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }

        });
    }

    private void validate(String userName, String userPassword){
        String username = Name.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if(TextUtils.isEmpty(username) && TextUtils.isEmpty(password)){
            Name.setError("Email is Required.");
            Password.setError("Password is Required.");
            return;
        }
        else if(TextUtils.isEmpty(username)){
            Name.setError("Email is Required.");
            return;
        }
        else if(TextUtils.isEmpty(password)){
            Password.setError("Password is Required.");
            return;
        }
        else if(password.length() < 6 || password.length() >16) {
            Password.setError("Password must be 6 to 16 Characters");
        }
        else if(username != "" && password != ""){
            firestore.collection("admin_accounts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot data : task.getResult()) {
                            map.add(data.getString("password"));
                            list.add(data.getString("username"));
                        }
                        String z = map.get(0);
                        String x = list.get(0);
                        String a = map.get(1);
                        String b = list.get(1);
                        String l = map.get(2);
                        String k = list.get(2);
                        if(x.equalsIgnoreCase(username) && z.equalsIgnoreCase(password)){
                            SessionUser user =new SessionUser("ADMIN-ACCOUNTS", "Admin");
                            SessionManagement sessionManagement = new SessionManagement(ALogin.this);
                            sessionManagement.saveSession(user);
                            Intent intent = new Intent(ALogin.this, ZAdminDrawer_MainAct.class);
                            startActivity(intent);
                            return;
                        }
                        else if(b.equalsIgnoreCase(username) && a.equalsIgnoreCase(password)){
                            SessionUser user =new SessionUser("ADMIN-CANTEEN", "Admin_canteen");
                            SessionManagement sessionManagement = new SessionManagement(ALogin.this);
                            sessionManagement.saveSession(user);
                            Intent intent = new Intent(ALogin.this, ZCanteenDrawer_MainAct.class);
                            startActivity(intent);
                            return;
                        }
                        else if(k.equalsIgnoreCase(username) && l.equalsIgnoreCase(password)) {
                            SessionUser user = new SessionUser("ADMIN-INVENTORY", "Admin_inventory");
                            SessionManagement sessionManagement = new SessionManagement(ALogin.this);
                            sessionManagement.saveSession(user);
                            Intent intent = new Intent(ALogin.this, ZAdminInventoryDrawer_MainAct.class);
                            startActivity(intent);
                            return;
                        }else {
                                firestore.collection("accounts").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().exists()) {
                                                String c = task.getResult().getString("username");
                                                String d = task.getResult().getString("password");
                                                if (c.equalsIgnoreCase(username) && d.equalsIgnoreCase(password)) {
                                                    SessionUser user = new SessionUser(username, username);
                                                    SessionManagement sessionManagement = new SessionManagement(ALogin.this);
                                                    sessionManagement.saveSession(user);
                                                    Intent intent = new Intent(ALogin.this, ZCustomerDrawer_MainAct.class);
                                                    startActivity(intent);
                                                }else{
                                                    Toast.makeText(ALogin.this, "Invalid Account!", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(ALogin.this, "Invalid Account!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ALogin.this, "Invalid Account!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                    }
                }
            });
        }
    }
}