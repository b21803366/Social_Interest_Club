package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordRecoveryActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button passwordRecoveryButton;
    EditText emailInput;

    private static final String TAG = "Password Recovery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        getSupportActionBar().setTitle("Forgot Password");


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.passwordRecoveryEmailInput);
        passwordRecoveryButton = findViewById(R.id.passwordRecoveryButton);


        passwordRecoveryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String emailString = emailInput.getText().toString().trim();

                if(TextUtils.isEmpty(emailString)){
                    emailInput.setError("Email cannot be empty.");
                }

                if(!TextUtils.isEmpty(emailString)){
                    mAuth.sendPasswordResetEmail(emailString).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PasswordRecoveryActivity.this, "Reset link sent to your Email.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "sendPasswordResetEmail:success");
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PasswordRecoveryActivity.this, "Error sending reset mail." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "sendPasswordResetEmail:failure", e.getCause());
                        }
                    });
                }

            }
        });

    }


}