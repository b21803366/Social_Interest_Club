package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_interest_club.DataStructsAndDAOs.ClubStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClubChatpageActivity extends AppCompatActivity {

    private static final String TAG = "Club Chatpage";
    Button homepageButton, reviewpageButton, chatpageButton, sendMassageButton;
    Spinner spinnerMessageTo;
    EditText sendMessageInput;
    TextView chatTextView;
    ScrollView scrollView;
    FirebaseFirestore db = null;
    FirebaseUser user;
    ArrayList<String> members = new ArrayList<String>();
    ArrayList<String> spinnerStrings = new ArrayList<String>();

    private String clubName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_chatpage);

        getSupportActionBar().hide();

        // Get club name from previous activity
        Bundle b = getIntent().getExtras();
        if(b != null)
            clubName = b.getString("clubName");

        user = FirebaseAuth.getInstance().getCurrentUser();

        homepageButton = findViewById(R.id.homeButton2);
        reviewpageButton = findViewById(R.id.reviewButton2);
        chatpageButton = findViewById(R.id.chatButton2);
        sendMassageButton = findViewById(R.id.sendMessageButton);

        chatTextView = findViewById(R.id.chatTextView);
        sendMessageInput = findViewById(R.id.sendMessageInput);
        spinnerMessageTo = findViewById(R.id.spinnerMassageTo);
        scrollView = findViewById(R.id.scrollView);
        readMessages();
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        },1000);
        readMembers();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerStrings);
        spinnerMessageTo.setAdapter(adapter);

        // Send Massage Button
        sendMassageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent for send club data to next activity
                String clubNameString = clubName;
                String fromString = user.getEmail();
                String toString;
                if(spinnerMessageTo.getSelectedItem().toString().equals("Everyone"))
                    toString = "Everyone";
                else
                    toString = members.get(spinnerStrings.indexOf(spinnerMessageTo.getSelectedItem().toString())-1);

                String messageString = sendMessageInput.getText().toString().trim();

                Map<String, Object> message = new HashMap<>();
                message.put("clubName", clubNameString);
                message.put("from", fromString);
                message.put("to", toString);
                message.put("message", messageString);

                sendMessageInput.setText("");

                createMessage(message);
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                },1000);
            }
        });
        // Club home page button
        homepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent for send club data to next activity
                Intent intent = new Intent(ClubChatpageActivity.this, ClubHomepageActivity.class);
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
                // Create intent for send club data to next activity
                Intent intent = new Intent(ClubChatpageActivity.this, ClubReviewpageActivity.class);
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

                Toast.makeText(ClubChatpageActivity.this, "You are already in club chat page.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void readMessages(){
        db = FirebaseFirestore.getInstance();
        db.collection("messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            String messagesString = "";
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(!document.getString("clubName").equals(clubName))
                                    continue;
                                if(document.getString("to").equals(user.getEmail())){
                                    String from = document.getString("from");
                                    messagesString = messagesString.concat(from.split("@")[0] + "(private): ");
                                    String message = document.getString("message");
                                    messagesString = messagesString.concat(message + "\n");
                                }
                                else if (document.getString("to").equals("Everyone")) {
                                    String from = document.getString("from");
                                    messagesString = messagesString.concat(from.split("@")[0] + ": ");
                                    String message = document.getString("message");
                                    messagesString = messagesString.concat(message + "\n");
                                }
                                else if(document.getString("from").equals(user.getEmail())){
                                    String from = document.getString("from");
                                    messagesString = messagesString.concat(from.split("@")[0] + "(private): ");
                                    String message = document.getString("message");
                                    messagesString = messagesString.concat(message + "\n");
                                }
                            }
                            chatTextView.setText(messagesString);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void readMembers(){
        db = FirebaseFirestore.getInstance();
        spinnerStrings.add("Everyone");
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String userName = document.getString("userName");
                                addIfMemberInClub(userName);
                               // members.add(userName);
                                //spinnerStrings.add(userName.split("@")[0]);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void addIfMemberInClub(String userName){

        db.collection("users").document(userName).collection("joined")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String tempClubName = document.getString("ClubName");
                                if(tempClubName.compareTo(clubName)==0){
                                    members.add(userName);
                                    spinnerStrings.add(userName.split("@")[0]);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });


    }


    public void createMessage(Map<String, Object> message){
        db = FirebaseFirestore.getInstance();
        db.collection("messages").document(Instant.now().toString()).set(message);
        readMessages();
    }
}