package com.tkmcnally.quizter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.adapters.GoogleNowCardArrayAdapter;
import com.tkmcnally.quizter.http.WebServiceCaller;
import com.tkmcnally.quizter.models.now.GoogleNowCard;
import com.tkmcnally.quizter.models.quizter.Answer;
import com.tkmcnally.quizter.models.quizter.Question;
import com.tkmcnally.quizter.models.quizter.UserData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by Thomas on 3/19/14.
 */
public class QuizOverviewFragment  extends Fragment implements WebServiceCaller {

    private ArrayList<Card> cards;
    private GoogleNowCardArrayAdapter mCardArrayAdapter;
    private CardListView questionListView;

    private TextView nameView, scoreView;

    private List<HashMap<String, String>> questionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_quiz_overview, container, false);

        questionListView = (CardListView) rootView.findViewById(R.id.quiz_questionListView);
        nameView = (TextView) rootView.findViewById(R.id.quiz_overview_name);
        scoreView = (TextView) rootView.findViewById(R.id.quiz_overview_score);
        setupQuestionList();




        return rootView;
    }
    @Override
    public void onPostWebServiceCall(String message) {

    }

    public void setupQuestionList() {

        Bundle bundle = getArguments();
        if(bundle != null) {
            questionList = new ArrayList<HashMap<String, String>>();
            cards = new ArrayList<Card>();

            JsonParser parser = new JsonParser();
            JsonObject o = (JsonObject) parser.parse(bundle.get("data").toString());
            JsonArray json_questions = (JsonArray) o.get("marked_questions");
            for(int i = 0; i < json_questions.size(); i++) {
                JsonObject map = (JsonObject) json_questions.get(i);

                GoogleNowCard card = new GoogleNowCard(this.getActivity());
                Question q = new Question(map.get("question").getAsString());
                Answer a = new Answer(map.get("given_answer").getAsString());
                card.setQuestion(q);
                card.setAnswer(a);
                card.setSwipeable(false);
                card.setIndex((cards.size() + 1) + "");
                card.setType(2);

                if("true".equals(map.get("correct_answer").getAsString())) {
                    card.setBackgroundResourceId(R.color.quiztewr_quiz_correct);
                } else {
                    card.setBackgroundResourceId(R.color.quizter_quiz_wrong);
                }
                card.init();
                cards.add(card);
            }

            UserData user = (UserData) getArguments().get("user_data");

            nameView.setText(user.getName());
            scoreView.setText(o.get("score").getAsString());
        }





        mCardArrayAdapter = new GoogleNowCardArrayAdapter(getActivity(),cards);
        mCardArrayAdapter.setInnerViewTypeCount(3);

        if (questionListView != null){
            questionListView.setAdapter(mCardArrayAdapter);
        }
    }
}
