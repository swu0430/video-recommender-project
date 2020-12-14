package videorecs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChildframeFavorites extends JFrame {
	
	// STATIC VARIABLES
	
	/**
	 * List of activity categories associated with user's favorited videos.
	 */
	static ArrayList<String> FAVORITE_ACTIVITIES = new ArrayList<String>();

	// CONSTRUCTOR 
	
	/**
	 * 
	 */
	public ChildframeFavorites() {
		
		super("Your Favorites");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		Control.MFRAME.setVisible(false);
		
    	JPanel panelIntro = new JPanel();
    	JLabel labelIntro = new JLabel("Here is a list of all of videos you've liked!");
    	Font fontIntro = new Font(labelIntro.getFont().getName(), Font.ITALIC, labelIntro.getFont().getSize());    	
    	labelIntro.setFont(fontIntro);
    	panelIntro.add(labelIntro);
    	
    	JPanel panelVideos = new JPanel();
    	BoxLayout boxlayoutVideos = new BoxLayout(panelVideos, BoxLayout.Y_AXIS);
    	panelVideos.setLayout(boxlayoutVideos);
    	panelVideos.setAlignmentX(Component.RIGHT_ALIGNMENT);
    	
    	for (int i = 0; i < Mainframe.FAVORITES_LIST.size(); i++) {
    		if (!ChildframeFavorites.FAVORITE_ACTIVITIES.contains(Mainframe.FAVORITES_LIST.get(i).getActivity())) {
    			ChildframeFavorites.FAVORITE_ACTIVITIES.add(Mainframe.FAVORITES_LIST.get(i).getActivity());
    		}
    	}
    	
    	for (int j = 0; j < ChildframeFavorites.FAVORITE_ACTIVITIES.size(); j++ ) {
    		JPanel panelVideosSection = new JPanel();
    		BoxLayout boxlayout = new BoxLayout(panelVideosSection, BoxLayout.Y_AXIS);
    		panelVideosSection.setLayout(boxlayout);
    		panelVideosSection.setBorder(BorderFactory.createTitledBorder(ChildframeFavorites.FAVORITE_ACTIVITIES.get(j).toUpperCase()));
    	
        	for (int k = 0; k < Mainframe.FAVORITES_LIST.size(); k++) {
        		if (Mainframe.FAVORITES_LIST.get(k).getActivity().equals(ChildframeFavorites.FAVORITE_ACTIVITIES.get(j))) {
        			
        			JLabel titleVideo = new JLabel(Mainframe.FAVORITES_LIST.get(k).getTitle());
        			Font f1 = titleVideo.getFont();
        			titleVideo.setFont(f1.deriveFont(f1.getStyle() & ~Font.BOLD));
        			panelVideosSection.add(titleVideo);
        			
        			JLabel urlVideo = new JLabel("https://www.youtube.com/watch?v=" + Mainframe.FAVORITES_LIST.get(k).getTitle());
        			Font f2 = urlVideo.getFont();
        			urlVideo.setFont(f2.deriveFont(f2.getStyle() & ~Font.BOLD));
        			panelVideosSection.add(urlVideo);
 
        			panelVideosSection.add(new JLabel(" "));
        			
        		}
        	}
    		panelVideos.add(panelVideosSection);
        	panelVideos.add(Box.createRigidArea(new Dimension(0,20)));
        	
    	}
   
    	JPanel panelClosingButtons = new JPanel();
    	
	   	JButton okButton = new JButton("OK");
    	okButton.addActionListener(new ActionListener() {
    		@Override
           	public void actionPerformed(ActionEvent e) {
    			dispose();
    			Control.MFRAME.setVisible(true);
    		}
    	});

    	panelClosingButtons.add(okButton);
		
    	this.add(Box.createRigidArea(new Dimension(0,10)));
    	this.add(panelIntro, Component.CENTER_ALIGNMENT);
    	this.add(Box.createRigidArea(new Dimension(0,30)));
    	this.add(panelVideos);
    	this.add(Box.createRigidArea(new Dimension(0,30)));
    	this.add(panelClosingButtons);
    	this.add(Box.createRigidArea(new Dimension(0,20)));
    	this.pack();
    	this.setVisible(true);
		
	}

}
