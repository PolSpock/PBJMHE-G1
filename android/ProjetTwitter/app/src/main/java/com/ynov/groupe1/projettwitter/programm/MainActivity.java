package com.ynov.groupe1.projettwitter.programm;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import com.ynov.groupe1.projettwitter.parse.ParseProfil;
import com.ynov.groupe1.projettwitter.parse.ParseRefresh;
import com.ynov.groupe1.projettwitter.parse.ParseSearch;
import com.ynov.groupe1.projettwitter.parse.ParseTimeLine;
import com.ynov.groupe1.projettwitter.classes.Tweet;
import com.ynov.groupe1.projettwitter.gui.TweetAdapter;
import com.ynov.groupe1.projettwitter.R;

/**
 * Created by daniel on 11/03/2016.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static Context context;
    private ListView mainListView ;
    RelativeLayout layout = null;
    //private ArrayAdapter<String> listAdapter;
    private TweetAdapter adapter;
    private EditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = (RelativeLayout) RelativeLayout.inflate(this, R.layout.activity_main, null);
        mainListView = (ListView) layout.findViewById(R.id.listView);
        adapter = new TweetAdapter(MainActivity.this, new ArrayList<Tweet>());

        // TIMELINE
        new ParseTimeLine(adapter, mainListView, MainActivity.this).parseTimeLine();

        // PROFIL
        new ParseProfil(layout).displayProfil();

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
                new ParseSearch(adapter, searchField, mainListView, MainActivity.this).parseSearch();

                break;

            case R.id.imageButton:
                new ParseRefresh(adapter, mainListView, MainActivity.this).parseRefresh();

                break;

            default:
                break;
        }
    }
}
