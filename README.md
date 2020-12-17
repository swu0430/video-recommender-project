# Educational Video Recommender

This Java Maven program takes in user input for activities they like to do or that they have coming up, as well as possibly preferences for video characteristics (e.g., video duration). The program will then call the YouTube Data API to search for educational videos based on these inputs and parse the API JSON outputs to filter for videos based on the user’s preferences and default criteria, such as a minimum number of views, a minimum rating (# likes / [# likes + # dislikes]), etc. The final output of the program be a list of three recommended YouTube URL links. The entire program also runs in the Java Swing GUI as opposed to the Eclipse console (including GUI windows to accept user input, display the recommended videos, and allow the user to view liked or favorited videos).

## Overview of Program Class Structure

* Control.java – Contains the main method which runs the program, sets up key parameters like the API Developer Key and criteria for selecting recommended videos, and includes a method for building an authorized API client service.

* Mainframe.java – Sets up mainframe GUI window through which the user can search for videos based on an inputted activity list and duration preference, view favorites, or exit the program. If multiple activities are entered, a random activity will be selected for 10 video searches at a time. This window will be displayed once the Control.java class is run, when the “Go Back” button is clicked in the ChildframeRecommendations.java GUI screen, or when the “OK” button is clicked in the ChildframeFavorites.java GUI screen.

* MainframeTest.java – Unit tests for the Mainframe.java class.

* ChildframeRecommendations.java – Sets up a childframe Java Swing GUI window which outputs 3 recommended videos to the user based on the inputted activity list and video duration preference in the Mainframe.java GUI window. It also asks whether the user liked each video (for the purposes of updating a “Favorites” list). This screen recommends videos based on the criteria in the Control.java class; it should never recommend the same video twice (assuming the program is never exited). This window can only be reached by clicking the “Submit” button in the Mainframe.java GUI window.

* ChildframeFavorites.java – Sets up a childframe Java Swing GUI window which displays an updated list of videos that the user has liked. This list will continuously update as long as the program is never exited. Clicking “OK” in this window will take the user back to the Mainframe.java GUI window. This screen can be reached by either clicking “Favorites” in the Mainframe.java GUI window or clicking “Submit” in the ChildframeRecommendations.java GUI window.

* Recommendation.java – Defines an object to represent a recommended video.

* RecommendationTest.java – Unit tests for the Recommendation.java class.

* ApiCalls.java – Contains the YouTube Data API’s used to search for recommended videos. For this program, the “Search: list” and “Videos: list” API’s are called.

* ApiDevKey.java – Contains a set of API Developer Keys for calling the YouTube API’s (this class is placed in .gitignore).

## Helpful Resources & Tutorials: 
* YouTube Data API v3 Tutorial - https://www.youtube.com/watch?v=TE66McLMMEw&t=306s
* Search List API (set 'Developer' to True) - https://developers.google.com/youtube/v3/docs/search/list?apix=true&apix_params=%7B%22part%22%3A%5B%22snippet%22%5D%2C%22q%22%3A%22the%20weeknd%22%7D
* Video list API - https://developers.google.com/youtube/v3/docs/videos/list
* Get personalized Developer API key - https://console.developers.google.com/projectselector2/apis/credentials?supportedpurview=project&organizationId=0&authuser=1
* Adding YouTube Data API v3 Client Library for Java (Maven) - https://github.com/googleapis/google-api-java-client-services/tree/master/clients/google-api-services-youtube/v3
* Additional Maven dependencies to add: https://stackoverflow.com/questions/51504370/eclipse-is-not-importing-com-google-api-client-imports
* More information on Video List API: https://stackoverflow.com/questions/15596753/how-do-i-get-video-durations-with-youtube-api-version-3
* Creating a Maven project - https://www.youtube.com/watch?v=sNEcpw8LPpo
* Java Swing GUI: https://www.javatpoint.com/java-swing
