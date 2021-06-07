package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CreateRequestedClubActivity extends AppCompatActivity
{
    private static final String TAG = "Create Requested Club";

    EditText descriptionInput, q1button, q2button, q3button;
    TextView requestedClubNameTextView;
    Button applyButton;
    String requestedClubName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_requested_club);
        getSupportActionBar().setTitle("Create Requested Club");

        Bundle b = getIntent().getExtras();
        if(b != null)
            requestedClubName = b.getString("clubName");


        requestedClubNameTextView = (TextView)findViewById(R.id.requestedClubNameTextView);
        descriptionInput = findViewById(R.id.requestedDescriptionInput);
        applyButton = findViewById(R.id.requestedApplyButton);
        q1button = findViewById(R.id.requestedQuestionInput1);
        q2button = findViewById(R.id.requestedQuestionInput2);
        q3button = findViewById(R.id.requestedQuestionInput3);


        requestedClubNameTextView.setText(requestedClubName);


        applyButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                String clubNameString = requestedClubNameTextView.getText().toString().trim();
                String descriptionString = descriptionInput.getText().toString().trim();
                String q1str = q1button.getText().toString().trim();
                String q2str = q2button.getText().toString().trim();
                String q3str = q3button.getText().toString().trim();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> club = new HashMap<>();
                club.put("clubName", clubNameString);
                club.put("clubDescription", descriptionString);
                club.put("q1", q1str);
                club.put("q2", q2str);
                club.put("q3", q3str);

                createClub(club);


            }
        });
    }

    public void createClub(Map<String, Object> clubs)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("clubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String clubName = document.getString("clubName");

                                String club_name = (String) clubs.get("clubName");
                                if (club_name.contains(clubName) && clubName.contains(club_name))
                                {
                                    Toast.makeText(CreateRequestedClubActivity.this, club_name + " already exists. ", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            // Create club on database
                            db.collection("clubs").document((String) clubs.get("clubName")).set(clubs);

                            // Delete club request since club is created.
                            db.collection("clubrequests").document(requestedClubName).delete();

                            finish();

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });
    }
}
