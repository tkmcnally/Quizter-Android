package com.tkmcnally.quizter.models.now;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.models.quizter.Answer;
import com.tkmcnally.quizter.models.quizter.Question;

import org.w3c.dom.Text;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * This class provides a simple card as Google Play
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class GoogleNowLabelCard extends Card {

    public boolean profileCard;

    protected String label, subLabel;

    public Bitmap bitmap;

    public GoogleNowLabelCard(Context context) {
        this(context, R.layout.cards_list_label);
    }

    public GoogleNowLabelCard(Context context, int innerLayout) {
        super(context, innerLayout);
        //init();
    }

    public void init() {

    }
    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView labelView = (TextView) view.findViewById(R.id.cards_list_label);
        labelView.setText(label);

        TextView subLabelView = (TextView) view.findViewById(R.id.cards_list_label_sub);
        subLabelView.setText(subLabel);
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSubLabel() {
        return subLabel;
    }

    public void setSubLabel(String subLabel) {
        this.subLabel = subLabel;
    }

    public void setIsProfileCard(boolean profileCard) {
        this.profileCard = profileCard;
    }

    public boolean isProfileCard() {
        return profileCard;
    }

}