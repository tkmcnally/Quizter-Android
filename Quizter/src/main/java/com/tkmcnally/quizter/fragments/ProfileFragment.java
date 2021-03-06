package com.tkmcnally.quizter.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tkmcnally.quizter.Constants;
import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.Util;
import com.tkmcnally.quizter.activities.NavDrawerActivity;
import com.tkmcnally.quizter.activities.WelcomeActivity;
import com.tkmcnally.quizter.adapters.GoogleNowCardArrayAdapter;
import com.tkmcnally.quizter.http.WebServiceCaller;
import com.tkmcnally.quizter.http.WebServiceCallerImpl;
import com.tkmcnally.quizter.view.models.GoogleNowCard;
import com.tkmcnally.quizter.view.models.GoogleNowLabelCard;
import com.tkmcnally.quizter.view.models.GoogleNowProfileCard;
import com.tkmcnally.quizter.view.models.profile.Profile;
import com.tkmcnally.quizter.view.models.profile.ProfilePhoto;
import com.tkmcnally.quizter.view.models.profile.ProfileQuestions;
import com.tkmcnally.quizter.view.quizter.models.Answer;
import com.tkmcnally.quizter.view.quizter.models.Question;
import com.tkmcnally.quizter.view.quizter.models.UserData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by Thomas on 1/10/14.
 */
public class ProfileFragment extends Fragment implements WebServiceCaller {

    private ImageView profilePictureImageView;
    private CardListView questionListView;
    private ImageBroadcastReceiver mReceiver;
    private Button saveButton;

    private Typeface typeface;

    private List<HashMap<Question, Answer>> questionListHashMap;
    private ArrayList<Card> cards;

    private UserData user;
    private boolean profile_setup;
    private GoogleNowProfileCard profileCard;
    private GoogleNowLabelCard labelCard;
    private int clickedQuestionPosition = 0;

    private ProfileFragment fragment;
    private GoogleNowCardArrayAdapter mCardArrayAdapter;
    private Bundle bundle;

    private Activity mActivity;

