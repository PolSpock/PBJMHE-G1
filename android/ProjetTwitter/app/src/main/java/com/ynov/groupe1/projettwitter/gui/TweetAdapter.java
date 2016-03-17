package com.ynov.groupe1.projettwitter.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ynov.groupe1.projettwitter.classes.DisplayImage;
import com.ynov.groupe1.projettwitter.classes.Tweet;
import com.ynov.groupe1.projettwitter.R;

import java.util.List;

/**
 * Created by Paul on 15/03/2016.
 */
public class TweetAdapter extends ArrayAdapter<Tweet> {

    // tweet = list of models to display
    public TweetAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_rowtextview,parent, false);
        }

        // get xml object
        TweetViewHolder viewHolder = (TweetViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TweetViewHolder();
            viewHolder.pseudo = (TextView) convertView.findViewById(R.id.pseudo);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
            viewHolder.alias = (TextView) convertView.findViewById(R.id.alias);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(viewHolder);
        }

        Tweet tweet = getItem(position);

        // set view
        viewHolder.pseudo.setText(tweet.getTweetAuthor() + " @" + tweet.getTweetAlias());
        viewHolder.text.setText(tweet.getTweetText());

        // set image
        try {
            viewHolder.avatar.setImageBitmap(new DisplayImage().getImage(tweet.getTweetAvatar()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class TweetViewHolder{
        public TextView pseudo;
        public TextView text;
        public TextView alias;
        public ImageView avatar;
    }
}