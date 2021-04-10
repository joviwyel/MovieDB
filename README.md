## CS 122B Spring 2021 Team10 Project 1

This is team10 project 1 final version. Team members developed team10 version of the project based on the api-example.


## Demo video URL:

https://youtu.be/i2CiywA60Yg

## How to deploy this application with Tomcat:

You can either use gui to deploy war file into Tomcat or do it in Terminal.
By gui:
Go to AWS Tomcat manager webapp -> WAR file to delpoy -> Choose File and deploy. 

Use Terminal:
1. inside your repo, use 'mvn package' to build the war file.
2. copy war file into Tomcat webapps folder
  cp ./target/*.war /var/lib/tomcat9/webapps/
  
After that, Tomcat web apps should have the new war file.

## Some briefly explain:

1. Movie List Page: Show top 20 rated movies include some detail informaiton(title, year, director, 3 genres, 3 stars, rating)

Have hyperlink for each movie and star.

2. Single Movie Page: Display title, year, director, all genres, all stars, rating for one movie.

Have hyperlink for each star.

3. Single Star Page: Show name, year of birth, all movies in which the star performed.

Have hyperlink for each movie.

4. Jump Functionality: We added button to Single Movie Page and Single Star Page which can jump back to Movie List Page.

5. All the data come from database 'moviedb'

## Each mamber's contribution:

1. First of all, both two members builed our own AWS, and setup MySQL, Tomcat and IDE on our local and AWS machine.

2. In order to better test and learn the entire project setup and build process, both members create database moviedb on our own local machine and AWS.

3. Contribution break down as follows:

Movie List Page:

*    Kai Li Tan: Added all the hyperlink for each stars and movie. Let page show top 20 rated movies.

*    Jing Wu: Added 3 genres and 3 stars on the page.

Single Movie Page:

* Kai Li Tan: Added display title, year, director, all genres, rating.

* Jing Wu: Added display all the stars. Added all the hyperlink for each stars.

Single Star Page:

* Kai Li Tan: Added display name, year, and birth.

* Jing Wu: Added all the hyperlink for each movie.

Jump Functionality:

* Kai Li Tan: Added all the buttons in the application.

Others:

* Kai Li Tan: Beautify the webpage. Setup AWS security group for graders to access, and will keep update the URL during grading period.

* Jing Wu: Make the demo video and work report.
