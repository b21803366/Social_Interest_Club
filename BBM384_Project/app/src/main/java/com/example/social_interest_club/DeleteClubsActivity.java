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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DeleteClubsActivity extends AppCompatActivity {

    private static final String TAG = "Delete Clubs";
    List<ClubStruct> clubs = new ArrayList<ClubStruct>();
    LinearLayout deleteClubsLayout;
    FirebaseFirestore db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_clubs);
        getSupportActionBar().setTitle("Delete Club");
        deleteClubsLayout = (LinearLayout) findViewById(R.id.banUsersLayout);
        readClubs();
    }

    public void readClubs(){
        db = FirebaseFirestore.getInstance();
        db.collection("clubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String clubsString = "";
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String clubName = document.getString("clubName");
                                String clubDescription = document.getString("clubDescription");
                                clubs.add(new ClubStruct(clubName, clubDescription));
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
            textView.setText(clubs.get(i).getClubName());
            Button button = new Button(this);
            final int index = i;
            button.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(DeleteClubsActivity.this).create();
                    alertDialog.setTitle("Delete Club");
                    alertDialog.setMessage("Are you sure you want to delete " + clubs.get(index).getClubName() + "?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteClubFromDB(clubs.get(index).getClubName());
                                }
                            });
                    alertDialog.show();
                }
            });
            button.setText("Delete");
            tableRow.addView(textView);
            tableRow.addView(button);
            deleteClubsLayout.addView(tableRow);
        }
    }

    public void deleteClubFromDB(String clubName)
    {
        db.collection("clubs").document(clubName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        Toast.makeText(DeleteClubsActivity.this, clubName + " deleted successfully." , Toast.LENGTH_SHORT).show();

                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        Toast.makeText(DeleteClubsActivity.this, clubName + " deletion failure." , Toast.LENGTH_SHORT).show();
                    }
         });
    }
}