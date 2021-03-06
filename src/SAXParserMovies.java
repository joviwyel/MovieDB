import java.io.IOException;
import java.util.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.sql.Connection;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class SAXParserMovies extends DefaultHandler {

    List<NewMovie> myNewMovie;
    private int insertMovieStatus;
    private int insertGenreStatus;
    private int insertGimStatus;

    private int ignoredMovie;
    private int ignoredGim;
    private int duplicateMovie;
    private int insertRatingStatus;


    private String newMaxId;
    private Integer newGenreMax;

    private String tempVal;
    private Connection connection;
    private HashMap<NewMovie, String> moviesMap;
    private HashSet<NewMovie> genreInMoviesMap;
    private HashMap<String, Integer> genreMap;
    private HashMap<String, Float> ratingMap;

    private HashMap<String, String> simMovieMap;

    private String reportMovie;

    //to maintain context
    private NewMovie tempMovie;

    public SAXParserMovies() {
        reportMovie = "";
        myNewMovie = new ArrayList<NewMovie>();
        moviesMap = new HashMap<NewMovie, String>();
        genreInMoviesMap = new HashSet<NewMovie>();
        genreMap = new HashMap<String, Integer>();
        simMovieMap = new HashMap<String, String>();
        ratingMap = new HashMap<String, Float>();
        insertGenreStatus = 0;
        insertMovieStatus = 0;
        insertGimStatus = 0;
        ignoredMovie = 0;
        ignoredGim = 0;
        insertRatingStatus = 0;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql:///moviedb", "mytestuser", "My6$Password");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void run() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        init();
        parseDocument();
        printData();
        connection.commit();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("../stanford-movies/mains243.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Iterate through the list and print
     * the contents
     */
    private String printData() {
        String report = "";
        report += "Total insert genre: " + insertGenreStatus + ".\n";
        report += "Total insert Movies: " + insertMovieStatus+ ".\n";
        report += "Found duplicate movies: " + duplicateMovie+ ".\n";
        report += "Ignored movies: " + ignoredMovie+ ".\n";
        report += "Total insert genre_in_movie: " + insertGimStatus+ ".\n";
        report += "Ignored genre_in_movie: " + ignoredGim+ ".\n";
        report += "Total insert rating: " + insertRatingStatus+ ".\n";
//        System.out.println(report);
        return report;
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("film")) {
            //create a new instance of NewMovie
            tempMovie = new NewMovie();

        }
    }
    private void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

        // get movies map
        Statement statement = connection.createStatement();
        String getAllMovies = "SELECT * FROM movies; ";
        ResultSet allMovies = statement.executeQuery(getAllMovies);

        while (allMovies.next()){
            NewMovie nm = new NewMovie();
            nm.setTitle(allMovies.getString("title"));
            nm.setDirector(allMovies.getString("director"));
            nm.setYear(allMovies.getInt("year"));
            moviesMap.put(nm, allMovies.getString("id"));
        }

        // get genres_in_movies map
        Statement gimSt = connection.createStatement();
        String gim = "SELECT * FROM genres_in_movies; ";
        ResultSet gimSet = gimSt.executeQuery(gim);

        while (gimSet.next()){
            genreInMoviesMap.add(new NewMovie(gimSet.getString("movieId"),
                    gimSet.getInt("genreId")));
        }
        gimSet.close();


        // get genres map
        Statement gSt = connection.createStatement();
        String g = "SELECT * FROM genres; ";
        ResultSet gSet = gSt.executeQuery(g);

        while (gSet.next()){
            genreMap.put(gSet.getString("name"), gSet.getInt("id"));
        }
        gSet.close();



        // get max id for insert later
        String getMaxId = "SELECT max(id) from movies;";
        Statement getMaxSt = connection.createStatement();
        ResultSet MaxId = getMaxSt.executeQuery(getMaxId);
        MaxId.next();
        String nowId = MaxId.getString("max(id)");
        newMaxId = nowId;
        allMovies.close();

        // get genre max id;
        String getGenreMax = "SELECT max(id) from genres;";
        Statement getGenreMaxSt = connection.createStatement();
        ResultSet getGenreMaxSet = getGenreMaxSt.executeQuery(getGenreMax);
        getGenreMaxSet.next();
        newGenreMax = getGenreMaxSet.getInt("max(id)");
        getGenreMaxSet.close();

        // get rating map;
        Statement rSt = connection.createStatement();
        String r = "SELECT * FROM ratings; ";
        ResultSet rSet = rSt.executeQuery(r);

        while (rSet.next()){
            ratingMap.put(rSet.getString("movieId"), rSet.getFloat("rating"));
        }
        rSet.close();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("film")) {
            //add it to the list
            myNewMovie.add(tempMovie);
            try {
                insertIntoMovies(tempMovie);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else if (qName.equalsIgnoreCase("t")) {
            tempMovie.setTitle(tempVal);
        } else if (qName.equalsIgnoreCase("fid")) {
            tempMovie.setFid(tempVal);
        } else if (qName.equalsIgnoreCase("year")) {
            if(isValidYear(tempVal))
                tempMovie.setYear(Integer.parseInt(tempVal));
            else
                tempMovie.setYear(-1);
        } else if (qName.equalsIgnoreCase("dirn")) {
            tempMovie.setDirector(tempVal);
        }  else if(qName.equalsIgnoreCase("cat")) {
            tempMovie.setGenre(tempVal);
            if(genreMap.containsKey(tempVal)) {
                tempMovie.setGenreID(genreMap.get(tempVal));
            }
        }
    }

    private boolean isValidYear(String year){
        for (int i = 0; i < year.length(); i++){
            if (!Character.isDigit(year.charAt(i)))
                return false;
        }
        return true;
    }
    public HashMap<String, String> getSimMovieMap(){return simMovieMap;}
    public String getReport(){
        reportMovie = printData();
        return reportMovie;
    }

    public void insertIntoMovies(NewMovie tempMovie) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{

        if(tempMovie.getTitle().equals("")) {
            int temp = 1;
            ignoredMovie += temp;
            return;
        }
        if(tempMovie.getYear()<=0){
            int temp = 1;
            ignoredMovie += temp;
            return;
        }
        if(tempMovie.getDirector() == null || tempMovie.getDirector().equals("")){
            int temp = 1;
            ignoredMovie += temp;
            return;
        }


        if(!moviesMap.containsKey(tempMovie)){
            String insertMovie = "INSERT INTO movies VALUES (?,?,?,?);";
            PreparedStatement insertMovieStatement = connection.prepareStatement(insertMovie);
            int temp ;
            String nowId = newMaxId.substring(2);
            temp = Integer.parseInt(nowId);
            temp = temp + 1;
            nowId = Integer.toString(temp);
            newMaxId = "tt" + nowId;
            insertMovieStatement.setString(1, newMaxId);
            tempMovie.setMovieID(newMaxId);
            insertMovieStatement.setString(2, tempMovie.getTitle());
            insertMovieStatement.setInt(3, tempMovie.getYear());
            insertMovieStatement.setString(4, tempMovie.getDirector());
            temp = insertMovieStatement.executeUpdate();
            insertMovieStatus += temp;
            insertMovieStatement.close();
            moviesMap.put(tempMovie, tempMovie.getMovieId());
            if(tempMovie.getFid()!= null) {
                if (tempMovie.getFid() != "") {
                    simMovieMap.put(tempMovie.getFid(), tempMovie.getMovieId());
                }
            }


            // insert into rating
            String insertRating = "INSERT INTO ratings VALUES (?,?,?);";
            PreparedStatement insertRatingStatement = connection.prepareStatement(insertRating);
            insertRatingStatement.setString(1, tempMovie.getMovieId());
            insertRatingStatement.setFloat(2, 0);
            insertRatingStatement.setInt(3,0);
            int insertR = 0;
            insertR = insertRatingStatement.executeUpdate();
            insertRatingStatus += insertR;
            insertRatingStatement.close();

        }
        else{
            int temp = 1;
            duplicateMovie += temp;
        }
        if(!genreMap.containsKey(tempMovie.getGenre())){
            if(tempMovie.getGenre()!= null) {
                if(!tempMovie.getGenre().equals("")) {
                    String insertGenre = "INSERT INTO genres VALUES (?,?);";
                    PreparedStatement insertGenreStatement = connection.prepareStatement(insertGenre);
                    newGenreMax = newGenreMax + 1;
                    insertGenreStatement.setInt(1, newGenreMax);
                    insertGenreStatement.setString(2, tempMovie.getGenre());

                    genreMap.put(tempMovie.getGenre(), newGenreMax);
                    tempMovie.setGenreID(newGenreMax);
                    int temp = insertGenreStatement.executeUpdate();
                    insertGenreStatus += temp;
                    insertGenreStatement.close();
                }
            }
        }

        // Insert into genres_in_movies;
        if(tempMovie.getGenre()!=null) {
            if(!tempMovie.getGenre().equals("")) {
                int gId = tempMovie.getGenreId();
                String mId = tempMovie.getMovieId();
                if (!genreInMoviesMap.contains(new NewMovie(mId, gId))) {
                    //                System.out.println(mId + ": " + gId);
                    String insertGim = "INSERT INTO genres_in_movies VALUES(?,?);";
                    PreparedStatement insertGimSt = connection.prepareStatement(insertGim);
                    insertGimSt.setInt(1, gId);
                    insertGimSt.setString(2, mId);
                    genreInMoviesMap.add(new NewMovie(mId, gId));
                    int temp = insertGimSt.executeUpdate();
                    insertGimStatus += temp;
                    insertGimSt.close();
                }
            }
            else{
                int temp = 1;
                ignoredGim += temp;
            }
        }
        else{
            int temp = 1;
            ignoredGim += temp;
        }
    }

}