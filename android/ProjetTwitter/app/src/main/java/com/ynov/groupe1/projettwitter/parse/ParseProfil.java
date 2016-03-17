package com.ynov.groupe1.projettwitter.parse;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ynov.groupe1.projettwitter.api.TwitterProfil;
import com.ynov.groupe1.projettwitter.R;
import com.ynov.groupe1.projettwitter.classes.DisplayImage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Paul on 17/03/2016.
 */
public class ParseProfil {

    private RelativeLayout layout;

    public ParseProfil(RelativeLayout layout) {
        this.layout = layout;
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
