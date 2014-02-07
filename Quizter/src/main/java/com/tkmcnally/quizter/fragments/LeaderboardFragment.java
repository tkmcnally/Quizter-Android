package com.tkmcnally.quizter.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.facebook.Session;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tkmcnally.quizter.Constants;
import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.activities.NavDrawerActivity;
import com.tkmcnally.quizter.http.WebService;
import com.tkmcnally.quizter.http.WebServiceCaller;
import com.tkmcnally.quizter.adapters.LeaderboardListAdapter;
import com.tkmcnally.quizter.models.now.profile.ProfilePhoto;
import com.tkmcnally.quizter.models.quizter.ScoreData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Thomas on 1/11/14.
 */
public class LeaderboardFragment extends Fragment implements WebServiceCaller {

    private ListView leaderboardListView;
    private ArrayList<ScoreData> scores;
    private LeaderboardListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        leaderboardListView = (ListView) view.findViewById(R.id.leaderboardList);
        scores = new ArrayList<ScoreData>();

        adapter = new LeaderboardListAdapter(this.getActivity(), R.layout.leaderboard_list_items, R.id.leaderboard_list_item_rank, R.id.leaderboard_list_item_picture, R.id.leaderboard_list_item_name,
                R.id.leaderboard_list_item_score, scores);
        leaderboardListView.setAdapter(adapter);

        adapter.setOptions(((NavDrawerActivity) getActivity()).getOptions());

        HashMap<String, String> jsonFields = new HashMap<String, String>();
        jsonFields.put(Constants.STRING_ACCESS_TOKEN, Session.getActiveSession().getAccessToken());
        jsonFields.put(Constants.STRING_URL_PATH, Constants.LEADERBOARD_API_PATH);

        WebService ws = new WebService(getActivity(), "Loading leaderboard...");
        ws.execute(jsonFields, this);

        return view;

    }

    @Override
    public void onPostWebServiceCall(String jsonString) {
        Type type = new TypeToken<JsonObject>(){}.getType();
        JsonObject jsonObject = new Gson().fromJson(jsonString, type);

        scores.clear();
        JsonArray array = null;

        array = jsonObject.getAsJsonArray("leaderboard");
        for (int i = 0; i < array.size(); i++) {
            JsonElement obj = array.get(i);
            JsonObject object = obj.getAsJsonObject();
            ScoreData scoreData = new ScoreData();
            scoreData.setRank(object.get("rank").getAsString());
            scoreData.setPhoto_url(object.get("photo_url").getAsString());
            scoreData.setName(object.get("name").getAsString());
            scoreData.setScore(object.get("score").getAsString());
            scores.add(scoreData);
        }


        adapter.notifyDataSetChanged();
    }
}
