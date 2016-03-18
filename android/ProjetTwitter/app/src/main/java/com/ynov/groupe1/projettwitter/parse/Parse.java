package com.ynov.groupe1.projettwitter.parse;

/**
 * Created by Paul on 18/03/2016.
 */

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ynov.groupe1.projettwitter.R;
import com.ynov.groupe1.projettwitter.api.TwitterProfil;
import com.ynov.groupe1.projettwitter.api.TwitterSearch;
import com.ynov.groupe1.projettwitter.api.TwitterTimeLine;
import com.ynov.groupe1.projettwitter.classes.DisplayImage;
import com.ynov.groupe1.projettwitter.classes.Tweet;
import com.ynov.groupe1.projettwitter.gui.TweetAdapter;
import com.ynov.groupe1.projettwitter.programm.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Paul on 17/03/2016.
 */
public class Parse {

    private TweetAdapter adapter;
    private ListView mainListView;
    private MainActivity mainActivity;
    private EditText searchField;
    private RelativeLayout layout;

    public Parse(TweetAdapter adapter, ListView mainListView, MainActivity mainActivity, EditText searchField, RelativeLayout layout){
        this.adapter = adapter;
        this.mainListView = mainListView;
        this.mainActivity = mainActivity;
        this.searchField = searchField;
        this.layout = layout;
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

    public void displayProfil() {
        // Request API
        TwitterProfil profil = new TwitterProfil();

        // Start request in new thread
        Thread s = new Thread(profil);
        s.start();

        // Wait for response
        try {
            s.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Parse response from API and display it
        try {
            JSONObject myProfil = new JSONObject(profil.getValue());

            // profil name
            TextView name = (TextView) layout.findViewById(R.id.textView3);
            name.setText(myProfil.getString("screen_name"));

            // profil alias
            TextView alias = (TextView) layout.findViewById(R.id.textView);
            alias.setText("@" + myProfil.getString("name"));

            // profil image
            ImageView icon = (ImageView) layout.findViewById(R.id.imageView);
            icon.setImageBitmap(new DisplayImage().getImage(myProfil.getString("profile_image_url")));

        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
