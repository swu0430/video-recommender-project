package videorecs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.swing.JTextField;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.VideoListResponse;

public class Mainframe extends JFrame {

	// STATIC VARIABLES
	
	/**
	 * List of recommended videos.
	 */
	static Recommendation[] RECOMMENDATION_LIST = new Recommendation[Control.NUMBER_VIDEOS];
	
	/**
	 * List of user's favorited videos.
	 */
	static ArrayList<Recommendation> FAVORITES_LIST = new ArrayList<Recommendation>();
	
	/**
	 * List of already seen videoID's.
	 */
	static ArrayList<String> SEEN_LIST = new ArrayList<String>();
	
	static JTextField ACTIVITIES_TEXT_FIELD = new JTextField();
	static ArrayList<String> ACTIVITY_ARRAY_LIST = new ArrayList<String>();
	static String DURATION;
	static JRadioButton JRB_DURATION_SHORT = new JRadioButton("<5 min");
	static JRadioButton JRB_DURATION_MEDIUM = new JRadioButton("5-15 min");
	static JRadioButton JRB_DURATION_LONG = new JRadioButton(">15 min");
	static JRadioButton JRB_DURATION_ANY = new JRadioButton("Any");
	
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public Mainframe() {
		
		super("Welcome!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
    	JPanel panelIntro = new JPanel();
    	JLabel labelIntro = new JLabel("Hi there! I'm going to dig up 5 neat educational videos for you. I just need a few pieces of information...");
    	Font fontIntro = new Font(labelIntro.getFont().getName(), Font.ITALIC, labelIntro.getFont().getSize());    	
    	labelIntro.setFont(fontIntro);
    	panelIntro.add(labelIntro);
    	
    	JPanel panelActivities = new JPanel();
    	BoxLayout boxlayout = new BoxLayout(panelActivities, BoxLayout.Y_AXIS);
    	panelActivities.setLayout(boxlayout);
    	panelActivities.setBorder(BorderFactory.createTitledBorder("ACTIVITIES"));
    	panelActivities.add(new Label("Enter a list of activities you do regularly, separated by commas (e.g., \"cooking, dancing, golf\"): "));
    	panelActivities.add(Mainframe.ACTIVITIES_TEXT_FIELD);
    	
    	JPanel panelDuration = new JPanel();
    	panelDuration.setBorder(BorderFactory.createTitledBorder("VIDEO DURATION PREFERENCE"));

        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton buttonDuration = (JRadioButton) e.getSource();
                Mainframe.DURATION = buttonDuration.getText();  
            }
        };
    	
        Mainframe.JRB_DURATION_SHORT.addActionListener(buttonListener);
        Mainframe.JRB_DURATION_MEDIUM.addActionListener(buttonListener);
        Mainframe.JRB_DURATION_LONG.addActionListener(buttonListener);
        Mainframe.JRB_DURATION_ANY.addActionListener(buttonListener);
        
    	ButtonGroup durationButtonGroup = new ButtonGroup();
    	durationButtonGroup.add(Mainframe.JRB_DURATION_SHORT);
    	durationButtonGroup.add(Mainframe.JRB_DURATION_MEDIUM);
    	durationButtonGroup.add(Mainframe.JRB_DURATION_LONG);
    	durationButtonGroup.add(Mainframe.JRB_DURATION_ANY);
    	
        panelDuration.add(Mainframe.JRB_DURATION_SHORT);
    	panelDuration.add(Mainframe.JRB_DURATION_MEDIUM);
    	panelDuration.add(Mainframe.JRB_DURATION_LONG);
    	panelDuration.add(Mainframe.JRB_DURATION_ANY);
    	panelDuration.setAlignmentX(Component.RIGHT_ALIGNMENT);
    	
    	JPanel panelClosingButtons = new JPanel();
    	
