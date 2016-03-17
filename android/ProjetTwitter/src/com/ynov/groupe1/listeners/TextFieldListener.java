package com.ynov.groupe1.listeners;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.http.HttpException;
import org.apache.http.ParseException;
import org.json.JSONException;

import com.ynov.groupe1.classes.Json;
import com.ynov.groupe1.entities.Tweet;
import com.ynov.groupe1.gui.TweetModel;

public class TextFieldListener implements Action {

	private JTable table;
	private Json listOfTweet;
	
	public TextFieldListener(JTable table, Json listOfTweet){
		this.table = table;
		this.listOfTweet = listOfTweet;
	}

	@Override
    public void actionPerformed(ActionEvent e)
    {
		// Get text of Textfield
    	JTextField text = (JTextField)e.getSource();
        try {
        	// Update table model by the search
			ArrayList<Tweet> tweetListSearch = listOfTweet.getTweetSearch(text.getText());			
			table.setModel(new TweetModel(tweetListSearch));			
		} catch (KeyManagementException | ParseException | NoSuchAlgorithmException | IOException
				| JSONException | HttpException e1) {
			e1.printStackTrace();
		}
    }

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putValue(String key, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
