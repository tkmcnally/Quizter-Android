package com.tkmcnally.quizter.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkmcnally.quizter.R;

import java.util.ArrayList;

public class ImageListAdapter extends ArrayAdapter
{
    Activity context;
    ArrayList<String> items;
    boolean[] arrows;
    int layoutId;
    int textId;
    int imageId;

    public ImageListAdapter(Activity context, int layoutId, int textId, int imageId, ArrayList<String> items) {
        super(context, layoutId, items);

        this.context = context;
        this.items = items;
        this.layoutId = layoutId;
        this.textId = textId;
        this.imageId = imageId;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {


        LayoutInflater inflater=context.getLayoutInflater();
        View row=inflater.inflate(layoutId, null);
        TextView label=(TextView)row.findViewById(textId);
        label.setText(items.get(pos));

        ImageView icon=(ImageView)row.findViewById(imageId);
        icon.setImageResource(R.drawable.arrow3);


        return(row);
    }
}