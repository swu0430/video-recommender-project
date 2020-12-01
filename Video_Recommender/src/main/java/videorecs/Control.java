package videorecs;

import java.util.Scanner;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

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
     * Your personal Developer Key (can be set up and obtained from link below).
     * https://console.developers.google.com/projectselector2/apis/credentials?supportedpurview=project&organizationId=0&authuser=1
     */
	private static final String DEVELOPER_KEY = "AIzaSyCzGG5RsUKlE554OlADJBjRvfoCweAgHzI";

	/**
	 * Required string for calling the YouTube "Search: list" API
	 */
    private static final String APPLICATION_NAME = "API code samples";
    
    /**
     * Variable used for pulling the JSON output from the YouTube "Search: list" API
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

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
        
    	// Open scanner
    	Scanner scanner = new Scanner(System.in);
    	
    	// Build and return an authorized API client service.
    	YouTube youtubeService = getService();
        
    	// Create a new ApiCall object
    	ApiCalls apiCalls = new ApiCalls();
    	
    	// ***Gather user and default inputs for YouTube "Search: list" API video search*** 
  
    	// part --> set this ArrayList to include "snippet"
    	ArrayList<String> partSearchList = new ArrayList<String>();
    	partSearchList.add("snippet");
    	
    	// number of search results per page (max is 50, but results may get funky that high)
    	long maxResults = 30L;
    	
    	// token for next page of searches if needed (set initial value to null)
    	String pageToken = null;
    	
    	// search query. ***Consider filtering by key words such as "how to", "tutorial", "demo", "tips",
    	// "beginner", "intermediate", "advanced", "learn", "easy", "hard", "great for", "try", "education",
    	// "like a pro", "like a boss", "hack", in (1)title (2)categories (3)description rather than just 
    	//adding "how to" in the query itself***
    	System.out.print("Enter an activity: ");
    	String query = scanner.nextLine();
    	query += " how to";
    	
    	// type of search --> set to "video"
    	ArrayList<String> type = new ArrayList<String>();
    	type.add("video");
    	
    	// preferred duration of video
    	System.out.print("Enter an preferred video length (any/long/medium/short): ");
    	String duration = scanner.nextLine();
    	System.out.println();
    	
    	// Call the YouTube "Search: list" API to search for a set of videos based on criteria above
    	SearchListResponse videoSearchResults = apiCalls.videoSearches(youtubeService, DEVELOPER_KEY, 
    		partSearchList, maxResults, pageToken, query, type, duration);
        
      
        List<SearchResult> items = videoSearchResults.getItems();
        for (SearchResult sr : items) {
        	System.out.println(sr.getId().getVideoId());
        }
    	
        // Print out the SearchList JSON
        // (Eventually don't want to print this out, want to be able to extract values from it behind the scenes)
        System.out.print("YouTube Search List API JSON: ");
        System.out.println(videoSearchResults);
        
        
        
        System.out.println();
        
               
        

        // ***Gather user and default inputs for YouTube "Search: list" API video search*** 
        
    	// part --> set this ArrayList to include "contentDetails" and "statistics"
    	ArrayList<String> partVideosList = new ArrayList<String>();
    	partVideosList.add("contentDetails");
    	partVideosList.add("statistics");
    	partVideosList.add("snippet");
        
    	// id --> set to the video id
        //ArrayLists added by SW
        ArrayList<String> id = new ArrayList<String>();
        id.add("ePm165e4c1E"); //FILLER --> to be filled in once we figure how to pull the video ID from SearchList JSON
        
        // Call the YouTube "Videos: list" API to filter video searches by further criteria and pick out
        // final recommendations for the user
        VideoListResponse videoDetails = apiCalls.videoDetails(youtubeService, DEVELOPER_KEY, partVideosList, id);
//        List<Video> items = videoDetails.getItems();
//        for (Video v : items) {
//        	v.
//        }
        
        // Print out the VideosList JSON
        // (Eventually don't want to print this out, want to be able to extract values from it behind the scenes)
        System.out.print("YouTube Videos List API JSON: ");
        System.out.println(videoDetails);
        
        //Close the scanner.
        scanner.close();
    }
}
