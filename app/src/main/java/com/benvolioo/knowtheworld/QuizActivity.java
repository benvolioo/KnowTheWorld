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


// TODO: Think about splitting this class up.
public class QuizActivity extends AppCompatActivity {
    // Constants for Result and Shared Preferences.
    public static final Integer REQUEST_CODE_ANSWER = 1;
    public static final String SHARED_PREFERENCES = "sharedPrefs";
    public static final String EXTRA_SCORE = "extraScore";

    // Constants for orientation changes.
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    // UI element declarations.
    private TextView tvQuestion;
    private TextView tvScore;
    private TextView tvQuestionCount;
    private Button btnAnswer1;
    private Button btnAnswer2;
    private Button btnAnswer3;


    public Integer questionCount = 0;
    public static Integer questionCountTotal;
    public Question currentQuestion;
    private long backPressedTime;
    protected Integer score = 0;
    private ArrayList<Question> questionList;

    /**
     * Called to setup the <code>QuizActivity</code> on app startup.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.txtViewQuestion);
        tvScore = findViewById(R.id.txtViewScore);
        tvQuestionCount = findViewById(R.id.txtViewQuestionCount);

        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);

        if (firstStart(savedInstanceState)) {
            questionSetUp();
            showNextQuestion();
        } else {
            questionRetrieve(savedInstanceState);
        }


        View.OnClickListener onclickAnswer = (View view) -> showAnswer(view);

        // Look to see if these answer#.method() calls can be standardized somehow... perhaps by passing a method to some function which
        // then calls it on all three
        btnAnswer1.setOnClickListener(onclickAnswer);
        btnAnswer2.setOnClickListener(onclickAnswer);
        btnAnswer3.setOnClickListener(onclickAnswer);
    }

    /**
     * When first starting the quiz, savedInstanceState is null
     * @param savedInstanceState
     * @return <code>true</code> if it is the first time starting the quiz.
     *         <code>false</code> if it is not the first time starting the quiz.
     */
    private <T extends Bundle> boolean firstStart(T savedInstanceState) {
        return savedInstanceState == null;
    }

    /**
     * Gets all questions from database, gets a count total, shuffles them.
     */
    private void questionSetUp() {
        QuizDatabaseHelper databaseHelper = new QuizDatabaseHelper(this);
        questionList = databaseHelper.getAllQuestions();
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);
    }

    /**
     * Retrieves question information after configuration changes.
     * @param savedInstanceState
     */
    // TODO: Come up with a better name for questionRetrieve method.
    private void questionRetrieve(Bundle savedInstanceState) {
        questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
        questionCountTotal = questionList.size();
        questionCount = savedInstanceState.getInt(KEY_QUESTION_COUNT);
        currentQuestion = questionList.get(questionCount - 1);
        score = savedInstanceState.getInt(KEY_SCORE);
    }

    /**
     * Displays text for next question.
     */
    private void showNextQuestion() {
        if (questionCount < questionCountTotal) {
            currentQuestion = questionList.get(questionCount++);

            tvQuestion.setText(currentQuestion.getQuestion());
            btnAnswer1.setText(currentQuestion.getAnswer1());
            btnAnswer2.setText(currentQuestion.getAnswer2());
            btnAnswer3.setText(currentQuestion.getAnswer3());

            setTvQuestionCount(questionCount);
        } else {
            finishQuiz();
        }
    }

    /**
     * Starts <code>AnswerActivity</code> for <code>currentQuestion</code>.
     * @param view the instance of the <code>QuizActivity</code> which is starting it's corresponding
     *             <code>AnswerActivity</code>.
     */
    private void showAnswer(View view) {
        Integer userAnswer = getUserAnswer(view);
        Intent intent = new Intent(QuizActivity.this, AnswerActivity.class);

        Boolean answerResult = checkUserAnswer(userAnswer);

        intent.putExtra("ANSWER_RESULT", answerResult);
        intent.putExtra("QUESTION_COUNT", questionCount);
        intent.putExtra("CURRENT_SCORE", score);
        startActivityForResult(intent, REQUEST_CODE_ANSWER);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    // TODO: Need to check request code is also in MainActivity, might need to extract method from these classes.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ANSWER) {
            if (resultCode == RESULT_OK) {
                score = data.getIntExtra(AnswerActivity.CURRENT_SCORE, 0);
                questionCount = data.getIntExtra(AnswerActivity.QUESTION_COUNT, 0);

                setTvScore(score);
                setTvQuestionCount(questionCount);
                showNextQuestion();
            }
        }
    }

    /**
     * Update <code>tvScore</code> after returning from <code>AnswerActivity</code>.
     * @param score
     */
    private void setTvScore(Integer score) {
        tvScore.setText("Score: " + score);
    }

    /**
     * Update <code>tvQuestionCount</code> after returning from <code>AnswerActivity</code>.
     * @param questionCount
     */
    private void setTvQuestionCount(Integer questionCount) {
        tvQuestionCount.setText("Question: " + questionCount + "/" + questionCountTotal);
    }

    /**
     * Gets the <code>userAnswer</code>, which corresponds to the <code>btnAnswer#</code> clicked.
     */
    private Integer getUserAnswer(View view) {
        switch (view.getId()) {
            case R.id.btnAnswer1: return 1;
            case R.id.btnAnswer2: return 2;
            case R.id.btnAnswer3: return 3;
        }
        return 0;
    }

    /**
     * Check that the <code>userAnswer</code> is the same as the <code>correctAnswerNumber</code>.
     * @param userAnswer
     * @return
     */
    private Boolean checkUserAnswer(Integer userAnswer) {
        // Next step is to have this check answer
        Integer correctAnswer = currentQuestion.getCorrectAnswer();
        if (correctAnswer == userAnswer) {
            setTvScore(score++);
            return true;
        }
        return false;
    }

    /**
     * Returns result to <code>MainActivity</code> when there are no more questions.
     */
    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * On back pressed, checks if back was pressed twice within two seconds. If so, returns to home page.
     * Else, nothing happens.
     */
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    /**
     * Preserves state before configuration changes.
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCount);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}