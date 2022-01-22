package com.example.lostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfound.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {

    AppCompatEditText emailId, password ,cpass;
    AppCompatButton btnSignUp;
    TextView tvSignIn;
    String email;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.username);
        password = findViewById(R.id.pass);
        cpass =findViewById(R.id.cpass);
        btnSignUp = findViewById(R.id.signUp);
        tvSignIn = findViewById(R.id.tvSignin);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailId.getText().toString();
                String pwd = password.getText().toString();
                String cpwd = cpass.getText().toString();

                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(cpwd.isEmpty()){
                    cpass.setError("Please enter your password");
                    cpass.requestFocus();
                }
                else  if(!cpwd.equals(pwd)){
                    cpass.setError("password NotMatch");
                    cpass.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty() && cpwd.isEmpty()){
                    Toast.makeText(Register.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty() && cpwd.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Login", "createUserWithEmail:success");
                                        user = mFirebaseAuth.getCurrentUser();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Login", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Register.this, "Email Id is Already exist.",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });
                }
                else{
                    Toast.makeText(Register.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });
    }
}
