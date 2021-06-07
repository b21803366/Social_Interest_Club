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
import com.example.social_interest_club.DataStructsAndDAOs.RequestClubStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminClubRequestsActivity extends AppCompatActivity {

    private static final String TAG = "Admin Club Request";
    List<RequestClubStruct> clubRequests = new ArrayList<RequestClubStruct>();
    LinearLayout clubRequestsLayout;
    FirebaseFirestore db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_club_requests);
        getSupportActionBar().setTitle("Club Requests");
        clubRequestsLayout = (LinearLayout) findViewById(R.id.clubRequestsLayout);
        readClubRequests();


    }// End of onCreate

    public void readClubRequests(){
        db = FirebaseFirestore.getInstance();
        db.collection("clubrequests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String requestedClubName = document.getString("clubName");
                                int numberOfRequests = document.getLong("numberOfRequests").intValue();
                                clubRequests.add(new RequestClubStruct(requestedClubName, numberOfRequests));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        createTable();
                    }
                });
    }

    public void createTable(){
        for(int i=0; i<clubRequests.size(); i++){
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText(clubRequests.get(i).getClubName());
            Button button = new Button(this);

            //If request number is less than 3, make button unclickable.
            if(clubRequests.get(i).getNumberOfRequests() < 3){
                button.setClickable(false);
                button.setEnabled(false);

            }
            TextView textView2 = new TextView(this);
            textView2.setText("Number of requests: " + Integer.toString(clubRequests.get(i).getNumberOfRequests()));


            button.setText("Create");

            final int index = i;
            button.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create intent for send club data to next activity
                    Intent intent = new Intent(AdminClubRequestsActivity.this, CreateRequestedClubActivity.class);
                    Bundle b = new Bundle();
                    b.putString("clubName", clubRequests.get(index).getClubName()); //Your id
                    intent.putExtras(b); // Put your id to your next Intent
                    startActivity(intent);
                    finish();
                }
            });
            //button.setText("Create");
            tableRow.addView(textView);
            tableRow.addView(button);
            tableRow.addView(textView2);
            clubRequestsLayout.addView(tableRow);
        }
    }

}