    	JButton submitButton = new JButton("Submit");
    	submitButton.addActionListener(new ActionListener() {
    		@Override
           	public void actionPerformed(ActionEvent e) {

	    		//Convert the user's input into a String array by splitting at each comma.
	    		String[] activityArray = Mainframe.ACTIVITIES_TEXT_FIELD.getText().trim().split("\\s*,\\s*");
	    		
	    		// Iterate over the array and add the activities to the ArrayList
	    		for (String activity : activityArray) {
	    			Mainframe.ACTIVITY_ARRAY_LIST.add(activity.toLowerCase());
	    		}

				if ((!Mainframe.ACTIVITY_ARRAY_LIST.get(0).trim().equals("")) && (Mainframe.DURATION != null)) {
	
					try {
						createRecommendedVideosList();
					} catch (GoogleJsonResponseException e1) {
						e1.printStackTrace();
					} catch (GeneralSecurityException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					new ChildframeRecommendations();
			   
				} else {
					JOptionPane.showMessageDialog(null, "Invalid entry. Please try again.");
					Mainframe.ACTIVITY_ARRAY_LIST.clear();
				}	
           	}
    	});
    	
    	JButton favoritesButton = new JButton("Favorites");
    	favoritesButton.addActionListener(new ActionListener() {
    		@Override
           	public void actionPerformed(ActionEvent e) {
    			Control.MFRAME.setVisible(false);
    			new ChildframeFavorites();
    		}
    	});    	
    	
    	JButton exitButton = new JButton("Exit");
    	exitButton.addActionListener(new ActionListener() {
    		@Override
           	public void actionPerformed(ActionEvent e) {
    			dispose();
    		}
    	});
    	
    	panelClosingButtons.add(submitButton);
    	panelClosingButtons.add(favoritesButton);
    	panelClosingButtons.add(exitButton);
    
    	this.add(Box.createRigidArea(new Dimension(0,10)));
    	this.add(panelIntro);
    	this.add(Box.createRigidArea(new Dimension(0,30)));
    	this.add(panelActivities);
    	this.add(Box.createRigidArea(new Dimension(0,10)));
    	this.add(panelDuration);
    	this.add(Box.createRigidArea(new Dimension(0,30)));
    	this.add(panelClosingButtons);
    	this.add(Box.createRigidArea(new Dimension(0,20)));
    	this.pack();
    	this.setVisible(true);
	}

	
	/**
	 * 
	 * @param activityArrayList
	 * @param duration
	 * @return
	 */
	static void createRecommendedVideosList() 
		throws GeneralSecurityException, IOException, GoogleJsonResponseException {
		
    	// Build and return an authorized API client service.
    	YouTube youtubeService = Control.getService();
		
    	// Create a new ApiCall object
    	ApiCalls apiCalls = new ApiCalls();
		
		// Set up the YouTube "Search: list" API call to search for videos
		  
    	// part --> set this ArrayList to include "snippet"
    	ArrayList<String> partSearchList = new ArrayList<String>();
    	partSearchList.add("snippet");
    	
    	// number of search results per page (max is 50, but results may get weird that high)
    	long maxResults = 10L;
    	
    	// token for next page of searches if needed (set initial value to null)
    	String pageToken = null;
    	
    	// type of search --> set to "video"
    	ArrayList<String> type = new ArrayList<String>();
    	type.add("video");
    	
    	// Set up a while loop to keep searching for YouTube videos until 3 videos that meet all the criteria 
    	// are found.
    	int counter_videos = 0;
    	
    	while (counter_videos < Control.NUMBER_VIDEOS) {

	    	// Pick a random activity from the user's list. 
			Random random = new Random();
			int index = random.nextInt(Mainframe.ACTIVITY_ARRAY_LIST.size());
			String activity = Mainframe.ACTIVITY_ARRAY_LIST.get(index);
		
	    	// Call the YouTube "Search: list" API to search for a set of videos based on criteria above
	    	SearchListResponse videoSearchResults = apiCalls.videoSearches(youtubeService, Control.DEVELOPER_KEY, 
	    		partSearchList, maxResults, pageToken, activity, type);
	    	
//	    	//***TESTING ONLY***
//	    	System.out.println(videoSearchResults);
//	    	System.out.println();
//	    	System.out.println();
//	    	//***TESTING ONLY***
	    	
	    	// Set the next page token for another search, if required
	    	pageToken = videoSearchResults.getNextPageToken();
	    	
	    	// Extract list of video search result items from the "Search: list" API JSON.
	    	List<SearchResult> items = videoSearchResults.getItems();
	    	
	    	// Iterate over the items to store all the video ID's in an ArrayList.
	    	ArrayList<String> videoIDs = new ArrayList<String>();
	    	for (SearchResult sr : items) {
	    		videoIDs.add(sr.getId().getVideoId());
	    	}
	    	
	    	// Set up the "Videos: list" API call to get more details on a video
	    	
	    	// part --> set this ArrayList to include "contentDetails" and "statistics"
	    	ArrayList<String> partVideosList = new ArrayList<String>();
	    	partVideosList.add("contentDetails");
	    	partVideosList.add("statistics");
	    	partVideosList.add("snippet");
	    	
	    	// Initialize variables to store relevant video information
	    	ArrayList<String> singleVideoID;
	        String videoDuration;
	        String videoTitle;
	        String videoDescription;
	        BigInteger numOfLikes;
	        BigInteger numOfDislikes;
	        double videoRating;
	        BigInteger viewCount;
	        
	        // Iterate over the videoIDs ArrayList to get more details on each video using the "Videos: list" API.
	        for (String videoID : videoIDs) {
	        	
	        	// Create ArrayList to store single video ID to pass to the "Videos: list" API 
	    		singleVideoID = new ArrayList<String>();
	    		singleVideoID.add(videoID);
	    		
		        // Call the YouTube "Videos: list" API to filter video searches by further criteria and pick out
		        // final recommendations for the user
		        VideoListResponse videoDetails = apiCalls.videoDetails(youtubeService, Control.DEVELOPER_KEY, 
		        		partVideosList, singleVideoID);
		        
//		        //***TESTING ONLY***
//		        System.out.println(videoDetails);
//		        System.out.println();
//		        //***TESTING ONLY***
		        
		        // Initialize boolean variables to false to search for videos that meet our criteria
		        boolean durationCriteria = false;  
		        boolean keywordCriteria = false;
		        boolean ratingCriteria = false;
		        boolean viewCountCriteria = false; 
		        boolean likeCountCriteria = false;
		        
		        // Extract relevant information for a single video from the videoIDs ArrayList
		        videoDuration = videoDetails.getItems().get(0).getContentDetails().getDuration();
		        videoTitle = videoDetails.getItems().get(0).getSnippet().getTitle();
		        videoDescription = videoDetails.getItems().get(0).getSnippet().getDescription();
		        numOfLikes = videoDetails.getItems().get(0).getStatistics().getLikeCount();
		        numOfDislikes = videoDetails.getItems().get(0).getStatistics().getDislikeCount();
		        viewCount = videoDetails.getItems().get(0).getStatistics().getViewCount();
		        
		        // Convert video duration to seconds 
		        String regexToUse = parseVideoDurationHelper(videoDuration); // Use helper method to parse duration
		        int videoDurationSeconds = parseVideoDuration(regexToUse, videoDuration); // Get video duration in seconds 
		        
		        // Check if video meets user-specified duration criteria
		        durationCriteria = videoMeetsDurationCriteria(Mainframe.DURATION, videoDurationSeconds);
		        
		        // Check if video meets keyword criteria 
		        keywordCriteria = videoMeetsKeywordCriteria(videoTitle, videoDescription);
		        
		        // Check if video meets video rating criteria (> 80%) 
		        videoRating = calculateVideoRating(numOfLikes, numOfDislikes);
		        ratingCriteria = videoMeetsRatingCriteria(videoRating);
		        
		        // Check if video meets minimum like count criteria (> 5k) 
		        likeCountCriteria = videoMeetsLikeCountCriteria(numOfLikes);
		        
		        // Check if video meets minimum view count criteria (> 15k) 
		        viewCountCriteria = videoMeetsViewCountCriteria(viewCount);
		        
		        // If the video meets all the criteria, create a new recommended video and increase counter_videos.	
		        if (durationCriteria && keywordCriteria && ratingCriteria &&
		        	viewCountCriteria && likeCountCriteria) {
		        	
			        	// Only recommend up to NUMBER_VIDEOS to the user 
		        	if ((counter_videos < Control.NUMBER_VIDEOS) && (!Mainframe.SEEN_LIST.contains(videoID))) {
		        		Mainframe.RECOMMENDATION_LIST[counter_videos] = new Recommendation(videoTitle, videoDescription, videoID, activity);
		        		Mainframe.SEEN_LIST.add(videoID);
		        		counter_videos++;
		        	} else if (counter_videos >= Control.NUMBER_VIDEOS) {
		        		// Stop searching for videos to recommend if you have > NUMBER_VIDEOS recommended videos
		        		break;
		        	}
		        }
	    	} // end of inner for loop
    	} // end of outer while loop 
	
//    	//***TESTING ONLY***
//        System.out.println("Test: Recommendation List");
//    	for (int x = 0; x < Mainframe.RECOMMENDATION_LIST.length; x++) {
//        	System.out.println(Mainframe.RECOMMENDATION_LIST[x].getVideoID());
//        }
//        System.out.println();
//        System.out.println("Test: Seen List");
//        for (int y = 0; y < Mainframe.SEEN_LIST.size(); y++) {
//        	System.out.println(Mainframe.SEEN_LIST.get(y)); 
//        }
//        System.out.println(); 
//        //***TESTING ONLY***
  
	}

