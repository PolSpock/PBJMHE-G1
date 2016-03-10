package com.ynov.json.gui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ynov.tweet.gui.Tweet;

public class Json {
private static ArrayList<Tweet> tweets = new ArrayList<Tweet>();;
	
	public static ArrayList<Tweet> Parse() {

	    String result = "";
	    try {
	    	BufferedReader br = new BufferedReader(new FileReader("C:/Users/michou/Desktop/home.json"));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            line = br.readLine();
	        }
	        result = sb.toString();
	        
			// parse JSON
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
				
				//System.out.println(obj.getString("screen_name"));
				//System.out.println(obj.getString("name"));
				//System.out.println(obj.getString("profile_image_url"));

				
				tweets.add(new Tweet(tweetAuthor, tweetText, tweetDate, tweetAlias, tweetAvatar));
				
				//JSONObject obj = jsonObject.getJSONObject("user");
			}
			
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    
	    return tweets;
	}
}
