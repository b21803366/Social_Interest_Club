package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BanUserActivity extends AppCompatActivity {

    private static final String TAG = "Ban User";
    List<UserStruct> users = new ArrayList<UserStruct>();
    LinearLayout banUsersLayout;
    FirebaseFirestore db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_clubs);
        getSupportActionBar().setTitle("Ban Users");
        banUsersLayout = (LinearLayout) findViewById(R.id.banUsersLayout);
        readUsers();
    }

    public void readUsers(){
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                String userName = document.getString("userName");
                                long status = document.getLong("status");
                                boolean isBanned = document.getBoolean("isBanned");
                                if(status == 0)
                                    continue;
                                users.add(new UserStruct());
                                users.get(users.size()-1).setUserName(userName);
                                users.get(users.size()-1).setStatus((int)status);
                                users.get(users.size()-1).setBanned(isBanned);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        createTable();
                    }
                });
    }
    public void createTable(){
        for(int i=0; i<users.size(); i++){
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText(users.get(i).getUserName());
            Button button = new Button(this);
            final int index = i;
            button.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(BanUserActivity.this).create();
                    alertDialog.setTitle("Ban User");
                    alertDialog.setMessage("Are you sure you want to ban " + users.get(index).getUserName() + "?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    banUserFromDB(users.get(index));
                                }
                            });
                    alertDialog.show();
                }
            });
            button.setText("Ban");
            tableRow.addView(textView);
            tableRow.addView(button);
            banUsersLayout.addView(tableRow);
        }
    }

    public void banUserFromDB(UserStruct user)
    {
        user.setBanned(true);
        db.collection("users").document(user.getUserName())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        Toast.makeText(BanUserActivity.this, user.getUserName() + " banned successfully." , Toast.LENGTH_SHORT).show();

                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        Toast.makeText(BanUserActivity.this, user.getUserName() + " ban failure." , Toast.LENGTH_SHORT).show();
                    }
                });
    }
}