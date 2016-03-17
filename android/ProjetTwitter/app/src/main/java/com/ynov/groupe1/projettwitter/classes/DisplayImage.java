package com.ynov.groupe1.projettwitter.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Paul on 17/03/2016.
 */
public class DisplayImage {

    private Bitmap bitMap;
    private URL url;

    public Bitmap getImage(final String image_url) throws InterruptedException {
        // new thread for network execution
        Thread i = new Thread(new Runnable() {
            public void run() {
                try {
                    url = new URL(image_url);
                    bitMap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // start execution
        i.start();
        // wait the end
        i.join();

        return bitMap;
    }
}
