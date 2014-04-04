package com.tkmcnally.quizter.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.tkmcnally.quizter.activities.NavDrawerActivity;

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
        storeBundle();
        bundle.putString("question", (String) bundle.get("originalQuestion"));
        bundle.putString("answer", (String) bundle.get("originalAnswer"));

        View view = inflater.inflate(R.layout.fragment_question_modify, container, false);

        ActionBar ab = getActivity().getActionBar(); //needs  import android.app.ActionBar;
        ab.setTitle("Quizter");
        ab.setSubtitle("Edit Question");
        ab.setDisplayHomeAsUpEnabled(true);

        questionField = (TextView) view.findViewById(R.id.questionEditField);



        answerField = (EditText) view.findViewById(R.id.answerEditField);
        answerField.setText(bundle.getString("originalAnswer"));

        discardButton = (Button) view.findViewById(R.id.questionEditDiscard);
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate("profileBackStack", FragmentManager.POP_BACK_STACK_INCLUSIVE);


            }
        });

        submitButton = (Button) view.findViewById(R.id.questionEditSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("question", questionField.getText().toString());
                bundle.putString("answer", answerField.getText().toString());
                storeBundle();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate("profileBackStack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        chooseNewQuestionButton = (Button) view.findViewById(R.id.chooseNewQuestion);
        chooseNewQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = bundle;

                Fragment fragment = new DefaultQuestionsFragment();
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "DefaultQuestionsFragment").addToBackStack(null).commit();
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

    public void storeBundle() {
        ((NavDrawerActivity)getActivity()).storeProfileBundle(bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(bundle.getString("new_question") != null) {
            Log.d("Quizter", "new_question: " + bundle.getString("new_question"));
            questionField.setText(bundle.getString("new_question"));
            questionField.refreshDrawableState();
        } else if (bundle.getString("question") != null) {
            questionField.setText(bundle.getString("question"));
        } else {
            questionField.setText(bundle.getString("originalQuestion"));
        }
    }
}

