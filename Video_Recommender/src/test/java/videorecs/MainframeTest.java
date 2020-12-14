package videorecs;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import org.junit.Before;
import org.junit.Test;

public class MainframeTest {

	double delta;
	
	@Before
	public void setUp() {
		
		this.delta = 0.000001;
		
		//Clear the Recommendation, Activity Array, Seen, and Favorites lists
		Mainframe.RECOMMENDATION_LIST = new Recommendation[Control.NUMBER_VIDEOS];
		Mainframe.ACTIVITY_ARRAY_LIST.clear();
		Mainframe.SEEN_LIST.clear();
		Mainframe.FAVORITES_LIST.clear();
		
	}
	
	@Test
	public void testCreateRecommendedVideosList() {
		
		//Add sample activities to the Activity Array list and a sample video duration setting
		Mainframe.ACTIVITY_ARRAY_LIST.add("cooking");
		Mainframe.ACTIVITY_ARRAY_LIST.add("dancing");
		Mainframe.ACTIVITY_ARRAY_LIST.add("golf");
		Mainframe.DURATION = "5-15 min";
		
		//Create a sample list of video recommendations.
		try {
			Mainframe.createRecommendedVideosList();
		} catch (GoogleJsonResponseException e1) {
			e1.printStackTrace();
		} catch (GeneralSecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//GENERAL CASES
		
		for (int i = 0; i < Mainframe.RECOMMENDATION_LIST.length; i++) {
			assertNotNull(Mainframe.RECOMMENDATION_LIST[i]);
			assertEquals(Mainframe.RECOMMENDATION_LIST[i].getVideoID(), Mainframe.SEEN_LIST.get(i));
		}
		
		//EDGE CASES
		
		//Create another sample list of video recommendations using same activity and duration 
		//parameters as before.
		try {
			Mainframe.createRecommendedVideosList();
		} catch (GoogleJsonResponseException e1) {
			e1.printStackTrace();
		} catch (GeneralSecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		for (int j = 0; j < Mainframe.RECOMMENDATION_LIST.length; j++) {
			assertNotNull(Mainframe.RECOMMENDATION_LIST[j]);
			assertNotEquals(Mainframe.RECOMMENDATION_LIST[j].getVideoID(), Mainframe.SEEN_LIST.get(j));
			assertEquals(Mainframe.RECOMMENDATION_LIST[j].getVideoID(), Mainframe.SEEN_LIST.get(j + Control.NUMBER_VIDEOS));
		}
		
	}

	@Test
	public void testVideoMeetsKeywordCriteria() {
		
		//Add sample activities to the Activity Array list and a sample video duration setting
		Mainframe.ACTIVITY_ARRAY_LIST.add("cooking");
		Mainframe.ACTIVITY_ARRAY_LIST.add("dancing");
		Mainframe.ACTIVITY_ARRAY_LIST.add("golf");
		Mainframe.DURATION = "5-15 min";
		
		//Create a sample list of video recommendations.
		try {
			Mainframe.createRecommendedVideosList();
		} catch (GoogleJsonResponseException e1) {
			e1.printStackTrace();
		} catch (GeneralSecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//GENERAL CASES
		
		for (int i = 0; i < Mainframe.RECOMMENDATION_LIST.length; i++) {
			assertTrue(Mainframe.videoMeetsKeywordCriteria(Mainframe.RECOMMENDATION_LIST[i].getTitle(), 
				Mainframe.RECOMMENDATION_LIST[i].getDescription()));
		}
		
		String videoTitle = "Test Video";
		String videoDescription = "This is a dummy description of a test video.";
		assertFalse(Mainframe.videoMeetsKeywordCriteria(videoTitle, videoDescription));
		
		videoTitle = "This is how to cook a steak";
		videoDescription = "";
		assertTrue(Mainframe.videoMeetsKeywordCriteria(videoTitle, videoDescription));
		
		videoTitle = "Salsa dance like a boss!";
		videoDescription = "";
		assertTrue(Mainframe.videoMeetsKeywordCriteria(videoTitle, videoDescription));
		
		//EDGE CASES
		
		videoTitle = "How to cook a steak";
		videoDescription = "";
		assertTrue(Mainframe.videoMeetsKeywordCriteria(videoTitle, videoDescription));
		
		videoTitle = "How to cook a steak";
		videoDescription = "";
		assertTrue(Mainframe.videoMeetsKeywordCriteria(videoTitle, videoDescription));
		
		videoTitle = "The history of American democracy";
		videoDescription = "";
		assertFalse(Mainframe.videoMeetsKeywordCriteria(videoTitle, videoDescription));
		
	}
	
	@Test
	public void testVideoMeetsRatingCriteria() {
		
		//GENERAL CASES
		assertTrue(Mainframe.videoMeetsRatingCriteria(100.0));
		assertTrue(Mainframe.videoMeetsRatingCriteria(0.81));
		assertFalse(Mainframe.videoMeetsRatingCriteria(0.79));
		
		//EDGE CASES
		assertFalse(Mainframe.videoMeetsRatingCriteria(0.80));
		assertFalse(Mainframe.videoMeetsRatingCriteria(0.0));
		
	}
	
	@Test
	public void testVideoMeetsLikeCountCriteria() {
		
		//GENERAL CASES
		assertTrue(Mainframe.videoMeetsLikeCountCriteria(BigInteger.valueOf(100000)));
		assertTrue(Mainframe.videoMeetsLikeCountCriteria(BigInteger.valueOf(5001)));
		assertFalse(Mainframe.videoMeetsLikeCountCriteria(BigInteger.valueOf(4999)));
		
		//EDGE CASES
		assertFalse(Mainframe.videoMeetsLikeCountCriteria(BigInteger.valueOf(5000)));
		assertFalse(Mainframe.videoMeetsLikeCountCriteria(BigInteger.valueOf(0)));
		
	}
	
	@Test
	public void testVideoMeetsViewCountCriteria() {
		
		//GENERAL CASES
		assertTrue(Mainframe.videoMeetsViewCountCriteria(BigInteger.valueOf(100000)));
		assertTrue(Mainframe.videoMeetsViewCountCriteria(BigInteger.valueOf(15001)));
		assertFalse(Mainframe.videoMeetsViewCountCriteria(BigInteger.valueOf(14999)));
		
		//EDGE CASES
		assertFalse(Mainframe.videoMeetsViewCountCriteria(BigInteger.valueOf(15000)));
		assertFalse(Mainframe.videoMeetsViewCountCriteria(BigInteger.valueOf(0)));
		
	}
	
	@Test
	public void testCalculateVideoRating() {
		
		//GENERAL CASES
		assertEquals(0.5, Mainframe.calculateVideoRating(new BigInteger("10000"), 
				new BigInteger("10000")), this.delta);
		
		assertEquals(.15386053468, Mainframe.calculateVideoRating(new BigInteger("12345"), 
				new BigInteger("67890")), this.delta);
		
		//EDGE CASES	
		assertEquals(1, Mainframe.calculateVideoRating(new BigInteger("10000"), 
				new BigInteger("0")), this.delta);
		
		assertEquals(0, Mainframe.calculateVideoRating(new BigInteger("0"), 
				new BigInteger("10000")), this.delta);
		
	}
	
	@Test
	public void testParseVideoDurationHelper() {
		
		//GENERAL CASES
		assertEquals("PT([0-9]+)H([0-9]*)M*([0-9]*)S*", Mainframe.parseVideoDurationHelper("PT1H23M45S"));
		assertEquals("PT([0-9]+)M([0-9]*)S*", Mainframe.parseVideoDurationHelper("PT15M7S"));
		assertEquals("PT([0-9]+)S", Mainframe.parseVideoDurationHelper("PT32S"));
		
		//EDGE CASES
		assertEquals("PT([0-9]+)H([0-9]*)M*([0-9]*)S*", Mainframe.parseVideoDurationHelper("PT1H"));
		assertEquals("PT([0-9]+)H([0-9]*)M*([0-9]*)S*", Mainframe.parseVideoDurationHelper("PT1H23M"));
		assertEquals("PT([0-9]+)H([0-9]*)M*([0-9]*)S*", Mainframe.parseVideoDurationHelper("PT1H45S"));
		assertEquals("PT([0-9]+)M([0-9]*)S*", Mainframe.parseVideoDurationHelper("PT23M"));
		
	}
	
	@Test
	public void testParseVideoDuration() {
		
		//GENERAL CASES
		assertEquals(5025, Mainframe.parseVideoDuration("PT([0-9]+)H([0-9]*)M*([0-9]*)S*", "PT1H23M45S"));
		assertEquals(907, Mainframe.parseVideoDuration("PT([0-9]+)M([0-9]*)S*", "PT15M7S"));
		assertEquals(32, Mainframe.parseVideoDuration("PT([0-9]+)S", "PT32S"));
		
		//EDGE CASES
		assertEquals(3600, Mainframe.parseVideoDuration("PT([0-9]+)H([0-9]*)M*([0-9]*)S*", "PT1H"));
		assertEquals(4980, Mainframe.parseVideoDuration("PT([0-9]+)H([0-9]*)M*([0-9]*)S*", "PT1H23M"));
		assertEquals(3645, Mainframe.parseVideoDuration("PT([0-9]+)H([0-9]*)M*([0-9]*)S*", "PT1H45S"));
		assertEquals(1380, Mainframe.parseVideoDuration("PT([0-9]+)M([0-9]*)S*", "PT23M"));
		
	}
	
	@Test
	public void testVideoMeetsDurationCriteria() {
		
		//GENERAL CASES
		assertTrue(Mainframe.videoMeetsDurationCriteria("<5 min", 1));
		assertTrue(Mainframe.videoMeetsDurationCriteria("<5 min", 299));
		assertFalse(Mainframe.videoMeetsDurationCriteria("<5 min", 300));
		assertTrue(Mainframe.videoMeetsDurationCriteria("5-15 min", 300));
		assertTrue(Mainframe.videoMeetsDurationCriteria("5-15 min", 899));
		assertTrue(Mainframe.videoMeetsDurationCriteria("5-15 min", 900));
		assertFalse(Mainframe.videoMeetsDurationCriteria(">15 min", 900));
		assertTrue(Mainframe.videoMeetsDurationCriteria(">15 min", 1000));
		assertFalse(Mainframe.videoMeetsDurationCriteria(">15 min", 100));
		assertTrue(Mainframe.videoMeetsDurationCriteria("Any", 1));
		assertTrue(Mainframe.videoMeetsDurationCriteria("Any", 300));
		assertTrue(Mainframe.videoMeetsDurationCriteria("Any", 900));
		assertTrue(Mainframe.videoMeetsDurationCriteria("Any", 1000));
		
		//EDGE CASES
		assertFalse(Mainframe.videoMeetsDurationCriteria("<5 min", 0));
		assertFalse(Mainframe.videoMeetsDurationCriteria("5-15 min", 0));
		assertFalse(Mainframe.videoMeetsDurationCriteria(">15 min", 0));
		assertTrue(Mainframe.videoMeetsDurationCriteria("Any", 0));
		assertTrue(Mainframe.videoMeetsDurationCriteria(">15 min", 123456789));
		assertTrue(Mainframe.videoMeetsDurationCriteria("Any", 123456789));
	
	}
	
}
