
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class SAXParserMovies extends DefaultHandler {

    List<NewMovie> myNewMovie;

    private String tempVal;

    //to maintain context
    private NewMovie tempMovie;

    public SAXParserMovies() {
        myNewMovie = new ArrayList<NewMovie>();
    }

    public void run() {
        parseDocument();
        printData();
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
        Iterator<NewMovie> it = myNewMovie.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
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

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("film")) {
            //add it to the list
            myNewMovie.add(tempMovie);

        } else if (qName.equalsIgnoreCase("t")) {
            tempMovie.setTitle(tempVal);
        } else if (qName.equalsIgnoreCase("fid")) {
            tempMovie.setMovieID(tempVal);
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

    public static void main(String[] args) {
        SAXParserMovies spe = new SAXParserMovies();
        spe.run();
    }

}
