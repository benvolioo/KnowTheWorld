package com.benvolioo.knowtheworld;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    // Constants For Result and Shared Preferences
    public static final Integer REQUEST_CODE_ANSWER = 1;
    public static final String SHARED_PREFERENCES = "sharedPrefs";
    public static final String EXTRA_SCORE = "extraScore";

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";


    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private Button answer1;
    private Button answer2;
    private Button answer3;

    public int questionCount = 0;
    public static int questionCountTotal;
    public Question currentQuestion;
    public Integer userAnswerNumber;

    private long backPressedTime;

    protected int score = 0;

    private ArrayList<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.txtViewQuestion);
        textViewScore = findViewById(R.id.txtViewScore);
        textViewQuestionCount = findViewById(R.id.txtViewQuestionCount);

        answer1 = findViewById(R.id.btnAnswer1);
        answer2 = findViewById(R.id.btnAnswer2);
        answer3 = findViewById(R.id.btnAnswer3);

        // When first starting the quiz, saved instance state is null.
        if (savedInstanceState == null) {
            QuizDatabaseHelper databaseHelper = new QuizDatabaseHelper(this);
            questionList = databaseHelper.getAllQuestions();
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCount = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCount - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
        }


        View.OnClickListener onclickAnswer = (View view) -> showAnswer(view);

        // Look to see if these answer#.method() calls can be standardized somehow... perhaps by passing a method to some function which
        // then calls it on all three
        answer1.setOnClickListener(onclickAnswer);
        answer2.setOnClickListener(onclickAnswer);
        answer3.setOnClickListener(onclickAnswer);
    }

    private void showNextQuestion() {
        if (questionCount < questionCountTotal) {
            currentQuestion = questionList.get(questionCount++);

            textViewQuestion.setText(currentQuestion.getQuestion());
            answer1.setText(currentQuestion.getAnswer1());
            answer2.setText(currentQuestion.getAnswer2());
            answer3.setText(currentQuestion.getAnswer3());

            updateQuestionCount(questionCount);
        } else {
            finishQuiz();
        }
    }

    private void showAnswer(View view) {
        getBtnNumber(view);
        Intent intent = new Intent(QuizActivity.this, AnswerActivity.class);

        Boolean answerResult = checkUserAnswer(userAnswerNumber);

        intent.putExtra("ANSWER_RESULT", answerResult);
        intent.putExtra("QUESTION_COUNT", questionCount);
        intent.putExtra("CURRENT_SCORE", score);
        startActivityForResult(intent, REQUEST_CODE_ANSWER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ANSWER) {
            if (resultCode == RESULT_OK) {
                score = data.getIntExtra(AnswerActivity.CURRENT_SCORE, 0);
                questionCount = data.getIntExtra(AnswerActivity.QUESTION_COUNT, 0);

                updateScore(score);
                updateQuestionCount(questionCount);
                showNextQuestion();
            }
        }
    }

    private void updateScore(Integer score) {
        textViewScore.setText("Score: " + score);
    }

    private void updateQuestionCount(Integer questionCount) {
        textViewQuestionCount.setText("Question: " + questionCount + "/" + questionCountTotal);
    }

    /**
     * Return the answer number corresponding to which button was clicked.
     *
     * View view is the answer view for that button
     * Returns the answer number corresponding to which button was clicked.
     */
    // Refactor
    private void getBtnNumber(View view) {
        Integer idNumber = view.getId();
        switch (idNumber) {
            case R.id.btnAnswer1:
                userAnswerNumber = 1;
                break;
            case R.id.btnAnswer2:
                userAnswerNumber = 2;
                break;
            case R.id.btnAnswer3:
                userAnswerNumber = 3;
                break;
        }
    }

    private Boolean checkUserAnswer(Integer userAnswerNumber) {
        // Next step is to have this check answer
        Integer correctAnswerNumber = currentQuestion.getAnswerNumber();
        if (correctAnswerNumber == userAnswerNumber) {
            updateScore(score++);
            return true;
        }
        return false;
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCount);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}