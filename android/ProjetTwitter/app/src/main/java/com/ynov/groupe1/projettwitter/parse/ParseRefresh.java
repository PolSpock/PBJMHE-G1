package com.ynov.groupe1.projettwitter.parse;

import android.os.AsyncTask;
import android.widget.ListView;

import com.ynov.groupe1.projettwitter.api.TwitterTimeLine;
import com.ynov.groupe1.projettwitter.classes.Tweet;
import com.ynov.groupe1.projettwitter.gui.TweetAdapter;
import com.ynov.groupe1.projettwitter.programm.MainActivity;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Paul on 17/03/2016.
 */
public class ParseRefresh {

    private TweetAdapter adapter;
    private ListView mainListView;
    private MainActivity mainActivity;

    public ParseRefresh(TweetAdapter adapter, ListView mainListView, MainActivity mainActivity){
        this.adapter = adapter;
        this.mainListView = mainListView;
        this.mainActivity = mainActivity;
    }

    public void parseRefresh() {
        // clean current listview
        adapter.clear();

        // start refresh request
        TwitterTimeLine timeLine = new TwitterTimeLine();

        // start in new thread (because network request separation)
        Thread t = new Thread(timeLine);
        t.start();

        // wait response
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // create new arrayList
        ArrayList<Tweet> tweetsRefresh = new ArrayList<Tweet>();

        // parse refresh request
        try {
            JSONArray jsonArray = new JSONArray(timeLine.getValue());

            tweetsRefresh = new ParseJson().getArrayParse(jsonArray);

        } catch(Exception e) {
            e.printStackTrace();
        }

        // new arraylist of tweet
        adapter = new TweetAdapter(mainActivity, tweetsRefresh);
        adapter.notifyDataSetChanged();

        // Set the new timeline
        mainListView.setAdapter(adapter);
    }
}
