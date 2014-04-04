package com.tkmcnally.quizter.view.quizter.models;

import java.io.Serializable;

/**
 * Created by missionary on 1/5/2014.
 */
public class Answer implements Serializable {

    public static String prefix = "A: ";
    private String answer;

    public Answer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return prefix + answer;
    }
}
