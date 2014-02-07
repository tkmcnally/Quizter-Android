package com.tkmcnally.quizter.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tkmcnally.quizter.Constants;

import java.util.List;

/**
 * Created by missionary on 1/6/2014.
 */
public class CustomArrayAdapter extends ArrayAdapter {

    private Typeface typeface;

    public CustomArrayAdapter(Context context, int resource, int textViewId, List objects) {
        super(context, resource, textViewId, objects);

      //  typeface = (Typeface) Typeface.createFromAsset(context.getAssets(), Constants.SEGOE_FONT_PATH);
          typeface = Typeface.create("sans-serif-light", Typeface.NORMAL);


    }

    public View getView (int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setTypeface(typeface);

        return view;
    }

}
