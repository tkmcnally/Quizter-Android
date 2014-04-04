package com.tkmcnally.quizter.view.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.view.quizter.models.Answer;
import com.tkmcnally.quizter.view.quizter.models.Question;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * This class provides a simple card as Google Play
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class GoogleNowCard extends Card {

    public boolean profileCard;
    public Bitmap bitmap;
    public Question question;
    public Answer answer;
    public String index;
    public int resourceIdThumbnail;
    protected TextView mQuestion;
    protected TextView mAnswer;
    protected TextView mIndex;
    protected int count;


    public GoogleNowCard(Context context) {
        this(context, R.layout.cards_list_items);
    }

    public GoogleNowCard(Context context, int innerLayout) {
        super(context, innerLayout);
        index = new String();
        //init();
    }

    public void init() {

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {


        //Retrieve elements
        mQuestion = (TextView) parent.findViewById(R.id.carddemo_myapps_main_inner_title);
        mAnswer = (TextView) parent.findViewById(R.id.carddemo_myapps_main_inner_secondaryTitle);
        mIndex = (TextView) parent.findViewById(R.id.question_card_index);

        if (mQuestion != null) {
            // Log.d("quizter", "color: " + ("#" + Integer.toHexString(getContext().getResources().getColor(R.color.quizter_theme_color))).replace("ff", ""));
            String html = "<b><font color='" + ("#" + Integer.toHexString(getContext().getResources().getColor(R.color.quizter_theme_color))).replace("ff", "") + "'>" + question.prefix + "</font></b>" + question.getQuestion();
            mQuestion.setText(Html.fromHtml(html));
        }

        if (mAnswer != null) {
            String html = "<b><font color='" + ("#" + Integer.toHexString(getContext().getResources().getColor(R.color.quizter_theme_color))).replace("ff", "") + "'>" + answer.prefix + "</font></b>" + answer.getAnswer();
            mAnswer.setText(Html.fromHtml(html));
        }

        if (mIndex != null) {
            mIndex.setText(index);
        }
    }


    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getResourceIdThumbnail() {
        return resourceIdThumbnail;
    }

    public void setResourceIdThumbnail(int resourceIdThumbnail) {
        this.resourceIdThumbnail = resourceIdThumbnail;
    }

    public void setIsProfileCard(boolean profileCard) {
        this.profileCard = profileCard;
    }

    public boolean isProfileCard() {
        return profileCard;
    }

}