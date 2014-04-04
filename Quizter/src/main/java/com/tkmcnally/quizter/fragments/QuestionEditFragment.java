package com.tkmcnally.quizter.fragments;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tkmcnally.quizter.R;

/**
 * Created by missionary on 1/6/2014.
 */
public class QuestionEditFragment extends Fragment {

    private Button submitButton, discardButton;
    private TextView questionField;
    private EditText answerField;

    private Typeface typeface;


    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bundle = getArguments();

        View view = inflater.inflate(R.layout.fragment_question_edit, container, false);

        ActionBar ab = getActivity().getActionBar(); //needs  import android.app.ActionBar;
        ab.setTitle("Quizter");
        ab.setSubtitle("Edit Question");
        ab.setDisplayHomeAsUpEnabled(true);

        typeface = Typeface.create("sans-serif-light", Typeface.NORMAL);

        questionField = (TextView) view.findViewById(R.id.questionValue);
        questionField.setTypeface(typeface);
        questionField.setText(bundle.getString("question"));
        answerField = (EditText) view.findViewById(R.id.answerEditField);

        discardButton = (Button) view.findViewById(R.id.questionEditDiscard);
        discardButton.setTypeface(typeface);
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = bundle;

                Fragment fragment = new DefaultQuestionsFragment();
                fragment.setArguments(args);

                getFragmentManager().popBackStack();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            }
        });

        submitButton = (Button) view.findViewById(R.id.questionEditSubmit);
        submitButton.setTypeface(typeface);
        submitButton.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    public void setQuestion(String question) {
        TextView questionValue = (TextView) getView().findViewById(R.id.questionValue);
        questionValue.setText(question);
    }
}