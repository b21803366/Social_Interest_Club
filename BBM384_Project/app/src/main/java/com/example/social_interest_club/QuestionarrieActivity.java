package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.social_interest_club.DataStructsAndDAOs.ClubStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionarrieActivity extends AppCompatActivity {

    FirebaseFirestore db = null;

    private static final String TAG = "Questionnaire";

    Button button1, button2, button3, button4, button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionarrie);
        getSupportActionBar().setTitle("Questionnaire");

        button1 = findViewById(R.id.ans1);
        button2 = findViewById(R.id.ans2);
        button3 = findViewById(R.id.ans3);
        button4 = findViewById(R.id.ans4);
        button5 = findViewById(R.id.ans5);

        // Get questions from the clubs (from club database)
        if (!Globals.is_questions_ready)
        {
            // Get the questions
            getQuestions(new MyCallback() {
                @Override
                public void onCallback(List<String> lst, List<String> lst2) {
                    Globals.questions = lst;
                    Globals.questClubs = lst2;
                    Globals.is_questions_ready = true;

                    // Ask questions one by one
                    if (Globals.question_index < Globals.questions.size())
                    {
                        // Ask
                        TextView questionTextView = (TextView) findViewById(R.id.questView);
                        questionTextView.setText(Globals.questions.get(Globals.question_index));
                    }
                    else
                    {
                        Globals.question_index = 0;
                        Globals.is_questions_ready = false;
                        Globals.questions.removeAll(Globals.questions); // Empty the list

                        // Store interest rate
                        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference userRef = db.collection("users").document(Globals.regUserName)
                                .collection("interestRates");
                        //userRef.add(Globals.interest_rates);*/

                        db.collection("users").document(Globals.regUserName).collection("interestRates").document("interestRates").set(Globals.interest_rates);




                        // Go to homepage
                        startActivity(new Intent(getApplicationContext(), HomepageActivity.class));
                    }

                }
            });
        }
        else
        {
            if (Globals.questions.size() < Globals.question_index)
            {
                // Ask
                TextView questionTextView = (TextView) findViewById(R.id.questView);
                questionTextView.setText(Globals.questions.get(Globals.question_index));
            }
            else
            {
                Globals.question_index = 0;
                Globals.is_questions_ready = false;
                Globals.questions.removeAll(Globals.questions); // Empty the list

                // Go to homepage

                // Store interest rate
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                /*CollectionReference userRef = db.collection("users").document(Globals.regUserName)
                        .collection("interestRates");
                userRef.add(Globals.interest_rates);*/

                db.collection("users").document(Globals.regUserName).collection("interestRates").document("interestRates").set(Globals.interest_rates);


                startActivity(new Intent(getApplicationContext(), HomepageActivity.class));
            }
        }

        // Buttons
        button1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Store rate
                int rate = 100;
                Globals.interest_rates.put(Globals.questClubs.get(Globals.question_index), rate);

                Globals.question_index += 1;

                if (Globals.question_index >= Globals.questions.size())
                {
                    Globals.question_index = 0;
                    Globals.is_questions_ready = false;
                    Globals.questions.removeAll(Globals.questions); // Empty the list

                    // Store interest rate
                    /*FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference userRef = db.collection("users").document(Globals.regUserName)
                            .collection("interestRates");
                    userRef.add(Globals.interest_rates);*/

                    db.collection("users").document(Globals.regUserName).collection("interestRates").document("interestRates").set(Globals.interest_rates);


                    // Go to homepage
                    startActivity(new Intent(getApplicationContext(), HomepageActivity.class));
                }
                else
                {
                    // Ask again (go new question)
                    TextView questionTextView = (TextView) findViewById(R.id.questView);
                    questionTextView.setText(Globals.questions.get(Globals.question_index));
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Store rate
                int rate = 75;
                Globals.interest_rates.put(Globals.questClubs.get(Globals.question_index), rate);

                Globals.question_index += 1;

                if (Globals.question_index >= Globals.questions.size())
                {
                    Globals.question_index = 0;
                    Globals.is_questions_ready = false;
                    Globals.questions.removeAll(Globals.questions); // Empty the list

                    // Store interest rate
                    /*FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference userRef = db.collection("users").document(Globals.regUserName)
                            .collection("interestRates");
                    userRef.add(Globals.interest_rates);*/

                    db.collection("users").document(Globals.regUserName).collection("interestRates").document("interestRates").set(Globals.interest_rates);


                    // Go to homepage
                    startActivity(new Intent(getApplicationContext(), HomepageActivity.class));
                }
                else
                {
                    // Ask again (go new question)
                    TextView questionTextView = (TextView) findViewById(R.id.questView);
                    questionTextView.setText(Globals.questions.get(Globals.question_index));
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Store rate
                int rate = 50;
                Globals.interest_rates.put(Globals.questClubs.get(Globals.question_index), rate);

                Globals.question_index += 1;

                if (Globals.question_index >= Globals.questions.size())
                {
                    Globals.question_index = 0;
                    Globals.is_questions_ready = false;
                    Globals.questions.removeAll(Globals.questions); // Empty the list

                    // Store interest rate
                    /*FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference userRef = db.collection("users").document(Globals.regUserName)
                            .collection("interestRates");
                    userRef.add(Globals.interest_rates);*/

                    db.collection("users").document(Globals.regUserName).collection("interestRates").document("interestRates").set(Globals.interest_rates);


                    // Go to homepage
                    startActivity(new Intent(getApplicationContext(), HomepageActivity.class));
                }
                else
                {
                    // Ask again (go new question)
                    TextView questionTextView = (TextView) findViewById(R.id.questView);
                    questionTextView.setText(Globals.questions.get(Globals.question_index));
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Store rate
                int rate = 25;
                Globals.interest_rates.put(Globals.questClubs.get(Globals.question_index), rate);

                Globals.question_index += 1;

                if (Globals.question_index >= Globals.questions.size())
                {
                    Globals.question_index = 0;
                    Globals.is_questions_ready = false;
                    Globals.questions.removeAll(Globals.questions); // Empty the list

                    // Store interest rate
                    /*FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference userRef = db.collection("users").document(Globals.regUserName)
                            .collection("interestRates");
                    userRef.add(Globals.interest_rates);*/

                    db.collection("users").document(Globals.regUserName).collection("interestRates").document("interestRates").set(Globals.interest_rates);


                    // Go to homepage
                    startActivity(new Intent(getApplicationContext(), HomepageActivity.class));
                }
                else
                {
                    // Ask again (go new question)
                    TextView questionTextView = (TextView) findViewById(R.id.questView);
                    questionTextView.setText(Globals.questions.get(Globals.question_index));
                }
            }
        });
        button5.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Store rate
                int rate = 0;
                Globals.interest_rates.put(Globals.questClubs.get(Globals.question_index), rate);

                Globals.question_index += 1;

                if (Globals.question_index >= Globals.questions.size())
                {
                    Globals.question_index = 0;
                    Globals.is_questions_ready = false;
                    Globals.questions.removeAll(Globals.questions); // Empty the list

                    // Store interest rate
                    /*FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference userRef = db.collection("users").document(Globals.regUserName)
                            .collection("interestRates");
                    userRef.add(Globals.interest_rates);*/

                    db.collection("users").document(Globals.regUserName).collection("interestRates").document("interestRates").set(Globals.interest_rates);


                    // Go to homepage
                    startActivity(new Intent(getApplicationContext(), HomepageActivity.class));
                }
                else
                {
                    // Ask again (go new question)
                    TextView questionTextView = (TextView) findViewById(R.id.questView);
                    questionTextView.setText(Globals.questions.get(Globals.question_index));
                }
            }
        });

    }

    private void getQuestions(MyCallback myCallback)
    {
        db = FirebaseFirestore.getInstance();
        db.collection("clubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> myQuests = new ArrayList<String>();
                            List<String> myCls = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                // Store question
                                String question = document.getString("q1");
                                String cl = document.getString("clubName");
                                myQuests.add(question);
                                myCls.add(cl);
                            }

                            myCallback.onCallback(myQuests, myCls);

                        } else {
                            Log.w(TAG, "Error getting club documents.", task.getException());
                        }
                    }
                });
    }


    public interface MyCallback {
        void onCallback(List<String> lst, List<String> lst2);
    }


}