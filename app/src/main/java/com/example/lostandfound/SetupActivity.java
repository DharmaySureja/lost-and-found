package com.example.lostandfound;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lostandfound.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SetupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ProgressBar progressBar;
    private CircleImageView userImg;
    private AppCompatEditText userName,userCity,usermobile;
    private ArrayAdapter<CharSequence> arrayAdapter;
    private Spinner spinner;
    private Button saveBtn;

    Uri mainImageURI = null;
    String user_id = null,download_uri;
    boolean isChanged = false;
    String DepartmentName;

    Map<String, String> userMap = new HashMap<>();

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();


        progressBar = findViewById(R.id.pBar);
        userImg = findViewById(R.id.userImg);
        userName = findViewById(R.id.username);
        usermobile = findViewById(R.id.usermo);
        userCity = findViewById(R.id.usercity);
        saveBtn = findViewById(R.id.savebtn);

        spinner = findViewById(R.id.departmentspinner);
        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.department, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        progressBar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");
                        String mobile = task.getResult().getString("mobile");
                        String city = task.getResult().getString("city");
                        String depart=task.getResult().getString("department");

                        int position = Integer.parseInt(String.valueOf(depart.charAt(0)));


                        mainImageURI = Uri.parse(image);

                        userName.setText(name);
                        usermobile.setText(mobile);
                        userCity.setText(city);
                        spinner.setSelection(position);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.mipmap.default_image);

                        Glide.with(SetupActivity.this).setDefaultRequestOptions(placeholderRequest).load(image).into(userImg);

                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }

                progressBar.setVisibility(View.INVISIBLE);
                saveBtn.setEnabled(true);

            }
        });




        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_name = userName.getText().toString();
                final String user_mo = usermobile.getText().toString();
                final String user_city = userCity.getText().toString();

                if(!TextUtils.isEmpty(user_name) && mainImageURI != null && user_mo != null && user_city != null ){

                    progressBar.setVisibility(View.VISIBLE);

                    if (isChanged) {

                        user_id = firebaseAuth.getCurrentUser().getUid();

                        File newImageFile = new File(mainImageURI.getPath());
                        try {

                            compressedImageFile = new Compressor(SetupActivity.this)
                                    .setMaxHeight(125)
                                    .setMaxWidth(125)
                                    .setQuality(50)
                                    .compressToBitmap(newImageFile);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] thumbData = baos.toByteArray();

                        UploadTask image_path = storageReference.child("profile_images").child(user_id + ".jpg").putBytes(thumbData);

                        image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {
                                    storeFirestore(task, user_name,user_city,user_mo);

                                } else {

                                    String error = task.getException().getMessage();
                                    Toast.makeText(SetupActivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();

                                    progressBar.setVisibility(View.INVISIBLE);

                                }
                            }
                        });

                    } else {

                        storeFirestore(null, user_name,user_city,user_mo);

                    }

                }

            }

        });

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                userImg.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }


    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(SetupActivity.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        DepartmentName = position + parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String user_name , String user_city , String user_mo) {

        userMap.put("name", user_name);
        userMap.put("city", user_city);
        userMap.put("mobile", user_mo);
        userMap.put("department", DepartmentName);

        if(task != null) {

            storageReference.child("profile_images").child(user_id + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    download_uri = task.getResult().toString();
                    userMap.put("image", download_uri);

                    firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(SetupActivity.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(SetupActivity.this, MainActivity.class);
                                i.putExtra("position", 3);
                                startActivity(i);
                                finish();
                            } else {

                                String error = task.getException().getMessage();
                                Toast.makeText(SetupActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                            }

                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });

                }
            });

        } else {

            download_uri = mainImageURI.toString();
            userMap.put("image", download_uri);

            firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        Toast.makeText(SetupActivity.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();

                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                    }

                    progressBar.setVisibility(View.INVISIBLE);

                }
            });

        }

    }

}
