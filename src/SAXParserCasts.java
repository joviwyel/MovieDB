
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

public class SAXParserCasts extends DefaultHandler {
    private Connection connection;
    private String tempVal;
    private HashSet<NewStar> simMap;

    List<NewStar> myNewStar;

    private NewStar tempNewStar;

    private HashMap<String, String> SimMovieMap;
    private HashMap<String, String> SimStarMap;

    private SAXParserMovies speMovies ;
    private SAXParserStars speStars ;

    private long insertStart;
    private long insertEnd;
    private long insertMovieStart;
    private long insertMovieEnd;

    private double timeOfStar;
    private double timeOfMovie;

    private int insertSimStatus;
    private int ignoredSim;

    private String reportMovie;
    private String reportStar;

    public SAXParserCasts() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        reportMovie = "";
        reportStar = "";
        simMap = new HashSet<NewStar>();
        myNewStar = new ArrayList<NewStar>();
        speMovies = new SAXParserMovies();
        speStars = new SAXParserStars();
        insertSimStatus = 0;
        ignoredSim = 0;

        insertMovieStart = System.currentTimeMillis();
        speStars.run();
        reportStar = speStars.getReportStar();

        insertMovieEnd = System.currentTimeMillis();

        insertStart = System.currentTimeMillis();
        speMovies.run();
        reportMovie = speMovies.getReport();

        insertEnd = System.currentTimeMillis();

        timeOfStar = (insertEnd - insertStart) / 1000.0;
        timeOfMovie = (insertMovieEnd - insertMovieStart) / 1000.0;

        SimMovieMap = speMovies.getSimMovieMap();
        SimStarMap = speStars.getSimStarMap();


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
    private void printData() {
        System.out.println();
        System.out.println("Total insert stars_in_movie:" + insertSimStatus);
        System.out.println("ignored stars_in_movie: " + ignoredSim);
    }

    private void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

        // get stars_in_movies map
        Statement statement = connection.createStatement();
        String sim = "SELECT * FROM stars_in_movies; ";
        ResultSet allSim = statement.executeQuery(sim);
        while (allSim.next()){
            simMap.add(new NewStar(allSim.getString("starId"),
                    allSim.getString("movieId")));
        }
        allSim.close();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("../stanford-movies/casts124.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("m")) {
            //create a new instance of NewMovie
            tempNewStar = new NewStar();
        }
    }


    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("m")) {
            //add it to the list
            myNewStar.add(tempNewStar);
            try {
                insertIntoSim(tempNewStar);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else if (qName.equalsIgnoreCase("f")) {
            if(tempVal!=null) {
                tempNewStar.setFid(tempVal);
            }
        }  else if(qName.equalsIgnoreCase("a")) {
            tempNewStar.setName(tempVal);
        }
    }

    public void insertIntoSim(NewStar tempStar) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
        String movieId = "";
        String starId = "";
        if(SimMovieMap.containsKey(tempStar.getFid())) {
            movieId = SimMovieMap.get(tempStar.getFid());
            if (SimStarMap.containsKey(tempStar.getName())) {
                starId = SimStarMap.get(tempStar.getName());
                if (!starId.equals("")) {
                    NewStar temp = new NewStar(starId, movieId);
                    if (!simMap.contains(temp)) {
                        int num = 0;
                        String insertStar = "INSERT INTO stars_in_movies VALUES (?, ?);";
                        PreparedStatement insertStarStatement = connection.prepareStatement(insertStar);
                        insertStarStatement.setString(1, temp.getStarId());
                        insertStarStatement.setString(2, temp.getMovieId());
                        simMap.add(temp);
                        num = insertStarStatement.executeUpdate();
                        insertSimStatus += num;
                        insertStarStatement.close();
                    }
                }
            }
            else{
                int temp = 1;
                ignoredSim += temp;
            }
        }
        else{
            int temp = 1;
            ignoredSim += temp;
        }


    }
    public String getReportMovie(){ return reportMovie; }
    public String getReportStar(){return reportStar;}

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

        SAXParserCasts speCasts = new SAXParserCasts();

        try {

            long start;
            long end;
            start = System.currentTimeMillis();
            speCasts.run();
            end = System.currentTimeMillis();

            String reportMovie = "";
            reportMovie = speCasts.getReportMovie();

            String reportStar = "";
            reportStar = speCasts.getReportStar();
            System.out.println(reportStar);
            System.out.println(reportMovie);
            System.out.println("Time in Seconds for Parser and insert for actors63.xml file is: " + speCasts.timeOfStar);
            System.out.println("Time in Seconds for Parser and insert for mains243.xml file is: " + speCasts.timeOfMovie);
            System.out.println("Time in Seconds for Parser and insert for casts124.xml file is: " + (end-start)/1000.0);
            String time = "";
            time += "Time in Seconds for Parser and insert for actors63.xml file is: " + speCasts.timeOfStar + ".\n";
            time += "Time in Seconds for Parser and insert for mains243.xml file is: " + speCasts.timeOfMovie + ".\n";
            time += "Time in Seconds for Parser and insert for casts124.xml file is: " + (end-start)/1000.0 + ".\n";

            File file = new File("ParsingReport.txt");
            if(!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter("ParsingReport.txt", true);
            PrintWriter pw = new PrintWriter(fw);
            pw.print(reportStar);
            pw.flush();
            pw.print(reportMovie);
            pw.flush();
            pw.print(time);
            pw.flush();

            pw.close();
            fw.close();

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

