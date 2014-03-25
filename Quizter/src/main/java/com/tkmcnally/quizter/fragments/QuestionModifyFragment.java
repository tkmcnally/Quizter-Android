package com.tkmcnally.quizter.fragments;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tkmcnally.quizter.R;

/**
 * Created by missionary on 1/31/2014.
 */
public class QuestionModifyFragment extends Fragment {

    private Button submitButton, discardButton, chooseNewQuestionButton;
    private TextView questionField;
    private EditText answerField;

   // private Typeface typeface;


    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bundle = getArguments();

        View view = inflater.inflate(R.layout.fragment_question_modify, container, false);

        ActionBar ab = getActivity().getActionBar(); //needs  import android.app.ActionBar;
        ab.setTitle("Quizter");
        ab.setSubtitle("Edit Question");
        ab.setDisplayHomeAsUpEnabled(true);

        questionField = (TextView) view.findViewById(R.id.questionEditField);

        if(bundle.getString("question") != null) {
            questionField.setText(bundle.getString("question"));
        } else {
            questionField.setText(bundle.getString("originalQuestion"));
        }

        answerField = (EditText) view.findViewById(R.id.answerEditField);
        answerField.setText(bundle.getString("originalAnswer"));

        discardButton = (Button) view.findViewById(R.id.questionEditDiscard);
        discardButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = bundle;
                args.putString("question", (String) bundle.get("originalQuestion"));
                args.putString("answer", (String) bundle.get("originalAnswer"));

                Fragment fragment = new ProfileFragment();
                fragment.setArguments(args);

                getFragmentManager().popBackStack();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            }
        });

        submitButton = (Button) view.findViewById(R.id.questionEditSubmit);
        submitButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = bundle;
                args.putString("question", questionField.getText().toString());
                args.putString("answer", answerField.getText().toString());

                Fragment fragment = new ProfileFragment();
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "ProfileFragment").commit();
            }
        });

        chooseNewQuestionButton = (Button) view.findViewById(R.id.chooseNewQuestion);
        chooseNewQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = bundle;

                Fragment fragment = new QuestionSelectionFragment();
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "QuestionSelectionFragment").commit();
            }
        });


        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Log.d("Quizter", "BACK IS CLICKED");
                    discardButton.performClick();
                }
                return false;
            }
        });

        return view;
    }

    public void setQuestion(String question) {
        TextView questionValue = (TextView) getView().findViewById(R.id.questionValue);
        questionValue.setText(question);
    }
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }


}