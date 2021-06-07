package com.example.social_interest_club;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.social_interest_club.DataStructsAndDAOs.UserStruct;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GuestHomePage extends AppCompatActivity {

    Button browseClubsButton, browseSubclubsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home_page);
        getSupportActionBar().setTitle("Guest Homepage");


        browseClubsButton = findViewById(R.id.browseClubsButton);
        browseSubclubsButton = findViewById(R.id.browseSubClubsButton);

        // Store user data for global using
        setUserInfo();

        browseClubsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BrowseClubsActivity.class));

            }
        });

        browseSubclubsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BrowseSubClubActivity.class));
            }
        });
    }

    // Gets the user info from database
    public void setUserInfo()
    {
        Globals.userName = "Unregistered";
        Globals.status = 3;
        Globals.banCount = -2;
        Globals.isBanned = false;
        Globals.isComplaint = false;
        Globals.bannedDay = -2;
        Globals.isUserDataLoaded = true;
    }
}