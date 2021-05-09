
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
    private String newMaxId;
    //to maintain context
    private NewStar tempStar;

    private Connection connection;

    private HashMap<String, String> simStarMap;

    private HashMap<String, NewStar> starsMap;
    private int insertStarStatus = 0;

    public SAXParserStars() {
        myNewStar = new ArrayList<NewStar>();
        starsMap = new HashMap<String, NewStar>();
        simStarMap = new HashMap<String, String>();

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
//        Iterator<NewStar> it = myNewStar.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next().toString());
//        }
        System.out.println("No of newStar '" + myNewStar.size() + "'.");
//        System.out.println("Total insert stars:" + insertStarStatus);
//        System.out.println("name + starID:" + simStarMap);
        System.out.println();
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

    private void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        Statement statement = connection.createStatement();
        String getAllStars = "SELECT * FROM stars; ";
        ResultSet allStars = statement.executeQuery(getAllStars);

        while (allStars.next()){
            starsMap.put(allStars.getString("name"),
                    new NewStar(allStars.getString("id"),
                    allStars.getString("name"),
                    allStars.getInt("birthYear"))
            );

        }


        String getMaxId = "SELECT max(id) from stars;";
        Statement getMaxSt = connection.createStatement();
        ResultSet MaxId = getMaxSt.executeQuery(getMaxId);
        MaxId.next();
        String nowId = MaxId.getString("max(id)");
        newMaxId = nowId;

        System.out.println("maxid is : " + newMaxId);
//        System.out.println(starsMap);

        allStars.close();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("actor")) {
            //add it to the list
            myNewStar.add(tempStar);
            try {
                insertIntoStars(tempStar);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else if (qName.equalsIgnoreCase("stagename")) {
            tempStar.setName(tempVal);
        } else if (qName.equalsIgnoreCase("dob")) {
            if (isValidYear(tempVal))
                tempStar.setBirthYear(Integer.parseInt(tempVal));
            else
                tempStar.setBirthYear(-1);
        }
    }

    public HashMap<String, String> getSimStarMap(){ return simStarMap;}

    public void insertIntoStars(NewStar tempStar) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
        if(tempStar.getName() == null)
            return;

        if(!starsMap.containsKey(tempStar.getName())){
            String nowId = newMaxId.substring(2);
            int temp = Integer.parseInt(nowId);
            temp = temp + 1;
            nowId = Integer.toString(temp);
            newMaxId = "nm" + nowId;

            if(tempStar.getBirthYear() <= 0){
                String insertStar = "INSERT INTO stars (id, name) VALUES(?, ?);";
                PreparedStatement insertStarStatement = connection.prepareStatement(insertStar);
                insertStarStatement.setString(1, newMaxId);
                insertStarStatement.setString(2, tempStar.getName());
                tempStar.setStarId(newMaxId);
                temp = insertStarStatement.executeUpdate();
                insertStarStatus += temp;
                insertStarStatement.close();
//                System.out.println("insert:");
//                System.out.println("insert id: " + newMaxId);
//                System.out.println("insert name:" + tempStar.getName());
//                System.out.println("insert num:" + insertStarStatus);
            }
            else{
                String insertStar = "INSERT INTO stars (id, name, birthYear) VALUES(?,?,?);";
                PreparedStatement insertStarStatement = connection.prepareStatement(insertStar);
                insertStarStatement.setString(1, newMaxId);
                insertStarStatement.setString(2, tempStar.getName());
                insertStarStatement.setInt(3, tempStar.getBirthYear());
                tempStar.setStarId(newMaxId);
                temp = insertStarStatement.executeUpdate();
                insertStarStatus += temp;
                insertStarStatement.close();
//                System.out.println("insert:");
//                System.out.println("insert id: " + newMaxId);
//                System.out.println("insert name:" + tempStar.getName());
//                System.out.println("insert num:" + insertStarStatus);
//                System.out.println("insert birthYear:" + tempStar.getBirthYear());
            }
            starsMap.put(newMaxId, tempStar);
            simStarMap.put(tempStar.getName(), tempStar.getStarId());
        }
//        System.out.println("Total insert stars:" + insertStarStatus);
    }


}
