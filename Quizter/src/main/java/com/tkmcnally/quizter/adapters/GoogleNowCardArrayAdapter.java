package com.tkmcnally.quizter.adapters;

import android.content.Context;

import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;

/**
 * Created by Thomas on 1/20/14.
 */
public class GoogleNowCardArrayAdapter extends CardArrayAdapter {
    /**
     * Constructor
     *
     * @param context The current context.
     * @param cards   The cards to represent in the ListView.
     */
    public GoogleNowCardArrayAdapter(Context context, List<Card> cards) {
        super(context, cards);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }
}
