StudyGroup
==========

Ruby on Rails web app that communicates with an Android app <br>
The Heroku application can be found <a href = "http://study-group-creator.herokuapp.com/">here</a>.

First Milestone 
---------------
* Android app authenticates the user if they are using the app for the first time
* Android app goes to myUH and gets the class names and class times of the user
* Android app displays classes and times in a ListView
 * This information will be persisted 
* Android app stores a boolean that reflects if a user is a new user or not

Second Milestone 
----------------
* Write an API using JSON
  * Android app sends how much people want in the group, user name and password to the web app
    * Web app gets name of people in the class from Laulima
  * Web app sends notifications to people on the list
    * First checks to see if they are a StudyGroup user
      * If yes, send a study group invite to that application
      * If not, send an email to their myUH email

Third Milestone
---------------
* Web app waits for responses
  * Types of responses
    * Joining a group (from phone)
    * Not joining a group (from phone)
    * Joining a group (from web app)
    * Not joining a group (from web app)
  * If joining a group
    * Updates the tables in the web app (Group, User)
  * If not joining a group
    * Removes them from possible group member roster for that group only

Once specified number of people join a group, a notification is sent to people who didn't respond that the group
has already been filled

Fourth Milestone 
-----------------
* People who are in the group get a notification that based on their schedules, these times are available
 * Times will be determined by the web app 
* People in the group vote on a set time
  * Once a person sends a response, it gets sent to the web app
    * Web app sends a notification to the app that shows who responded and at what time (this will be displayed in a activity, in percentages)
      * If person doesn't have the app, it will be sent in an email 
* Once everybody has voted, another notification will be sent out that shows the votes.  
* Users will have an option to pick between the top two times.
* Once all responses are in another notification will be sent that notifies everyone in the group of their study group time
* Android app will sync to display the various state of the process
