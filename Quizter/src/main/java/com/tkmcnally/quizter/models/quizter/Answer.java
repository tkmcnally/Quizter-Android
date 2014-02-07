package com.tkmcnally.quizter.models.quizter;

import java.io.Serializable;

/**
 * Created by missionary on 1/5/2014.
 */
public class Answer implements Serializable {

    public static String prefix = "A: ";

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private String answer;

    public Answer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return prefix + answer;
    }
}
