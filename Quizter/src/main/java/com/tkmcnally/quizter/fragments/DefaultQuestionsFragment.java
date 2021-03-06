package com.tkmcnally.quizter.fragments;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Session;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkmcnally.quizter.Constants;
import com.tkmcnally.quizter.R;
import com.tkmcnally.quizter.activities.NavDrawerActivity;
import com.tkmcnally.quizter.adapters.DefaultQuestionArrayAdapter;
import com.tkmcnally.quizter.http.WebServiceCaller;
import com.tkmcnally.quizter.http.WebServiceCallerImpl;
import com.tkmcnally.quizter.view.quizter.models.UserData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * Created by missionary on 1/5/2014.
 */
public class DefaultQuestionsFragment extends Fragment implements WebServiceCaller {

    private OnItemSelectedListener listener;

    private ListView questionListView;
    private ArrayList<String> questionList;
    private ArrayAdapter adapter;

    private Button loadMoreQuestions;

    private Bundle bundle;

    private Typeface typeface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_question_selection, container, false);


        bundle = getArguments();
        ActionBar ab = getActivity().getActionBar(); //needs  import android.app.ActionBar;
        ab.setTitle("Quizter");
        ab.setSubtitle("Add Question");
        ab.setDisplayHomeAsUpEnabled(true);

        loadMoreQuestions = (Button) rootView.findViewById(R.id.loadMoreQuestions);
        typeface = Typeface.create("sans-serif-light", Typeface.NORMAL);
        loadMoreQuestions.setTypeface(typeface);
        loadMoreQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadList();
            }
        });

        questionListView = (ListView) rootView.findViewById(R.id.questionSelectionListView);

        questionList = new ArrayList<String>();

        adapter = new DefaultQuestionArrayAdapter(this.getActivity(),
                R.layout.question_search_items, R.id.question_search_item, R.id.side_facing_arrow_list, questionList);
        questionListView.setAdapter(adapter);

        questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                String textView = (String) parent.getItemAtPosition(position);
                swapFragmentQuestionEdit(textView);
            }

        });

        loadList();

        return rootView;
    }

    private void loadList() {
        int endIndex = questionList.size();

        HashMap<String, String> jsonFields = new HashMap<String, String>();
        jsonFields.put(Constants.STRING_ACCESS_TOKEN, Session.getActiveSession().getAccessToken());
        jsonFields.put(Constants.STRING_URL_PATH, Constants.API.GET_DEFAULT_QUESTIONS_PATH);
        jsonFields.put(Constants.STRING_CURRENT_END_INDEX, (endIndex + 10) + "");

        WebServiceCallerImpl ws = new WebServiceCallerImpl(this.getActivity(), "Loading more questions...");
        ws.execute(jsonFields, this);
    }

    @Override
    public void onPostWebServiceCall(String message) {

        Type type = new TypeToken<UserData>() {
        }.getType();
        UserData jsonObject = new Gson().fromJson(message, type);

        List<HashMap<String, String>> questions = jsonObject.getQuestions();
        List<String> tempList = new ArrayList<String>();
        for (HashMap<String, String> h : questions) {
            if (h.containsKey(Constants.STRING_QUESTION)) {
                tempList.add(h.get(Constants.STRING_QUESTION));
            }
        }

        Collections.reverse(tempList);
        questionList.addAll(tempList);
        adapter.notifyDataSetChanged();
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

    private void swapFragmentQuestionEdit(String textView) {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        Arrays.sort(map.keySet().toArray());


        bundle.putString("new_question", textView);
        ((NavDrawerActivity) getActivity()).storeProfileBundle(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();

    }

    public interface OnItemSelectedListener {
        public void OnQuestionItemSelected(String question);
    }
}