    @Override
    public void onPostWebServiceCall(String tempString) {
        if (tempString != null) {
            Type type = new TypeToken<UserData>() {
            }.getType();
            UserData jsonObject = new Gson().fromJson(tempString, type);
            setupQuestionList(jsonObject);
            user = jsonObject;
            ((NavDrawerActivity) getActivity()).setUserData(user);
        }
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

    public void setupQuestionList(List<HashMap<Question, Answer>> questions) {

        questionListHashMap.addAll(questions);

        cards = new ArrayList<Card>();

        profileCard = new GoogleNowProfileCard(this.getActivity());
        profileCard.setName(user.getName());
        profileCard.setScore(user.getScore());
        profileCard.setDateCreated(user.getDate_created());
        profileCard.PHOTO_URL = user.getPhoto_url();
        profileCard.setId("profile_card");
        profileCard.USE_VIGNETTE = 1;
        profileCard.init();
        cards.add(profileCard);

        labelCard = new GoogleNowLabelCard(this.getActivity());
        labelCard.setLabel("Your Questions");
        labelCard.setSubLabel("Swipe left/right on a question to change its position");
        labelCard.init();
        cards.add(labelCard);

        for (HashMap<Question, Answer> map : questionListHashMap) {
            for (Question q : map.keySet()) {
                GoogleNowCard card = new GoogleNowCard(this.getActivity());
                card.setQuestion(q);
                card.setAnswer(map.get(q));
                card.setIndex((cards.size() - 1) + "");
                card.setOnSwipeListener(swipeListener());
                card.setOnClickListener(cardClickListener());
                card.setType(2);
                card.setSwipeable(true);
                cards.add(card);
            }
        }

        mCardArrayAdapter = new GoogleNowCardArrayAdapter(getActivity(), cards);
        mCardArrayAdapter.setInnerViewTypeCount(3);

        if (questionListView != null) {
            questionListView.setAdapter(mCardArrayAdapter);
        }
    }

    public void setupQuestionList(UserData user) {

        questionListHashMap.clear();

        List<HashMap<String, String>> questions = user.getQuestions();
        for (HashMap<String, String> h : questions) {
            Question q = new Question(h.get(Constants.STRING_QUESTION));
            Answer a = new Answer(h.get(Constants.STRING_ANSWER));
            HashMap<Question, Answer> qa = new HashMap<Question, Answer>();
            qa.put(q, a);
            questionListHashMap.add(qa);
        }

        cards = new ArrayList<Card>();

        Log.d("Quizter", "Activity: " + this.getActivity());
        profileCard = new GoogleNowProfileCard(this.getActivity());
        profileCard.setName(user.getName());
        profileCard.setScore(user.getScore());
        profileCard.setDateCreated(user.getDate_created());
        profileCard.PHOTO_URL = user.getPhoto_url();
        profileCard.USE_VIGNETTE = 1;
        profileCard.init();
        cards.add(profileCard);

        labelCard = new GoogleNowLabelCard(this.getActivity());
        labelCard.setLabel("Your Questions");
        labelCard.setSubLabel("Swipe left/right on a question to change its position");
        labelCard.init();
        cards.add(labelCard);

        for (HashMap<Question, Answer> map : questionListHashMap) {
            for (Question q : map.keySet()) {
                GoogleNowCard card = new GoogleNowCard(this.getActivity());
                card.setQuestion(q);
                card.setAnswer(map.get(q));
                card.setIndex((cards.size() - 1) + "");
                card.setOnSwipeListener(swipeListener());
                card.setOnClickListener(cardClickListener());
                card.setType(2);
                card.setSwipeable(true);
                cards.add(card);
            }
        }
        mCardArrayAdapter = new GoogleNowCardArrayAdapter(getActivity(), cards);
        mCardArrayAdapter.setInnerViewTypeCount(3);

        if (questionListView != null) {
            questionListView.setAdapter(mCardArrayAdapter);
        }
    }

    private void swapFragmentQuestionSelection(String originalQuestion, String originalAnswer, int clickedQuestionPosition) {

        Fragment fragment = new QuestionModifyFragment();
        Bundle args = new Bundle();

        Profile profile = new Profile();
        profile.setScore(user.getScore());
        profile.setDateCreated(user.getDate_created());
        profile.setName(user.getName());
        profile.setPhoto_url(user.getPhoto_url());
        args.putParcelable("profile", profile);

        ProfilePhoto photo = new ProfilePhoto();
        Bitmap bitmap = ((GoogleNowProfileCard.GoogleNowProfilePicture) cards.get(0).getCardThumbnail()).bitmap;
        photo.setBitmap(bitmap);
        args.putParcelable("photo", photo);
        args.putString("originalQuestion", originalQuestion);
        args.putString("originalAnswer", originalAnswer);

        ProfileQuestions questions = new ProfileQuestions();
        questions.setQuestions(questionListHashMap);
        args.putSerializable("questions", questions);

        args.putInt("position", clickedQuestionPosition);

        fragment.setArguments(args);



        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "QuestionModifyFragment").addToBackStack("profileBackStack").commit();

    }

    private void replaceQuestion(String originalQuestion, String question, String answer, int position) {

        List<HashMap<Question, Answer>> temp = new ArrayList<HashMap<Question, Answer>>();
        temp.addAll(questionListHashMap);

        for (HashMap<Question, Answer> qa : temp) {
            for (Question q : qa.keySet()) {
                if (q.getQuestion().toString().equals(originalQuestion) && temp.indexOf(qa) == position) {
                    Question quest = new Question(question);
                    Answer ans = new Answer(answer);
                    questionListHashMap.get(questionListHashMap.indexOf(qa)).clear();
                    questionListHashMap.get(questionListHashMap.indexOf(qa)).put(quest, ans);

                    ((GoogleNowCard) cards.get(position + 2)).setQuestion(quest);
                    ((GoogleNowCard) cards.get(position + 2)).setAnswer(ans);
                }
            }
        }

        mCardArrayAdapter.notifyDataSetChanged();
    }

