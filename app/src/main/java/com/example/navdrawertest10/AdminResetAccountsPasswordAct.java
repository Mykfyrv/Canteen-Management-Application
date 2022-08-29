package com.example.navdrawertest10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminResetAccountsPasswordAct extends AppCompatActivity {
    private String uId, uUsername, uPassword;
    private Button update, cancel;
    private TextView tUsername;
    private EditText ePassword;
    FirebaseFirestore db;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_reset_accounts_password_act);

        db = FirebaseFirestore.getInstance();
        update =findViewById(R.id.reset_pass);
        cancel = findViewById(R.id.AdminBack);
        tUsername=findViewById(R.id.textUsername);
        ePassword=findViewById(R.id.updatePassword);
        progressBar = findViewById(R.id.progress_Bar);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            uUsername = bundle.getString("uUsername");
            uPassword = bundle.getString("uPassword");
            tUsername.setText(uUsername);
            ePassword.setText(uPassword);
            update.setEnabled(false);

            ePassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String  input = ePassword.getText().toString().trim();
                    String set = uPassword;
                    update.setEnabled(!input.equals(set));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uusername = uUsername;
                    String password = ePassword.getText().toString();
                    Bundle bundle1 = getIntent().getExtras();
                    if (TextUtils.isEmpty(password)) {
                        ePassword.setError("This Field Required.");
                        return;
                    }
                    if (password.length() < 6 || password.length() > 16) {
                        ePassword.setError("Password must be 6 to 16 Characters");
                    } else {
                        if (bundle1 != null) {
                            progressBar.setVisibility(View.VISIBLE);
                            updateToFirestore(uusername, password);
                        } else {
                            Toast.makeText(AdminResetAccountsPasswordAct.this, "Error! ", Toast.LENGTH_SHORT).show();
                        }
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
    }
    private void updateToFirestore(String username, String password){
        db.collection("accounts").document(username).update("password", password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AdminResetAccountsPasswordAct.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),ZAdminDrawer_MainAct.class));
                }else{
                    Toast.makeText(AdminResetAccountsPasswordAct.this, "Error : " +task.getException(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminResetAccountsPasswordAct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}