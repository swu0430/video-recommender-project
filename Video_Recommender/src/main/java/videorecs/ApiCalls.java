package videorecs;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse; //for the YouTube "Search: list" API
import com.google.api.services.youtube.model.VideoCategoryListResponse; //for the YouTube "VideoCategories: list" API
import com.google.api.services.youtube.model.VideoListResponse; //for the YouTube "Videos: list" API

/**
 * This class contains the YouTube API calls that will be used to generate the video recommendations for the user.
 * Includes the "Search: list" & "Videos: list" API's
 * @author Alicia Yen and Steven Wu
 */
public class ApiCalls {

	/**
	 * This method calls the YouTube "Search: list" API to produce a JSON list of YouTube video searches
	 * based on specified user input.
	 * @param youtubeService - authorized YouTube API client service
	 * @param devKey - developer key needed for YouTube API client service authorization
	 * @param part - ArrayList of strings; should be set to "snippet"
	 * @param maxResults - number of video search results per page (capped at 50)
	 * @param pageToken - string used to move to next page of search results if needed
	 * @param query - key words for the search
	 * @param type - should be set to "video" for video searches
	 * @param duration - length of video; can be set to "any"/"long"/"medium"/"short"
	 * @return JSON for the YouTube "Search: list" API
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws GoogleJsonResponseException
	 */
	public SearchListResponse videoSearches(YouTube youtubeService, String devKey, ArrayList<String> part, 
		long maxResults, String pageToken, String query, ArrayList<String> type, String duration) 
		throws GeneralSecurityException, IOException, GoogleJsonResponseException {
		
        // Define and execute the "Search: list" API request
        YouTube.Search.List request = youtubeService.search()
        	.list(part);	
        SearchListResponse response = request.setKey(devKey)
            .setMaxResults(maxResults)
            .setPageToken(pageToken)
        	.setQ(query)
        	.setType(type)
        	.setVideoDuration(duration)
            .execute();
        
        // Return the "Search: list" API JSON
        return response;
	}
	
	/**
	 * This method calls the YouTube "Videos: list" API to produce a JSON list of more drilled-down detail
	 * for a given YouTube video.
	 * @param youtubeService - authorized YouTube API client service
	 * @param devKey - developer key needed for YouTube API client service authorization
	 * @param part - ArrayList of Strings; should be set to "content details" and "statistics"
	 * @param id - unique videoId for the YouTube video 
	 * @return JSON for the YouTube "Videos: list" API
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws GoogleJsonResponseException
	 */
	public VideoListResponse videoDetails(YouTube youtubeService, String devKey, ArrayList<String> part, 
		ArrayList<String> id) throws GeneralSecurityException, IOException, GoogleJsonResponseException {
	
		// Define and execute the "Videos: list" API request
	    YouTube.Videos.List request = youtubeService.videos()
	    	.list(part);	
	    VideoListResponse response = request.setKey(devKey)
	    	.setId(id)
	        .execute();
	    
	    // Return the "Videos: list" API JSON
	    return response;
	}
	
	/**
	 * This method calls the YouTube "VideoCategories: list" API to produce a JSON of the full list of 
	 * video categories for YouTube videos in the US region.
	 * @param youtubeService - authorized YouTube API client service
	 * @param devKey - developer key needed for YouTube API client service authorization
	 * @param part - ArrayList of Strings; should be set to "content details" and "statistics"
	 * @param regionCode of the country the user lives in (should be set to "us")
	 * @return JSON for the YouTube "VideoCategories: list" API
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws GoogleJsonResponseException
	 */
	public VideoCategoryListResponse videoCategories(YouTube youtubeService, String devKey, ArrayList<String> part,
		String regionCode) throws GeneralSecurityException, IOException, GoogleJsonResponseException {
		
		// Define and execute the "VideoCategories: list" API request
	    YouTube.VideoCategories.List request = youtubeService.videoCategories()
	    	.list(part);	
	    VideoCategoryListResponse response = request.setKey(devKey)
	    	.setRegionCode(regionCode)
	        .execute();
	    
	    // Return the "VideoCategories: list" API JSON
	    return response;
	}
}
