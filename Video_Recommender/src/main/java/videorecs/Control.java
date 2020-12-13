package videorecs;

import javax.swing.SwingUtilities;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

/**
 * This class initiates the mainframe, defines static variables to be referenced in other classes,
 * and contains a method to build and return an authorized API client service.
 * @author Alicia Yen and Steven Wu
 */
public class Control {

	// STATIC VARIABLES
	
	/**
	 * Required string for building an authorized API client service
	 */
    static final String APPLICATION_NAME = "API code samples";
    
    /**
     * Variable used for pulling the JSON output from the YouTube "Search: list" API
     */
    static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    
    /**
     * Developer Key to be used for YouTube API client service authorization.
     */
	static final String DEVELOPER_KEY = ApiDevKey.DEV_KEY;

    /**
     * Number of videos the program will recommend to the user.
     */
    static final int NUMBER_VIDEOS = 3;
    
    /**
     * Minimum approval rating required for a recommended video
     */
//    static final double MIN_RATING = 0.80; // Original criteria
    static final double MIN_RATING = 0.20; // Reduced criteria 
    
    /**
     * Minimum number of likes required for a recommended video 
     */
//    static final int MIN_LIKES = 5000; // Original criteria 
    static final int MIN_LIKES = 50; // Reduced criteria 
    
    /**
     * Minimum number of views required for a recommended video
     */
//    static final int MIN_VIEWS = 15000; // Original criteria
    static final int MIN_VIEWS = 150; // Reduced criteria
	
	/**
	 * Mainframe of Java Swing GUI that takes user input for YouTube video searches.
	 */
	static Mainframe MFRAME;
	
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
     * Main function - creates a Java Swing UI mainframe to take user input and then search for YouTube videos.
     */
    public static void main(String[] args) {
		
    	SwingUtilities.invokeLater(new Runnable() {
    		@Override
    		public void run() {
    			Control.MFRAME = new Mainframe();
    		}
    	});
    }
}

