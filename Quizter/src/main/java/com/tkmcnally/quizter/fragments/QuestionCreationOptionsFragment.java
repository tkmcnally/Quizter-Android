package com.tkmcnally.quizter.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tkmcnally.quizter.R;

/**
 * Created by Thomas on 3/21/14.
 */
public class QuestionCreationOptionsFragment extends Fragment {

    private Button createNew, modifyExisting, createFromScratch, createFromExisting;
    private TranslateAnimation translateAnimation;

    private Boolean createToggled;

    private Point modifyExistingOriginalP;

    private int[] currentLocation;
    private Boolean firstClick;

    private LinearLayout optionContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_question_creation_option, container, false);

        createToggled = true;
        firstClick = true;
        currentLocation = new int[2];

        createFromExisting = (Button) rootView.findViewById(R.id.create_from_existing);
        createFromScratch = (Button) rootView.findViewById(R.id.create_your_own);

        createNew = (Button) rootView.findViewById(R.id.create_new_question);
        createNew.setOnClickListener(createNewOnClickListener(rootView));

        modifyExisting = (Button) rootView.findViewById(R.id.modify_existing_question);



        optionContainer = (LinearLayout) rootView.findViewById(R.id.question_option_container);

        return rootView;
    }


    public View.OnClickListener createNewOnClickListener(View rootView) {
        View.OnClickListener o = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(firstClick) {
                    updateCoordinates();
                    modifyExistingOriginalP = new Point(currentLocation[0], currentLocation[1]);
                    firstClick = false;
                }

                int newY = 0;
                if(createToggled) {
                    newY = (createNew.getHeight()*2);
                } else {
                    newY = -(createNew.getHeight()*2);
                }
                translateAnimation = new TranslateAnimation(0, 0, 0, newY);
                translateAnimation.setDuration(500);
                translateAnimation.setAnimationListener(modifyAnimationListener());
                optionContainer.startAnimation(translateAnimation);

            }
        };
        return o;
    }

    public Animation.AnimationListener modifyAnimationListener() {
        Animation.AnimationListener aL = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                updateCoordinates();
                if(createToggled) {

                    optionContainer.setY(modifyExistingOriginalP.y + (createNew.getHeight()*2));
                    createToggled = false;
                } else {

                    optionContainer.setY(modifyExistingOriginalP.y);
                    createToggled = true;
                }
                TranslateAnimation flicker = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                flicker.setDuration(1);
                optionContainer.startAnimation(flicker);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        };

        return aL;
    }


    public void updateCoordinates() {
        int[] buttonLocation = new int[2];
        int[] viewLocation = new int[2];
        int[] parentLocation = new int[2];

        optionContainer.getLocationInWindow(buttonLocation);
        optionContainer.getRootView().findViewById(android.R.id.content).getLocationInWindow(viewLocation);
        optionContainer.getRootView().getRootView().findViewById(android.R.id.content).getLocationInWindow(parentLocation);

        currentLocation[0] = (buttonLocation[0] - viewLocation[0] - parentLocation[0]);
        currentLocation[1] = (buttonLocation[1] - viewLocation[1] - parentLocation[1]);

        //Log.d("Quizter", " " + currentLocation[1] + " " + buttonLocation[1] + " " + viewLocation[1] + " " + parentLocation[1]);
    }

}
