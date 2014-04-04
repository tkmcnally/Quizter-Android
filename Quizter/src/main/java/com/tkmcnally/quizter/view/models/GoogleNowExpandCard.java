package com.tkmcnally.quizter.view.models;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkmcnally.quizter.R;

import it.gmariotti.cardslib.library.internal.CardExpand;

/**
 * This class provides a simple example of expand area
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class GoogleNowExpandCard extends CardExpand {

    String dateCreated;

    public GoogleNowExpandCard(Context context) {
        super(context, R.layout.profile_card_inner_expand);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view == null) return;

        //Retrieve TextView elements
        TextView tx1 = (TextView) view.findViewById(R.id.profile_card_expand_1);


        if (tx1 != null) {
            tx1.setText(dateCreated);
        }
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }


}