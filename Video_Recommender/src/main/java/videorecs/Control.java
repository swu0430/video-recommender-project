package videorecs;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

import java.awt.BorderLayout;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.VideoListResponse;

/**
 * This class controls the interface with the user and contains the main method, which asks 
 * the user for input and outputs the final YouTube video recommendations. 
 * @author Alicia Yen and Steven Wu
 */
public class Control {

	// STATIC VARIABLES
	
	/**
	 * Required string for calling the YouTube "Search: list" API
	 */
    private static final String APPLICATION_NAME = "API code samples";
    
    /**
     * Variable used for pulling the JSON output from the YouTube "Search: list" API
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    
    /**
     * Developer Key to be used for YouTube API client service authorization.
     */
	private static final String DEVELOPER_KEY = ApiDevKey.DEV_KEY;

    /**
     * Number of videos the program will recommend to the user.
     */
    private static final int NUMBER_VIDEOS = 5;
    
    /**
     * Minimum approval rating required for a recommended video
     */
    private static final double MIN_RATING = 0.80;
    
    /**
     * Minimum number of likes required for a recommended video 
     */
    private static final int MIN_LIKES = 5000;
    
    /**
     * Minimum number of views required for a recommended video
     */
    private static final int MIN_VIEWS = 15000;
	
    /**
     * Build and return an authorized API client service.
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
            .setApplicationName(APPLICATION_NAME)
            .build();
    }
    
    /**
     * Helper method to check if a potential recommended video meets the keyword criteria by 
     * searching for keywords in video (1) titles (2) categories (3) descriptions
     * @param videoTitle of given video 
     * @param videoDescription of given video
     * @return true if a keyword match is found, otherwise return false
     */
    private static boolean videoMeetsKeywordCriteria(String videoTitle, String  videoDescription) {
    	
    	// KEYWORDS: "how to", "tutorial", "demo", "tips", "beginner", "intermediate", "advanced", "learn",
        // "easy", "hard", "great for", "try", "education", "like a pro", "like a boss", "hack", etc.
        String[] searchKeywords = {"how to", "tutorial", "demo", "tips", "beginner", "intermediate", 
        		"advanced", "learn", "easy", "hard", "great for", "try", "education", 
        		"like a pro", "like a boss", "hack"};
        
        for (String keyword : searchKeywords) {
        	if ((videoTitle.toLowerCase().contains(keyword)) || 
        		(videoDescription.toLowerCase().contains(keyword))) {
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
    private static boolean videoMeetsRatingCriteria(double videoRating) {
    	
        if (videoRating > MIN_RATING) {
        	return true;
        }
        return false;
    }
    
    /**
     * Helper method to check if a potential recommended video meets the minimum like count criteria
     * @param numOfLikes of given video 
     * @return true if video's like count > 5000, otherwise return false
     */
    private static boolean videoMeetsLikeCountCriteria(BigInteger numOfLikes) {
    	
    	try {
            if (numOfLikes.doubleValue() > MIN_LIKES) {
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
    private static boolean videoMeetsViewCountCriteria(BigInteger viewCount) {
    	
    	try {
            if (viewCount.doubleValue() > MIN_VIEWS) {
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
    private static double calculateVideoRating(BigInteger numOfLikes, BigInteger numOfDislikes) {
    	
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
    private static String parseVideoDurationHelper(String duration) {
    	
    	String regex = "";
    	// Duration contains hours, minutes, and seconds information
    	if (duration.contains("H")) {
    		regex = "PT([0-9]+)H([0-9]+)M([0-9]+)S";
    		
    	// Duration contains minutes and seconds information
    	} else if ((!duration.contains("H")) && (duration.contains("M"))) {
    		regex = "PT([0-9]+)M([0-9]+)S";
    	
    	// Duration only contains seconds information
    	} else if ((!duration.contains("H")) && (!duration.contains("M"))) {
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
    private static int parseVideoDuration(String regex, String duration) {
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
			
			// Get number of capturing groups
			int groupCount = m.groupCount();
			
			// Duration includes hours, minutes, and seconds information
			if (groupCount == 3) {
				hours = m.group(1);
				minutes = m.group(2);
				seconds = m.group(3);
				
			// Duration includes minutes and seconds information only 
			} else if (groupCount == 2) {
				minutes = m.group(1);
				seconds = m.group(2); 
				
			// Duration includes seconds information only
			} else if (groupCount == 1) {
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
    private static boolean videoMeetsDurationCriteria(char durationCriteria, int videoDurationSeconds) {
    	
    	// Check if video duration is < 5 minutes (under 300 seconds)
    	if ((durationCriteria == 'a') || (durationCriteria == 'A')) {
    		if (videoDurationSeconds < 300) {
    			return true;
    		} else {
    			return false;
    		}
    	}
    	
    	// Check if video duration is between 5 and 15 minutes (between 300 and 900 seconds)
    	if ((durationCriteria == 'b') || (durationCriteria == 'B')) {
    		if ((300 <= videoDurationSeconds) &&  (videoDurationSeconds < 900)) {
    			return true;
    		} else {
    			return false;
    		}
    	}
    	
    	// Check if video duration is > 15 minutes (greater than 900 seconds)
    	if ((durationCriteria == 'c') || (durationCriteria == 'C')) {
    		if (900 < videoDurationSeconds) {
    			return true;
    		} else {
    			return false;
    		}
    	}
    	
    	// User is fine with any duration, always return true
    	if ((durationCriteria == 'd') || (durationCriteria == 'D')) {
    		return true;
    	}
    	return false;
    }
	
    /**
     * Call function to create API service object. Define and execute API request. 
     * Print API response.
     * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
     */
    public static void main(String[] args)
        throws GeneralSecurityException, IOException, GoogleJsonResponseException {
        
    	// Open scanner
    	Scanner scanner = new Scanner(System.in);
    	
    	// Build and return an authorized API client service.
    	YouTube youtubeService = getService();
        
    	// Create a new ApiCall object
    	ApiCalls apiCalls = new ApiCalls();
    	
		// Create a while loop to keep the program running until the user wants to stop searching.
		boolean programRunning = true;
		while (programRunning) {
    	
	    	// Print welcome message.
	        System.out.println("--------------");
	        System.out.println("NEW SEARCH");
	        System.out.println("--------------");
	    	System.out.println("Hi there! I'm going to dig up 5 neat educational videos for you! I just need a few pieces of information...");
	    	System.out.println();
	    	
	    	// ***Gather user and default inputs for YouTube "Search: list" API video search*** 
	  
	    	// part --> set this ArrayList to include "snippet"
	    	ArrayList<String> partSearchList = new ArrayList<String>();
	    	partSearchList.add("snippet");
	    	
	    	// number of search results per page (max is 50, but results may get funky that high)
	    	long maxResults = 10L;
	    	
	    	// token for next page of searches if needed (set initial value to null)
	    	String pageToken = null;
	    	
	    	// type of search --> set to "video"
	    	ArrayList<String> type = new ArrayList<String>();
	    	type.add("video");
	    	
	    	// activities for search query --> store the activities in in ArrayList
	    	boolean askForActivities = true;
	    	ArrayList<String> activityArrayList = new ArrayList<String>();
	    	
	    	while (askForActivities) {
		    	// search query --> ask user for a list of activities
		    	System.out.print("Enter a list of activities you do regularly, separated by commas (e.g., \"cooking, dancing, golf,\"): ");
		    	String query = scanner.nextLine();
		    	
		    	try {
		        	// Convert the user's input into a String array by splitting at each comma.
		    		String[] activityArray = query.trim().split("\\s*,\\s*");
		    		
		    		// Iterate over the array and add the activities to the ArrayList
		    		for (String activity : activityArray) {
		    			activityArrayList.add(activity);
		    			//System.out.println(activity); //FOR TESTING ONLY
		    		}
	
		    		// If a valid response was entered, stop asking for activities.
			        askForActivities = false;
		        	
		    	} catch (Exception e) {
		    		System.out.println("Invalid response. Please try again.");
		    	}
	    	}
	 	
	    	// Ask user for preferred duration of video
	    	
	    	char duration = 'z'; // Initialize duration with a dummy variable (so it is in scope)
	    	boolean askForDuration = true;
	    	while (askForDuration) {
	    		System.out.println("What is your preferred video length? (a) <5 min (b) 5-15 min (c) >15 min (d) any");
	    		System.out.print("Enter a/b/c/d: ");
	    		String durationResponse = scanner.nextLine();
	    		System.out.println();
	    		
		        try {
		        	duration = durationResponse.charAt(0);
				        if ((duration == 'a') || (duration == 'A') || (duration == 'b') || (duration == 'B') || 
				        	(duration == 'c') || (duration == 'C') || (duration == 'd') || (duration == 'D')) {
				        	askForDuration = false;
				        }
		        } catch (Exception e) {
		        	System.out.println("Invalid response. Please try again.");
		        }
	    	}
	    	System.out.println("I'm searching for relevant videos for you now!");
	    	System.out.println();
	
	    	// Initialize an array of recommended videos. Set up a while loop to keep searching for YouTube videos
	    	// until 5 videos that meet all the criteria are found.
	    	Recommendation[] recommendationList = new Recommendation[NUMBER_VIDEOS];
	    	int counter_videos = 0;

	    	while (counter_videos < NUMBER_VIDEOS) {

		    	// Pick a random activity from the user's list. 
				Random random = new Random();
				int index = random.nextInt(activityArrayList.size());
				String activity = activityArrayList.get(index);
			
		    	// Call the YouTube "Search: list" API to search for a set of videos based on criteria above
		    	SearchListResponse videoSearchResults = apiCalls.videoSearches(youtubeService, DEVELOPER_KEY, 
		    		partSearchList, maxResults, pageToken, activity, type);
		    	
//		    	 System.out.println(videoSearchResults); // ***FOR TESTING PURPOSES ONLY***
		    	
		    	// Set the next page token for another search, if required
		    	pageToken = videoSearchResults.getNextPageToken();
		    	
		    	// Extract list of video search result items from the "Search: list" API JSON.
		    	List<SearchResult> items = videoSearchResults.getItems();
		    	
		    	// Iterate over the items to store all the video ID's in an ArrayList.
		    	ArrayList<String> videoIDs = new ArrayList<String>();
		    	for (SearchResult sr : items) {
		    		videoIDs.add(sr.getId().getVideoId());
		    	}
		    	
//		    	for (String s : videoIDs) {
//		    		System.out.println(s); //***FOR TESTING PURPOSES ONLY***
//		    	}
		    	
	    		// Set up the "Videos: list" API
		        
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
			        VideoListResponse videoDetails = apiCalls.videoDetails(youtubeService, DEVELOPER_KEY, 
			        		partVideosList, singleVideoID);
			        
//			        System.out.println(videoDetails); //***FOR TESTING PURPOSES ONLY***
			        
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
			        durationCriteria = videoMeetsDurationCriteria(duration, videoDurationSeconds);
			        
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
			        	if (counter_videos < NUMBER_VIDEOS) {
			        		recommendationList[counter_videos] = new Recommendation(videoTitle, videoDescription, videoID);
			        	} else if (counter_videos >= NUMBER_VIDEOS) {
			        		// Stop searching for videos to recommend if you have > NUMBER_VIDEOS recommended videos
			        		break;
			        	}
				        counter_videos++;
			        }
		    	} // end of inner for loop
	    	} // end of outer while loop 

	    	// Create ArrayList to store favorite user videos
	    	ArrayList<Recommendation> favoritesList = new ArrayList<Recommendation>();
    		String recommendedTitle;
    		String recommendedDescription;
    		String recommendedVideoID;
	    	
    		// Ask for user feedback on each video. If the user likes a video, store it in the user's favorites. 
	    		
    		// Iterate over videos in recommendationList
	    	for (Recommendation recommendedVideo : recommendationList) {
	    		
	    		// Get relevant video information 
	    		recommendedTitle = recommendedVideo.getTitle();
	    		recommendedDescription = recommendedVideo.getDescription();
	    		recommendedVideoID = recommendedVideo.getvideoID();
	    		
	    		// Display video title and video URL 
	    		System.out.println("Here's a video for you: ");
	    		System.out.println(recommendedTitle);
	    		System.out.println("https://www.youtube.com/watch?v="+ recommendedVideoID);
	    		System.out.println();
	    		
	    		// Ask user if they liked the recommended video 
	    		System.out.print("Did you like this video? (y/n) ");
	    		String likedVideoPrompt = scanner.nextLine();
	    		
	    		try {
		        	char likedVideoAnswer = likedVideoPrompt.charAt(0);
		        	
		        		// Store video in the user's favorites if they liked the video 
				        if ((likedVideoAnswer == 'y') || (likedVideoAnswer == 'Y')) {
				        	System.out.println("Great! I'll add it to your favorites list.");
				        	System.out.println();
				        	
				        	// Create a new recommendation from the liked video 
				        	Recommendation favoritedVideo = new Recommendation(recommendedTitle, 
				        			recommendedDescription, recommendedVideoID);
				        	
				        	// Set like status to true 
				        	favoritedVideo.setLike(true);
				        	
				        	// Add video to user's favorite's list 
				        	favoritesList.add(favoritedVideo);
				        	
				        } else if ((likedVideoAnswer == 'n') || (likedVideoAnswer == 'N')) {
				        	System.out.println("Thanks for your feedback.");
				        	System.out.println();
				        }
		        } catch (Exception e) {
		        	System.out.println("Invalid response. Please try again.");
		        	System.out.println();
		        }
	    	}
	    	
	        // Create a while loop to keep asking the user if he or she wants to keep searching for videos until a valid 
		    // response is received.
	        boolean keepAsking = true;
		    
	        while (keepAsking) {
	        	
			    System.out.print("I hope you found those videos helpful! Want me to dig up some more for you? (y/n) ");
		        String prompt = scanner.nextLine();
		        
		        // If the user enters a response to begins with "y" or "Y," start a new search round.
		        // If the user enters a response to begins with "n" or "N," exit the program.
		        try {
		        	char firstLetter = prompt.charAt(0);
				        if ((firstLetter == 'y') || (firstLetter == 'Y')) {
				        	programRunning = true;
				        	keepAsking = false;
				        } else if ((firstLetter == 'n') || (firstLetter == 'N')) {
				        	System.out.println("Goodbye!");
				        	programRunning = false;
				        	keepAsking = false;
				        }
		        } catch (Exception e) {
		        	System.out.println("Invalid response. Please try again.");
		        	keepAsking = true;
		        }
	        }
	        System.out.println();
		}
		
//		//SWING TEST
//	    NativeInterface.open();
//	    SwingUtilities.invokeLater(new Runnable() {
//	    	public void run() {
//	    		JFrame frame = new JFrame("YouTube Viewer");
//	    		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	    		frame.getContentPane().add(getBrowserPanel(), BorderLayout.CENTER);
//	    		frame.setSize(800, 600);
//	    		frame.setLocationByPlatform(true);
//	    		frame.setVisible(true);
//	    	}
//	    });
//	    
//	    NativeInterface.runEventPump();
//	    
//	    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//	    	@Override
//	    	public void run() {
//	    		NativeInterface.close(); //closing NativeInterface
//	    	}
//	    }));

        //Close the scanner.
        scanner.close();
    }

    /**
     * This method return the panel with our video.
     * @return
     */
    public static JPanel getBrowserPanel() {
        JPanel webBrowserPanel = new JPanel(new BorderLayout());
        JWebBrowser webBrowser = new JWebBrowser();
        webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
        webBrowser.setBarsVisible(false);
        webBrowser.navigate("https://www.youtube.com/watch?v=GKiHB5AzihE");
        return webBrowserPanel;
    }
}
