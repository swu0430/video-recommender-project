package videorecs;

import static org.junit.Assert.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class RecommendationTest {
	
	Recommendation recommendation;
	String title;
	String description;
	String videoID;
	String activity;

	@Before
	public void setup() {
		
		title = "How To Master 5 Basic Cooking Skills | Gordon Ramsay";
		description = "We've compiled five previous videos into one, helping you to master your "
				+ "basic skills in the kitchen. Cooking rice, chopping an onion, sharpening a knife, "
				+ "deboning a fish and cooking pasta.\\nFrom Gordon's Ultimate Cookery Course.\n"
				+ "Gordon Ramsay Ultimate Fit Food: http://amzn.to/2FznHtk\\n\\n#GordonRamsay #Cooking "
				+ "\n\nGordon Ramsay's Ultimate Fit Food/Healthy, Lean and Fit – http://po.st/REpVfP\n\n"
				+ "Follow Gordon:\\nText him: +1 (310) 620-6468\\nInstagram: "
				+ "http://www.instagram.com/gordongram\nTwitter: http://www.twitter.com/gordonramsay  \n"
				+ "Facebook: http://www.facebook.com/GordonRamsay\\n\\nIf you liked this clip check out "
				+ "the rest of Gordon's channels:\n\nhttp://www.youtube.com/gordonramsay\n"
				+ "http://www.youtube.com/kitchennightmares\\nhttp://www.youtube.com/thefword\"";
		videoID = "ZJy1ajvMU1k";
		activity = "cooking";
		
		recommendation = new Recommendation(title, description, videoID, activity);
		
	}
	
	@Test
	public void testRecommendation() {
		
		assertEquals(recommendation.getTitle(), "How To Master 5 Basic Cooking Skills | Gordon Ramsay");
		assertEquals(recommendation.getDescription(), "We've compiled five previous videos into one, helping you to master your "
				+ "basic skills in the kitchen. Cooking rice, chopping an onion, sharpening a knife, "
				+ "deboning a fish and cooking pasta.\\nFrom Gordon's Ultimate Cookery Course.\n"
				+ "Gordon Ramsay Ultimate Fit Food: http://amzn.to/2FznHtk\\n\\n#GordonRamsay #Cooking "
				+ "\n\nGordon Ramsay's Ultimate Fit Food/Healthy, Lean and Fit – http://po.st/REpVfP\n\n"
				+ "Follow Gordon:\\nText him: +1 (310) 620-6468\\nInstagram: "
				+ "http://www.instagram.com/gordongram\nTwitter: http://www.twitter.com/gordonramsay  \n"
				+ "Facebook: http://www.facebook.com/GordonRamsay\\n\\nIf you liked this clip check out "
				+ "the rest of Gordon's channels:\n\nhttp://www.youtube.com/gordonramsay\n"
				+ "http://www.youtube.com/kitchennightmares\\nhttp://www.youtube.com/thefword\"");
		assertEquals(recommendation.getVideoID(), "ZJy1ajvMU1k");
		assertFalse(recommendation.getLike());
		
	}
	
	@Test
	public void testGetTitle() {
		
		assertEquals(recommendation.getTitle(), "How To Master 5 Basic Cooking Skills | Gordon Ramsay");
		assertNotEquals(recommendation.getTitle(), "");
		assertNotNull(recommendation.getTitle());
		
	}
	
	@Test
	public void testGetDescription() {
		
		assertEquals(recommendation.getDescription(), "We've compiled five previous videos into one, helping you to master your "
				+ "basic skills in the kitchen. Cooking rice, chopping an onion, sharpening a knife, "
				+ "deboning a fish and cooking pasta.\\nFrom Gordon's Ultimate Cookery Course.\n"
				+ "Gordon Ramsay Ultimate Fit Food: http://amzn.to/2FznHtk\\n\\n#GordonRamsay #Cooking "
				+ "\n\nGordon Ramsay's Ultimate Fit Food/Healthy, Lean and Fit – http://po.st/REpVfP\n\n"
				+ "Follow Gordon:\\nText him: +1 (310) 620-6468\\nInstagram: "
				+ "http://www.instagram.com/gordongram\nTwitter: http://www.twitter.com/gordonramsay  \n"
				+ "Facebook: http://www.facebook.com/GordonRamsay\\n\\nIf you liked this clip check out "
				+ "the rest of Gordon's channels:\n\nhttp://www.youtube.com/gordonramsay\n"
				+ "http://www.youtube.com/kitchennightmares\\nhttp://www.youtube.com/thefword\"");
		assertNotEquals(recommendation.getDescription(), "");
		assertNotNull(recommendation.getDescription());
		
	}
	
	@Test
	public void testGetVideoID() {
		
		assertEquals(recommendation.getVideoID(), "ZJy1ajvMU1k");
		assertNotEquals(recommendation.getVideoID(), "");
		assertNotNull(recommendation.getVideoID());
	
	}
	
	@Test
	public void testGetActivity() {
		
		assertEquals(recommendation.getActivity(), "cooking");
		assertNotEquals(recommendation.getActivity(), "");
		assertNotNull(recommendation.getActivity());
		
	}
	
	@Test
	public void testGetLike() {
		
		assertFalse(recommendation.getLike());
		assertNotNull(recommendation.getLike());
		recommendation.setLike(true);
		assertTrue(recommendation.getLike());
		
	}
	
	@Test
	public void testSetTitle() {
		
		recommendation.setTitle("Test setting title");
		assertEquals("Test setting title", recommendation.getTitle());
		
		recommendation.setTitle("Test setting another title");
		assertEquals("Test setting another title", recommendation.getTitle());
		
		recommendation.setTitle("");
		assertEquals("", recommendation.getTitle());
		
	}
	
	@Test
	public void testSetDescription() {
		
		recommendation.setDescription("Test setting description");
		assertEquals("Test setting description", recommendation.getDescription());
		
		recommendation.setDescription("Test setting another description");
		assertEquals("Test setting another description", recommendation.getDescription());
		
		recommendation.setDescription("");
		assertEquals("", recommendation.getDescription());
		
	}
	
	@Test
	public void testSetVideoID() {
		
		recommendation.setVideoID("Test setting videoID");
		assertEquals("Test setting videoID", recommendation.getVideoID());
		
		recommendation.setVideoID("Test setting another videoID");
		assertEquals("Test setting another videoID", recommendation.getVideoID());
		
		recommendation.setVideoID("");
		assertEquals("", recommendation.getVideoID());
		
	}
	
	@Test
	public void testSetActivity() {
		
		recommendation.setActivity("Test setting activity");
		assertEquals("Test setting activity", recommendation.getActivity());
		
		recommendation.setActivity("Test setting another activity");
		assertEquals("Test setting another activity", recommendation.getActivity());
		
		recommendation.setActivity("");
		assertEquals("", recommendation.getActivity());
		
	}
	
	@Test
	public void testSetLike() {
		
		recommendation.setLike(true);
		assertTrue(recommendation.getLike());
		
		recommendation.setLike(false);
		assertFalse(recommendation.getLike());
		
		recommendation.setLike(true);
		assertTrue(recommendation.getLike());
		
	}
	
}
