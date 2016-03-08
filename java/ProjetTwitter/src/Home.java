import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Home {

	
	// public class Fenetre extends JFrame
	
	public static void Windows() {
		JFrame fenetre = new JFrame();
		fenetre.setSize(800, 400);
		fenetre.setLocationRelativeTo(null);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Mon nom et icone",JLabel.CENTER), BorderLayout.NORTH);

		fenetre.setContentPane(panel);		
		fenetre.setVisible(true);

		
	}
}
