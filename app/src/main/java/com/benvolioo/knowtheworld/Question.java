package com.benvolioo.knowtheworld;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    public static final String TOPIC_ECONOMICS = "Economics";
    public static final String TOPIC_SOCIETY = "Society";
    public static final String TOPIC_NECESSITIES = "Necessities";
    public static final String TOPIC_GOVERNMENT = "Government";
    public static final String TOPIC_ENERGY = "Energy";
    public static final String TOPIC_HEALTH = "Health";

    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private Integer correctAnswer;
    private String answerInfo;
    private String topic;

    public Question(String question, String answer1, String answer2,
                    String answer3, Integer correctAnswer, String answerInfo, String topic) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.correctAnswer = correctAnswer;
        this.answerInfo = answerInfo;
        this.topic = topic;
    }

    public Question() { }

// Question(Parcel in) and writeToParcel() need to have the same order of r/w
    protected Question(Parcel in) {
        question = in.readString();
        answer1 = in.readString();
        answer2 = in.readString();
        answer3 = in.readString();
        if (in.readByte() == 0) {
            correctAnswer = null;
        } else {
            correctAnswer = in.readInt();
        }
        answerInfo = in.readString();
        topic = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(answer1);
        dest.writeString(answer2);
        dest.writeString(answer3);
        if (correctAnswer == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(correctAnswer);
        }
        dest.writeString(answerInfo);
        dest.writeString(topic);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getAnswerInfo() { return answerInfo; }

    public void setAnswerInfo(String answerInfo) { this.answerInfo = answerInfo; }

    public String getTopic() { return topic; }

    public void setTopic(String topic) { this.topic = topic; }

    public static String[] getAllTopics() {
        return new String[] { TOPIC_ECONOMICS, TOPIC_ENERGY, TOPIC_GOVERNMENT, TOPIC_HEALTH,
                TOPIC_NECESSITIES, TOPIC_SOCIETY};
    }
}
