package com.benvolioo.knowtheworld;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // Constants for Result and SharedPreferences.
    private static final Integer REQUEST_CODE_QUIZ = 1;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";

    // UI element declarations.
    private TextView tvHighscore;
    private Button btnStartQuiz;
    private Integer highscore;

    /**
     * Called to setup the <code>MainActivity</code> on app startup.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Shows highscore of previous games.
        tvHighscore = findViewById(R.id.txtHighscore);
        loadHighscore();

        btnStartQuiz = findViewById(R.id.btnMain);
        btnStartQuiz.setOnClickListener((View view) -> startQuiz());
    }

    /**
     * Called when user clicks <code>btnStartQuiz</code>. Starts <code>QuizActivity</code> for result.
     */
    private void startQuiz() {
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    /**
     * When <code>QuizActivity</code> passes results back to <code>MainActivity</code>, calls <code>updateHighscore(score)</code>.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (checkRequestCode(requestCode) && checkResultCode(resultCode)) {
            Integer score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
            updateHighscore(score);
        }
    }

    /**
     * Checks whether requestCode from activity result is as expected.
     * @param requestCode
     * @return
     */
    private boolean checkRequestCode(Integer requestCode) {
        return requestCode == REQUEST_CODE_QUIZ;
    }

    /**
     * Checks whether resultCode from activity result is as expected.
     * @param resultCode
     * @return
     */
    private boolean checkResultCode(Integer resultCode) {
        return resultCode == RESULT_OK;
    }

    /**
     * Loads highscore from SharedPreferences then print it to screen.
     */
    private void loadHighscore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        setTvHighscore(highscore);
    }

    /**
     * Updates highscore if current score is greater than the previous highscore.
     * @param score
     */
    private void updateHighscore(Integer score) {
        if (score > highscore) {
            highscore = score;
            setTvHighscore(highscore);

            SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_HIGHSCORE, highscore);
            editor.apply();
        }
    }

    /**
     * Sets the highscore text view.
     * @param highscore
     */
    private void setTvHighscore(Integer highscore) {
        tvHighscore.setText("Highscore: " + highscore);
    }

}