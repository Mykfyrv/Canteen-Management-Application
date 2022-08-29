package com.example.navdrawertest10;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class CustomerChangePasswordFragment extends Fragment {

    private Button change, cancel;
    private EditText cPassword,nPassword,cfPassword;
    private ProgressBar aprogressBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.customer_change_password_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        change =(Button) view.findViewById(R.id.changeCustomer_pass);
        cPassword = (EditText) view.findViewById(R.id.Customer_pass);
        nPassword = (EditText) view.findViewById(R.id.enterCAPassword);
        cfPassword = (EditText) view.findViewById(R.id.confirmCAPassword);
        aprogressBar = (ProgressBar) view.findViewById(R.id.CAprogressBar);
        cancel = (Button) view.findViewById(R.id.cancelCustomer_pass);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChange(cPassword.getText().toString().trim(), nPassword.getText().toString().trim() ,cfPassword.getText().toString().trim());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerChangePasswordFragment.this.getActivity(), ZCustomerDrawer_MainAct.class));
            }
        });


    }
    private void setChange(String CPassword, String NPassword, String CFPassword){
        SessionManagement sessionManagement = new SessionManagement(CustomerChangePasswordFragment.this.getContext());
        String userID = sessionManagement.getSession();

        if(TextUtils.isEmpty(CPassword) && TextUtils.isEmpty(NPassword) && TextUtils.isEmpty(CFPassword)){
            cPassword.setError("This Field Required.");
            nPassword.setError("This Field Required.");
            cfPassword.setError("This Field Required.");
            return;
        }
        else if(TextUtils.isEmpty(NPassword) && TextUtils.isEmpty(CFPassword)){
            nPassword.setError("This Field Required.");
            cfPassword.setError("This Field Required.");
            return;
        }
        else if(TextUtils.isEmpty(CPassword) && TextUtils.isEmpty(CFPassword)){
            cPassword.setError("This Field Required.");
            cfPassword.setError("This Field Required.");
            return;
        }
        else if(TextUtils.isEmpty(CPassword) && TextUtils.isEmpty(NPassword)){
            cPassword.setError("This Field Required.");
            nPassword.setError("This Field Required.");
            return;
        }
        else if(TextUtils.isEmpty(CPassword)){
            cPassword.setError("This Field Required.");
            return;
        }
        else if(TextUtils.isEmpty(NPassword)){
            nPassword.setError("This Field Required.");
            return;
        }
        else if(TextUtils.isEmpty(CFPassword)){
            cfPassword.setError("This Field Required.");
            return;
        }
        else if(NPassword.length() < 6 || NPassword.length() > 16) {
            nPassword.setError("Password must be 6 to 16 Characters");
        }
        else if(!NPassword.equals(CFPassword)) {
            cfPassword.setError("Password Mismatch");
        }
        else if(!"".equals(CPassword) && NPassword.equals(NPassword) && NPassword.length() >= 6 && NPassword.length() <=16) {
            aprogressBar.setVisibility(View.VISIBLE);
            db.collection("accounts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot data : task.getResult()) {
                            String a = data.getString("username");
                            String b = data.getString("password");
                            if (a.equalsIgnoreCase(userID)) {
                                if (!CPassword.equals(b)) {
                                    cPassword.setError("Incorrect Password");
                                    aprogressBar.setVisibility(View.GONE);
                                } else {
                                    db.collection("accounts").document(userID).update("password", NPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(CustomerChangePasswordFragment.this.getActivity(), "Password Change", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(CustomerChangePasswordFragment.this.getActivity(), ZCustomerDrawer_MainAct.class));
                                            } else {
                                                Toast.makeText(CustomerChangePasswordFragment.this.getActivity(), "Error : " + task.getException(), Toast.LENGTH_SHORT).show();
                                                aprogressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CustomerChangePasswordFragment.this.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            aprogressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
