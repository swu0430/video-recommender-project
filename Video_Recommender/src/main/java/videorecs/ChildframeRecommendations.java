package videorecs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;

/**
 * This class represents a childframe Java Swing GUI that displays a list of 3 YouTube video
 * recommendations to the user based on the user's inputted activity list and video duration
 * preferences in the mainframe GUI.
 * @author Alicia Yen and Steven Wu
 */
public class ChildframeRecommendations extends JFrame {

	/**
	 * JPanel objects for the first recommended video.
	 */
	static JPanel PANEL_VIDEO1 = new JPanel();
	
	/**
	 * JPanel objects for the second recommended video.
	 */
	static JPanel PANEL_VIDEO2 = new JPanel();
	
	/**
	 * JPanel objects for the third recommended video.
	 */
	static JPanel PANEL_VIDEO3 = new JPanel();
	
	/**
	 * List to store multiple JPanel objects 
	 */
	static JPanel[] JPANEL_LIST = {PANEL_VIDEO1, PANEL_VIDEO2, PANEL_VIDEO3};
	
	/**
	 * List to store multiple JLabel objects
	 */
	static JLabel[] JLABEL_LIST = new JLabel [Control.NUMBER_VIDEOS];
	
	/**
	 * Button a user can press to indicate whether they liked the first recommended video.
	 */
	static JRadioButton JRB_LIKE1 = new JRadioButton("Like");
	
	/**
	 * Button a user can press to indicate whether they liked the second recommended video.
	 */
	static JRadioButton JRB_LIKE2 = new JRadioButton("Like");
	
	/**
	 * Button a user can press to indicate whether they liked the third recommended video.
	 */
	static JRadioButton JRB_LIKE3 = new JRadioButton("Like");
	
	/**
	 * Button a user can press to indicate whether they disliked the first recommended video.
	 */
	static JRadioButton JRB_DISLIKE1 = new JRadioButton("Dislike");
	
	/**
	 * Button a user can press to indicate whether they disliked the second recommended video.
	 */
	static JRadioButton JRB_DISLIKE2 = new JRadioButton("Dislike");
	
	/**
	 * Button a user can press to indicate whether they disliked the third recommended video.
	 */
	static JRadioButton JRB_DISLIKE3 = new JRadioButton("Dislike");
	
	/**
	 * Boolean variable to capture feedback from user 
	 * True indicates user liked the first video
	 * False indicates user disliked the first video
	 */
	static Boolean LIKE1;
	
	/**
	 * Boolean variable to capture feedback from user 
	 * True indicates user liked the second video
	 * False indicates user disliked the second video
	 */
	static Boolean LIKE2;
	
	/**
	 * Boolean variable to capture feedback from user 
	 * True indicates user liked the third video
	 * False indicates user disliked the third video
	 */
	static Boolean LIKE3;
	
