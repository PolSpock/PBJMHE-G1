package com.ynov.groupe1.projettwitter.parse;

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
public class ParseTimeLine {

    private TweetAdapter adapter;
    private ListView mainListView;
    private MainActivity mainActivity;

    public ParseTimeLine(TweetAdapter adapter, ListView mainListView, MainActivity mainActivity){
        this.adapter = adapter;
        this.mainListView = mainListView;
        this.mainActivity = mainActivity;
    }

    public void parseTimeLine() {
        TwitterTimeLine timeLine = new TwitterTimeLine();
        Thread t = new Thread(timeLine);

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        try {
            JSONArray jsonArray = new JSONArray(timeLine.getValue());

            tweets = new ParseJson().getArrayParse(jsonArray);

        } catch(Exception e) {
            e.printStackTrace();
        }

        adapter = new TweetAdapter(mainActivity, tweets);

        mainListView.setAdapter(adapter);
    }
}
