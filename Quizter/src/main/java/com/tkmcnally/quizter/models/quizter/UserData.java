package com.tkmcnally.quizter.models.quizter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by missionary on 1/3/2014.
 */
public class UserData implements Serializable {

    private String id;
    private String name;
    private String date_created;
    private String photo_url;
    private String setup_profile;
    private String score;

    private List<HashMap<String, String>> questions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getSetup_profile() {
        return setup_profile;
    }

    public void setSetup_profile(String setup_profile) {
        this.setup_profile = setup_profile;
    }

    public List<HashMap<String, String>>   getQuestions() {
        return questions;
    }

    public void setQuestions(List<HashMap<String, String>>  questions) {
        this.questions = questions;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String toString() {
        StringBuilder returnString = new StringBuilder();
        if(id != null) {
            returnString.append("id: " + id + " ");
        }

        if(name != null) {
            returnString.append("name: " + name + " ");
        }

        if(date_created != null) {
            returnString.append("date_created: " + date_created + " ");
        }

        if(questions != null) {
            returnString.append("questions: " + questions.toString() + " ");
        }
        return String.format(returnString.toString());
    }



}
