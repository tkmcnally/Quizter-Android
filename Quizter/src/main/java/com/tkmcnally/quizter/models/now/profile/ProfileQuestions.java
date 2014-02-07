package com.tkmcnally.quizter.models.now.profile;


import com.tkmcnally.quizter.models.quizter.Answer;
import com.tkmcnally.quizter.models.quizter.Question;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Thomas on 1/10/14.
 */
public class ProfileQuestions implements Serializable {


    private List<HashMap<Question, Answer>> questions;

    public ProfileQuestions() {

    }

    public List<HashMap<Question, Answer>> getQuestions() {
        return questions;
    }

    public void setQuestions(List<HashMap<Question, Answer>> questions) {
        this.questions = questions;
    }


}
