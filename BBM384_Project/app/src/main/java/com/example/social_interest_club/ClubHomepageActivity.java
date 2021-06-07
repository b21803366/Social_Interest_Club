package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_interest_club.DataStructsAndDAOs.ClubStruct;
import com.example.social_interest_club.DataStructsAndDAOs.SubClubStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClubHomepageActivity extends AppCompatActivity {
    private static final String TAG = "Club Homepage";
    List<SubClubStruct> clubs = new ArrayList<SubClubStruct>();
    LinearLayout clubHomepageLayout;

    FirebaseFirestore db = null;
    FirebaseUser user;

    private String clubName = "";
    private String clubDescription = "";

    private double clubRate = -1.0;

    //static String tempString;

    Button homepageButton, reviewpageButton, chatpageButton, joinClubButton;
    TextView totalRatingTextView, joinClubAlreadyJoinedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_homepage);
        getSupportActionBar().hide();
        clubHomepageLayout = (LinearLayout) findViewById(R.id.subClubListTable);

        // Get club name from previous activity
        Bundle b = getIntent().getExtras();
        if(b != null){
            clubName = b.getString("clubName");
        }


        //System.out.println(clubName);

        // Write club name to page
        TextView clubNameTextView = (TextView) findViewById(R.id.clubNameTextView);
        TextView clubDescriptionTextView = (TextView) findViewById(R.id.clubDescriptionTextView);

        joinClubAlreadyJoinedTextView = (TextView) findViewById(R.id.joinClubAlreadyJoinedTextView);
        joinClubAlreadyJoinedTextView.setVisibility(View.GONE);

        clubNameTextView.setText(clubName);

        //Set description of the club
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("clubs").document(clubName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        clubDescriptionTextView.setText(document.getString("clubDescription"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        // Assign buttons
        homepageButton = findViewById(R.id.homeButton);
        reviewpageButton = findViewById(R.id.reviewButton);
        chatpageButton = findViewById(R.id.chatButton);
        joinClubButton = findViewById(R.id.joinClubButton);

        // Assign rating text view
        totalRatingTextView = findViewById(R.id.totalRatingTextView);

        NumberFormat formatter = new DecimalFormat("#0.00");

        calculateRating(new MyCallback() {
            @Override
            public void onCallback(double rate) {

                totalRatingTextView.setText("Average rating: " + String.valueOf(rate));

            }
        });

        // Create subClub table
        readClubs();

        // Club home page button
        homepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClubHomepageActivity.this, "You are already in club homepage.", Toast.LENGTH_SHORT).show();
            }
        });

        // Club review page button
        reviewpageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent for send club data to next activity
                Intent intent = new Intent(ClubHomepageActivity.this, ClubReviewpageActivity.class);
                Bundle b = new Bundle();
                b.putString("clubName", clubName); // Club name
                intent.putExtras(b); // Put your id to your next Intent
                startActivity(intent);
                finish();
            }
        });

        // Club chat page button
        chatpageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent for send club data to next activity
                Intent intent = new Intent(ClubHomepageActivity.this, ClubChatpageActivity.class);
                Bundle b = new Bundle();
                b.putString("clubName", clubName); // Club name
                intent.putExtras(b); // Put your id to your next Intent
                startActivity(intent);
                finish();
            }
        });

        // Join club page button
        joinClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Ask questions
                Intent intent = new Intent(ClubHomepageActivity.this, QuizActivity.class);
                Bundle b = new Bundle();
                b.putString("clubName", clubName); // Club name
                b.putString("clubType", "clubs");
                intent.putExtras(b); // Put your id to your next Intent
                startActivityForResult(intent, 1);

            }
        });

        //If browsing user is not-registered, then he/she can not do certain things:.
        if(Globals.isUserDataLoaded && Globals.status==3){
            homepageButton.setVisibility(View.GONE);
            homepageButton.setEnabled(false);

            reviewpageButton.setVisibility(View.GONE);
            reviewpageButton.setEnabled(false);

            chatpageButton.setVisibility(View.GONE);
            chatpageButton.setEnabled(false);

            //joinClubButton.setVisibility(View.GONE);
            joinClubAlreadyJoinedTextView.setVisibility(View.VISIBLE);
            joinClubAlreadyJoinedTextView.setText("Login to join this club.");
            joinClubButton.setEnabled(false);



        }

        //If browsing user is admin, then he/she can not do certain things:.
        if(Globals.isUserDataLoaded && Globals.status==0){
            joinClubButton.setVisibility(View.GONE);
            joinClubButton.setEnabled(false);
        }

        String currentUserEmail = Globals.userName;


        checkIfJoinedBefore(currentUserEmail);


        user = FirebaseAuth.getInstance().getCurrentUser();
        String currentTempEmail = user.getEmail();

        db = FirebaseFirestore.getInstance();
        // Get current user's interest rate of the current club.

        DocumentReference newDocRef = db.collection("users").document(currentTempEmail).collection("interestRates").document("interestRates");
        newDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                    } else {
                    }
                } else {
                }
            }
        });
    }

    // After returning back to this activity, refresh the Recommended and New Club layouts.
    @Override
    protected void onResume() {
        super.onResume();

        calculateRating(new MyCallback() {
            @Override
            public void onCallback(double rate) {
                totalRatingTextView.setText("Average rating: " + String.valueOf(rate));
            }
        });


        //user = FirebaseAuth.getInstance().getCurrentUser();
        //String currentUserEmail = user.getEmail();

        //readNewClubs(currentUserEmail);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(1, resultCode, data);

        // If the user couldn't pass the quiz, she/he can not join the club
        //todo update interest rate
        if (!Globals.passedQuiz)
        {
            Toast.makeText(ClubHomepageActivity.this, "Your quiz rate is too low.", Toast.LENGTH_LONG).show();

            user = FirebaseAuth.getInstance().getCurrentUser();


            Map<String, Object> newmap = new HashMap<>();
            newmap.put(clubName, Globals.quizResult);

            db = FirebaseFirestore.getInstance();

            // Update interest rate of corresponding club
            db.collection("users").document(user.getEmail()).collection("interestRates").document("interestRates").update(newmap);
            return;
        }
        if(Globals.passedQuiz){
            user = FirebaseAuth.getInstance().getCurrentUser();
            Map<String, Object> newmap = new HashMap<>();
            newmap.put(clubName, Globals.quizResult);

            System.out.println(Globals.quizResult);


            db = FirebaseFirestore.getInstance();

            // Update interest rate of corresponding club
            db.collection("users").document(user.getEmail()).collection("interestRates").document("interestRates").update(newmap);
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserEmail = user.getEmail();

        Map<String, Object> newmap = new HashMap<>();
        newmap.put("memberEmail", currentUserEmail);

        db = FirebaseFirestore.getInstance();

        // Save user in clubs database
        db.collection("clubs").document(clubName).collection("members").document(currentUserEmail).set(newmap);

        // Save club in user database
        CollectionReference ref =  db.collection("users").document(currentUserEmail).collection("joined");
        Map<String, Object> mp = new HashMap<>();
        mp.put("ClubName", clubName);
        mp.put("IsSubClub", false);
        ref.add(mp);

        // Delete club from "new clubs" category for current user
        db.collection("users").document(currentUserEmail).collection("newClubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                if (document.getString("clubName").compareTo(clubName) == 0)
                                {
                                    DocumentReference ref = document.getReference();
                                    Map<String, Object> del = new HashMap<>();
                                    del.put("clubName", "DELETED_FROM_HERE");

                                    ref.update(del).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        // [START_EXCLUDE]
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {}
                                        // [START_EXCLUDE]
                                    });
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        createTable();
                    }
                });

        /*user = FirebaseAuth.getInstance().getCurrentUser();

        Map<String, Object> newestmp = new HashMap<>();
        newestmp.put(clubName, Globals.quizResult);
        db = FirebaseFirestore.getInstance();
        // Update interest rate of corresponding club
        db.collection("users").document(user.getEmail()).collection("interestRates").document("interestRates").update(newestmp);
        */

        Globals.passedQuiz = false;

        Toast.makeText(ClubHomepageActivity.this, "You have successfully joined the " + clubName, Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public double calculateRating(MyCallback myCallback){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reviews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        double averageRating = 0.0;
                        double reviewCount = 0;

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String tempClubName = document.getString("clubName");

                                // If tempClubName is equal to requested club's name:
                                if(tempClubName.equals(clubName)){
                                    double clubRate = document.getLong("rate").doubleValue()/20;
                                    averageRating = averageRating + clubRate;
                                    reviewCount++;
                                    //System.out.println(averageRating);

                                }
                                else{

                                }
                            }

                            myCallback.onCallback(averageRating);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        //System.out.println(averageRating);
        //return averageRating/reviewCount;

        return 0;
    }

    public interface MyCallback {
        void onCallback(double rate);
    }


    public void readClubs(){
        db = FirebaseFirestore.getInstance();
        db.collection("subclubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String parentClubName = document.getString("parentClub");
                                String tempClubName = document.getString("clubName");
                                String clubDescription = document.getString("clubDescription");

                                System.out.println(tempClubName);
                                System.out.println(clubName);

                                if(parentClubName.equals(clubName)){
                                    clubs.add(new SubClubStruct(parentClubName, tempClubName, clubDescription));
                                    System.out.println(clubs);
                                }
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
            textView.setText(clubs.get(i).getSubClubName());
            Button button = new Button(this);
            final int index = i;
            button.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create intent for send club data to next activity
                    Intent intent = new Intent(ClubHomepageActivity.this, SubClubHomepageActivity.class);
                    Bundle b = new Bundle();
                    b.putString("clubName", clubs.get(index).getSubClubName()); //Your id
                    b.putString("clubDescription", clubs.get(index).getClubDescription());
                    b.putString("parentClub", clubName);
                    intent.putExtras(b); // Put your id to your next Intent
                    startActivity(intent);
                    //finish();
                }
            });
            button.setText("Browse");
            tableRow.addView(textView);
            tableRow.addView(button);
            clubHomepageLayout.addView(tableRow);
        }
    }

    public void checkIfJoinedBefore(String userEmail){
        db = FirebaseFirestore.getInstance();
        db.collection("clubs").document(clubName).collection("members")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(userEmail.equals(document.getString("memberEmail"))){
                                    //joinClubButton.setVisibility(View.GONE);
                                    joinClubButton.setEnabled(false);

                                    joinClubAlreadyJoinedTextView.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

}
