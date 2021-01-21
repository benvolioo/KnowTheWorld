package com.benvolioo.knowtheworld;

import androidx.appcompat.app.AppCompatActivity;
import com.benvolioo.knowtheworld.QuizActivity.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AnswerActivity extends AppCompatActivity {
    public static final String CURRENT_SCORE = "currentScore";
    public static final String QUESTION_COUNT = "questionCounter";


    private Integer pbMax = QuizActivity.questionCountTotal;
    private Integer currentScore;


    private TextView txtAnswerResult;
    private Button btnNextQuestion;
    private ProgressBar pbPercentCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Intent intent = getIntent();
        Boolean checkedAnswer = intent.getBooleanExtra("ANSWER_RESULT", false);
        txtAnswerResult = findViewById(R.id.txtAnswerResult);

//        currentScore = intent.getIntExtra("CURRENT_SCORE", 0);
//
        setTxtCheckedAnswer(checkedAnswer);
//
        btnNextQuestion = findViewById(R.id.btnNextQuestion);
//
//        Integer questionCounter = intent.getIntExtra("QUESTION_COUNT", 0);
//        pbPercentCorrect.setProgress(questionCounter/pbMax);

    }


    private void setTxtCheckedAnswer(Boolean checkedAnswer) {
        if (checkedAnswer) {
            txtAnswerResult.setText("Correct");
        } else {
            txtAnswerResult.setText("Not quite");
        }
    }
}
