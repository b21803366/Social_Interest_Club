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

public class CreateSubClubActivity extends AppCompatActivity {

    private static final String TAG = "Create Sub-Clubs";
    List<ClubStruct> clubs = new ArrayList<ClubStruct>();
    LinearLayout createSubClubsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sub_club);
        getSupportActionBar().setTitle("Create Sub-Club");
        createSubClubsLayout = (LinearLayout) findViewById(R.id.createSubClubsLayout);
        readClubs();
    }

    public void readClubs() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("clubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String clubsString = "";
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

    public void createTable() {
        for (int i = 0; i < clubs.size(); i++) {
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText(clubs.get(i).getClubName());
            Button button = new Button(this);
            button.setText("Choose");

            final int index = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to do update selected club activity açılacak ve parametre yollanacak
                    Intent intent = new Intent(CreateSubClubActivity.this, CreateSubClubForSelectedClubActivity.class);
                    Bundle b = new Bundle();
                    b.putString("clubName", clubs.get(index).getClubName()); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                    finish();
                }
            });

            tableRow.addView(textView);
            tableRow.addView(button);
            createSubClubsLayout.addView(tableRow);
        }
    }
}