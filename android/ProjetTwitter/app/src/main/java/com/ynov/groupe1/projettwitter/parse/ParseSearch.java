package com.ynov.groupe1.projettwitter.parse;

/**
 * Created by Paul on 17/03/2016.
 */

import android.widget.EditText;
import android.widget.ListView;

import com.ynov.groupe1.projettwitter.api.TwitterSearch;
import com.ynov.groupe1.projettwitter.classes.Tweet;
import com.ynov.groupe1.projettwitter.gui.TweetAdapter;
import com.ynov.groupe1.projettwitter.programm.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseSearch {

    private TweetAdapter adapter;
    private EditText searchField;
    private ListView mainListView;
    private MainActivity mainActivity;

    public ParseSearch(TweetAdapter adapter, EditText searchField, ListView mainListView, MainActivity mainActivity){
        this.adapter = adapter;
        this.searchField = searchField;
        this.mainListView = mainListView;
        this.mainActivity = mainActivity;
    }

    public void parseSearch() {
        // clean current listview
        adapter.clear();

        // start search request
        TwitterSearch search = new TwitterSearch(searchField.getText().toString());

        // start in new thread (because network request separation)
        Thread s = new Thread(search);
        s.start();

        // wait response
        try {
            s.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // create new arrayList
        ArrayList<Tweet> tweetsSearch = new ArrayList<Tweet>();

        // parse search request
        try {
            JSONObject firstObj = new JSONObject(search.getValue());

            JSONArray arrayObj = firstObj.getJSONArray("statuses");

            tweetsSearch = new ParseJson().getArrayParse(arrayObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // new arraylist of tweet
        adapter = new TweetAdapter(mainActivity, tweetsSearch);
        adapter.notifyDataSetChanged();

        // Set the new view
        mainListView.setAdapter(adapter);
    }
}

