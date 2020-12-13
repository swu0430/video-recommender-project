package videorecs;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ChildframeFavorites extends JFrame {

	public ChildframeFavorites() {
		super("Your Favorites");
		
		Control.MFRAME.setVisible(false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
	   	JButton okButton = new JButton("OK");
    	okButton.setAlignmentX(Component.CENTER_ALIGNMENT);

    	
    	okButton.addActionListener(new ActionListener() {
    		@Override
           	public void actionPerformed(ActionEvent e) {
    			dispose();
    			Control.MFRAME.setVisible(true);
    		}
    	});
		
		this.add(new JLabel("Empty JFrame"));
		this.add(okButton);
		this.pack();
		this.setVisible(true);
	}

}
