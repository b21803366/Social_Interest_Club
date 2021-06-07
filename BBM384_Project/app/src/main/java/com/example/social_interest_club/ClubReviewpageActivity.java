package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ClubReviewpageActivity extends AppCompatActivity {

    private static final String TAG = "Review/Rate Club";

    Button homepageButton, reviewpageButton, chatpageButton, applyButton;
    EditText userReviewInput;
    TextView otherReviewsTextView;
    String userReview = "";
    String reviewOwnerEmail = "";
    RatingBar ratingBar;
    private String clubName = "";


    int rating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_reviewpage);

        getSupportActionBar().hide();

        // Get club name from previous activity
        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            clubName = b.getString("clubName");
        }

        homepageButton = findViewById(R.id.homeButton3);
        reviewpageButton = findViewById(R.id.reviewButton3);
        chatpageButton = findViewById(R.id.chatButton3);
        applyButton = findViewById(R.id.reviewApplyButton);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        userReviewInput = findViewById(R.id.reviewInput);
        otherReviewsTextView = findViewById(R.id.otherReviewsTextView);

        // Club home page button
        homepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent for send club data to next activity
                Intent intent = new Intent(ClubReviewpageActivity.this, ClubHomepageActivity.class);
                Bundle b = new Bundle();
                b.putString("clubName", clubName); // Club name
                intent.putExtras(b); // Put your id to your next Intent
                startActivity(intent);
                finish();
            }
        });

        // Club review page button
        reviewpageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClubReviewpageActivity.this, "You are already in club review page.", Toast.LENGTH_SHORT).show();
            }
        });

        // Club chat page button
        chatpageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent for send club data to next activity
                Intent intent = new Intent(ClubReviewpageActivity.this, ClubChatpageActivity.class);
                Bundle b = new Bundle();
                b.putString("clubName", clubName); // Club name
                intent.putExtras(b); // Put your id to your next Intent
                startActivity(intent);
                finish();
            }
        });

        // Apply button
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get rating
                rating = (int)(ratingBar.getRating() * 20);

                // Get review text
                userReview = userReviewInput.getText().toString().trim();

                // Get email of current user.
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                reviewOwnerEmail = user.getEmail();

                //Map values
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> reviewMap = new HashMap<>();
                reviewMap.put("clubName", clubName);
                reviewMap.put("review", userReview);
                reviewMap.put("rate", rating);
                reviewMap.put("reviewOwner", reviewOwnerEmail);

                addReview(reviewMap);



                //Send clubName to ClubHomepage, to allow multiple reviews concurrently
                Intent intent = new Intent(ClubReviewpageActivity.this, ClubHomepageActivity.class);
                Bundle b = new Bundle();
                b.putString("clubName", clubName); // Club name
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                finish();

            }
        });


        readReviews();


    }   //end of onCreate

    public void addReview(Map<String, Object> reviewMap)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reviews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            // Create club on database
                            db.collection("reviews").document().set(reviewMap);
                            Toast.makeText(ClubReviewpageActivity.this, "Review & Rate entered successfully.", Toast.LENGTH_SHORT).show();


                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            Toast.makeText(ClubReviewpageActivity.this, "Review & Rate failed.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    public void readReviews(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reviews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String reviewString = "";
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String tempClubName = document.getString("clubName");
                                double clubRate = document.getLong("rate").doubleValue()/20;
                                String clubReview = document.getString("review");
                                String reviewOwner = document.getString("reviewOwner");

                                //if current comment belongs to this club, type it into the string:
                                if(clubName.equals(tempClubName)){
                                    reviewString = reviewString.concat(reviewOwner + "'s review: \n\"" + clubReview + "\"\t" + "(Rating: " + clubRate + "/5)" + "\n\n");
                                }
                            }

                            otherReviewsTextView.setText(reviewString);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }



}