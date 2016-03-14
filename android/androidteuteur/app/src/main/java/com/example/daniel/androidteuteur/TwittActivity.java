package com.example.daniel.androidteuteur;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by daniel on 11/03/2016.
 */
public class TwittActivity extends AppCompatActivity {
    private static Context context;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    public static Context getAppContext() {
        return TwittActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TwittActivity.context = getApplicationContext();

        TwitterTimeLine timeLine = new TwitterTimeLine();
        Thread t = new Thread(timeLine);

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Je me lance alors que jai pas le droit", Toast.LENGTH_LONG).show();


        String value = timeLine.getValue();
        System.out.println("je suis de retour");
        System.out.println(value);

                final ArrayList<Tweet> tweets = new ArrayList<Tweet>();

                 String result = value;
                try {
                    System.out.println(result);

                    JSONArray jsonArray = new JSONArray(result);

                    // use
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String tweetText = jsonObject.getString("text");
                        String tweetDate = jsonObject.getString("created_at");

                        JSONObject obj = jsonObject.getJSONObject("user");

                        String tweetAuthor = obj.getString("name");
                        String tweetAlias = obj.getString("screen_name");
                        String tweetAvatar = obj.getString("profile_image_url");

                        System.out.println(tweetText);
                        System.out.println(tweetDate);
                        System.out.println(tweetAuthor);
                        System.out.println(tweetAlias);
                        System.out.println(tweetAvatar);


                        tweets.add(new Tweet(tweetAuthor, tweetText, tweetDate, tweetAlias, tweetAvatar));
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }






        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.listView );

        // Create and populate a List of planet names.
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll( Arrays.asList(planets) );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter(getAppContext(), R.layout.activity_rowtextview, R.id.rowTextView);

        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.

        int listSize = tweets.size();

        for (int i = 0; i < listSize; i++) {
            listAdapter.add( tweets.get(i).getTweetAuthor() + tweets.get(i).getTweetAlias() + Html.fromHtml("<p style=\"color:red;\">" + tweets.get(i).getTweetText() + "</p>") + Html.fromHtml("<img src=" +tweets.get(i).getTweetAvatar() + ">"));
        }


        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );

    }
}
