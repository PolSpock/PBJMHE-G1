import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.http.HttpException;
import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

public class Home extends JFrame {
	
	private JTextField textField;

	public Home() throws IOException, KeyManagementException, ParseException, NoSuchAlgorithmException, JSONException, HttpException{
		Json listOfTweet = new Json();
		ArrayList<Tweet> tweetList = listOfTweet.Parse();
		
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setTitle("JTable");
	    this.setSize(800, 400);
	    	
		// TAB
		JTable table = createTab(this, tweetList);
			    
	    // MON PROFIL
	    JSONObject myProfil = listOfTweet.getMyProfil();
	    
		JPanel panel = new Panneau();
		this.getContentPane().add(panel, BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("Refresh");
		btnNewButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				
				System.out.println(table.getRowCount());
	
				ArrayList<Tweet> tweetListUpdate = listOfTweet.Parse();

				System.out.println("Refresh");
				
				table.setModel(new TweetModel(tweetListUpdate));
				
				System.out.println(table.getRowCount());				
			}
		});
		panel.add(btnNewButton);
		
    	Image image = null;
        try {
            URL url = new URL(myProfil.getString("profile_image_url"));
            image = ImageIO.read(url);
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
        JLabel label = new JLabel(new ImageIcon(image));
        panel.add(label);
        
		JLabel lblNewLabel_1 = new JLabel("@" + myProfil.getString("name"));
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel = new JLabel(myProfil.getString("screen_name"));
		panel.add(lblNewLabel);
		
		Action action = new AbstractAction()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		    	JTextField text = (JTextField)e.getSource();
		        System.out.println(text.getText());
		        System.out.println("some action");
		        try {
		       
					ArrayList<Tweet> tweetListSearch = listOfTweet.getTweetSearch(text.getText());

					System.out.println("Refresh");
					
					table.setModel(new TweetModel(tweetListSearch));
		        	
					
				} catch (KeyManagementException | ParseException | NoSuchAlgorithmException | IOException
						| JSONException | HttpException e1) {
					e1.printStackTrace();
				}
		    }
		};

		
		textField = new JTextField();
		textField.addActionListener( action );
		panel.add(textField);
		textField.setColumns(10);

		
		this.setVisible(true);
	}
	
	public JTable createTab(Home home, ArrayList<Tweet> tweetList)
	{
	    JTable tweetsTable = new JTable();
	    tweetsTable.setModel(new TweetModel(tweetList));
	    tweetsTable.setRowHeight(100);	    
	    home.add(new JScrollPane(tweetsTable));
	    
	    return tweetsTable;
	}
	
	public class Panneau extends JPanel {
		public void paintComponent(Graphics g){		
			System.out.println("Je suis exécutée !");
		    //x1, y1, width, height, arcWidth, arcHeight
			g.drawRoundRect(0, 0, 800, 100, 00, 0);
		}               
	}
	
	public class ProfilName extends JPanel {
		public void paintComponent(Graphics g){		
			System.out.println("Je suis exécutée ! 2");
		    //x1, y1, width, height, arcWidth, arcHeight
			g.drawString("Mon nom", 50, 50);
		}    
	}

}
