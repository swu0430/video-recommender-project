package videorecs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * This class represents a childframe Java Swing GUI that displays the user's list of favorited
 * YouTube videos (videos that the user has liked) in the form of hyper-linked URL's.
 * @author Alicia Yen and Steven Wu
 */
public class ChildframeFavorites extends JFrame {
	
	// STATIC VARIABLES
	
	/**
	 * List of activity categories associated with user's favorited videos.
	 */
	static ArrayList<String> FAVORITE_ACTIVITIES = new ArrayList<String>();

	// CONSTRUCTOR 
	
	/**
	 * Constructs the GUI display for the user's favorited videos.
	 * Calls the constructor in the JFrame super class.
	 */
	public ChildframeFavorites() {
		
		// Call constructor in JFrame super class. 
		// Define the title, close operation, and layout (boxlayout) of window.
		super("Your Favorites");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		// Set the mainframe to invisible.
		Control.MFRAME.setVisible(false);
		
		// Create an overarching JPanel and set to boxlayout format
		JPanel panel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxlayout);
		
		// Create a JScrollPane object to add a scrollbar
		JScrollPane scrollPane = new JScrollPane(panel);
		
		// Create JPanel for the introduction message
    	JPanel panelIntro = new JPanel();
    	JLabel labelIntro = new JLabel("Here is a list of all the videos you've liked!");
    	Font fontIntro = new Font(labelIntro.getFont().getName(), Font.ITALIC, labelIntro.getFont().getSize());    	
    	labelIntro.setFont(fontIntro);
    	panelIntro.add(labelIntro);
    	
    	// Create JPanel (panelVideos) for the all the favorited videos to display.
    	JPanel panelVideos = new JPanel();
    	BoxLayout boxlayoutVideos = new BoxLayout(panelVideos, BoxLayout.Y_AXIS);
    	panelVideos.setLayout(boxlayoutVideos);
    	panelVideos.setAlignmentX(Component.RIGHT_ALIGNMENT);
    	
    	// Collect a list of all the activities associated with the user's favorited videos.
    	for (int i = 0; i < Mainframe.FAVORITES_LIST.size(); i++) {
    		if (!ChildframeFavorites.FAVORITE_ACTIVITIES.contains(Mainframe.FAVORITES_LIST.get(i).getActivity())) {
    			ChildframeFavorites.FAVORITE_ACTIVITIES.add(Mainframe.FAVORITES_LIST.get(i).getActivity());
    		}
    	}
    	
    	// Create a sub-JPanel within the panelVideos JPanel for each of the activities.
    	for (int j = 0; j < ChildframeFavorites.FAVORITE_ACTIVITIES.size(); j++ ) {
    		JPanel panelVideosSection = new JPanel();
    		BoxLayout boxlayoutSection = new BoxLayout(panelVideosSection, BoxLayout.Y_AXIS);
    		panelVideosSection.setLayout(boxlayoutSection);
    		panelVideosSection.setBorder(BorderFactory.createTitledBorder(ChildframeFavorites.FAVORITE_ACTIVITIES.get(j).toUpperCase()));
    	
        	// Within each activity sub-JPanel, display all the videos associated with that activity that 
    		// the user has liked. Display both the title and URL of each video.
    		for (int k = 0; k < Mainframe.FAVORITES_LIST.size(); k++) {
        		if (Mainframe.FAVORITES_LIST.get(k).getActivity().equals(ChildframeFavorites.FAVORITE_ACTIVITIES.get(j))) {
        			
        			JLabel titleVideo = new JLabel(Mainframe.FAVORITES_LIST.get(k).getTitle());
        			Font f1 = titleVideo.getFont();
        			titleVideo.setFont(f1.deriveFont(f1.getStyle() & ~Font.BOLD));
        			panelVideosSection.add(titleVideo);
        			
        			JLabel urlVideo = new JLabel("https://www.youtube.com/watch?v=" + Mainframe.FAVORITES_LIST.get(k).getVideoID());
        			final String VIDEOID = Mainframe.FAVORITES_LIST.get(k).getVideoID();
        			Font f2 = urlVideo.getFont();
        			urlVideo.setForeground(Color.BLUE.darker());
        			urlVideo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        			
        			// Add mouse listener to JLabel (hyper link) 
        			urlVideo.addMouseListener(new MouseAdapter() {
        				@Override
        				public void mouseClicked(MouseEvent e) {
        					try {
        						Desktop.getDesktop().browse(
        								new URI("https://www.youtube.com/watch?v=" 
        										+ VIDEOID)); // Video ID 
        					} catch (Exception e1) {
        						System.out.println("Sorry, this URL could not be opened.");
        					} 
        				}

        				@Override
        				public void mouseEntered(MouseEvent e) {
        					ChildframeRecommendations.JLABEL_LIST[0].setText("<html><a href=''>"
        							+ "https://www.youtube.com/watch?v="
        							+ VIDEOID // Video ID 
        							+ "</a></html>");
        				}
        	    	});
        			
        			
        			panelVideosSection.add(urlVideo);
 
        			panelVideosSection.add(new JLabel(" "));
        			
        		}
        	}
    		
    		// Add each activity sub-JPanel to the over-arching panelVideos JPanel.
    		panelVideos.add(panelVideosSection);
        	panelVideos.add(Box.createRigidArea(new Dimension(0,20)));
        	
    	}
   
    	// Create a JPanel for the closing "OK" button.
    	JPanel panelClosingButtons = new JPanel();
    	
    	// Create a JButton for the closing "OK" button that returns the user to the mainframe.
	   	JButton okButton = new JButton("OK");
    	okButton.addActionListener(new ActionListener() {
    		@Override
           	public void actionPerformed(ActionEvent e) {
    			dispose();
    			Control.MFRAME.setVisible(true);
    		}
    	});

    	// Add the JButton to the closing JPanel.
    	panelClosingButtons.add(okButton);
    	
    	// Add all the panels to the overarching JPanel
    	panel.add(Box.createRigidArea(new Dimension(0,10)));
    	panel.add(panelIntro, Component.CENTER_ALIGNMENT);
    	panel.add(Box.createRigidArea(new Dimension(0,30)));
    	panel.add(panelVideos);
    	panel.add(Box.createRigidArea(new Dimension(0,30)));
    	panel.add(panelClosingButtons);
    	panel.add(Box.createRigidArea(new Dimension(0,20)));
    	
    	// Add the scrollPane to the JFrame
    	this.getContentPane().add(scrollPane);
    	
    	// Size the JFrame and set the JFrame to visible.
    	this.pack();
    	this.setVisible(true);
		
	}

}
