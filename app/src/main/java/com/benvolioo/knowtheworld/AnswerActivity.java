package com.benvolioo.knowtheworld;

import androidx.appcompat.app.AppCompatActivity;
import com.benvolioo.knowtheworld.QuizActivity.*;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AnswerActivity extends AppCompatActivity {
    private int pbMax = QuizActivity.questionCountTotal;
    private TextView txtCheckedAnswer;
//    private Bundle extras = getIntent().getExtras();
//    private final Integer answerNumber = getIntent().getIntExtra("ANSWER_NUMBER", 0);
//    private final Boolean checkedAnswer = getIntent().getBooleanExtra("CHECKED_ANSWER", false);
//    private Boolean checkedAnswer = extras.getBoolean("CHECKED_ANSWER");
//    private Integer answerNumber = extras.getInt("ANSWER_NUMBER");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Intent intent = getIntent();
        Boolean checkedAnswer = intent.getBooleanExtra("CHECKED_ANSWER", false);
        txtCheckedAnswer = findViewById(R.id.txtCheckedAnswer);
        setTxtCheckedAnswer(checkedAnswer);
    }

    private void setTxtCheckedAnswer(Boolean checkedAnswer) {
        if (checkedAnswer) {
            txtCheckedAnswer.setText("Correct");
        } else {
            txtCheckedAnswer.setText("Not quite");
        }
    }
}