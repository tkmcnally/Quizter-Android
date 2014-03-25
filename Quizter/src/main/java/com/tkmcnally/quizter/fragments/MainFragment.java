package com.tkmcnally.quizter.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.activities.NavDrawerActivity;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by missionary on 1/2/2014.
 */
public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private UiLifecycleHelper uiHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }



        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main, container, false);

        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);

        Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/segoeui.ttf");

        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(1000);
        animation1.setStartOffset(200);

        AlphaAnimation animation2 = new AlphaAnimation(0.0f, 1.0f);
        animation2.setDuration(1000);
        animation2.setStartOffset(500);

        AlphaAnimation animation3 = new AlphaAnimation(0.0f, 1.0f);
        animation3.setDuration(1000);
        animation3.setStartOffset(1000);

        ImageView imageView = (ImageView) view.findViewById(R.id.welcome_logo);
        imageView.startAnimation(animation1);

        TextView textView2 = (TextView) view.findViewById(R.id.connectMoto);
        textView2.setTypeface(typeface);
        textView2.startAnimation(animation2);

        TextView textView3 = (TextView) view.findViewById(R.id.welcome_bold_title);
        textView3.setTypeface(typeface);
        textView3.startAnimation(animation2);
        textView3.setTypeface(null, Typeface.BOLD);

        TextView textView = (TextView) view.findViewById(R.id.connectDescription);
        textView.setTypeface(typeface);
        textView.startAnimation(animation3);

        TextView textView1 = (TextView) view.findViewById(R.id.welcome_information);
        textView1.setTypeface(typeface);
        textView1.startAnimation(animation3);

        authButton.startAnimation(animation3);

        return view;
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Intent registerIntent = new Intent(getActivity(), NavDrawerActivity.class);
            registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(registerIntent);
        }
    }

}
