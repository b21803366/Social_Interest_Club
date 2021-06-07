package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.social_interest_club.DataStructsAndDAOs.AllDataOperationAccessObject;
import com.example.social_interest_club.DataStructsAndDAOs.UserStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginButton, loginGuestButton;
    FirebaseAuth mAuth;
    FirebaseFirestore db = null;
    ArrayList<UserStruct> users = new ArrayList<UserStruct>();

    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        AllDataOperationAccessObject allDataOperationAccessObject=AllDataOperationAccessObject.getInstance();
        allDataOperationAccessObject.getClubMemberStructDAO().setClubMemberStructByID(1,"sdc",1,1,1);
        getSupportActionBar().setTitle("Social Interest eClub");

        readUsers();

        loginGuestButton = findViewById(R.id.guestLogin);

        emailInput = findViewById(R.id.loginEmailInput);
        passwordInput = findViewById(R.id.loginPasswordInput);

        loginButton = findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();

        //If already logged in, directly go to Homepage
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), HomepageActivity.class));

        }


        // Login as guest button
        loginGuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GuestHomePage.class));
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBanned = false;
                String emailString = emailInput.getText().toString().trim();
                String passwordString = passwordInput.getText().toString().trim();



                if(TextUtils.isEmpty(emailString)){
                    emailInput.setError("Email cannot be empty.");
                }
                for(UserStruct u : users){
                    System.out.println(users.size());

                    System.out.println(emailString);
                    System.out.println(u.getUserName());
                    System.out.println("xx: " + emailString.equals(u.getUserName()));
                    System.out.println(u.isBanned);



                    if(emailString.equals(u.getUserName()) && u.isBanned){
                        isBanned = true;
                        emailInput.setError("This user is banned!");
                        return;
                    }

                }
                /*if(isBanned){
                    emailInput.setError("This user is banned!");
                    return;
                }*/
                if(TextUtils.isEmpty(passwordString)){
                    passwordInput.setError("Password cannot be empty.");
                }
                if(passwordString.length() < 6){
                    passwordInput.setError("Password length must be equal or longer than 6 characters.");
                }

                //Authenticate the user

                if(!TextUtils.isEmpty(emailString) && !TextUtils.isEmpty(passwordString) && passwordString.length() >= 6){
                    mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmailAndPassword:success");

                                Toast.makeText(MainActivity.this, "Logged-in successfully.", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(MainActivity.this, HomepageActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                            else{
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmailAndPassword:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Incorrect email or password.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });

    }

    public void goToRegister(View view) {
        // Do something in response to button
        Intent myIntent = new Intent(this, RegisterActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        this.startActivity(myIntent);

    }

    public void goToForgotPassword(View view) {
        // Do something in response to button
        Intent myIntent = new Intent(this, PasswordRecoveryActivity.class);

        this.startActivity(myIntent);

    }
    public void readUsers(){
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String userName = document.getString("userName");
                                long status = document.getLong("status");
                                boolean isBanned = document.getBoolean("isBanned");
                                if(status == 0){
                                    continue;
                                }

                                users.add(new UserStruct());
                                users.get(users.size()-1).setUserName(userName);
                                users.get(users.size()-1).setStatus((int)status);
                                users.get(users.size()-1).setBanned(isBanned);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}