package com.tkmcnally.quizter.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.tkmcnally.quizter.Constants;
import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.Util;
import com.tkmcnally.quizter.activities.NavDrawerActivity;
import com.tkmcnally.quizter.http.WebService;
import com.tkmcnally.quizter.http.WebServiceCaller;
import com.tkmcnally.quizter.models.quizter.UserData;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by Thomas on 2/6/14.
 */
public class QuizMeFragment extends Fragment implements WebServiceCaller {

    private Bundle bundle;
    private TextView questions[];
    private WebService ws;
    private QuizMeFragment fragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_quiz_me, container, false);

        fragment = this;
        bundle = getArguments();

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle("Quizter");
        ab.setSubtitle("Quiz In Progress");
        ab.setDisplayHomeAsUpEnabled(true);
        questions = new TextView[5];
        questions[0] = (TextView) rootView.findViewById(R.id.quiz_me_question_one);
        questions[1] = (TextView) rootView.findViewById(R.id.quiz_me_question_two);
        questions[2] = (TextView) rootView.findViewById(R.id.quiz_me_question_three);
        questions[3] = (TextView) rootView.findViewById(R.id.quiz_me_question_four);
        questions[4] = (TextView) rootView.findViewById(R.id.quiz_me_question_five);

        HashMap<String, String> jsonFields = new HashMap<String, String>();
        jsonFields.put(Constants.STRING_ACCESS_TOKEN, Session.getActiveSession().getAccessToken());
        jsonFields.put(Constants.STRING_URL_PATH, Constants.FETCH_PLAYER_QUESTIONS_API_PATH);
        jsonFields.put(Constants.STRING_PLAYER_ID, "512421166");

        ws = new WebService(getActivity(), "Fetching questions...");
        ws.execute(jsonFields, fragment);

        return rootView;
    }

    @Override
    public void onPostWebServiceCall(String message) {

        if(message != null) {
            Type type = new TypeToken<UserData>(){}.getType();
            JsonParser parser = new JsonParser();
            JsonObject o = (JsonObject) parser.parse(message);
            JsonArray json_questions = (JsonArray) o.get("questions");
            for(int i = 0; i < json_questions.size(); i++) {
                JsonObject map = (JsonObject) json_questions.get(i);
                questions[i].setText(map.get("question").getAsString());
            }

            Log.d("Quizter", "QUESTIONS LOADED QUIZ ME");
        }
    }
}
