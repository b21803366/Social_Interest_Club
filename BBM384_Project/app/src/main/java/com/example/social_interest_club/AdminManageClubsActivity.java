package com.example.social_interest_club;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminManageClubsActivity extends AppCompatActivity {

    Button createClubsButton, seeClubsButton, updateClubsButton, deleteClubsButton, clubRequestsButton, subClubAdminRequestsButton, banUserButton, unbanUserButton;
    Button createSubClubsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_clubs);
        getSupportActionBar().setTitle("Manage Clubs");

        // Get user details


        // Buttons
        createClubsButton = findViewById(R.id.createClubsButton);
        seeClubsButton = findViewById(R.id.seeClubsButton);
        updateClubsButton = findViewById(R.id.updateClubsButton);
        deleteClubsButton = findViewById(R.id.deleteClubsButton);
        clubRequestsButton = findViewById(R.id.clubRequestsButton);
        createSubClubsButton = findViewById(R.id.createSubClubsButton);
        banUserButton = findViewById(R.id.banUserButton);
        unbanUserButton = findViewById(R.id.unbanUserButton);

        createClubsButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), CreateClubActivity.class));
            }
        });

        seeClubsButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), SeeClubsActivity.class));
            }
        });

        updateClubsButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), UpdateClubsActivity.class));
            }
        });

        deleteClubsButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), DeleteClubsActivity.class));
            }
        });

        clubRequestsButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), AdminClubRequestsActivity.class));
            }
        });

        createSubClubsButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), CreateSubClubActivity.class));
            }
        });
        banUserButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), BanUserActivity.class));
            }
        });
        unbanUserButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), UnbanUserActivity.class));
            }
        });


    }
}