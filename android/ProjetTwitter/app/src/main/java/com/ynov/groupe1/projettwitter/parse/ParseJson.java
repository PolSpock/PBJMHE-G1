package com.ynov.groupe1.projettwitter.parse;

import com.ynov.groupe1.projettwitter.classes.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Paul on 17/03/2016.
 */
public class ParseJson {

    private final ArrayList<Tweet> tweets = new ArrayList<Tweet>();

    public ArrayList<Tweet> getArrayParse(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject firstObj = jsonArray.getJSONObject(i);

            String tweetText = firstObj.getString("text");
            String tweetDate = firstObj.getString("created_at");

            JSONObject secondObj = firstObj.getJSONObject("user");

            String tweetAuthor = secondObj.getString("name");
            String tweetAlias = secondObj.getString("screen_name");
            String tweetAvatar = secondObj.getString("profile_image_url");

            tweets.add(new Tweet(tweetAuthor, tweetText, tweetDate, tweetAlias, tweetAvatar));
        }
        return tweets;
    }
}
