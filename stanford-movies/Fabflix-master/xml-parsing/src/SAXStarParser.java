import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class SAXStarParser extends DefaultHandler{

    //////////////////////// Constants /////////////////////////////////
    private static final String pathToXmlFile = "../stanford-movies/casts124.xml";
    private static final String dbUser = "testuser";
    private static final String dbPassword = "testpass";
    private static final String dbName = "moviedb";
    private static final String dbUrl = "jdbc:mysql:///" + dbName;

    /////////////////////// Flags //////////////////////////////////////
    private boolean bTitle = false;
    private boolean bActor = false;
    private boolean bFilmID = false;

    /////////////////////// Temp Object ////////////////////////////////
    private StarInMovie tempStarInMovie; 

    /////////////////////// DB Connection Object ///////////////////////
    private Connection connection;


    ////////////////////// In Memory Cache /////////////////////////////
    private Map<StarInMovie, Integer> starsMap;   // Maps stars to their id in the database.
    private Map<StarInMovie, Integer> movieMap;   // Maps movies to their id in the database.
    private Set<StarInMovie> starsInMoviesSet;    // Set of all stars in the database.
    
    private void init(HashMap<GenreInMovie, Integer> movieMapFromMovieParser)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        
        Statement statement = connection.createStatement();
        String getAllStars = "SELECT * FROM stars; "; 
        ResultSet allStars = statement.executeQuery(getAllStars); 
        while (allStars.next())
            starsMap.put(new StarInMovie(allStars.getString("first_name").toLowerCase().trim(), allStars.getString("last_name").toLowerCase().trim()), allStars.getInt("id"));
        allStars.close();
        
        String getStarsInMovies = "SELECT * FROM stars_in_movies; "; 
        ResultSet allStarsInMovies = statement.executeQuery(getStarsInMovies); 
        while (allStarsInMovies.next())
            starsInMoviesSet.add(new StarInMovie(allStarsInMovies.getInt("star_id"), allStarsInMovies.getInt("movie_id"))); 
        allStarsInMovies.close();
        
        statement.close(); 
        
        for (GenreInMovie g : movieMapFromMovieParser.keySet()){
            if (g.getMovieIdString() != null)
                movieMap.put(new StarInMovie(g.getTitle().toLowerCase().trim(), g.getMovieIdString().toLowerCase().trim(), 0), movieMapFromMovieParser.get(g));
            else
                movieMap.put(new StarInMovie(g.getTitle().toLowerCase().trim(), null, 0), movieMapFromMovieParser.get(g));
        }
        
    }
    
    private void insertIntoTable(StarInMovie tempStarInMovie) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
        
        //System.out.println(tempStarInMovie.getFirstName() + " --- " + tempStarInMovie.getActorLastName() + " --- " + tempStarInMovie.getMovieTitle());
        
        if (tempStarInMovie.getFirstName() == null || tempStarInMovie.getActorLastName() == null || tempStarInMovie.getFirstName().equals("") || tempStarInMovie.getActorLastName().equals(""))
            return; 
        
        int insertStarStatus = 0; 
        if (!starsMap.containsKey(new StarInMovie(tempStarInMovie.getFirstName().toLowerCase().trim(), tempStarInMovie.getActorLastName().toLowerCase().trim()))){
            
            String insertStar = "INSERT INTO stars (first_name, last_name) VALUES (?, ?); "; 
            PreparedStatement insertStarStatement = connection.prepareStatement(insertStar);
            insertStarStatement.setString(1, tempStarInMovie.getFirstName());
            insertStarStatement.setString(2, tempStarInMovie.getActorLastName());
            insertStarStatus = insertStarStatement.executeUpdate(); 
            insertStarStatement.close(); 
            
        }
        
        if (insertStarStatus > 0){
            
            String getLastKey = "SELECT LAST_INSERT_ID(); "; 
            PreparedStatement getLastKeyStatement = connection.prepareStatement(getLastKey);
            ResultSet primaryKey = getLastKeyStatement.executeQuery(); 
            primaryKey.next(); 
            starsMap.put(new StarInMovie(tempStarInMovie.getFirstName().toLowerCase().trim(), tempStarInMovie.getActorLastName().toLowerCase().trim()), primaryKey.getInt(1));
            primaryKey.close(); 
            getLastKeyStatement.close(); 
            
        }
        
        if (tempStarInMovie.getMovieTitle() == null || tempStarInMovie.getMovieTitle().equals("") || tempStarInMovie.getFilmID() == null || tempStarInMovie.getFilmID().equals("") || !movieMap.containsKey(new StarInMovie(tempStarInMovie.getMovieTitle().toLowerCase().trim(), tempStarInMovie.getFilmID().toLowerCase().trim(), 0))){
            
            return; 
            
        }
        
        int starID = starsMap.get(new StarInMovie(tempStarInMovie.getFirstName().toLowerCase().trim(), tempStarInMovie.getActorLastName().toLowerCase().trim()));
        int movieID = movieMap.get(new StarInMovie(tempStarInMovie.getMovieTitle().toLowerCase().trim(), tempStarInMovie.getFilmID().toLowerCase().trim(), 0)); 
        
        int insertStarInMovieStatus = 0; 
        if (!starsInMoviesSet.contains(new StarInMovie(starID, movieID))){
            
            String insertStarsInMoviesSqlString = "INSERT INTO stars_in_movies (star_id, movie_id) VALUES (?, ?); ";
            PreparedStatement insertStarsInMoviesStatement = connection.prepareStatement(insertStarsInMoviesSqlString);
            insertStarsInMoviesStatement.setInt(1, starID);
            insertStarsInMoviesStatement.setInt(2, movieID);
            insertStarInMovieStatus = insertStarsInMoviesStatement.executeUpdate(); 
            insertStarsInMoviesStatement.close(); 
            
        }
        
        if (insertStarInMovieStatus > 0){
            starsInMoviesSet.add(new StarInMovie(starID, movieID));
        }
        
    }
    
    public SAXStarParser() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
        
        starsMap = new HashMap<StarInMovie, Integer>();
        movieMap = new HashMap<StarInMovie, Integer>(); 
        starsInMoviesSet = new HashSet<StarInMovie>(); 
        
    
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        connection.setAutoCommit(false);
        
    }
    
    public void run(HashMap<GenreInMovie, Integer> movieMapFromMovieParser)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        
        init(movieMapFromMovieParser);
        parseDocument();
        connection.commit();
        
    }

    private void parseDocument() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
        
            SAXParser sp = factory.newSAXParser();
            sp.parse(pathToXmlFile, this);
        } catch(SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (qName.equalsIgnoreCase("m"))
            tempStarInMovie = new StarInMovie(); 
        else if (qName.equalsIgnoreCase("t"))
            bTitle = true; 
        else if (qName.equalsIgnoreCase("a"))
            bActor = true; 
        else if (qName.equalsIgnoreCase("f"))
            bFilmID = true;

    }
    

    public void characters(char[] ch, int start, int length) throws SAXException {
        
        if (bTitle){
            
            tempStarInMovie.setMovieTitle(new String(ch, start, length).trim());
            bTitle = false; 
            
        }
        
        else if (bActor){
            
            String name[] = new String(ch, start, length).trim().split(" "); 
            if (name.length == 2) {
                
                tempStarInMovie.setFirstName(name[0]);
                tempStarInMovie.setActorLastName(name[1]);
                
            }
            
            bActor = false; 
            
        }
        
        else if (bFilmID){
            
            tempStarInMovie.setFilmID(new String(ch, start, length).trim());
            bFilmID = false; 
            
        }
        
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("m")){
            
            try {
                insertIntoTable(tempStarInMovie);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
            tempStarInMovie = null; 
            
        }
        
    }

}
