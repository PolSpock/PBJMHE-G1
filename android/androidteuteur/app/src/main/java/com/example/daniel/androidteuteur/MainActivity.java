package com.example.daniel.androidteuteur;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    public class Splash extends Activity {

        /**
         * Duration of wait
         **/
        private final int SPLASH_DISPLAY_LENGTH = 1000;

        /**
         * Called when the activity is first created.
         */
        @Override
        public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.activity_launch);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(Splash.this, Menu.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }
}
