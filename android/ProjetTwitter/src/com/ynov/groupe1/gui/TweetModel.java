package com.ynov.groupe1.gui;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;
import com.ynov.groupe1.entities.Tweet;

public class TweetModel extends AbstractTableModel {

	private ArrayList<Tweet> tweets;
	
	public TweetModel(ArrayList<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return this.tweets.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Tweet tweet = this.tweets.get(rowIndex);
		JLabel tweetRecup;
		switch(columnIndex) {
			case 0 :
				tweetRecup = new JLabel("<html><img src="+ tweet.getTweetAvatar() +">&nbsp;<b>"+ tweet.getTweetAuthor() + "</b> @" + tweet.getTweetAlias() + "<br>" +  tweet.getTweetText());
				return tweetRecup.getText();
			default:
				return "Unknown";
		}
	}
	
	@Override
	public String getColumnName(int col) {
		switch(col)
		{
			case 0:
				return "Tweet";
			default :
				return "Unknown";
		}
	}

}
