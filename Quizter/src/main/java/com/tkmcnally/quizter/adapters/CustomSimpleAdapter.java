package com.tkmcnally.quizter.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tkmcnally.quizter.Constants;
import com.tkmcnally.quizter.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by missionary on 1/6/2014.
 */
public class CustomSimpleAdapter extends SimpleAdapter {

    private Typeface typeface;

    public CustomSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

        InputStream is = null;
        try {
            is = context.getAssets().open("fonts/segoe_ui_light.ttf");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("quizter", is.toString());

        typeface = Typeface.create("sans-serif-light", Typeface.NORMAL);
    }

    public View getView (int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(R.id.questionListTextItem);
        textView.setTypeface(typeface);
        textView.setTextColor(Color.BLACK);

        TextView textView2 = (TextView) view.findViewById(R.id.answerListTextItem);
        textView2.setTypeface(typeface);
        textView2.setTextColor(Color.BLACK);
        return view;
    }
}
