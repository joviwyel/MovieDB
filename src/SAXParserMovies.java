
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
    private String newMaxId;
    private String tempVal;
    private Connection connection;
    private HashMap<NewMovie, String> moviesMap;
    //to maintain context
    private NewMovie tempMovie;

    public SAXParserMovies() {

        myNewMovie = new ArrayList<NewMovie>();
        moviesMap = new HashMap<NewMovie, String>();
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
            sp.parse("../cs122b-spring21-team-10/stanford-movies/mains243.xml", this);

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
    private void printData() {
//        Iterator<NewMovie> it = myNewMovie.iterator();
//        while (it.hasNext()) {
//            String temp = it.next().getMovieId();
//            if(temp == null)
//                System.out.println("movieId is null");
//        }
        System.out.println("No of newMovie '" + myNewMovie.size() + "'.");
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
        Statement statement = connection.createStatement();
        String getAllMovies = "SELECT * FROM movies; ";
        ResultSet allMovies = statement.executeQuery(getAllMovies);

        while (allMovies.next()){
            moviesMap.put(new NewMovie(allMovies.getString("title"),
                            allMovies.getInt("year"),
                            allMovies.getString("director"),
                            allMovies.getString("id")),
                            allMovies.getString("id")
            );

        }

        // get max id for insert later
        String getMaxId = "SELECT max(id) from movies;";
        Statement getMaxSt = connection.createStatement();
        ResultSet MaxId = getMaxSt.executeQuery(getMaxId);
        MaxId.next();
        String nowId = MaxId.getString("max(id)");
        newMaxId = nowId;

        System.out.println("maxid is : " + newMaxId);
        System.out.println("old movies table number:" + moviesMap.size());

        allMovies.close();

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
            tempMovie.setMovieID(tempVal);
            if(tempVal == null)
                System.out.println("I am null");
        } else if (qName.equalsIgnoreCase("year")) {
            if(isValidYear(tempVal))
                tempMovie.setYear(Integer.parseInt(tempVal));
            else
                tempMovie.setYear(-1);
        } else if (qName.equalsIgnoreCase("dirn")) {
            tempMovie.setDirector(tempVal);
        }

    }

    private boolean isValidYear(String year){
        for (int i = 0; i < year.length(); i++){
            if (!Character.isDigit(year.charAt(i)))
                return false;
        }
        return true;
    }
    public void insertIntoMovies(NewMovie tempMovie) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{

        if(tempMovie.getTitle() == null)
            return;

        if(!moviesMap.containsKey(tempMovie)){
            String insertMovie = "INSERT INTO movies VALUES(?,?,?,?);";
            PreparedStatement insertMovieStatement = connection.prepareStatement(insertMovie);
            int temp ;
            if(tempMovie.getMovieId() == null){
                String nowId = newMaxId.substring(2);
                temp = Integer.parseInt(nowId);
                temp = temp + 1;
                nowId = Integer.toString(temp);
                newMaxId = "tt" + nowId;
                insertMovieStatement.setString(1, newMaxId);
//                System.out.println("insert id: " + newMaxId);
            }
            else {
                insertMovieStatement.setString(1, tempMovie.getMovieId());
//                System.out.println("insert id: " + tempMovie.getMovieId());
            }
            insertMovieStatement.setString(2, tempMovie.getTitle());
            insertMovieStatement.setInt(3, tempMovie.getYear());
            insertMovieStatement.setString(4, tempMovie.getDirector());
            temp = insertMovieStatement.executeUpdate();
            insertMovieStatus += temp;
//            insertMovieStatus += 1;
            insertMovieStatement.close();
//            System.out.println("insert:");
//
//            System.out.println("insert title:" + tempMovie.getTitle());
//            System.out.println("insert year:" + tempMovie.getYear());
//            System.out.println("insert dir:" + tempMovie.getDirector());
            }
//            System.out.println("Total insert movies:" + insertMovieStatus);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        SAXParserMovies spe = new SAXParserMovies();
        long insertMovieStart;
        long insertMovieEnd;
        insertMovieStart = System.currentTimeMillis();
        spe.run();
        insertMovieEnd = System.currentTimeMillis();
        System.out.println("Time in Seconds for insert Movie Parser: " + ((insertMovieEnd - insertMovieStart) / 1000.0));
    }

}
