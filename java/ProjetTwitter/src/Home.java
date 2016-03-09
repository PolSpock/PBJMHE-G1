import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Home extends JFrame {

	public Home() throws IOException{
		new Json();
		ArrayList<Tweet> tweetList = Json.Parse();
		
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setTitle("JTable");
	    this.setSize(800, 400);
	    
	    // A CREER METHODE SEARCH
		String urlReload = "http://iconizer.net/files/Wireframe_mono_icons/orig/playback_reload.png";
		JLabel button = new JLabel("<html><img src=" + urlReload + ">");
		this.add(button, BorderLayout.NORTH);
		
	    // A CREER METHODE TABLEAU
	    createTab(this, tweetList);	
	}
	  
	  
	public void createTab(Home home, ArrayList<Tweet> tweetList)
	{
	    JTable tweetsTable = new JTable(new TweetModel(tweetList));
	    tweetsTable.setRowHeight(100);
	    
	    home.add(new JScrollPane(tweetsTable));
	}
}
