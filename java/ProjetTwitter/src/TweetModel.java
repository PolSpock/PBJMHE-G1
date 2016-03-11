import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class TweetModel extends AbstractTableModel {

	private ArrayList<Tweet> tweets;
	
	public TweetModel(ArrayList<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return this.tweets.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Tweet tweet = this.tweets.get(rowIndex);
		JLabel tweetRecup;
		switch(columnIndex) {
			case 0 :
				//return tweet.getTweetAuthor();
				tweetRecup = new JLabel("<html><img src="+ tweet.getTweetAvatar() +"><b>"+ tweet.getTweetAuthor() + "</b> @" + tweet.getTweetAlias() + "<br>" +  tweet.getTweetText() + "<br>" +  tweet.getTweetDate());
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
