package com.ynov.groupe1.classes;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.JButton;

import org.apache.http.HttpException;
import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ynov.groupe1.api.TwitterAPI;
import com.ynov.groupe1.entities.Tweet;

public class Json {
	// Get config file
	ResourceBundle rb = ResourceBundle.getBundle("com.ynov.groupe1.config.config");
		
	// Parse TimeLine
	public ArrayList<Tweet> getTimeLine() {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	    String result = "";
	    
	    try {	        
	    	if ("false".equals(rb.getString("parseByFile"))) {
	    		result = parseJsonByAPI();
	    	}
	    	else {
	    		result = parseJsonByFile();
	    	}
				    	
			JSONArray jsonArray = new JSONArray(result); 
			 
			// use
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
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    
	    return tweets;
	}
	
	// get TimeLine by file
	public String parseJsonByFile() throws IOException {
		BufferedReader jsonFile = new BufferedReader(new InputStreamReader(new FileInputStream(rb.getString("pathFile")), "UTF8"));
		StringBuilder sb = new StringBuilder();
		
        String line = jsonFile.readLine();
        while (line != null) {
            sb.append(line);
            line = jsonFile.readLine();
        }
        jsonFile.close();
        return sb.toString();
	}
	
	// get TimeLine by Twitter API
	public String parseJsonByAPI() throws IOException, KeyManagementException, ParseException, NoSuchAlgorithmException, JSONException, HttpException {
    	return new TwitterAPI().getTimeLine();
	}
	
	// get Search by Twitter API and parse result
	public ArrayList<Tweet> getTweetSearch(String text) throws IOException, KeyManagementException, ParseException, NoSuchAlgorithmException, JSONException, HttpException {
		ArrayList<Tweet> tweetsSearch = new ArrayList<Tweet>();

  		JSONObject result = new TwitterAPI().searchTweets(text); 
  		
		JSONArray firstObj = result.getJSONArray("statuses");
		for (int i = 0; i < firstObj.length(); i++) {
			JSONObject secondObj = firstObj.getJSONObject(i);
			
			String tweetText = secondObj.getString("text");
			String tweetDate = secondObj.getString("created_at");
			
			JSONObject thirdObject = secondObj.getJSONObject("user");

			String tweetAuthor = thirdObject.getString("name");
			String tweetAlias = thirdObject.getString("screen_name");
			String tweetAvatar = thirdObject.getString("profile_image_url");

			tweetsSearch.add(new Tweet(tweetAuthor, tweetText, tweetDate, tweetAlias, tweetAvatar));	
		}	

		return tweetsSearch;
	}
}
