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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AdminRegisterAccountsAct extends AppCompatActivity {

    EditText rUsername,rPassword,cPassword;
    Button rButton,cancel;
    FirebaseFirestore db;
    ProgressBar progressBar;
    DocumentReference userAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_register_accounts_act);
        rUsername = findViewById(R.id.enterUsername);
        rPassword = findViewById(R.id.enterPassword);
        cPassword = findViewById(R.id.confirmPassword);
        rButton = findViewById(R.id.register);
        cancel = findViewById(R.id.cancelRegister);
        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        // Write a message to the database

        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = rUsername.getText().toString().trim();
                String password = rPassword.getText().toString().trim();
                String confirm_password = cPassword.getText().toString().trim();

                if(TextUtils.isEmpty(username) && TextUtils.isEmpty(password) && TextUtils.isEmpty(confirm_password)){
                    rUsername.setError("This Field Required.");
                    rPassword.setError("This Field Required.");
                    cPassword.setError("This Field Required.");
                    return;
                }
                else if(TextUtils.isEmpty(password) && TextUtils.isEmpty(confirm_password)){
                    rPassword.setError("This Field Required.");
                    cPassword.setError("This Field Required.");
                    return;
                }
                else if(TextUtils.isEmpty(username) && TextUtils.isEmpty(confirm_password)){
                    rUsername.setError("This Field Required.");
                    cPassword.setError("This Field Required.");
                    return;
                }
                else if(TextUtils.isEmpty(username) && TextUtils.isEmpty(password)){
                    rUsername.setError("This Field Required.");
                    rPassword.setError("This Field Required.");
                    return;
                }
                else if(TextUtils.isEmpty(username)){
                    rUsername.setError("This Field Required.");
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    rPassword.setError("This Field Required.");
                    return;
                }
                else if(TextUtils.isEmpty(confirm_password)){
                    cPassword.setError("This Field Required.");
                    return;
                }
                else if(password.length() < 6 || password.length() >16) {
                    rPassword.setError("Password must be 6 to 16 Characters");
                }
                else if(!password.equals(confirm_password)) {
                    cPassword.setError("Password Mismatch");
                }
                else if(!"".equals(username) && password.equals(confirm_password) && password.length() >= 6 && password.length() <=16) {
                    progressBar.setVisibility(View.VISIBLE);
                    saveToFireStore(username,password);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ZAdminDrawer_MainAct.class));
            }
        });
    }

    private void saveToFireStore(String username, String password){
        userAcc = db.collection("accounts").document(username);
        userAcc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Toast.makeText(AdminRegisterAccountsAct.this, "Sorry,this user exists", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }else{
                    HashMap<String , Object> map = new HashMap<>();
                    map.put("username", username);
                    map.put("password", password);
                    db.collection("accounts").document(username).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AdminRegisterAccountsAct.this, "User Created", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(),ZAdminDrawer_MainAct.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminRegisterAccountsAct.this, "Error!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }
}