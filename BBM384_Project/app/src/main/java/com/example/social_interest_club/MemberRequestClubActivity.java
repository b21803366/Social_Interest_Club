package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_interest_club.DataStructsAndDAOs.RequestClubStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberRequestClubActivity extends AppCompatActivity {

    private static final String TAG = "Member Club Request";
    List<RequestClubStruct> clubRequests = new ArrayList<RequestClubStruct>();
    LinearLayout memberClubRequestsLayout;
    FirebaseFirestore db = null;

    FirebaseUser user;

    Button applyButton;

    EditText memberRequestClubName;

    int temporaryNumOfReq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_request_club);
        getSupportActionBar().setTitle("Request a Club");

        applyButton = findViewById(R.id.applyButton);
        memberRequestClubName = findViewById(R.id.memberRequestClubNameInput);

        applyButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                if(memberRequestClubName.getText().toString().length() < 7){
                    Toast.makeText(MemberRequestClubActivity.this,"Input length for club request must be longer than 6 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("clubrequests")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        String clubName = document.getString("clubName");

                                        if (clubName.equals(memberRequestClubName.getText().toString()))
                                        {
                                            Toast.makeText(MemberRequestClubActivity.this, "Club request for " + clubName + " already exists. ", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    String tempS = memberRequestClubName.getText().toString();

                                    // Create clubrequest
                                    Map<String, Object> req = new HashMap<>();
                                    req.put("clubName", memberRequestClubName.getText().toString());
                                    req.put("numberOfRequests", 1);
                                    db.collection("clubrequests").document(memberRequestClubName.getText().toString()).set(req);
                                    Toast.makeText(MemberRequestClubActivity.this,"Club successfully requested. ", Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }

                            }
                        });
                finish();
            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();


        memberClubRequestsLayout = (LinearLayout) findViewById(R.id.memberClubRequestsLayout);
        readClubRequests();





    }// End of onCreate function


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

            //todo if already supported the request, make button unclickable.
            /*if(){
                button.setClickable(false);
                button.setEnabled(false);
            }*/

            TextView textView2 = new TextView(this);
            textView2.setText("Number of requests: " + Integer.toString(clubRequests.get(i).getNumberOfRequests()));


            button.setText("Support");

            final int index = i;
            button.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo Increment number of requests of a club by 1, and add current user's email to requestedBy collection

                    db = FirebaseFirestore.getInstance();
                    db.collection("clubrequests")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {



                                            // If current button's clubname is equal to database's
                                            if(clubRequests.get(index).getClubName().equals(document.getString("clubName"))){
                                                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx");
                                                DocumentReference docref = db.collection("clubrequests").document(clubRequests.get(index).getClubName());
                                                docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        DocumentSnapshot document = task.getResult();
                                                        temporaryNumOfReq = document.getLong("numberOfRequests").intValue();
                                                        System.out.println(temporaryNumOfReq);
                                                        docref.update("numberOfRequests", temporaryNumOfReq+1);

                                                    }
                                                });

                                            //docref.update("numberOfRequests", temporaryNumOfReq+1);


                                            }

                                            //Log.d(TAG, document.getId() + " => " + document.getData());
                                            //String requestedClubName = document.getString("clubName");
                                            //int numberOfRequests = document.getLong("numberOfRequests").intValue();
                                            //clubRequests.add(new RequestClubStruct(requestedClubName, numberOfRequests));
                                        }
                                    } else {
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                    //createTable();
                                }
                            });

                    // Create intent for send club data to next activity
                    //Intent intent = new Intent(MemberRequestClubActivity.this, CreateRequestedClubActivity.class);
                    //Bundle b = new Bundle();
                    //b.putString("clubName", clubRequests.get(index).getClubName()); //Your id
                    //intent.putExtras(b); // Put your id to your next Intent
                    //startActivity(intent);
                    Toast.makeText(MemberRequestClubActivity.this, "Club request successfully supported.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            //button.setText("Create");
            tableRow.addView(textView);
            tableRow.addView(button);
            tableRow.addView(textView2);
            memberClubRequestsLayout.addView(tableRow);
        }
    }

}