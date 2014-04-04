package com.tkmcnally.quizter.fragments;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tkmcnally.quizter.Constants;
import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.Util;
import com.tkmcnally.quizter.activities.NavDrawerActivity;
import com.tkmcnally.quizter.http.WebServiceCaller;
import com.tkmcnally.quizter.http.WebServiceCallerImpl;
import com.tkmcnally.quizter.view.quizter.models.UserData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Thomas on 2/6/14.
 */
public class QuizFragment extends Fragment implements WebServiceCaller, Animation.AnimationListener {

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Bundle bundle;
    private TextView quizTip;
    private WebServiceCallerImpl ws;
    private QuizFragment fragment;
    private UserData user;

    private ImageView profilePicture;
    private Button finishButton;

    private List<String> questionList;
    private List<String> answerList;

    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_quiz_me, container, false);

        fragment = this;
        bundle = getArguments();

        user = (UserData) bundle.getSerializable("user_data");
        quizTip = (TextView) rootView.findViewById(R.id.quiz_tip);
        profilePicture = (ImageView) rootView.findViewById(R.id.quiz_player_image);
        imageLoader.displayImage(user.getPhoto_url(), profilePicture, ((NavDrawerActivity) getActivity()).getOptions(), animateFirstListener);

        finishButton = (Button) rootView.findViewById(R.id.quiz_finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View inflatedView = getActivity().getLayoutInflater().inflate(R.layout.quizter_submit_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(inflatedView);
                builder.setInverseBackgroundForced(true);
                builder.setCancelable(true);

                Button noBtn = (Button) inflatedView.findViewById(R.id.btn_no);
                noBtn.setText("No");

                Button okBtn = (Button) inflatedView.findViewById(R.id.btn_yes);
                okBtn.setText("Yes");

                String mess = "Do you wish you submit your quiz?";
                TextView messageView = (TextView) inflatedView.findViewById(R.id.txt_dia);
                messageView.setText(mess);


                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.hide();
                        submitQuiz();
                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.hide();
                    }
                });


                alertDialog = builder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.show();

            }
        });


        questionList = new ArrayList<String>();
        answerList = new ArrayList<String>();
        ActionBar ab = getActivity().getActionBar();
        ab.setTitle("Quizter");
        ab.setSubtitle("Quiz In Progress");
        ab.setDisplayHomeAsUpEnabled(true);

        HashMap<String, String> jsonFields = new HashMap<String, String>();
        jsonFields.put(Constants.STRING_ACCESS_TOKEN, Session.getActiveSession().getAccessToken());
        jsonFields.put(Constants.STRING_URL_PATH, Constants.API.GET_PLAYER_PROFILE_PATH);
        jsonFields.put(Constants.STRING_PLAYER_ID, user.getId());

        ws = new WebServiceCallerImpl(getActivity(), "Loading quiz...");
        ws.execute(jsonFields, fragment);

        return rootView;
    }

    @Override
    public void onPostWebServiceCall(String message) {

        if (message != null && !message.contains("valid")) {
            Type type = new TypeToken<UserData>() {
            }.getType();
            JsonParser parser = new JsonParser();
            JsonObject o = (JsonObject) parser.parse(message);
            JsonArray json_questions = (JsonArray) o.get("questions");
            for (int i = 0; i < json_questions.size(); i++) {
                JsonObject map = (JsonObject) json_questions.get(i);
                questionList.add(map.get("question").getAsString());
                answerList.add("");
            }

            createQuestionList();
        } else {
            JsonParser parser = new JsonParser();
            JsonObject o = (JsonObject) parser.parse(message);
            if ("true".equals(o.get("valid").getAsString())) {
                goToStats(message);
            }
            //Log.d("Quizter", "QUIZREPLY: " + message);
        }
    }

    public void createQuestionList() {
        ViewPager viewPager = (ViewPager) getView().findViewById(R.id.view_pager);

        AnimationSet animationSet = new AnimationSet(true);

        TranslateAnimation animation1 = new TranslateAnimation(viewPager.getX(), viewPager.getX(), getResources().getDisplayMetrics().heightPixels, viewPager.getY());
        animation1.setDuration(500);

        TranslateAnimation animation2 = new TranslateAnimation(viewPager.getX(), viewPager.getX(), viewPager.getY(), viewPager.getY());
        animation1.setInterpolator(new AccelerateDecelerateInterpolator());


        animationSet.addAnimation(animation1);
        animationSet.addAnimation(animation2);

        animation1.setAnimationListener(this);
        viewPager.startAnimation(animationSet);

        StringPageAdapter adapter = new StringPageAdapter();
        adapter.setList(questionList);
        viewPager.setAdapter(adapter);
    }

    public void submitQuiz() {
        EditText answerText = (EditText) getView().findViewById(R.id.quiz_question_answer_input);
        answerText.setFocusable(false);

        List<JsonObject> final_answers = new ArrayList<JsonObject>();
        for (int i = 0; i < questionList.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("question", questionList.get(i));
            jsonObject.addProperty("answer", answerList.get(i));
            final_answers.add(jsonObject);
        }

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String questions = gson.toJson(final_answers);

        HashMap<String, String> jsonFields = new HashMap<String, String>();
        jsonFields.put(Constants.STRING_ACCESS_TOKEN, Session.getActiveSession().getAccessToken());
        jsonFields.put(Constants.STRING_URL_PATH, Constants.API.SUBMIT_QUIZ_PATH);
        jsonFields.put(Constants.STRING_PLAYER_ID, user.getId());
        jsonFields.put(Constants.STRING_QUESTION_ANSWER, questions);
        jsonFields.put(Constants.STRING_DENSITY, Util.getPictureSize(getResources()));

        ws = new WebServiceCallerImpl(getActivity(), "Submitting quiz...");
        ws.execute(jsonFields, fragment);

    }

    public void goToStats(String message) {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        Arrays.sort(map.keySet().toArray());
        Fragment fragment = new QuizOverviewFragment();
        Bundle args = getArguments();
        args.putSerializable("data", message);

        fragment.setArguments(args);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "QuizOverviewFragment").commit();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        quizTip.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);

        //Log.d("Quizter", "ANIMATION IS WORKING?");

        quizTip.startAnimation(alphaAnimation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void handleUnauthorizedError() {
        ((NavDrawerActivity) getActivity()).handleUnauthorizedError();
    }

    @Override
    public void handleExceptionError() {
        Toast toast = Toast.makeText(getActivity(), "Server error. Try again!", Toast.LENGTH_LONG);
        toast.show();
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        public static Bitmap getRoundedBitmap(Bitmap bitmap, View view) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(output);

            final int color = Color.RED;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);


            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawOval(rectF, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            bitmap.recycle();

            return output;
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                imageView.setImageBitmap(getRoundedBitmap(loadedImage, view));

                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    private class StringPageAdapter extends PagerAdapter {
        private List<String> mQuestions;

        @Override
        public int getCount() {
            return mQuestions.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final int p = position;
            Context context = getActivity();
            //Log.d("Quizter", "INSTANTIATED NOW");

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.quiz_question_list, null);

            TextView indexView = (TextView) layout.findViewById(R.id.quiz_question_index);
            TextView questionView = (TextView) layout.findViewById(R.id.quiz_question_text);
            EditText answerInputEditText = (EditText) layout.findViewById(R.id.quiz_question_answer_input);

            indexView.setText((position + 1) + "");
            questionView.setText(mQuestions.get(position));
            if ("".equals(answerList.get(p))) {
                answerInputEditText.setHint("Answer Here!");
            } else {
                answerInputEditText.setText(answerList.get(p));
            }
            answerInputEditText.setSelection(answerInputEditText.getText().length());
            answerInputEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    StringBuilder updated_text = new StringBuilder();
                    updated_text.append(s.toString());
                    answerList.set(p, updated_text.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }

            });

            ((ViewPager) container).addView(layout, 0);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((LinearLayout) object);
        }

        public void setList(List<String> questions) {
            this.mQuestions = questions;
        }
    }
}
