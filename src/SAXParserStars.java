
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

public class SAXParserStars extends DefaultHandler {

    List<NewStar> myNewStar;

    private String tempVal;

    //to maintain context
    private NewStar tempStar;

    private Connection connection;


    private HashMap<String, NewStar> starsMap;

    public SAXParserStars() {
        myNewStar = new ArrayList<NewStar>();
        starsMap = new HashMap<String, NewStar>();
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
            System.out.println("here");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection.setAutoCommit(false);
            System.out.println("here1");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
    private void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        Statement statement = connection.createStatement();
        String getAllStars = "SELECT * FROM stars; ";
        ResultSet allStars = statement.executeQuery(getAllStars);
        int i=0;
        while (allStars.next()){

            starsMap.put(allStars.getString("id"),
                    new NewStar(allStars.getString("id"),
                                allStars.getString("name"),
                                allStars.getInt("birthYear"))
            );
            if(i<20){
                System.out.println(starsMap.get(allStars.getString("id")));
            }
            i++;
        }
        allStars.close();

        System.out.println(starsMap.size());
    }
    public void run() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        init();
        parseDocument();
//        printData();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();
            //parse the file and also register this class for call backs
            sp.parse("../cs122b-spring21-team-10/stanford-movies/actors63.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
    private boolean isValidYear(String year){
        if(year.length() == 0)
            return false;
        else{
            for (int i = 0; i < year.length(); i++){
                if (!Character.isDigit(year.charAt(i)))
                    return false;
            }
            return true;
        }
    }
    /**
     * Iterate through the list and print
     * the contents
     */
    private void printData() {
        Iterator<NewStar> it = myNewStar.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
        System.out.println("No of newMovie '" + myNewStar.size() + "'.");
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("actor")) {
            //create a new instance of NewMovie
            tempStar = new NewStar();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("actor")) {
            //add it to the list
            myNewStar.add(tempStar);

        } else if (qName.equalsIgnoreCase("stagename")) {
            tempStar.setName(tempVal);
        } else if (qName.equalsIgnoreCase("dob")) {
            if (isValidYear(tempVal))
                tempStar.setBirthYear(Integer.parseInt(tempVal));
            else
                tempStar.setBirthYear(-1);
        }
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        SAXParserStars spe = new SAXParserStars();
        spe.run();
    }

}
