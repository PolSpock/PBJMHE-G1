import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class Json {
	public static void Parse() {

	    String result = "";
	    try {
	        BufferedReader br = new BufferedReader(new FileReader("C:/Users/Paul/home.json"));
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
			 
				System.out.println(jsonObject.getString("created_at"));
				System.out.println(jsonObject.getInt("id"));				
				System.out.println(jsonObject.getString("text"));		
			}
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
}
