package videorecs;

import java.util.Scanner;

import java.io.IOException;
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
	private static String DEVELOPER_KEY;

    /**
     * Number of videos the program will recommend to the user.
     */
    private static final int NUMBER_VIDEOS = 5;
	
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
     * Call function to create API service object. Define and execute API request. 
     * Print API response.
     * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
     */
    public static void main(String[] args)
        throws GeneralSecurityException, IOException, GoogleJsonResponseException {
        
    	// Load the developer key using the ignored Git class 'ApiDevKey'
    	ApiDevKey apiDevKey = new ApiDevKey();
    	DEVELOPER_KEY = apiDevKey.getDevKey();
    	
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
	 	
	    	// preferred duration of video
	    	boolean askForDuration = true;
	    	while (askForDuration) {
	    		System.out.println("What is your preferred video length? (a) <5 min (b) 5-15 min (c) >15 min (d) any");
	    		System.out.print("Enter a/b/c/d: ");
	    		String durationResponse = scanner.nextLine();
	    		
		        try {
		        	char duration = durationResponse.charAt(0);
				        if ((duration == 'a') || (duration == 'A') || (duration == 'b') || (duration == 'B') || 
				        	(duration == 'c') || (duration == 'C') || (duration == 'd') || (duration == 'D')) {
				        	askForDuration = false;
				        }
		        } catch (Exception e) {
		        	System.out.println("Invalid response. Please try again.");
		        }
	    	}
	
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
		    	// System.out.println(videoSearchResults); // ***FOR TESTING PURPOSES ONLY***
		    	
		    	// Set the next page token for another search, if required
		    	pageToken = videoSearchResults.getNextPageToken();
		    	
		    	// Extract list of video search result items from the "Search: list" API JSON.
		    	List<SearchResult> items = videoSearchResults.getItems();
		    	
		    	// Iterate over the items to store all the video ID's in an ArrayList.
		    	ArrayList<String> videoIDs = new ArrayList<String>();
		    	for (SearchResult sr : items) {
		    		videoIDs.add(sr.getId().getVideoId());
		    	}
		    	
		    	for (String s : videoIDs) {
		    		System.out.print(videoIDs + " ");
		    	}
		    	
		    	
		    	// Iterate over the videoIDs ArrayList to get more details on each video using the "Videos: list" API.
		    	//for (String videoID : videoIDs) {
		    		
		        // Set up and call the "Videos: list" API
		        
		    	// part --> set this ArrayList to include "contentDetails" and "statistics"
		    	ArrayList<String> partVideosList = new ArrayList<String>();
		    	partVideosList.add("contentDetails");
		    	partVideosList.add("statistics");
		        
		        // Call the YouTube "Videos: list" API to filter video searches by further criteria and pick out
		        // final recommendations for the user
		        VideoListResponse videoDetails = apiCalls.videoDetails(youtubeService, DEVELOPER_KEY, partVideosList, videoIDs);
		        System.out.println(videoDetails); //***FOR TESTING PURPOSES ONLY***
		        
		        //TODO
		        
		        // KEYWORDS: "how to", "tutorial", "demo", "tips", "beginner", "intermediate", "advanced", "learn",
		        // "easy", "hard", "great for", "try", "education", "like a pro", "like a boss", "hack", etc.
		        // Search for these in video (1) titles (2) categories (3) descriptions
		        
		        //If the video meets all the criteria, create a new recommended video and increase counter_videos.	
		        //recommendationList[counter_videos] = new Recommendation(title, description, videoID);
		        counter_videos++;
		    	
		    	
	    	}

	    	// Ask for user feedback on each video. If the user likes a video, store it in the user's favorites.
	    	ArrayList<Recommendation> favoritesList = new ArrayList<Recommendation>();
	    	for (int i = 0; i < NUMBER_VIDEOS; i++) {
	    		//TODO
	    	}
		        
   
	        // Create a while loop to keep asking the user if he or she wants to keep searching for videos until a valid 
		    // response is received.
	        boolean keepAsking = true;
		    
	        while (keepAsking) {
	        	
			    System.out.print("I hoped you found those videos helpful! Want me to dig up some more for you? (y/n) ");
		        String prompt = scanner.nextLine();
		        
		        // If the user enters a response to begins with "y" or "Y," start a new search round.
		        // If the user enters a response to begins with "n" or "N," exit the program.
		        try {
		        	char firstLetter = prompt.charAt(0);
				        if ((firstLetter == 'y') || (firstLetter == 'Y')) {
				        	programRunning = true;
				        	keepAsking = false;
				        } else if ((prompt.charAt(0) == 'n') || (prompt.charAt(0) == 'N')) {
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
		
	        
        //Close the scanner.
        scanner.close();

    }
}
