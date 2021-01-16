package com.benvolioo.knowtheworld;

import androidx.appcompat.app.AppCompatActivity;
import com.benvolioo.knowtheworld.QuizActivity.*;
import android.os.Bundle;

public class Answer extends AppCompatActivity {
    private int pbMax = QuizActivity.questionCountTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
    }
}