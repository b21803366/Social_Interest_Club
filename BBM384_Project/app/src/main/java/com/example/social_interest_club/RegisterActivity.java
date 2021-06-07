package com.example.social_interest_club;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.social_interest_club.DataStructsAndDAOs.UserStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {


    EditText emailInput, passwordInput, confirmPasswordInput;
    Button registerButton;

    private static final String TAG = "Register";

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //usernameInput = findViewById(R.id.registerUsernameInput);
        emailInput = findViewById(R.id.registerEmailInput);
        passwordInput = findViewById(R.id.registerPasswordInput);
        confirmPasswordInput = findViewById(R.id.loginEmailInput);

        registerButton = findViewById(R.id.registerButton);

        //If already logged in, directly go to Homepage
        if(mAuth.getCurrentUser() != null){

            startActivity(new Intent(getApplicationContext(), HomepageActivity.class));
            Toast.makeText(RegisterActivity.this, "You are already logged in.", Toast.LENGTH_SHORT).show();
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailString = emailInput.getText().toString().trim();
                String passwordString = passwordInput.getText().toString().trim();
                String confirmPasswordString = confirmPasswordInput.getText().toString().trim();


                if(TextUtils.isEmpty(emailString)){
                    emailInput.setError("Email cannot be empty.");
                }
                if(TextUtils.isEmpty(passwordString)){
                    passwordInput.setError("Password cannot be empty.");
                }
                if(TextUtils.isEmpty(confirmPasswordString)){
                    confirmPasswordInput.setError("Confirm Password cannot be empty.");
                }
                if(passwordString.length() < 6){
                    passwordInput.setError("Password length must be equal or longer than 6 characters.");
                }
                if(!passwordString.equals(confirmPasswordString)){
                    confirmPasswordInput.setError("Passwords does not match.");
                }

                if(!TextUtils.isEmpty(emailString) && !TextUtils.isEmpty(passwordString) && !TextUtils.isEmpty(confirmPasswordString) && passwordString.length() >= 6 && passwordString.equals(confirmPasswordString)){
                    mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");

                                // Store the user to database
                                // User object
                                UserStruct user_obj = new UserStruct(emailString,2, 0,
                                        false, false, 0);

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("users").document(emailString).set(user_obj);
                                Toast.makeText(RegisterActivity.this, "User created.", Toast.LENGTH_SHORT).show();

                                Globals.regUserName = emailString;
                                startActivity(new Intent(getApplicationContext(), QuestionarrieActivity.class));
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.\n\n" + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

}