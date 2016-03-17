package com.ynov.groupe1.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTable;

import com.ynov.groupe1.classes.Json;
import com.ynov.groupe1.entities.Tweet;
import com.ynov.groupe1.gui.TweetModel;

public class ButtonListener implements ActionListener {
	
	private JTable table;
	private Json listOfTweet;
	
	public ButtonListener(JTable table, Json listOfTweet){
		this.table = table;
		this.listOfTweet = listOfTweet;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// Update table model with new timeline
		ArrayList<Tweet> tweetListUpdate = listOfTweet.getTimeLine();		
		table.setModel(new TweetModel(tweetListUpdate));
	}

}
