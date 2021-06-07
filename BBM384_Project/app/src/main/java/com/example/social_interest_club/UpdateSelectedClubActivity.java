package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateSelectedClubActivity extends AppCompatActivity {
    TextView selectedClubNameText;
    EditText clubDescriptionInput, questionUpdateInput, questionUpdateInput2, questionUpdateInput3;
    Button updateButton;
    String clubName;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_selected_club);
        getSupportActionBar().setTitle("Update Selected Club");

        Bundle b = getIntent().getExtras();
        if(b != null)
            clubName = b.getString("clubName");

        selectedClubNameText = findViewById(R.id.selectedClubNameText);
        clubDescriptionInput = findViewById(R.id.clubDescriptionInput);
        questionUpdateInput = findViewById(R.id.questionUpdateInput);
        questionUpdateInput2 = findViewById(R.id.questionUpdateInput2);
        questionUpdateInput3 = findViewById(R.id.questionUpdateInput3);

        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("clubs").document(clubName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        clubDescriptionInput.setText(document.getString("clubDescription"));
                        questionUpdateInput.setText(document.getString("q1"));
                        questionUpdateInput2.setText(document.getString("q2"));
                        questionUpdateInput3.setText(document.getString("q3"));
                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        updateButton = findViewById(R.id.updateButton);

        selectedClubNameText.setText(clubName);

        updateButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String descriptionString = clubDescriptionInput.getText().toString().trim();
                String questionString = questionUpdateInput.getText().toString().trim();
                String questionString2 = questionUpdateInput2.getText().toString().trim();
                String questionString3 = questionUpdateInput3.getText().toString().trim();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> club = new HashMap<>();
                club.put("clubName", clubName);
                club.put("clubDescription", descriptionString);
                club.put("q1", questionString);
                club.put("q2", questionString2);
                club.put("q3", questionString3);

                db.collection("clubs").document(clubName).set(club);

                Toast.makeText(UpdateSelectedClubActivity.this, "Club updated successfully.", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }
}