    /**
     * Helper method to check if a potential recommended video meets the keyword criteria by 
     * searching for keywords in video (1) titles (2) descriptions
     * @param videoTitle of given video 
     * @param videoDescription of given video
     * @return true if a keyword match is found, otherwise return false
     */
    static boolean videoMeetsKeywordCriteria(String videoTitle, String  videoDescription) {
    	
    	// KEYWORDS: "how to", "tutorial", "demo", "tips on", "beginner", "intermediate", "advanced", "learn",
        // "easy", "hard", "great for", "education", "like a pro", "like a boss", "hack", etc.
        String[] searchKeywords = {"how to", "tutorial", "demo", "beginner", "intermediate", "advanced", "learn",
        	"tips on", "easy", "hard", "great for", "education", "like a pro", "like a boss", "hack"};
        
        for (String keyword : searchKeywords) {
        	
    		String regex = "\\b" + keyword + "\\b";
        	Pattern p = Pattern.compile(regex);
    		Matcher mVideoTitle = p.matcher(videoTitle.toLowerCase());
    		Matcher mVideoDescription = p.matcher(videoDescription.toLowerCase());
        	
        	if (mVideoTitle.find() || mVideoDescription.find()) {
        		return true;
        	}
        }
		return false;
    }
    
    /**
     * Helper method to check if a potential recommended video meets the approval rating criteria 
     * @param videoRating of given video 
     * @return true if video's approval rating > 80%, otherwise return false
     */
    static boolean videoMeetsRatingCriteria(double videoRating) {
    	
        if (videoRating > Control.MIN_RATING) {
        	return true;
        }
        return false;
    }
    
