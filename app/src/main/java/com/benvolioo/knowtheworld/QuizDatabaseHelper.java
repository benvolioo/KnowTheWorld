package com.benvolioo.knowtheworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.benvolioo.knowtheworld.QuizContract.*;

import java.util.ArrayList;

public class QuizDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "KnowTheWorld2.database";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME +
                " (" +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER1 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER2 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NUMBER + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("10 is correct", "10", "20", "Neither", 1);
        addQuestion(q1);

        Question q2 = new Question("30 is correct", "30", "20", "Neither", 1);
        addQuestion(q2);

        Question q3 = new Question("20 is correct", "10", "20", "Neither", 2);
        addQuestion(q3);
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_ANSWER1, question.getAnswer1());
        cv.put(QuestionsTable.COLUMN_ANSWER2, question.getAnswer2());
        cv.put(QuestionsTable.COLUMN_ANSWER3, question.getAnswer3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NUMBER, question.getCorrectAnswer());

        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setAnswer1(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_ANSWER1)));
                question.setAnswer2(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_ANSWER2)));
                question.setAnswer3(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_ANSWER3)));
                question.setCorrectAnswer(cursor.getInt(cursor.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NUMBER)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return questionList;
    }


}
