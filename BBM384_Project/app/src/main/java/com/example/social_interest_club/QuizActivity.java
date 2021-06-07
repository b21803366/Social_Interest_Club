package com.example.social_interest_club;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    String clubName = "";
    String clubType = "";
    int questionCount = 3;
    int questIndex = 0;
    int totalRate = 0;
    FirebaseFirestore db = null;
    private static final String TAG = "Questionnaire";

    Button button1, button2, button3, button4, button5;

    List<String> lsst = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getSupportActionBar().setTitle("Quiz");

        // Get club name from previous activity
        Bundle b = getIntent().getExtras();
        if(b != null){
            clubName = b.getString("clubName");
            clubType = b.getString("clubType");
        }

        TextView questionTextView = (TextView) findViewById(R.id.questView3);
        button1 = findViewById(R.id.ans10);
        button2 = findViewById(R.id.ans11);
        button3 = findViewById(R.id.ans12);
        button4 = findViewById(R.id.ans13);
        button5 = findViewById(R.id.ans14);

        ask(clubType, new MyCallback() {
            @Override
            public void onCallback(List<String> lst) {

                questionTextView.setText(lst.get(questIndex));
                lsst.addAll(lst);
            }
        });

        // Buttons
        button1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                int rate = 100;
                totalRate += rate;

                questIndex++;
                if (questIndex >= questionCount)
                {
                    Globals.quizResult = totalRate / questionCount;

                    if (totalRate / questionCount >= 50)
                        Globals.passedQuiz = true;
                    finish();
                    return;
                }
                questionTextView.setText(lsst.get(questIndex));
            }
        });
        button2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                int rate = 75;
                totalRate += rate;

                questIndex++;
                if (questIndex >= questionCount)
                {
                    Globals.quizResult = totalRate / questionCount;

                    if (totalRate / questionCount >= 50)
                        Globals.passedQuiz = true;
                    finish();
                    return;
                }
                questionTextView.setText(lsst.get(questIndex));
            }
        });
        button3.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                int rate = 50;
                totalRate += rate;

                questIndex++;
                if (questIndex >= questionCount)
                {
                    Globals.quizResult = totalRate / questionCount;

                    if (totalRate / questionCount >= 50)
                        Globals.passedQuiz = true;
                    finish();
                    return;
                }
                questionTextView.setText(lsst.get(questIndex));
            }
        });
        button4.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                int rate = 25;
                totalRate += rate;

                questIndex++;
                if (questIndex >= questionCount)
                {
                    Globals.quizResult = totalRate / questionCount;

                    if (totalRate / questionCount >= 50)
                        Globals.passedQuiz = true;
                    finish();
                    return;
                }
                questionTextView.setText(lsst.get(questIndex));
            }
        });
        button5.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                int rate = 0;
                totalRate += rate;

                questIndex++;
                if (questIndex >= questionCount)
                {
                    Globals.quizResult = totalRate / questionCount;
                    if (totalRate / questionCount >= 50){
                        Globals.passedQuiz = true;
                    }

                    finish();
                    return;
                }
                questionTextView.setText(lsst.get(questIndex));
            }
        });
    }

    public void ask(String clubType, MyCallback myCallback)
    {
        // Get questions from database
        db = FirebaseFirestore.getInstance();
        db.collection(clubType).document(clubName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            List<String> quests = new ArrayList<String>();
                            quests.add(document.getString("q1"));
                            quests.add(document.getString("q2"));
                            quests.add(document.getString("q3"));

                            myCallback.onCallback(quests);

                        } else {
                            Log.w(TAG, "Error getting club documents.", task.getException());
                        }
                    }
                });

    }

    public interface MyCallback {
        void onCallback(List<String> lst);
    }
}