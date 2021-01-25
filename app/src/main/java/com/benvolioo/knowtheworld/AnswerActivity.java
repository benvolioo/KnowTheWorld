package com.benvolioo.knowtheworld;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AnswerActivity extends AppCompatActivity {
    public static final String CURRENT_SCORE = "currentScore";
    public static final String QUESTION_COUNT = "questionCounter";


    private Integer pbMax = QuizActivity.questionCountTotal;
    private Integer currentScore;
    private Integer questionCount;
    private Boolean checkedAnswer;


    private TextView tvAnswerResult;
    private TextView tvAnswerInfo;
    private Button btnNextQuestion;
    private ProgressBar pbPercentCorrect;
    private Intent intent;

    private ArrayList<Question> questionList;
    private Question currentQuestion;
    private String answerInfo;

    private long backPressedTime;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        intent = getIntent();

        tvAnswerResult = findViewById(R.id.txtAnswerResult);
        tvAnswerInfo = findViewById(R.id.txtAnswerInfo);
        btnNextQuestion = findViewById(R.id.btnNextQuestion);
        pbPercentCorrect = findViewById(R.id.pbAnswers);

        checkedAnswer = intent.getBooleanExtra("ANSWER_RESULT", true);
        currentScore = intent.getIntExtra("CURRENT_SCORE", 0);
        questionCount = intent.getIntExtra("QUESTION_COUNT", 0);

        setTxtCheckedAnswer();

        if (lastQuestion()){
            btnNextQuestion.setText("Finish Quiz");
        }
        View.OnClickListener onclickNextQuestion = (View view) -> finishAnswer();
        btnNextQuestion.setOnClickListener(onclickNextQuestion);

        int currentProgress = (int) (currentScore * 100.0f)/pbMax;
        pbPercentCorrect.setProgress(currentProgress);

        updateAnswerInfo();

    }

    private void updateAnswerInfo() {
        QuizDatabaseHelper databaseHelper = new QuizDatabaseHelper(this);
        questionList = databaseHelper.getAllQuestions();
        currentQuestion = questionList.get(questionCount - 1);
        answerInfo = currentQuestion.getAnswerInfo();
        setTvAnswerInfo(answerInfo);
    }

    private void setTvAnswerInfo(String answerInfo) {
        tvAnswerInfo.setText(answerInfo);
    }

    private boolean lastQuestion() {
        return questionCount == QuizActivity.questionCountTotal;
    }

    private void finishAnswer() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(QUESTION_COUNT, questionCount);
        resultIntent.putExtra(CURRENT_SCORE, currentScore);
        setResult(RESULT_OK, resultIntent);
        finish();
    }


    private void setTxtCheckedAnswer() {
        if (checkedAnswer) {
            tvAnswerResult.setText("Correct");
        } else {
            tvAnswerResult.setText("Not quite");
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToQuestion();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }


    private void backToQuestion() {
        startActivity(intent);
    }
}
