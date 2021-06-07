package com.example.social_interest_club;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ManageSubClubActivity extends AppCompatActivity {

    private String clubName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sub_club);

        // Get sub-club name from previous activity
        Bundle b = getIntent().getExtras();
        if(b != null){
            clubName = b.getString("clubName");
        }


    }
}