package com.example.navdrawertest10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CanteenUploadFoodAct extends AppCompatActivity {

    private Button uploadBtn;
    private ImageView imageView;
    private ProgressBar progressBar;
    private EditText fName, fPrice, fQuantity;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("A-Food List");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;
    private Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canteen_upload_food_act);

        uploadBtn = findViewById(R.id.upload_btn);
        imageView = findViewById(R.id.imageView);
        drawable =imageView.getDrawable();
        fName = findViewById(R.id.foodName);
        fPrice = findViewById(R.id.foodPrice);
        fQuantity = findViewById(R.id.foodQuan);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imageView.getDrawable() == drawable && TextUtils.isEmpty(fPrice.getText().toString().trim()) && TextUtils.isEmpty(fQuantity.getText().toString().trim()) && TextUtils.isEmpty(fName.getText().toString().trim())){
                    Toast.makeText(CanteenUploadFoodAct.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    fName.setError("This Field Required.");
                    fQuantity.setError("This Field Required.");
                    fPrice.setError("This Field Required.");
                }
                else if (TextUtils.isEmpty(fPrice.getText().toString().trim()) && TextUtils.isEmpty(fQuantity.getText().toString().trim()) && TextUtils.isEmpty(fName.getText().toString().trim())){
                    fName.setError("This Field Required.");
                    fQuantity.setError("This Field Required.");
                    fPrice.setError("This Field Required.");
                }
                else if(imageView.getDrawable() == drawable && TextUtils.isEmpty(fQuantity.getText().toString().trim()) && TextUtils.isEmpty(fName.getText().toString().trim())){
                    Toast.makeText(CanteenUploadFoodAct.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    fName.setError("This Field Required.");
                    fQuantity.setError("This Field Required.");
                }
                else if(imageView.getDrawable() == drawable && TextUtils.isEmpty(fPrice.getText().toString().trim()) && TextUtils.isEmpty(fName.getText().toString().trim())){
                    Toast.makeText(CanteenUploadFoodAct.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    fName.setError("This Field Required.");
                    fPrice.setError("This Field Required.");
                }
                else if(imageView.getDrawable() == drawable && TextUtils.isEmpty(fPrice.getText().toString().trim()) && TextUtils.isEmpty(fQuantity.getText().toString().trim())){
                    Toast.makeText(CanteenUploadFoodAct.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    fQuantity.setError("This Field Required.");
                    fPrice.setError("This Field Required.");
                }
                else if(TextUtils.isEmpty(fQuantity.getText().toString().trim()) && TextUtils.isEmpty(fName.getText().toString().trim())){
                    fName.setError("This Field Required.");
                    fQuantity.setError("This Field Required.");
                }
                else if(imageView.getDrawable() == drawable && TextUtils.isEmpty(fPrice.getText().toString().trim())){
                    Toast.makeText(CanteenUploadFoodAct.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    fPrice.setError("This Field Required.");
                }
                else if(imageView.getDrawable() == drawable && TextUtils.isEmpty(fQuantity.getText().toString().trim())){
                    Toast.makeText(CanteenUploadFoodAct.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    fQuantity.setError("This Field Required.");
                }
                else if(imageView.getDrawable() == drawable && TextUtils.isEmpty(fName.getText().toString().trim())){
                    Toast.makeText(CanteenUploadFoodAct.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    fName.setError("This Field Required.");
                }
                else if(TextUtils.isEmpty(fPrice.getText().toString().trim()) && TextUtils.isEmpty(fQuantity.getText().toString().trim())){
                    fPrice.setError("This Field Required.");
                    fQuantity.setError("This Field Required.");
                }
                else if(TextUtils.isEmpty(fPrice.getText().toString().trim()) && TextUtils.isEmpty(fName.getText().toString().trim())){
                    fPrice.setError("This Field Required.");
                    fName.setError("This Field Required.");
                }
                else if(imageView.getDrawable() == drawable){
                    Toast.makeText(CanteenUploadFoodAct.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(fPrice.getText().toString().trim())){
                    fPrice.setError("This Field Required.");
                }
                else if(TextUtils.isEmpty(fQuantity.getText().toString().trim())){
                    fQuantity.setError("This Field Required.");
                }
                else if(TextUtils.isEmpty(fName.getText().toString().trim())){
                    fName.setError("This Field Required.");
                }
                else {
                    uploadToFirebase(imageUri);
                }
            }
        });

    }// end of On create
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void uploadToFirebase(Uri uri){
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        FirebaseDatabase.getInstance().getReference("A-Food List").child(fName.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null){
                        Toast.makeText(CanteenUploadFoodAct.this, "Food Already Exist", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
                        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String TempFoodName = fName.getText().toString().trim();
                                        String TempFoodPrice = fPrice.getText().toString().trim();
                                        String TempFoodQuantity = fQuantity.getText().toString().trim();
                                        String key = UUID.randomUUID().toString().replace("-","").substring(0,5);
                                        ModelFood model = new ModelFood(key, TempFoodName, TempFoodPrice, TempFoodQuantity, uri.toString());
                                        root.child(fName.getText().toString()).setValue(model);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(CanteenUploadFoodAct.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        imageView.setImageResource(R.drawable.ic_addphoto);
                                        startActivity(new Intent(CanteenUploadFoodAct.this, ZCanteenDrawer_MainAct.class));
                                    }
                                });
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(CanteenUploadFoodAct.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    } // end of uploadToFirebase

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

}


