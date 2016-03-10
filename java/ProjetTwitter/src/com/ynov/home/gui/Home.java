package com.ynov.home.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.TextField;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import com.ynov.json.gui.Json;
import com.ynov.tweet.gui.Tweet;
import com.ynov.tweet.gui.Tweetmodel;

public class Home extends JFrame {
	 
    public Home() throws IOException{
        new Json();
        ArrayList<Tweet> tweetList = Json.Parse();    
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Twitter");
        this.setSize(800, 400);
        
       
        // A CREER METHODE SEARCH

        String urlReload = "http://iconizer.net/files/Wireframe_mono_icons/orig/playback_reload.png";
        JLabel button = new JLabel("<html><img src=" + urlReload + ">");
        this.add(button, BorderLayout.NORTH);
        JTextField textField = new JTextField(3);
        
        
        
        
        
    	
       
        // A CREER METHODE TABLEAU
        createTab(this, tweetList);
    }
     
     
    public void createTab(Home home, ArrayList<Tweet> tweetList)
    {
        JTable tweetsTable = new JTable(new Tweetmodel(tweetList));
        tweetsTable.setRowHeight(100);
       
        home.add(new JScrollPane(tweetsTable));
    }
}