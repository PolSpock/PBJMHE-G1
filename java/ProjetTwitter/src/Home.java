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

	
	// public class Fenetre extends JFrame


	  public Home() throws IOException{
		new Json();
		ArrayList<Tweet> tweetList = Json.Parse();
		
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setTitle("JTable");
	    this.setSize(800, 400);
	    

	    URL url = new URL("http://www.digitalphotoartistry.com/rose1.jpg");
	    Image image = ImageIO.read(url); 
	    JLabel label = new JLabel(new ImageIcon(image));
	    
	    JLabel tweet = new JLabel("<html><b>"+ tweetList.get(0).getTweetAuthor() + "</b> @" + tweetList.get(0).getTweetAlias() + "<br>" +  tweetList.get(0).getTweetText() + "<br>" +  tweetList.get(0).getTweetDate() + image);
	    
 
	    
	    //Les données du tableau
	    Object[][] data = {
	      {tweet.getText()},
	      {label.getIcon()}
	    };
	
	    //Les titres des colonnes
	    String  title[] = {"Tweet"};
	    JTable tableau = new JTable(data, title);
	    
	    tableau.setRowHeight(70);
	
	    //Nous ajoutons notre tableau à notre contentPane dans un scroll
	
	    //Sinon les titres des colonnes ne s'afficheront pas !
	
	    this.getContentPane().add(new JScrollPane(tableau));
	
	  }   
	/*
	public static void Windows() {
		JFrame fenetre = new JFrame();
		fenetre.setSize(800, 400);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Object[][] donnees = {  
				   {"Swing", "Astral", "standard", 
				      Color.red, Boolean.TRUE}, 
				   {"Swing", "Mistral", "standard", 
				      Color.yellow, Boolean.FALSE}, 
				   {"Gin", "Oasis", "standard", 
				      Color.blue, Boolean.FALSE},
				   {"Gin", "boomerang", "compétition", 
				      Color.green, Boolean.TRUE},
				   {"Advance", "Omega", "performance", 
				      Color.cyan, Boolean.TRUE}, 
				};
		String[] titreColonnes = { 
				   "marque","modèle", "homologation",
				   "couleur", "vérifiée ?"}; 
		
		
		JTable jTable1 = new JTable(donnees, titreColonnes);
		
		
		
		fenetre.setContentPane(jTable1);		
		fenetre.setVisible(true);	
	}
	*/
}
