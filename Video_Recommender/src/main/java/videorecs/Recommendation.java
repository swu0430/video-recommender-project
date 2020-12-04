package videorecs;

/**
 * This class represents a recommended video.
 * @author Alicia Yen and Steven Wu
 */
public class Recommendation {

	// Instance variables
	
	/**
	 * Title of recommended video.
	 */
	private String title;
	
	/**
	 * Description of recommended video.
	 */
	private String description;
	
	/**
	 * videoID of recommended video.
	 */
	private String videoID;
	
	/**
	 * Boolean to represent whether the user likes the video.
	 */
	private boolean like; 
	
	// Constructor
	
	/**
	 * Constructs a video recommendation with the given title, description, and videoID.
	 * @param title of recommended video
	 * @param description of recommended video
	 * @param videoID of recommended video
	 */
	public Recommendation(String title, String description, String videoID) {
		this.title = title;
		this.description = description;
		this.videoID = videoID;
		this.like = false;
	}
	
	// Getters
	
	/**
	 * Returns the title of the recommended video.
	 * @return title of video
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Returns the description of the recommended video.
	 * @return description of video
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Returns the videoID of the recommended video.
	 * @return videoID of video
	 */
	public String getvideoID() {
		return this.videoID;
	}
	
	/**
	 * Returns whether the user has liked the recommended video.
	 * @return like status (boolean) of video
	 */
	public boolean getLike() {
		return this.like;
	}
	
	// Setters
	
	/**
	 * Sets the title of the recommended video.
	 * @param title of video
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Sets the description of the recommended video.
	 * @param description of video
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Sets the videoID of the recommended video.
	 * @param videoID of video
	 */
	public void setvideoID(String videoID) {
		this.videoID = videoID;
	}
	
	/**
	 * Sets whether the user likes the recommended video.
	 * @param like status (boolean) of video
	 */
	public void setLike(boolean like) {
		this.like = like;
	}
}
