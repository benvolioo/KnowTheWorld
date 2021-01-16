package com.benvolioo.knowtheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private Button answer1;
    private Button answer2;
    private Button answer3;

    private int questionCounter = 0;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;

    private List<Question> questionList;

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

        QuizDatabaseHelper databaseHelper = new QuizDatabaseHelper(this);
        questionList = databaseHelper.getAllQuestions();
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);

        // TODO: need to add setOnClickListener for the three buttons which then checkAnswer

        checkAnswer();
        showNextQuestion();

    }



    private void showNextQuestion() {
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter++);

            textViewQuestion.setText(currentQuestion.getQuestion());
            answer1.setText(currentQuestion.getAnswer1());
            answer2.setText(currentQuestion.getAnswer2());
            answer3.setText(currentQuestion.getAnswer3());

            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
        } else {
            finishQuiz();
        }
    }

    private void checkAnswer() {
        // Next step is to have this check answer
    }

    private void finishQuiz() {
        finish();
    }

}