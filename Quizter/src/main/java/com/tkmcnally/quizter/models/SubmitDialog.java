package com.tkmcnally.quizter.models;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tkmcnally.quizter.R;


public class SubmitDialog extends AlertDialog {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    private View.OnClickListener noListener;
    private View.OnClickListener yesListener;

    public SubmitDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quizter_submit_dialog);

        LayoutInflater inflater = c.getLayoutInflater();


        Log.d("Quizter", "LISTENERS SET");


    }

    public void setNoListener(View.OnClickListener listener) {
        this.noListener = listener;
    }

    public void setYesListener(View.OnClickListener listener) {
        this.yesListener = listener;
    }
}