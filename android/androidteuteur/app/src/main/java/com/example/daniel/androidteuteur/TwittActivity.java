package com.example.daniel.androidteuteur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by daniel on 11/03/2016.
 */
public class TwittActivity extends AppCompatActivity implements View.OnClickListener{
    private static Context context;
    private ListView mainListView ;
    RelativeLayout layout = null;
    private ArrayAdapter<String> listAdapter ;

    public static Context getAppContext() {
        return TwittActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwittActivity.context = getApplicationContext();

        layout = (RelativeLayout) RelativeLayout.inflate(this, R.layout.activity_main, null);


        Button mButton = (Button) layout.findViewById(R.id.angry_btn);
        final EditText mEdit = (EditText) layout.findViewById(R.id.editText);

        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Toast.makeText(TwittActivity.this, mEdit.getText().toString(), Toast.LENGTH_SHORT).show();

                        listAdapter.clear();

                        TwitterSearch search = new TwitterSearch(mEdit.getText().toString());
                        Thread s = new Thread(search);

                        s.start();

                        try {
                            s.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        String value = search.getValue();
                        System.out.println("je suis de retour du search");
                        System.out.println(value);

                        ArrayList<Tweet> tweetsSearch = new ArrayList<Tweet>();

                        JSONObject resultat = null;
                        try {
                            resultat = new JSONObject(value);

                            JSONArray obj = resultat.getJSONArray("statuses");
                            System.out.println(obj);
                            System.out.println(resultat);

                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject jsonObject = obj.getJSONObject(i);

                                String tweetText = jsonObject.getString("text");
                                String tweetDate = jsonObject.getString("created_at");

                                System.out.println(tweetText);
                                System.out.println(tweetDate);

                                JSONObject objDeux = jsonObject.getJSONObject("user");

                                String tweetAuthor = objDeux.getString("name");
                                String tweetAlias = objDeux.getString("screen_name");
                                String tweetAvatar = objDeux.getString("profile_image_url");
                                System.out.println(tweetAuthor);
                                System.out.println(tweetAlias);
                                System.out.println(tweetAvatar);

                                tweetsSearch.add(new Tweet(tweetAuthor, tweetText, tweetDate, tweetAlias, tweetAvatar));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mainListView = (ListView) layout.findViewById(R.id.listView);


                        int listSize = tweetsSearch.size();

                        for (int i = 0; i < listSize; i++) {
                            listAdapter.add( tweetsSearch.get(i).getTweetAuthor() + tweetsSearch.get(i).getTweetAlias() + Html.fromHtml("<p style=\"color:red;\">" + tweetsSearch.get(i).getTweetText() + "</p>") + Html.fromHtml("<img src=" +tweetsSearch.get(i).getTweetAvatar() + ">"));
                        }


                        listAdapter.notifyDataSetChanged();

                    }
                });


        ImageButton button = (ImageButton) layout.findViewById(R.id.imageButton);
        button.setOnClickListener(this);

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

        LinearLayout layoutLi = (LinearLayout) LinearLayout.inflate(this, R.layout.activity_rowtextview, null);
        ImageView imageView = (ImageView) layoutLi.findViewById(R.id.image);

        System.out.println("je dois contenir des trucs");

        System.out.println(imageView);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().
                permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(tweets.get(0).getTweetAvatar());
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(image);


        // Find the ListView resource.
        mainListView = (ListView) layout.findViewById(R.id.listView);

        // Create and populate a List of planet names.
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll(Arrays.asList(planets));




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
        mainListView.setAdapter(listAdapter);

        setContentView(layout);

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "clique", Toast.LENGTH_LONG).show();

        listAdapter.clear();

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


        System.out.println("je dois contenir des trucs");


        // Find the ListView resource.
        mainListView = (ListView) layout.findViewById(R.id.listView);

        // Create and populate a List of planet names.
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll(Arrays.asList(planets));




        // Create ArrayAdapter using the planet list.

        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.



        int listSize = tweets.size();

        for (int i = 0; i < listSize; i++) {
            listAdapter.add( tweets.get(i).getTweetAuthor() + tweets.get(i).getTweetAlias() + Html.fromHtml("<p style=\"color:red;\">" + tweets.get(i).getTweetText() + "</p>") + Html.fromHtml("<img src=" +tweets.get(i).getTweetAvatar() + ">"));
        }


        listAdapter.notifyDataSetChanged();
    }
}