    /**
     * Helper method to check if a potential recommended video meets the minimum like count criteria
     * @param numOfLikes of given video 
     * @return true if video's like count > 5000, otherwise return false
     */
    static boolean videoMeetsLikeCountCriteria(BigInteger numOfLikes) {
    	
    	try {
            if (numOfLikes.doubleValue() > Control.MIN_LIKES) {
            	return true;
            }
    	} catch (Exception e) {
    		return false;
    	}
    	return false;
    }
    
    /**
     * Helper method to check if a potential recommended video meets the minimum view count criteria
     * @param viewCount of given video 
     * @return true if video's view count > 15000, otherwise return false
     */
    static boolean videoMeetsViewCountCriteria(BigInteger viewCount) {
    	
    	try {
            if (viewCount.doubleValue() > Control.MIN_VIEWS) {
            	return true;
            }
    	} catch (Exception e) {
    		return false;
    	}
    	return false;
    }
    
    /**
     * Calculate video approval rating = # LIKES / (# LIKES + # DISLIKES)
     * @param numOfLikes of given video
     * @param numOfDislikes of given video
     * @return rating of video as a decimal number (0.00 if rating cannot be calculated)
     */
    static double calculateVideoRating(BigInteger numOfLikes, BigInteger numOfDislikes) {
    	
    	try {
    		return (numOfLikes.doubleValue() / (numOfLikes.doubleValue() + numOfDislikes.doubleValue()));
    	} catch (Exception e) {
    		return 0.00;
    	}
    }
    
