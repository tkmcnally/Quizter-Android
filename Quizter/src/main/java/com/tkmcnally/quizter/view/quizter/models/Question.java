package com.tkmcnally.quizter.view.quizter.models;

import java.io.Serializable;

/**
 * Created by missionary on 1/3/2014.
 */
public class Question implements Serializable {

    public static String prefix = "Q: ";
    private String question;

    public Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return prefix + question;
    }
}
