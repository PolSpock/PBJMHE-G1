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

import org.apache.http.HttpException;
import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json {
	
	//private static ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	
	public ArrayList<Tweet> Parse() {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();

	    String result = "";
	    try {	        
	    	boolean parseByFile = false;
	    	if (parseByFile == false) {
	    		result = parseJsonByAPI();
	    	}
	    	else {
	    		result = parseJsonByFile();
	    	}
			
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
				
				tweets.add(new Tweet(tweetAuthor, tweetText, tweetDate, tweetAlias, tweetAvatar));	
			}			
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    
	    return tweets;
	}
	
	public String parseJsonByFile() throws IOException {
		BufferedReader jsonFile = new BufferedReader(new InputStreamReader(new FileInputStream("C:/Users/Paul/home.json"), "UTF8"));
		StringBuilder sb = new StringBuilder();
		
        String line = jsonFile.readLine();
        while (line != null) {
            sb.append(line);
            line = jsonFile.readLine();
        }
        return sb.toString();
	}
	
	public String parseJsonByAPI() throws IOException, KeyManagementException, ParseException, NoSuchAlgorithmException, JSONException, HttpException {
    	TweeterAPI twitter = new TweeterAPI();
		String access_token = "706818055054233600-axPLau20pgEjl752BmJRsVx0Fzaa6ON";
		String access_token_secret = "TLQ5uK4KK2ZTWnpMA1I8lA6m5IyuoPdCiJWBiB16wNKpz";
		
		return twitter.getTimeLine(access_token, access_token_secret);
	}
	
	
	public JSONObject getMyProfil() throws IOException, KeyManagementException, ParseException, NoSuchAlgorithmException, JSONException, HttpException {
    	TweeterAPI twitter = new TweeterAPI();
		String access_token = "706818055054233600-axPLau20pgEjl752BmJRsVx0Fzaa6ON";
		String access_token_secret = "TLQ5uK4KK2ZTWnpMA1I8lA6m5IyuoPdCiJWBiB16wNKpz";
		
		String me = twitter.getMyProfil(access_token, access_token_secret);		
		JSONObject jsonObject = new JSONObject(me); 
		
		return jsonObject;
	}
	
	public ArrayList<Tweet> getTweetSearch(String text) throws IOException, KeyManagementException, ParseException, NoSuchAlgorithmException, JSONException, HttpException {
		ArrayList<Tweet> tweetsSearch = new ArrayList<Tweet>();
		
		TweeterAPI twitter = new TweeterAPI();
  		String access_token = "706818055054233600-axPLau20pgEjl752BmJRsVx0Fzaa6ON";
   		String access_token_secret = "TLQ5uK4KK2ZTWnpMA1I8lA6m5IyuoPdCiJWBiB16wNKpz";
        
        System.out.println(text);

  		JSONObject resultat = twitter.searchTweets(text, access_token, access_token_secret);
        System.out.println(resultat);
        		
		JSONArray obj = resultat.getJSONArray("statuses");
		System.out.println(obj);

        
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

		return tweetsSearch;
	}
}