	/**
	 * Constructs the GUI display for 3 recommended videos.
	 * Calls the constructor in the JFrame super class.
	 */
	public ChildframeRecommendations() {
		
		// Call constructor in JFrame super class.
		// Define the title, close operation, and layout (boxlayout) of window.
		super("Your Video Recommendations");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		// Set the mainframe to invisible.
		Control.MFRAME.setVisible(false);

		// JPanel introduction section 
    	JPanel panelIntro = new JPanel();
    	JLabel labelIntro = new JLabel("Here are your recommended videos. Take a look and see if you like these!");
    	Font fontIntro = new Font(labelIntro.getFont().getName(), Font.ITALIC, labelIntro.getFont().getSize());    	
    	labelIntro.setFont(fontIntro);
    	panelIntro.add(labelIntro);
    	
    	// JPanel closing section
    	JPanel panelClosingButtons = new JPanel();
    	
    	// Create multiple JPanels for recommended videos 
    	for (int i = 0; i < Mainframe.RECOMMENDATION_LIST.length; i++) {
    		ChildframeRecommendations.JPANEL_LIST[i].setLayout(new FlowLayout(FlowLayout.LEFT));
    		ChildframeRecommendations.JPANEL_LIST[i].setBorder(BorderFactory.createTitledBorder("VIDEO " + (i + 1) + ": "));
    		ChildframeRecommendations.JPANEL_LIST[i].add(new Label(Mainframe.RECOMMENDATION_LIST[i].getTitle()));
    		ChildframeRecommendations.JPANEL_LIST[i].add(new Label());
    	}
    	
    	/* 
    	 * JLABELS
    	 * Used to display YouTube hyper links
    	 */
    	
    	// Create multiple JLabels for recommended videos 
    	for (int i = 0; i < Mainframe.RECOMMENDATION_LIST.length; i++) {
    		ChildframeRecommendations.JLABEL_LIST[i] = new JLabel("https://www.youtube.com/watch?v=" 
    													+ Mainframe.RECOMMENDATION_LIST[i].getVideoID());
    		ChildframeRecommendations.JLABEL_LIST[i].setForeground(Color.BLUE.darker());
    		ChildframeRecommendations.JLABEL_LIST[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	}
    	

    	/* 
    	 * LIKE/DISLIKE BUTTONS
    	 */

    	// Group like/dislike buttons for video 1
    	final ButtonGroup likeDislikeButtonGroup1 = new ButtonGroup();
    	likeDislikeButtonGroup1.add(ChildframeRecommendations.JRB_LIKE1);
    	likeDislikeButtonGroup1.add(ChildframeRecommendations.JRB_DISLIKE1);
    	
    	// Group like/dislike buttons for video 2
    	final ButtonGroup likeDislikeButtonGroup2 = new ButtonGroup();
    	likeDislikeButtonGroup2.add(ChildframeRecommendations.JRB_LIKE2);
    	likeDislikeButtonGroup2.add(ChildframeRecommendations.JRB_DISLIKE2);
    	
    	// Group like/dislike buttons for video 3
    	final ButtonGroup likeDislikeButtonGroup3 = new ButtonGroup();
    	likeDislikeButtonGroup3.add(ChildframeRecommendations.JRB_LIKE3);
    	likeDislikeButtonGroup3.add(ChildframeRecommendations.JRB_DISLIKE3);
    	

    	/*
    	 * Create action listener for like/dislike buttons 
    	 * If a user clicks "like", then that video is later added to their favorites list
    	 * If a user clicks "dislike", the video is not later added to the favorites list.
    	 * User must click either "like" or "dislike" for each video before clicking "submit" 
    	 */
    	ActionListener buttonListener = new ActionListener() {
    		@Override 
    		public void actionPerformed(ActionEvent ae) {
    			JRadioButton button = (JRadioButton) ae.getSource();
    			
    			// Check if like button for video 1 is pressed 
    			if (button.equals(ChildframeRecommendations.JRB_LIKE1)) {
    				if (ChildframeRecommendations.JRB_LIKE1.getModel().isSelected()) {
    					ChildframeRecommendations.LIKE1 = Boolean.TRUE;
    				}
    			}
    			// Check if like button for video 2 is pressed  
    			if (button.equals(ChildframeRecommendations.JRB_LIKE2)) {
    				if (ChildframeRecommendations.JRB_LIKE2.getModel().isSelected()) {
    					ChildframeRecommendations.LIKE2 = Boolean.TRUE;
    				}
    			}
    			// Check if like button for video 3 is pressed 
    			if (button.equals(ChildframeRecommendations.JRB_LIKE3)) {
    				if (ChildframeRecommendations.JRB_LIKE3.getModel().isSelected()) {
    					ChildframeRecommendations.LIKE3 = Boolean.TRUE;
    				}
    			}
    			// Check if dislike button for video 1 is pressed 
    			if (button.equals(ChildframeRecommendations.JRB_DISLIKE1)) {
    				if (ChildframeRecommendations.JRB_DISLIKE1.getModel().isSelected()) {
    					ChildframeRecommendations.LIKE1 = Boolean.FALSE;
    				}
    			}
    			// Check if dislike button for video 2 is pressed  
    			if (button.equals(ChildframeRecommendations.JRB_DISLIKE2)) {
    				if (ChildframeRecommendations.JRB_DISLIKE2.getModel().isSelected()) {
    					ChildframeRecommendations.LIKE2 = Boolean.FALSE;
    				}
    			}
    			// Check if dislike button for video 3 is pressed 
    			if (button.equals(ChildframeRecommendations.JRB_DISLIKE3)) {
    				if (ChildframeRecommendations.JRB_DISLIKE3.getModel().isSelected()) {
    					ChildframeRecommendations.LIKE3 = Boolean.FALSE;
    				}
    			}
    		}
    	};
    	
    	// Add action listeners to all like/dislike buttons 
    	ChildframeRecommendations.JRB_LIKE1.addActionListener(buttonListener);
    	ChildframeRecommendations.JRB_LIKE2.addActionListener(buttonListener);
    	ChildframeRecommendations.JRB_LIKE3.addActionListener(buttonListener);
    	ChildframeRecommendations.JRB_DISLIKE1.addActionListener(buttonListener);
    	ChildframeRecommendations.JRB_DISLIKE2.addActionListener(buttonListener);
    	ChildframeRecommendations.JRB_DISLIKE3.addActionListener(buttonListener);
    	
    	/*
    	 * MOUSE LISTENERS
    	 * Used to interact with the hyper links (JLabels)
    	 */
    	
    	// Add mouse listener (used to click on hyperlink) for video 1
    	ChildframeRecommendations.JLABEL_LIST[0].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(
							new URI("https://www.youtube.com/watch?v=" 
									+ Mainframe.RECOMMENDATION_LIST[0].getVideoID())); // Video ID 
				} catch (Exception e1) {
					System.out.println("Sorry, this URL could not be opened.");
				} 
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				ChildframeRecommendations.JLABEL_LIST[0].setText("<html><a href=''>"
						+ "https://www.youtube.com/watch?v="
						+ Mainframe.RECOMMENDATION_LIST[0].getVideoID() // Video ID 
						+ "</a></html>");
			}
    	});
    	
    	// Add mouse listener (used to click on hyperlink) for video 2
    	ChildframeRecommendations.JLABEL_LIST[1].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(
							new URI("https://www.youtube.com/watch?v=" 
									+ Mainframe.RECOMMENDATION_LIST[1].getVideoID())); // Video ID
				} catch (Exception e1) {
					System.out.println("Sorry, this URL could not be opened.");
				} 
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				ChildframeRecommendations.JLABEL_LIST[1].setText("<html><a href=''>"
						+ "https://www.youtube.com/watch?v="
						+ Mainframe.RECOMMENDATION_LIST[1].getVideoID() // Video ID 
						+ "</a></html>");
			}
    	});
    	
    	// Add mouse listener (used to click on hyperlink) for video 3
    	ChildframeRecommendations.JLABEL_LIST[2].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(
							new URI("https://www.youtube.com/watch?v=" 
									+ Mainframe.RECOMMENDATION_LIST[2].getVideoID())); // Video ID
				} catch (Exception e1) {
					System.out.println("Sorry, this URL could not be opened.");
				} 
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				ChildframeRecommendations.JLABEL_LIST[2].setText("<html><a href=''>"
						+ "https://www.youtube.com/watch?v="
						+ Mainframe.RECOMMENDATION_LIST[2].getVideoID() // Video ID 
						+ "</a></html>");
			}
    	});
    	
    	// Add label (YouTube hyper link) and like/dislike buttons to panel 1 
    	ChildframeRecommendations.PANEL_VIDEO1.add(ChildframeRecommendations.JLABEL_LIST[0]);
    	ChildframeRecommendations.PANEL_VIDEO1.add(new JLabel(" "));
    	ChildframeRecommendations.PANEL_VIDEO1.add(ChildframeRecommendations.JRB_LIKE1);
    	ChildframeRecommendations.PANEL_VIDEO1.add(ChildframeRecommendations.JRB_DISLIKE1);
    	
    	// Add label (YouTube hyper link) and like/dislike buttons to panel 2
    	ChildframeRecommendations.PANEL_VIDEO2.add(ChildframeRecommendations.JLABEL_LIST[1]);
    	ChildframeRecommendations.PANEL_VIDEO2.add(new JLabel(" "));
    	ChildframeRecommendations.PANEL_VIDEO2.add(ChildframeRecommendations.JRB_LIKE2);
    	ChildframeRecommendations.PANEL_VIDEO2.add(ChildframeRecommendations.JRB_DISLIKE2);
    	
    	// Add label (YouTube hyper link) and like/dislike buttons to panel 3
    	ChildframeRecommendations.PANEL_VIDEO3.add(ChildframeRecommendations.JLABEL_LIST[2]);
    	ChildframeRecommendations.PANEL_VIDEO3.add(new JLabel(" "));
    	ChildframeRecommendations.PANEL_VIDEO3.add(ChildframeRecommendations.JRB_LIKE3);
    	ChildframeRecommendations.PANEL_VIDEO3.add(ChildframeRecommendations.JRB_DISLIKE3);
		
		
    	/*
    	 * Clicking this button brings you back to the first window 
    	 * This also clears the information from the JFrame, JPanels, and JRadioButtons
    	 */
	   	JButton backButton = new JButton("Go Back");
	   	backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
	   	backButton.addActionListener(new ActionListener() {
    		@Override
           	public void actionPerformed(ActionEvent e) {
    			
    			// Close the current childframe window and bring the mainframe back to front view
    			dispose();
    			Control.MFRAME.setVisible(true);
    			
    			// Clear the activity arraylist
    			Mainframe.ACTIVITY_ARRAY_LIST.clear();
    			
    			// Clear the information from the JPanels
    			ChildframeRecommendations.PANEL_VIDEO1.removeAll();
    			ChildframeRecommendations.PANEL_VIDEO2.removeAll();
    			ChildframeRecommendations.PANEL_VIDEO3.removeAll();
    			
    			// Clear the JRadio buttons
    			likeDislikeButtonGroup1.clearSelection();
    			likeDislikeButtonGroup2.clearSelection();
    			likeDislikeButtonGroup3.clearSelection();
    		}
    	});
    	
    	/*
    	 * Submit your video likes/dislikes and takes you to Favorites page
    	 */
    	JButton submitButton = new JButton("Submit");
    	submitButton.addActionListener(new ActionListener() {
    		@Override
           	public void actionPerformed(ActionEvent e) {
    			
    			// Must provide feedback on all videos in order to click "submit"
    			if ((ChildframeRecommendations.LIKE1 != null) 
    				&& (ChildframeRecommendations.LIKE2 != null) 
    				&& (ChildframeRecommendations.LIKE3 != null))  {
    				
    				// Adjust favorites list
    				if (ChildframeRecommendations.LIKE1) {
    					Mainframe.RECOMMENDATION_LIST[0].setLike(true);
    				}
    				if (ChildframeRecommendations.LIKE2) {
    					Mainframe.RECOMMENDATION_LIST[1].setLike(true);
    				}
    				if (ChildframeRecommendations.LIKE3) {
    					Mainframe.RECOMMENDATION_LIST[2].setLike(true);
    				}
    				
    				for (int i = 0; i < Mainframe.RECOMMENDATION_LIST.length; i++) {
    					if (Mainframe.RECOMMENDATION_LIST[i].getLike()) {
    						Mainframe.FAVORITES_LIST.add(Mainframe.RECOMMENDATION_LIST[i]);
    					}
    				}

    				// Clear information 
        			dispose();
        			Control.MFRAME.setVisible(false);
        			Mainframe.ACTIVITY_ARRAY_LIST.clear();
        			ChildframeRecommendations.PANEL_VIDEO1.removeAll();
        			ChildframeRecommendations.PANEL_VIDEO2.removeAll();
        			ChildframeRecommendations.PANEL_VIDEO3.removeAll();
        			
        			// De-select radio buttons 
        			likeDislikeButtonGroup1.clearSelection();
        			likeDislikeButtonGroup2.clearSelection();
        			likeDislikeButtonGroup3.clearSelection();
        			
        			// Reset Boolean variables to null 
        			// So user has to select like/dislike before they clicking "submit"
        			ChildframeRecommendations.LIKE1 = null;
        			ChildframeRecommendations.LIKE2 = null;
        			ChildframeRecommendations.LIKE3 = null;
        			
        			// Open favorites window
        			new ChildframeFavorites();
    			
    			} else {
    				JOptionPane.showMessageDialog(null, "Please provide feedback on all videos.");
    			}
           	}
    	});
    	
    	// Add buttons to panel of closing buttons 
    	panelClosingButtons.add(backButton);
    	panelClosingButtons.add(submitButton);
		
    	// Add all the panels to the JFrame, size the JFrame, and set the JFrame to visible.
		this.add(Box.createRigidArea(new Dimension(0,10)));
		this.add(panelIntro);
		
		this.add(ChildframeRecommendations.PANEL_VIDEO1);
		this.add(ChildframeRecommendations.PANEL_VIDEO2);
		this.add(ChildframeRecommendations.PANEL_VIDEO3);
		
		this.add(panelClosingButtons);
		this.pack();
		
		this.setVisible(true);
	}

}
