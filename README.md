## CS 122B Spring 2021 Team10

Important notes: 
  For the whole project, there are two contributers, do some clarify about their commit name. 

Committer name "Kai Li Tan", "kaiiiliii" are made by member Kai Li Tan, who is using Github Username "kaiiiliii".

Committer name "Jovi Wyel", "Jing Wu", "joviwyel" are made by member Jing Wu, who is using Github Username "joviwyel".


# Project 3

## XML file insert time report:
  Parser actors63.xml file and insert 6025 records into table stars cost total 2.73 sec.
  
  Parser mains243.xml file and insert 136 genres into table genres, 12115 Movies into table movies, 8778 genres & Movies into table genres_in_movies, total cost 4.557 sec.
  
  Parser casts123.xml file and insert 23082 records into table stars_in_movies, cost total 3.272 sec.
  
## XML parsing optimization report:
  Except as mentioned in our project file such like set auto-commit off and using PreparedStatement methods, there are many other ways can be done to reduce the running time of our program. Here do some small describe about the method which our project is using. 
  
  Method One: (This project is using this method.)
  Since for project 3, we need to insert some new information into the old database. In order to do this, we have to check if the information we want to insert is already exists. Therefore, Our main approach is to reduce the time it takes to retrieve data already in the database. 
  1. We create an new Object to access and modify data easier. For movies, we have Object NewMovie, which will store all the data related to the Movie. For Stars, we have Object NewStar, which will store all the data telated to the Star. 

  2. We are using HashMap to store key and value. For table stars, movies, stars_in_movies, genres_in_movies and genres, we create bounch of HashMap to hold all the key and value for those information. For example, HashMap<NewMovie, String> moviesMap, this Hashmap has a key which is the Object NewMovie which include movie's title, year and director, and the value is movieId. Before parsing, we will create this HashMap. During the parsing time, we will create a new Object which include the information got from XML file, and check if this HashMap already contains such key. If not, then doing insert. HashMap will reach the key and value efficiently. Then significantly reduced running time.

  Method Two:
  Another method is to use load data infile statement. By using this, can read rows from a text file into a table in a short time. 
  
## Each mamber's contribution:

Kai Li Tan: 1)Password Encryption 
            2)Prepared Statement 
            3)Employee Dashboard.
Jing Wu   : 1)reCAPTCHA           
            2)HTTPS               
            3)XML Parsing.

# Project 2
## Demo video URL: https://www.youtube.com/watch?v=rE5Uz4C_JIo

## How to deploy this application with Tomcat:

You can either use gui to deploy war file into Tomcat or do it in Terminal.
By gui:
Go to AWS Tomcat manager webapp -> WAR file to delpoy -> Choose File and deploy. 

Use Terminal:
1. inside your repo, use 'mvn package' to build the war file.
2. copy war file into Tomcat webapps folder
  cp ./target/*.war /var/lib/tomcat9/webapps/
  
After that, Tomcat web apps should have the new war file.

## Substring matching design:
+ Our searching support substring matching searching. When user type in characters, will find all strings that contain the pattern anywhere. User don't need to type in any special characters. 
- For example, the movie "Dominator" can be search by 
  i) domina%
  ii) %mina%
  iii) %tor
 and many more combinations.
 
+ LIKE was used to search for a specified pattern in the Title, Director, and Star Name columns using the wildcards, '%'.
 - '%' represents zero or more characters.
 
+ 'ABC%': All strings that start with 'ABC'. E.g. 'ABCD' and 'ABCABC'.

+ '%XYZ': All strings that end with 'XYZ'. E.g. 'WXYZ' and 'ZZXYZ'.

+ '%AN%': All strings that contain the pattern 'AN' anywhere. E.g. 'LOS ANGELES' and 'SAN FRANCISCO'.

## Each mamber's contribution:

+ Task 1:
    - Jing Wu did Login page and Main page. For our main page is an nagvigation page to jump to Browsing and Searching.
    
+ Task 2: 
    - Kai Li Tan did Searching page.
    - Jing Wu did Browsing page.

+ Task 3:
    - Kai Li Tan did Movie List Page(Extended from Project 1) and related jump functionaly. 
    - Jing Wu did Single Movie Page(Extended from Project 1) and related jump functionaly.

+ Task 4:
    - Kai Li Tan did Payment Page, Place Order action.
    - Jing Wu did Shopping Cart Page, Add to Shopping Cart Button.

+ Kai Li Tan make the demo video for project 2.
+ Both members beautify the webpage and work report.  
    
+ The two of us communicated very timely, discussed, solved many difficult problems and fixed some bugs together. 

# Project 1
This is team10 project 1 final version. Team members developed team10 version of the project based on the api-example.


## Demo video URL:

https://youtu.be/i2CiywA60Yg

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

* Kai Li Tan: Beautify the webpage. 

* Jing Wu: Make the demo video and work report.
