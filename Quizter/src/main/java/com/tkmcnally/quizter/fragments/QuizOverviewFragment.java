package com.tkmcnally.quizter.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.activities.NavDrawerActivity;
import com.tkmcnally.quizter.adapters.GoogleNowCardArrayAdapter;
import com.tkmcnally.quizter.http.WebServiceCaller;
import com.tkmcnally.quizter.models.now.GoogleNowCard;
import com.tkmcnally.quizter.models.now.GoogleNowLabelCard;
import com.tkmcnally.quizter.models.now.GoogleNowOverviewCard;
import com.tkmcnally.quizter.models.now.GoogleNowOverviewListCard;
import com.tkmcnally.quizter.models.quizter.Answer;
import com.tkmcnally.quizter.models.quizter.Question;
import com.tkmcnally.quizter.models.quizter.UserData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by Thomas on 3/19/14.
 */
public class QuizOverviewFragment  extends Fragment {

    private ArrayList<Card> cards;
    private GoogleNowCardArrayAdapter mCardArrayAdapter;
    private CardListView questionListView;

    private List<HashMap<String, String>> questionList;

    private Button tryAgainButton, finishButton;

    private UserData userData;

    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_quiz_overview, container, false);

        bundle = getArguments();

        userData = (UserData) bundle.get("user_data");

        questionListView = (CardListView) rootView.findViewById(R.id.quiz_questionListView);
        setupQuestionList();


        tryAgainButton = (Button) rootView.findViewById(R.id.try_again);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Integer, String> map = new HashMap<Integer, String>();
                Arrays.sort(map.keySet().toArray());
                Fragment fragment = new QuizMeFragment();

                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "QuizMeFragment").commit();
            }
        });
        finishButton = (Button) rootView.findViewById(R.id.finish_review);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ProfileFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "ProfileFragment").commit();

            }
        });




        return rootView;
    }

    public void setupQuestionList() {

        Bundle bundle = getArguments();
        if(bundle != null) {
            questionList = new ArrayList<HashMap<String, String>>();
            cards = new ArrayList<Card>();

            JsonParser parser = new JsonParser();
            JsonObject o = (JsonObject) parser.parse(bundle.get("data").toString());

            UserData user = (UserData) getArguments().get("user_data");

            GoogleNowLabelCard labelCard = new GoogleNowLabelCard(this.getActivity());
            labelCard.setLabel("Review");
            labelCard.setSubLabel("Score is only earned once per unique question");
            cards.add(labelCard);

            GoogleNowOverviewCard overviewCard = new GoogleNowOverviewCard(this.getActivity());
            overviewCard.setResourceIDImage(R.id.card_overview_photo);
            overviewCard.setPhotoUrl(o.get("photo_url").getAsString());
            overviewCard.setNameString(user.getName());
            overviewCard.setScoreString(o.get("score").getAsString());

            cards.add(overviewCard);

            int score = 0;
            JsonArray json_questions = (JsonArray) o.get("marked_questions");
            for(int i = 0; i < json_questions.size(); i++) {
                JsonObject map = (JsonObject) json_questions.get(i);

                GoogleNowOverviewListCard card = new GoogleNowOverviewListCard(this.getActivity());
                Question q = new Question(map.get("question").getAsString());
                Answer a = new Answer(map.get("given_answer").getAsString());
                card.setQuestion(q);
                card.setAnswer(a);
                card.setSwipeable(false);
                card.setIndex((cards.size() - 1) + "");
                card.setType(2);

                if("true".equals(map.get("correct_answer").getAsString())) {
                    score++;
                    card.setMarkIconID(R.drawable.check_mark);
                    if("true".equals((map.get("already_answered").getAsString()))) {
                        card.setAlreadyAnswered(true);
                    }
                } else {
                    card.setMarkIconID(R.drawable.incorrect);

                }


                card.init();
                cards.add(card);
            }
            overviewCard.setMark(score + "/5");


        }



        mCardArrayAdapter = new GoogleNowCardArrayAdapter(getActivity(),cards);
        mCardArrayAdapter.setInnerViewTypeCount(3);

        if (questionListView != null){
            questionListView.setAdapter(mCardArrayAdapter);
        }
    }



}
