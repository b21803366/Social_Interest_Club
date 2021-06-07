package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.social_interest_club.DataStructsAndDAOs.ClubStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BrowseClubsActivity extends AppCompatActivity {

    private static final String TAG = "Browse Clubs";
    List<ClubStruct> clubs = new ArrayList<ClubStruct>();
    LinearLayout browseClubsLayout;
    Button requestClubAsMemberButton;
    FirebaseFirestore db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_clubs);
        getSupportActionBar().setTitle("Browse Clubs");
        browseClubsLayout = (LinearLayout) findViewById(R.id.banUsersLayout);

        requestClubAsMemberButton = findViewById(R.id.clubRequestByMemberButton);


        readClubs();

        // Club home page button
        requestClubAsMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MemberRequestClubActivity.class));
                //finish();
            }
        });

        //If browsing user is not-registered, then he/she can not request clubs.
        if(Globals.isUserDataLoaded && Globals.status==3){
            requestClubAsMemberButton.setVisibility(View.GONE);
            requestClubAsMemberButton.setEnabled(false);
        }


    }

    public void readClubs(){
        db = FirebaseFirestore.getInstance();
        db.collection("clubs")//.document(currentemailadress).collection("interestRate").get()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String clubName = document.getString("clubName");
                                String clubDescription = document.getString("clubDescription");
                                clubs.add(new ClubStruct(clubName, clubDescription));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        createTable();
                    }
                });
    }

    public void createTable(){
        for(int i=0; i<clubs.size(); i++){
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText(clubs.get(i).getClubName());
            Button button = new Button(this);
            final int index = i;
            button.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create intent for send club data to next activity
                    Intent intent = new Intent(BrowseClubsActivity.this, ClubHomepageActivity.class);
                    Bundle b = new Bundle();
                    b.putString("clubName", clubs.get(index).getClubName()); //Your id
                    b.putString("clubDescription", clubs.get(index).getClubDescription());
                    intent.putExtras(b); // Put your id to your next Intent
                    startActivity(intent);
                    //finish();
                }
            });
            button.setText("Browse");
            tableRow.addView(textView);
            tableRow.addView(button);
            browseClubsLayout.addView(tableRow);
        }
    }

}