    /**
     * Helper method to help parse video duration
     * @param duration of video to parse
     * @return regex to use for parseVideoDuration
     */
    static String parseVideoDurationHelper(String duration) {
    	
    	String regex = "";
    	// Duration contains hours, minutes, and seconds information
    	if (duration.contains("H")) {
    		regex = "PT([0-9]+)H([0-9]*)M*([0-9]*)S*";
    		
    	// Duration contains minutes and seconds information
    	} else if (duration.contains("M")) {
    		regex = "PT([0-9]+)M([0-9]*)S*";
    	
    	// Duration only contains seconds information
    	} else if (duration.contains("S")) {
    		regex = "PT([0-9]+)S";
    	}
    	
		return regex;
    }
    
    /**
     * Parses video duration information by converting duration to seconds 
     * Uses helper methods: parseVideoDurationHelper 
     * @param regex to use for parsing
     * @param duration of given video
     * @return duration of video in seconds (returns 0 if duration cannot be parsed to seconds)
     */
    static int parseVideoDuration(String regex, String duration) {
    	// PT1H23M45S - 1 hour(s), 23 minute(s), 45 second(s)
    	// PT3M20S - 0 hour(s), 3 minute(s), 20 second(s)
    	// PT1M13S - 0 hour(s), 1 minute(s), 13 second(s)
    	// PT48S - 0 hour(s), 0 minute(s), 48 second(s)
    	
    	Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(duration);
		
		// Initialize hours, minutes, and seconds to "0"
		String hours = "0";
		String minutes = "0";
		String seconds = "0";
		int totalSeconds = 0;
		
		while (m.find()) {
			
			// Match the hours, minutes, and seconds with the groups in the regex matcher
			if (duration.contains("H")) {
				hours = m.group(1);
				if (duration.contains("M")) {
					minutes = m.group(2);
					if (duration.contains("S")) {
						seconds = m.group(3);
					}
				} else {
					if (duration.contains("S")) {
						seconds = m.group(2);
					}
				}
			} else if (duration.contains("M")) {
				minutes = m.group(1);
				if (duration.contains("S")) {
					seconds = m.group(2);
				}
			} else if (duration.contains("S")) {
				seconds = m.group(1);
			}
		}
		
		// Convert hours, minutes, and seconds (currently in String format) to integers
		// Converts total duration to seconds 
		try {
			int hoursInt = Integer.parseInt(hours); // Convert hour to integer
			int hoursSecs = hoursInt * 60 * 60; // Convert hour to seconds 
			
			int minutesInt = Integer.parseInt(minutes); // Convert minute to integer
			int minutesSecs = minutesInt * 60; // Convert minute to seconds 
			
			int secondsInt = Integer.parseInt(seconds); 
			
			totalSeconds = hoursSecs + minutesSecs + secondsInt;
		} catch (Exception e) {
			System.out.println("Invalid value cannot be parsed to an integer.");
		}
		
		return totalSeconds;
    }
    
    /**
     * Checks whether video meets duration criteria specified by user 
     * @param durationCriteria: (a) <5 min (b) 5-15 min (c) >15 min (d) any
     * @param videoDurationSeconds total duration of given video, given in seconds
     * @return true if video meets duration criteria, false otherwise
     */
    static boolean videoMeetsDurationCriteria(String durationCriteria, int videoDurationSeconds) {
    	
    	// Check if video duration is < 5 minutes (under 300 seconds)
    	if (durationCriteria.equals("<5 min")) {
    		if ((0 < videoDurationSeconds) && (videoDurationSeconds < 300)) {
    			return true;
    		} else {
    			return false;
    		}
    	}
    	
    	// Check if video duration is between 5 and 15 minutes (between 300 and 900 seconds)
    	if (durationCriteria.equals("5-15 min")) {
    		if ((300 <= videoDurationSeconds) && (videoDurationSeconds <= 900)) {
    			return true;
    		} else {
    			return false;
    		}
    	}
    	
    	// Check if video duration is > 15 minutes (greater than 900 seconds)
    	if (durationCriteria.equals(">15 min")) {
    		if (900 < videoDurationSeconds) {
    			return true;
    		} else {
    			return false;
    		}
    	}
    	
    	// User is fine with any duration, always return true
    	if (durationCriteria.equals("Any")) {
    		return true;
    	}
    	return false;
    }	
}