    public Bundle createBundle() {
        Bundle args = new Bundle();
        Profile profile = new Profile();
        profile.setScore(user.getScore());
        profile.setDateCreated(user.getDate_created());
        profile.setName(user.getName());
        profile.setPhoto_url(user.getPhoto_url());
        args.putParcelable("profile", profile);

        ProfilePhoto photo = new ProfilePhoto();
        Bitmap bitmap = ((GoogleNowProfileCard.GoogleNowProfilePicture) cards.get(0).getCardThumbnail()).bitmap;
        photo.setBitmap(bitmap);
        args.putParcelable("photo", photo);

        ProfileQuestions questions = new ProfileQuestions();
        questions.setQuestions(questionListHashMap);
        args.putSerializable("questions", questions);

        ((NavDrawerActivity) this.getActivity()).storeProfileBundle(args);

        return args;
    }

    public Card.OnSwipeListener swipeListener() {
        return new Card.OnSwipeListener() {
            @Override
            public void onSwipe(Card card) {
                cards.add(card);
                for (int i = 2; i < cards.size(); i++) {
                    ((GoogleNowCard) cards.get(i)).setIndex((i - 1) + "");
                }
                updateQuestionHashList();
            }
        };
    }

    public Card.OnCardClickListener cardClickListener() {
        return new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                GoogleNowCard questionCard = (GoogleNowCard) card;
                int position = cards.indexOf(card);
                clickedQuestionPosition = position;
                swapFragmentQuestionSelection(questionCard.getQuestion().getQuestion().toString(), questionCard.getAnswer().getAnswer().toString(), clickedQuestionPosition - 2);
            }
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mReceiver == null)
            mReceiver = new ImageBroadcastReceiver();
        activity.registerReceiver(mReceiver, new IntentFilter(it.gmariotti.cardslib.library.Constants.IntentManager.INTENT_ACTION_IMAGE_DOWNLOADED));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(((NavDrawerActivity)getActivity()).profileBundle != null) {
            bundle = ((NavDrawerActivity)getActivity()).profileBundle;

        } else if (savedInstanceState != null) {
            bundle = savedInstanceState;
        } else {
            bundle = getArguments();
        }

        View view = inflater.inflate(R.layout.activity_register, container, false);

        if (Session.getActiveSession() != null) {

            ActionBar ab = getActivity().getActionBar(); //needs  import android.app.ActionBar;
            ab.setTitle("Quizter");
            ab.setSubtitle("Dashboard");
            ab.setDisplayHomeAsUpEnabled(true);

            typeface = Typeface.create("sans-serif-light", Typeface.NORMAL);

            questionListHashMap = new ArrayList<HashMap<Question, Answer>>();

            questionListView = (CardListView) view.findViewById(R.id.questionListView);

            fragment = this;

            saveButton = (Button) view.findViewById(R.id.saveProfileButton);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String, Object> jsonFields = new HashMap<String, Object>();
                    jsonFields.put(Constants.STRING_ACCESS_TOKEN, Session.getActiveSession().getAccessToken());
                    jsonFields.put(Constants.STRING_DENSITY, Util.getPictureSize(getResources()));
                    jsonFields.put(Constants.STRING_URL_PATH, Constants.API.UPDATE_PROFILE_PATH);

                    List<JsonObject> jsonQuestions = new ArrayList<JsonObject>();
                    try {
                        for (HashMap<Question, Answer> qa : questionListHashMap) {
                            for (Question q : qa.keySet()) {
                                JsonObject object = new JsonObject();
                                String q_filtered = q.getQuestion();
                                q_filtered = q_filtered.replace("'", "\'");
                                String a_filtered = qa.get(q).getAnswer();
                                a_filtered = a_filtered.replace("'", "\'");

                                object.addProperty(Constants.STRING_QUESTION, q_filtered);
                                object.addProperty(Constants.STRING_ANSWER, a_filtered);
                                jsonQuestions.add(object.getAsJsonObject());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

                    //  String questions = gson.toJson(jsonQuestions);
                    jsonFields.put(Constants.STRING_UPDATED_QUESTIONS, gson.toJson(jsonQuestions));

                    WebServiceCallerImpl ws = new WebServiceCallerImpl(getActivity(), "Saving profile...");
                    ws.execute(jsonFields, fragment);
                }
            });

            if (bundle != null) {

                Profile profile = (Profile) bundle.getParcelable("profile");
                ProfilePhoto photo = (ProfilePhoto) bundle.getParcelable("photo");
                ProfileQuestions questions = (ProfileQuestions) bundle.getSerializable("questions");


                Log.d("Quizter", "bundle: " + bundle + " size: " + bundle.size() + "profile: " + profile + " photo: " + photo + " questions: " + questions);

                user = new UserData();
                user.setDate_created(profile.getDateCreated());
                user.setName(profile.getName());
                user.setScore(profile.getScore());
                user.setPhoto_url(profile.getPhoto_url());

                setupQuestionList(questions.getQuestions());
                if (bundle.getString("originalQuestion") != null) {
                    replaceQuestion(bundle.getString("originalQuestion"), bundle.getString("question"), bundle.getString("answer"), bundle.getInt("position"));
                }

                ((LinearLayout) view.findViewById(R.id.containerRegisterActivity)).setVisibility(0);

            } else {

                HashMap<String, String> jsonFields = new HashMap<String, String>();
                jsonFields.put(Constants.STRING_ACCESS_TOKEN, Session.getActiveSession().getAccessToken());
                jsonFields.put(Constants.STRING_DENSITY, Util.getPictureSize(getResources()));
                jsonFields.put(Constants.STRING_URL_PATH, Constants.API.GET_PROFILE_PATH);

                WebServiceCallerImpl ws = new WebServiceCallerImpl(getActivity(), "Fetching profile...");
                Log.d("Quizter", "execute called: " + bundle);
                ws.execute(jsonFields, this);
            }
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle bundleTwo) {
        Log.d("Quizter", "onSaveInstanceSate has been called!: " + bundle);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        Log.d("Quizter", "onActivityCreated has been called!: " + bundle);
        super.onActivityCreated(bundle);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mReceiver != null)
            getActivity().unregisterReceiver(mReceiver);
    }

    public void updateQuestionHashList() {
        for (int i = 2; i < cards.size(); i++) {
            String question = ((GoogleNowCard) cards.get(i)).getQuestion().getQuestion();
            String answer = ((GoogleNowCard) cards.get(i)).getAnswer().getAnswer();

            questionListHashMap.get(i - 2).clear();
            questionListHashMap.get(i - 2).put(new Question(question), new Answer(answer));
        }
    }

    public boolean hasSetupProfile() {
        if ("false".equals(user.getSetup_profile())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Broadcast for image downloaded by CardThumbnail
     */
    private class ImageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                boolean result = extras.getBoolean(it.gmariotti.cardslib.library.Constants.IntentManager.INTENT_ACTION_IMAGE_DOWNLOADED_EXTRA_RESULT);
                String id = extras.getString(it.gmariotti.cardslib.library.Constants.IntentManager.INTENT_ACTION_IMAGE_DOWNLOADED_EXTRA_CARD_ID);
                boolean processError = extras.getBoolean(it.gmariotti.cardslib.library.Constants.IntentManager.INTENT_ACTION_IMAGE_DOWNLOADED_EXTRA_ERROR_LOADING);
                if (result) {
                    if (profileCard != null && id != null && id.equalsIgnoreCase(profileCard.getId())) {
                        createBundle();
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Session.getActiveSession() == null) {
            Intent intent = new Intent(this.getActivity(), WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


}
