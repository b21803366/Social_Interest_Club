package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;

import com.example.social_interest_club.DataStructsAndDAOs.ClubStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminProfileActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView emailText;
    Button manageClubsButton;

    private static final String TAG = "Profile";
    LinearLayout browseClubsLayout;

    FirebaseFirestore db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        getSupportActionBar().setTitle("Profile");

        manageClubsButton = findViewById(R.id.manageClubButton);
        browseClubsLayout = findViewById(R.id.joinedClubsView);

        mAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.emailText);
        emailText.setText(mAuth.getCurrentUser().getEmail() + "\'s profile");

        if(Globals.status != 0){
            manageClubsButton.setVisibility(View.GONE);
            manageClubsButton.setEnabled(false);
        }

        // Store joined clubs and show them
        storeJoinedClubs(new MyCallback() {
            @Override
            public void onCallback(List<String> lst, List<Boolean> lst2) {
                // When loading is finished, show the clubs
                createClubTable(lst, lst2);
            }
        });

        manageClubsButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), AdminManageClubsActivity.class));
            }
        });
    }

    public void storeJoinedClubs(MyCallback myCallback){
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(Globals.userName).collection("joined")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Boolean> isSubclub = new ArrayList<Boolean>();
                            List<String> joinedClubs = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                joinedClubs.add(document.getString("ClubName"));
                                isSubclub.add(document.getBoolean("IsSubClub"));
                            }
                            myCallback.onCallback(joinedClubs, isSubclub);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public interface MyCallback {
        void onCallback(List<String> lst, List<Boolean> lst2);
    }


    public void createClubTable(List<String> clubs, List<Boolean> isSubClub){
        for(int i=0; i<clubs.size(); i++){
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText(clubs.get(i));
            Button button = new Button(this);
            final int index = i;
            button.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create intent for send club data to next activity
                    Intent intent;
                    if (isSubClub.get(index))
                    {
                        intent = new Intent(AdminProfileActivity.this, SubClubHomepageActivity.class);
                    }
                    else
                    {
                        intent = new Intent(AdminProfileActivity.this, ClubHomepageActivity.class);
                    }

                    Bundle b = new Bundle();
                    b.putString("clubName", clubs.get(index));

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
