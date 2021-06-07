package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_interest_club.DataStructsAndDAOs.ClubStruct;
import com.example.social_interest_club.DataStructsAndDAOs.UserStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomepageActivity extends AppCompatActivity {

    private static final String TAG = "Homepage";

    String currentUserString;
    Button logoutButton, profileButton, browseclubButton, browseSubclubButton;
    LinearLayout recommendedClubsLayout, newClubsLayout;

    FirebaseFirestore db = null;
    FirebaseUser user;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        getSupportActionBar().setTitle("Homepage");

        mAuth = FirebaseAuth.getInstance();

        currentUserString = mAuth.getCurrentUser().getEmail();

        // Store user data for global using
        getUserInfo();

        // Text plane
        setContentView(R.layout.activity_homepage);
        TextView textView = (TextView) findViewById(R.id.homepageUserEmail);
        textView.setText(currentUserString);

        // Buttons
        logoutButton = findViewById(R.id.homepageLogoutButton);
        profileButton = findViewById(R.id.homepageProfileButton);
        browseclubButton = findViewById(R.id.browseClubButton);
        browseSubclubButton = findViewById(R.id.browseSubClubButton);

        recommendedClubsLayout = (LinearLayout) findViewById(R.id.recView);
        newClubsLayout = (LinearLayout) findViewById(R.id.newView);

        // Clear linear layouts
        recommendedClubsLayout.removeAllViews();
        newClubsLayout.removeAllViews();

        if(savedInstanceState != null){
            readRecClubs();

            // Create New Clubs table
            user = FirebaseAuth.getInstance().getCurrentUser();
            String currentUserEmail = user.getEmail();

            readNewClubs(currentUserEmail);

        }



        // Browse Sub Club button
        browseSubclubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BrowseSubClubActivity.class));

            }
        });

        // Logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(HomepageActivity.this).create();
                alertDialog.setTitle("Logout");
                alertDialog.setMessage("Are you sure you want to log out?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent i = new Intent(HomepageActivity.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);

                                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                Toast.makeText(HomepageActivity.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                alertDialog.show();
            }
        });

        // Profile button
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminProfileActivity.class));

            }
        });

        // Browse club button
        browseclubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BrowseClubsActivity.class));

            }
        });


    }   // End of onCreate

    // After returning back to this activity, refresh the Recommended and New Club layouts.
    @Override
    protected void onResume() {
        super.onResume();

        recommendedClubsLayout.removeAllViews();
        newClubsLayout.removeAllViews();


        readRecClubs();

        user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserEmail = user.getEmail();

        readNewClubs(currentUserEmail);
    }

    public void readRecClubs(){
        db = FirebaseFirestore.getInstance();
        db.collection("clubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<ClubStruct> recClubs = new ArrayList<ClubStruct>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String clubName = document.getString("clubName");
                                String clubDescription = document.getString("clubDescription");

                                //Check interest rate, if greater or equal to 50, then add it to the recommended club list.
                                //int interestRate = document.getLong("").intValue()

                                recClubs.add(new ClubStruct(clubName, clubDescription));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        createRecTable(recClubs);
                    }
                });
    }

    public void createRecTable(List<ClubStruct> clubs){
        for(int i=0; i<clubs.size(); i++){
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText(clubs.get(i).getClubName());
            TextView textView2 = new TextView(this);

            int tempRate;

            int finalI = i;
            getRate(currentUserString, clubs.get(i).getClubName(), new MyCallback() {
                @Override
                public void onCallback(boolean done) {}
                @Override
                public void onCallback(long rate) {
                    String text = " Interest: " + String.valueOf(rate);
                    textView2.setText(text);

                    Button button = new Button(HomepageActivity.this);
                    final int index = finalI;
                    button.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Create intent for send club data to next activity
                            Intent intent = new Intent(HomepageActivity.this, ClubHomepageActivity.class);
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
                    tableRow.addView(textView2);
                    tableRow.addView(button);


                    if (Double.parseDouble(String.valueOf(rate)) >= 50.0)
                    {
                        recommendedClubsLayout.addView(tableRow);
                    }

                }
            });

        }
    }

    public void readNewClubs(String userEmail){
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(userEmail).collection("newClubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> newClubs = new ArrayList<String>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                if (document.getString("clubName").compareTo("DELETED_FROM_HERE") != 0)
                                    newClubs.add(document.getString("clubName"));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        createNewTable(newClubs);
                    }
                });
    }

    public void createNewTable(List<String> clubs) {
        for (int i = 0; i < clubs.size(); i++) {
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText(clubs.get(i));
            TextView textView2 = new TextView(this);

            Button button = new Button(this);
            final int index = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create intent for send club data to next activity
                    Intent intent = new Intent(HomepageActivity.this, ClubHomepageActivity.class);
                    Bundle b = new Bundle();
                    b.putString("clubName", clubs.get(index)); //Your id
                    intent.putExtras(b); // Put your id to your next Intent
                    startActivity(intent);
                    //finish();
                }
            });
            button.setText("Browse");

            tableRow.addView(textView);
            tableRow.addView(textView2);
            tableRow.addView(button);
            newClubsLayout.addView(tableRow);
        }
    }




    public void getRate(String username, String clubname, MyCallback myCallback)
    {
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(username).collection("interestRates")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String ret = "NONE";
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> mp = document.getData();
                                for (Map.Entry<String, Object> ent : mp.entrySet())
                                {
                                    if (ent.getKey().compareTo(clubname) == 0)
                                    {
                                        myCallback.onCallback((long)ent.getValue());
                                        return;
                                    }

                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    //Not sure about this
    @Override
    public void onBackPressed()
    {
        this.moveTaskToBack(false);
    }

    // Gets the user info from database
    public void getUserInfo()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(currentUserString)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserStruct usr = documentSnapshot.toObject(UserStruct.class);
                Globals.userName = usr.getUserName();
                Globals.status = usr.getStatus();
                Globals.banCount = usr.getBanCount();
                Globals.isBanned = usr.isBanned();
                Globals.isComplaint = usr.isComplaint();
                Globals.bannedDay = usr.getBannedDay();
                Globals.isUserDataLoaded = true;
            }
        });
    }

    public interface MyCallback {
        void onCallback(boolean done);
        void onCallback(long rate);
    }

}