package com.ynov.groupe1.gui;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.apache.http.HttpException;
import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import com.ynov.groupe1.entities.Tweet;
import com.ynov.groupe1.listeners.ButtonListener;
import com.ynov.groupe1.listeners.TextFieldListener;
import com.ynov.groupe1.api.TwitterAPI;
import com.ynov.groupe1.classes.Json;

public class Home extends JFrame {
	
	private JTextField textField;
	private JPanel panel;
	private JTable table;
	private JButton refreshButton;
	private JSONObject myProfil;

	public Home() throws IOException, KeyManagementException, ParseException, NoSuchAlgorithmException, JSONException, HttpException{
		
		Json listOfTweet = new Json();
		ArrayList<Tweet> tweetList = listOfTweet.getTimeLine();

		// JFrame params
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setTitle("Twitter");
	    this.setSize(800, 400);
	    	
		// Tab
		table = createTab(this, tweetList);
			
		// Header
		panel = new Header();
		
		// Add Header to JFrame
		this.getContentPane().add(panel, BorderLayout.NORTH);
		
		// Rrefresh button
		refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ButtonListener(table, listOfTweet));
		panel.add(refreshButton);
        
	    // Get my profil
	    myProfil = new TwitterAPI().getMyProfil();
	    displayProfil(myProfil);
		
		// Search		
		textField = new JTextField();
		textField.addActionListener(new TextFieldListener(table, listOfTweet));
		panel.add(textField);
		textField.setColumns(10);
	}
	
	public JTable createTab(Home home, ArrayList<Tweet> tweetList)
	{
	    JTable tweetsTable = new JTable();
	    tweetsTable.setModel(new TweetModel(tweetList));
	    tweetsTable.setRowHeight(100);	    
	    home.add(new JScrollPane(tweetsTable));
	    
	    return tweetsTable;
	}
	
	public class Header extends JPanel {
		public void paintComponent(Graphics g){		
		    //x1, y1, width, height, arcWidth, arcHeight
			g.drawRoundRect(0, 0, 800, 100, 0, 0);
		}               
	}
	
	private void displayProfil(JSONObject myProfil) throws JSONException {
    	Image profilImage = null;
        try {
            URL url = new URL(myProfil.getString("profile_image_url"));
            profilImage = ImageIO.read(url);
        } catch (IOException e) {
        	e.printStackTrace();
        }
	    
        JLabel label = new JLabel(new ImageIcon(profilImage));
        panel.add(label);
        
		JLabel lblNewLabel_1 = new JLabel("@" + myProfil.getString("name"));
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel = new JLabel(myProfil.getString("screen_name"));
		panel.add(lblNewLabel);		
	}


}
