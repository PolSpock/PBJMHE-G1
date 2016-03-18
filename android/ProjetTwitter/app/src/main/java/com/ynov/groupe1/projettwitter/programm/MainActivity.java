package com.ynov.groupe1.projettwitter.programm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import com.ynov.groupe1.projettwitter.parse.Parse;
import com.ynov.groupe1.projettwitter.classes.Tweet;
import com.ynov.groupe1.projettwitter.gui.TweetAdapter;
import com.ynov.groupe1.projettwitter.R;

/**
 * Created by daniel on 11/03/2016.
 */
public class MainActivity extends Activity implements View.OnClickListener{
    private ListView mainListView ;
    RelativeLayout layout = null;
    private TweetAdapter adapter;
    private EditText searchField;
    private Parse parse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = (RelativeLayout) RelativeLayout.inflate(this, R.layout.activity_main, null);
        mainListView = (ListView) layout.findViewById(R.id.listView);
        adapter = new TweetAdapter(MainActivity.this, new ArrayList<Tweet>());
        parse = new Parse(adapter, mainListView, MainActivity.this, searchField, layout);

        // TIMELINE
        parse.parseTimeLine();

        // PROFIL
        parse.displayProfil();

        // SEARCH
        Button buttonSearch = (Button) layout.findViewById(R.id.go_search);
        searchField = (EditText) layout.findViewById(R.id.editText);
        buttonSearch.setOnClickListener(this);

        // REFRESH
        ImageButton refreshButton = (ImageButton) layout.findViewById(R.id.imageButton);
        refreshButton.setOnClickListener(this);

        setContentView(layout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.go_search:
                parse.parseSearch();

                break;

            case R.id.imageButton:
                parse.parseRefresh();

                break;

            default:
                break;
        }
    }
}
