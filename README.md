# Educational Video Recommender

This Java Maven program takes in user input for activities they like to do or that they have coming up, as well as possibly preferences for video characteristics (e.g., video duration). The program will then call the YouTube Data API to search for educational videos based on these inputs and parse the API JSON outputs to filter for videos based on the user’s preferences and default criteria, such as a minimum number of views, a minimum rating (# likes / [# likes + # dislikes]), etc. The final output of the program be a list of three recommended YouTube URL links. The entire program also runs in the Java Swing GUI as opposed to the Eclipse console (including GUI windows to accept user input, display the recommended videos, and allow the user to view liked or favorited videos).




Helpful Resources & Tutorials: 
- YouTube Data API v3 Tutorial - https://www.youtube.com/watch?v=TE66McLMMEw&t=306s
- Search List API (set 'Developer' to True) - https://developers.google.com/youtube/v3/docs/search/list?apix=true&apix_params=%7B%22part%22%3A%5B%22snippet%22%5D%2C%22q%22%3A%22the%20weeknd%22%7D
- Video list API - https://developers.google.com/youtube/v3/docs/videos/list
- Get personalized Developer API key - https://console.developers.google.com/projectselector2/apis/credentials?supportedpurview=project&organizationId=0&authuser=1
- Adding YouTube Data API v3 Client Library for Java (Maven) - https://github.com/googleapis/google-api-java-client-services/tree/master/clients/google-api-services-youtube/v3
- Additional Maven dependencies to add: https://stackoverflow.com/questions/51504370/eclipse-is-not-importing-com-google-api-client-imports
- More information on Video List API: https://stackoverflow.com/questions/15596753/how-do-i-get-video-durations-with-youtube-api-version-3
- Creating a Maven project - https://www.youtube.com/watch?v=sNEcpw8LPpo
- Java Swing GUI: https://www.javatpoint.com/java-swing
