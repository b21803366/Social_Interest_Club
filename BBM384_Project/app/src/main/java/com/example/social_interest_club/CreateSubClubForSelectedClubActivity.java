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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CreateSubClubForSelectedClubActivity extends AppCompatActivity {
    private static final String TAG = "Create Sub-Club";

    EditText subClubNameInput, descriptionInput, q1button, q2button, q3button;
    TextView parentClubNameTextView;

    Button applyButton;

    String parentClubName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);
        getSupportActionBar().setTitle("Create Sub-Club");

        Bundle b = getIntent().getExtras();
        if(b != null)
            parentClubName = b.getString("clubName");

        System.out.println(parentClubName);

        //String tempString = parentClubName;

        subClubNameInput = findViewById(R.id.clubNameInput);
        descriptionInput = findViewById(R.id.memberRequestDescriptionInput);


        //todo parentClubNameTextView nedense hata veriyor, nedenine bakılabilir
        //todo CreateRequestedClubActivity'de aynı şekilde kullanmıştım, onda hata yoktu.
        //parentClubNameTextView = (TextView)findViewById(R.id.parentClubNameTextView);
        //parentClubNameTextView.setText(tempString);

        applyButton = findViewById(R.id.applyButton);
        q1button = findViewById(R.id.questionInput1);
        q2button = findViewById(R.id.questionInput2);
        q3button = findViewById(R.id.requestedQuestionInput3);






        applyButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {

                String parentClubNameString = parentClubName;
                String subClubNameString = subClubNameInput.getText().toString().trim();
                String descriptionString = descriptionInput.getText().toString().trim();
                String q1str = q1button.getText().toString().trim();
                String q2str = q2button.getText().toString().trim();
                String q3str = q3button.getText().toString().trim();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> subclub = new HashMap<>();
                subclub.put("parentClub", parentClubNameString);
                subclub.put("clubName", subClubNameString);
                subclub.put("clubDescription", descriptionString);
                subclub.put("q1", q1str);
                subclub.put("q2", q2str);
                subclub.put("q3", q3str);
                subclub.put("adminEmail", null);

                createClub(subclub);


            }
        });
    }

    public void createClub(Map<String, Object> clubs)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("subclubs")
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
                                    Toast.makeText(CreateSubClubForSelectedClubActivity.this, club_name + " already exists. ", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            // Create club on database
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("subclubs").document((String) clubs.get("clubName")).set(clubs);
                            Toast.makeText(CreateSubClubForSelectedClubActivity.this, "Sub-club successfully created.", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });
    }
}