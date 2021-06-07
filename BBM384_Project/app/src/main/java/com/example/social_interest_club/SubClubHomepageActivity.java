package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.TextView;
import android.widget.Toast;

import com.example.social_interest_club.DataStructsAndDAOs.SubClubStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Random;



public class SubClubHomepageActivity extends AppCompatActivity {
    private static final String TAG = "Sub-Club Homepage";

    private static boolean isJoinedBefore = false;

    FirebaseFirestore db = null;
    FirebaseUser user;

    private String clubName = "";
    private String clubDescription = "";

    Button homepageButton, reviewpageButton, chatpageButton, joinClubButton, applyForAdminButton, subClubAdminManageSubClubButton;
    TextView totalRatingTextView, subClubAdminTextView, joinClubAlreadyJoinedTextView, subClubSelectedAdminTextView, joinClubAlreadyJoinedTextView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_club_homepage);
        getSupportActionBar().hide();

        // Get sub-club name from previous activity
        Bundle b = getIntent().getExtras();
        if(b != null){
            clubName = b.getString("clubName");
            clubDescription = b.getString("clubDescription");
            //parentClub = b.getString("parentClub");
        }


        //System.out.println(clubName);

        // Write club name to page
        TextView clubNameTextView = (TextView) findViewById(R.id.clubNameTextView2);
        TextView clubDescriptionTextView = (TextView) findViewById(R.id.subClubDescriptionTextView2);


        clubNameTextView.setText(clubName);
        clubDescriptionTextView.setText(clubDescription);

        // Assign buttons
        homepageButton = findViewById(R.id.homeButton4);
        reviewpageButton = findViewById(R.id.reviewButton5);
        chatpageButton = findViewById(R.id.chatButton4);
        joinClubButton = findViewById(R.id.joinClubButton2);
        applyForAdminButton = findViewById(R.id.subClubAdminApplyButton);
        subClubAdminManageSubClubButton = findViewById(R.id.subClubAdminManageSubClubButton);

        // Disable sub club admin button, because firstly the system will check if user is the sub-club admin.
        subClubAdminManageSubClubButton.setEnabled(false);
        subClubAdminManageSubClubButton.setVisibility(View.GONE);

        subClubSelectedAdminTextView = findViewById(R.id.subClubSelectedAdminTextView);
        subClubSelectedAdminTextView.setVisibility(View.GONE);

        subClubAdminTextView = findViewById(R.id.subClubAdminTextView);

        //Firstly disable joinClubButton
        joinClubButton.setEnabled(false);
        //joinClubButton.setText("");

        joinClubAlreadyJoinedTextView3 = findViewById(R.id.joinClubAlreadyJoinedTextView3);
        joinClubAlreadyJoinedTextView3.setText("Join parent club first.");

        joinClubAlreadyJoinedTextView = findViewById(R.id.joinClubAlreadyJoinedTextView2);
        joinClubAlreadyJoinedTextView.setVisibility(View.GONE);


        // Assign rating text view
        totalRatingTextView = findViewById(R.id.totalRatingTextView2);

        calculateRating(new MyCallback() {
            @Override
            public void onCallback(double rate) {

                totalRatingTextView.setText("Average rating: " + String.valueOf(rate));

            }
        });

        NumberFormat formatter = new DecimalFormat("#0.00");

        // Apply for subclub admin
        applyForAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = FirebaseAuth.getInstance().getCurrentUser();
                String currentUserEmail = user.getEmail();
                Map<String, Object> newmap = new HashMap<>();
                newmap.put("memberEmail", currentUserEmail);
                db.collection("subclubs").document(clubName).collection("adminRequests").document(currentUserEmail).set(newmap);
                Toast.makeText(SubClubHomepageActivity.this, "You applied for sub-club admin.", Toast.LENGTH_SHORT).show();
                applyForAdminButton.setEnabled(false);
            }
        });

        // Club home page button
        homepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SubClubHomepageActivity.this, "You are already in club homepage.", Toast.LENGTH_SHORT).show();
            }
        });

        // Club review page button
        reviewpageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent for send club data to next activity
                Intent intent = new Intent(SubClubHomepageActivity.this, ClubReviewpageActivity.class);
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
                Intent intent = new Intent(SubClubHomepageActivity.this, ClubChatpageActivity.class);
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

                if(isJoinedBefore){
                    Toast.makeText(SubClubHomepageActivity.this, "You have joined this club before!", Toast.LENGTH_LONG).show();
                    return;
                }

                // Ask questions
                Intent intent = new Intent(SubClubHomepageActivity.this, QuizActivity.class);
                Bundle b = new Bundle();
                b.putString("clubName", clubName); // Club name
                b.putString("clubType", "subclubs");
                intent.putExtras(b); // Put your id to your next Intent
                startActivityForResult(intent, 2);

            }
        });

        subClubAdminManageSubClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubClubHomepageActivity.this, ManageSubClubActivity.class);
                Bundle b = new Bundle();
                b.putString("clubName", clubName); // Club name
                intent.putExtras(b); // Put your id to your next Intent
                startActivity(intent);
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
            joinClubButton.setEnabled(false);
            joinClubAlreadyJoinedTextView.setVisibility(View.VISIBLE);
            joinClubAlreadyJoinedTextView.setText("Login to join this sub-club.");


            applyForAdminButton.setEnabled(false);

            subClubAdminTextView.setText("Login to apply for Sub-Club Admin");
        }

        //If browsing user is admin, then he/she can not do certain things:.
        if(Globals.isUserDataLoaded && Globals.status==0){
            joinClubButton.setVisibility(View.GONE);
            joinClubButton.setEnabled(false);

            applyForAdminButton.setVisibility(View.GONE);
            applyForAdminButton.setEnabled(false);
        }

        //Non-registered users should not be checked, because it yields errors.
        if(Globals.isUserDataLoaded && Globals.status!=3 && Globals.status!=0){
            user = FirebaseAuth.getInstance().getCurrentUser();
            String currentUserEmail = user.getEmail();

            getParent(new MyCallback3() {
                @Override
                public void onCallback(String parent) {
                    checkIfJoinedToParentClub(parent);
                }
            });


            checkIfAdmin(currentUserEmail);

            checkIfJoinedBefore(currentUserEmail);

            checkIfRequestedBefore(currentUserEmail);

            assignSubClubAdmin();

            checkIfAdmin(currentUserEmail);

        }

        //Admin don't have to join the sub-club:
        if(Globals.isUserDataLoaded && Globals.status==0){
            joinClubAlreadyJoinedTextView.setVisibility(View.GONE);
            joinClubAlreadyJoinedTextView3.setVisibility(View.GONE);
            subClubAdminTextView.setVisibility(View.GONE);
            subClubSelectedAdminTextView.setVisibility(View.GONE);



        }


    }

    public void checkIfAdmin(String email)
    {
        db = FirebaseFirestore.getInstance();
        db.collection("subclubs").document(clubName).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();



                            // If an admin is selected before,
                            if (document.getString("adminEmail") != null)
                            {
                                String tempString = document.getString("adminEmail");

                                subClubSelectedAdminTextView.setText("Sub-club Admin: " + tempString);
                                subClubSelectedAdminTextView.setVisibility(View.VISIBLE);

                                subClubAdminTextView.setText("Sub-club admin is already selected.");

                                // If current user is admin:
                                if (document.getString("adminEmail").equals(email))
                                {
                                    // Set subClubAdminManageSubClubButton visible.
                                    subClubAdminManageSubClubButton.setEnabled(true);
                                    subClubAdminManageSubClubButton.setVisibility(View.VISIBLE);
                                }
                            }

                            // Admin is not selected before:
                            else
                            {
                                //Toast.makeText(SubClubHomepageActivity.this, "There is no admin!", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void checkIfRequestedBefore(String userEmail){
        db = FirebaseFirestore.getInstance();
        db.collection("subclubs").document(clubName).collection("adminRequests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            boolean isRequested = false;

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(userEmail.equals(document.getString("memberEmail"))){
                                    isRequested = true;

                                    //joinClubButton.setVisibility(View.GONE);
                                    applyForAdminButton.setEnabled(false);
                                    subClubAdminTextView.setText("You have already applied.");


                                    //joinClubAlreadyJoinedTextView.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void getParent(MyCallback3 myCallback)
    {
        //Loop through current user's joined clubs:
        db = FirebaseFirestore.getInstance();
        db.collection("subclubs").document(clubName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String par = "unassigned";
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            par = doc.getString("parentClub");

                            myCallback.onCallback(par);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void checkIfJoinedToParentClub(String parentClub){
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        //Loop through current user's joined clubs:
        db.collection("clubs").document(parentClub).collection("members")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //boolean isMember = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //If current club name is equal to any of the joined club name
                                if(user.getEmail().compareTo(document.getString("memberEmail")) == 0){
                                    joinClubButton.setEnabled(true);
                                    joinClubAlreadyJoinedTextView3.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        //Toast.makeText(SubClubHomepageActivity.this, "You have to join the parent club first!", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(2, resultCode, data);

        // If the user couldn't pass the quiz, she/he can not join the club
        if (!Globals.passedQuiz)
        {
            Toast.makeText(SubClubHomepageActivity.this, "Your quiz rate is too low.", Toast.LENGTH_LONG).show();
            return;
        }
        Globals.passedQuiz = false;

        user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserEmail = user.getEmail();

        //checkIfJoinedBefore(currentUserEmail);
        System.out.println(currentUserEmail);

        Map<String, Object> newmap = new HashMap<>();
        newmap.put("memberEmail", currentUserEmail);

        System.out.println(clubName);

        db = FirebaseFirestore.getInstance();

        // Save user in club database
        db.collection("subclubs").document(clubName).collection("members").document(currentUserEmail).set(newmap);

        // Save club in user database
        CollectionReference ref =  db.collection("users").document(currentUserEmail).collection("joined");
        Map<String, Object> mp = new HashMap<>();
        mp.put("ClubName", clubName);
        mp.put("IsSubClub", true);
        ref.add(mp);

        Toast.makeText(SubClubHomepageActivity.this, "You have successfully joined the " + clubName, Toast.LENGTH_SHORT).show();
        finish();
    }









    @Override
    public void onBackPressed() {
        finish();
    }

    public void checkIfJoinedBefore(String userEmail){
        db = FirebaseFirestore.getInstance();
        db.collection("subclubs").document(clubName).collection("members")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isMember = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(userEmail.equals(document.getString("memberEmail"))){
                                    //joinClubButton.setVisibility(View.GONE);
                                    joinClubButton.setEnabled(false);
                                    isMember = true;
                                    joinClubAlreadyJoinedTextView.setVisibility(View.VISIBLE);
                                    joinClubButton.setEnabled(false);
                                }
                            }
                            // If the user is not a member
                            if(!isMember)
                            {
                                applyForAdminButton.setEnabled(false);
                                subClubAdminTextView.setText("Only members can apply for Sub-Club Admin.");

                            }
                            else{
                                joinClubButton.setEnabled(false);
                                isJoinedBefore = true;
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void checkIfRequestsAreEnough(){
        db = FirebaseFirestore.getInstance();
        db.collection("subclubs").document(clubName).collection("adminRequests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            int requestCount = 0;
                            List<String> emailArrayList = new ArrayList<String>();



                            for (QueryDocumentSnapshot document : task.getResult()) {
                                requestCount++;
                                emailArrayList.add(document.getString("memberEmail"));
                            }
                            // If at least 3 people requested to be a sub-club admin, select the sub-club admin.
                            if(requestCount >= 3){
                                String newAdminEmail = "";

                                Random rand = new Random();

                                // Obtain a number between [0 - requestCount].
                                int n = rand.nextInt(requestCount);
                                newAdminEmail = emailArrayList.get(n);

                                Map<String, Object> newmap = new HashMap<>();
                                newmap.put("adminEmail", newAdminEmail);

                                db.collection("subclubs").document(clubName).update(newmap);

                                Toast.makeText(SubClubHomepageActivity.this, "The sub-club admin has been selected.", Toast.LENGTH_SHORT).show();
                                subClubAdminTextView.setVisibility(View.GONE);

                                //.setText("Sub-club Admin: " + newAdminEmail);
                                //subClubSelectedAdminTextView.setVisibility(View.VISIBLE);
                                //subClubSelectedAdminTextView.setEnabled(true);

                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
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

    public void assignSubClubAdmin(){
        //Check the requests, and assign sub-club admin
        DocumentReference docRef = db.collection("subclubs").document(clubName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //If admin is not selected, check requests.
                        if(document.getString("adminEmail")==null){
                            checkIfRequestsAreEnough();
                        }
                    }
                    else {

                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public interface MyCallback {
        void onCallback(double rate);
    }

    public interface MyCallback2 {
        void onCallback(List<String> members);
    }

    public interface MyCallback3 {
        void onCallback(String parent);
    }

}