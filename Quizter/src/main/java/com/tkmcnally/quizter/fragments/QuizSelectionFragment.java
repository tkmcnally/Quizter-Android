package com.tkmcnally.quizter.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.google.gson.Gson;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Thomas on 2/5/14.
 */
public class QuizSelectionFragment extends Fragment implements WebServiceCaller {

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Button skipButton, quizMeButton;
    private ImageView profilePicture;
    private TextView profileName;
    private QuizSelectionFragment fragment;
    private int player_index = 0;

    private UserData userData;

    private WebServiceCallerImpl ws;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quizme_selection, container, false);
        fragment = this;
        profilePicture = (ImageView) view.findViewById(R.id.quiz_me_selection_image);

        profileName = (TextView) view.findViewById(R.id.quiz_me_selection_name);

        skipButton = (Button) view.findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> jsonFields = new HashMap<String, String>();
                jsonFields.put(Constants.STRING_ACCESS_TOKEN, Session.getActiveSession().getAccessToken());
                jsonFields.put(Constants.STRING_DENSITY, Util.getPictureSize(getResources()));
                jsonFields.put(Constants.STRING_URL_PATH, Constants.API.GET_AVAILABLE_PLAYER_PATH);
                jsonFields.put(Constants.STRING_PLAYER_INDEX, ++player_index + "");

                ws = new WebServiceCallerImpl(getActivity(), "Fetching next player...");
                ws.execute(jsonFields, fragment);
            }
        });
        quizMeButton = (Button) view.findViewById(R.id.quiz_me_button);
        quizMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });
        HashMap<String, String> jsonFields = new HashMap<String, String>();
        jsonFields.put(Constants.STRING_ACCESS_TOKEN, Session.getActiveSession().getAccessToken());
        jsonFields.put(Constants.STRING_DENSITY, Util.getPictureSize(getResources()));
        jsonFields.put(Constants.STRING_URL_PATH, Constants.API.GET_AVAILABLE_PLAYER_PATH);
        jsonFields.put(Constants.STRING_PLAYER_INDEX, player_index + "");

        ws = new WebServiceCallerImpl(getActivity(), "Finding players...");
        ws.execute(jsonFields, fragment);

        return view;
    }

    @Override
    public void onPostWebServiceCall(String message) {
        ((LinearLayout) getView().findViewById(R.id.quiz_me_selection_container)).setVisibility(0);
        if (message != null) {
            Type type = new TypeToken<UserData>() {
            }.getType();
            JsonParser parser = new JsonParser();
            JsonObject o = (JsonObject) parser.parse(message);
            if ("true".equals(o.get("available_players").getAsString())) {
                UserData jsonObject = new Gson().fromJson(message, type);
                profileName.setText(jsonObject.getName());
                //Log.d("Quizter", "this is: " + jsonObject.getPhoto_url());
                imageLoader.displayImage(jsonObject.getPhoto_url(), profilePicture, ((NavDrawerActivity) getActivity()).getOptions(), animateFirstListener);

                userData = jsonObject;

            } else {

                skipButton.setVisibility(View.GONE);
                quizMeButton.setVisibility(View.GONE);
                profileName.setVisibility(View.GONE);
                profilePicture.setVisibility(View.GONE);
                ((RelativeLayout) getView().findViewById(R.id.button_container)).setVisibility(View.GONE);

                ((LinearLayout) getView().findViewById(R.id.no_friends_found_container)).setVisibility(View.VISIBLE);

                TextView noFriendsMessage = (TextView) getView().findViewById(R.id.no_friends_found_text);
                noFriendsMessage.setText("No more available friends!");

                Button searchAgainButton = (Button) getView().findViewById(R.id.search_again);
                searchAgainButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        player_index = -1;

                        skipButton.setVisibility(View.VISIBLE);
                        quizMeButton.setVisibility(View.VISIBLE);
                        profileName.setVisibility(View.VISIBLE);
                        profileName.setText("");
                        profilePicture.setVisibility(View.INVISIBLE);

                        ((RelativeLayout) getView().findViewById(R.id.button_container)).setVisibility(View.VISIBLE);
                        ((LinearLayout) getView().findViewById(R.id.no_friends_found_container)).setVisibility(View.GONE);
                        skipButton.performClick();
                    }
                });
            }
        }
    }

    public void startQuiz() {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        Arrays.sort(map.keySet().toArray());
        Fragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putSerializable("user_data", userData);

        fragment.setArguments(args);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "QuizFragment").commit();
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
                imageView.setVisibility(View.VISIBLE);
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
