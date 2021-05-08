import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

import java.sql.*; 

public class SAXMovieParser extends DefaultHandler{


    //////////////////////// Constants /////////////////////////////////
    private static final String pathToXmlFile = "../stanford-movies/mains243.xml";
    private static final String dbUser = "testuser";
    private static final String dbPassword = "testpass";
    private static final String dbName = "moviedb";
    private static final String dbUrl = "jdbc:mysql:///" + dbName;



    //////////////////////// Flags /////////////////////////////////////
    private boolean bFilm = false; 
    private boolean bTitle = false; 
    private boolean bYear = false; 
    private boolean bDirector = false; 
    private boolean bGenre = false; 
    private boolean bFilmID = false; 
    
    private Connection connection;
    
    private GenreInMovie tempGenreInMovie; 
    
    private HashMap<GenreInMovie, Integer> movieMap;
    private HashMap<GenreInMovie, Integer> movieMapWithID; 
    private HashMap<GenreInMovie, Integer> genreMap;
    private HashSet<GenreInMovie> genresInMoviesSet; 
    
    private void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        
        Statement statement = connection.createStatement();
        String sqlStringForAllMovies = "SELECT * FROM movies; ";
        ResultSet allMovies = statement.executeQuery(sqlStringForAllMovies);
        while (allMovies.next())
            movieMap.put(new GenreInMovie(allMovies.getString("title").toLowerCase().trim(), allMovies.getInt("year"), allMovies.getString("director").toLowerCase().trim(), null), allMovies.getInt("id"));
        allMovies.close(); 
        
        String sqlStringForAllGenres = "SELECT * FROM genres; ";
        ResultSet allGenres = statement.executeQuery(sqlStringForAllGenres);
        while (allGenres.next()) {
            genreMap.put(new GenreInMovie(allGenres.getString("name").toLowerCase().trim()), allGenres.getInt("id"));
        }
        allGenres.close();
        
        String getAllGenreInMovies = "SELECT * FROM genres_in_movies; "; 
        ResultSet allGenresInMovies = statement.executeQuery(getAllGenreInMovies); 
        while (allGenresInMovies.next())
            genresInMoviesSet.add(new GenreInMovie(allGenresInMovies.getInt("genre_id"), allGenresInMovies.getInt("movie_id"))); 
        allGenresInMovies.close();
            
        statement.close(); 
        
    }
    
    
    private void insertIntoTable(GenreInMovie tempGenreInMovie) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{

        if (tempGenreInMovie.getTitle() == null || tempGenreInMovie.getYear() == -1 || tempGenreInMovie.getDirector() == null || tempGenreInMovie.getMovieIdString() == null || tempGenreInMovie.getTitle().equals("") ||
                tempGenreInMovie.getDirector().equals("") || tempGenreInMovie.getMovieIdString().equals("")) {
            return;
        }
        
        int insertMovieStatus = 0; 
        if (!movieMap.containsKey(new GenreInMovie(tempGenreInMovie.getTitle().toLowerCase().trim(), tempGenreInMovie.getYear(), tempGenreInMovie.getDirector().toLowerCase().trim(), null))){
        
            String insertMovie = "INSERT INTO movies (title, year, director) VALUES (?, ?, ?); "; 
            PreparedStatement insertMovieStatement = connection.prepareStatement(insertMovie);
            insertMovieStatement.setString(1, tempGenreInMovie.getTitle());
            insertMovieStatement.setInt(2, tempGenreInMovie.getYear());
            insertMovieStatement.setString(3, tempGenreInMovie.getDirector());
            insertMovieStatus = insertMovieStatement.executeUpdate(); 
            insertMovieStatement.close(); 
        
        }

        if (insertMovieStatus > 0){
            
            String getLastKey = "SELECT LAST_INSERT_ID(); ";
            PreparedStatement getLastKeyStatement = connection.prepareStatement(getLastKey);
            ResultSet insertedId = getLastKeyStatement.executeQuery();
            insertedId.next();
            movieMap.put(new GenreInMovie(tempGenreInMovie.getTitle().toLowerCase().trim(), tempGenreInMovie.getYear(), tempGenreInMovie.getDirector().toLowerCase().trim(), null), insertedId.getInt(1));
            movieMapWithID.put(new GenreInMovie(tempGenreInMovie.getTitle().toLowerCase().trim(), tempGenreInMovie.getYear(), tempGenreInMovie.getDirector().toLowerCase().trim(), tempGenreInMovie.getMovieIdString().toLowerCase().trim()), insertedId.getInt(1));
            insertedId.close();
            getLastKeyStatement.close(); 
            
        }

        if (tempGenreInMovie.getGenre() == null || "".equals(tempGenreInMovie.getGenre())){
            return;
        }
        
        int insertGenreStatus = 0; 
        if (!genreMap.containsKey(new GenreInMovie(tempGenreInMovie.getGenre().toLowerCase().trim()))){
        
            String insertGenreSqlString = "INSERT INTO genres (name) VALUES (?); ";
            PreparedStatement insertGenreStatement = connection.prepareStatement(insertGenreSqlString);
            insertGenreStatement.setString(1, tempGenreInMovie.getGenre());
            insertGenreStatus = insertGenreStatement.executeUpdate(); 
            insertGenreStatement.close(); 
        
        }
        
        if (insertGenreStatus > 0){
            
            String getLastKey = "SELECT LAST_INSERT_ID(); "; 
            PreparedStatement getLastKeyStatement = connection.prepareStatement(getLastKey);
            ResultSet primaryKey = getLastKeyStatement.executeQuery(); 
            primaryKey.next(); 
            genreMap.put(new GenreInMovie(tempGenreInMovie.getGenre().toLowerCase().trim()), primaryKey.getInt(1)); 
            primaryKey.close(); 
            getLastKeyStatement.close();
            
        }
        

        int genreID = genreMap.get(new GenreInMovie(tempGenreInMovie.getGenre().toLowerCase().trim())); 
        int movieID = movieMap.get(new GenreInMovie(tempGenreInMovie.getTitle().toLowerCase().trim(), tempGenreInMovie.getYear(), tempGenreInMovie.getDirector().toLowerCase().trim(), tempGenreInMovie.getMovieIdString().toLowerCase().trim()));
        
        int insertGenresInMoviesStatus = 0; 
        if (!genresInMoviesSet.contains(new GenreInMovie(genreID, movieID))){
            
            String insertGenreInMovie = "INSERT INTO genres_in_movies (genre_id, movie_id) VALUES (?, ?); "; 
            PreparedStatement insertGenreInMovieStatement = connection.prepareStatement(insertGenreInMovie);
            insertGenreInMovieStatement.setInt(1, genreID);
            insertGenreInMovieStatement.setInt(2, movieID);
            insertGenresInMoviesStatus = insertGenreInMovieStatement.executeUpdate(); 
            insertGenreInMovieStatement.close(); 
            
        }
        
        if (insertGenresInMoviesStatus > 0){
            
            genresInMoviesSet.add(new GenreInMovie(genreID, movieID)); 
            
        }
        
    }
    
    private boolean isValidYear(String year){
        for (int i = 0; i < year.length(); i++){
            if (!Character.isDigit(year.charAt(i)))
                return false;
        }
        return true;
    }

    public SAXMovieParser() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
        
        movieMap = new HashMap<GenreInMovie, Integer>(); 
        movieMapWithID = new HashMap<GenreInMovie, Integer>(); 
        genreMap = new HashMap<GenreInMovie, Integer>(); 
        genresInMoviesSet = new HashSet<GenreInMovie>(); 
        
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        connection.setAutoCommit(false);
        
    }
    
    public void run() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        
        init();
        parseDocument();
        connection.commit();
        
    }

    private void parseDocument() {
        
        
        SAXParserFactory spf = SAXParserFactory.newInstance();

        try {
            SAXParser sp = spf.newSAXParser();
            sp.parse(pathToXmlFile, this);
        } catch(SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        
        if (qName.equalsIgnoreCase("film")){
            
            tempGenreInMovie = new GenreInMovie(); 
            bFilm = true; 
            
        }
        else if (qName.equalsIgnoreCase("t"))
            bTitle = true; 
        else if (qName.equalsIgnoreCase("year"))
            bYear = true; 
        else if (qName.equalsIgnoreCase("dirn") && bFilm == true)
            bDirector = true; 
        else if (qName.equalsIgnoreCase("cat"))
            bGenre = true; 
        else if (qName.equalsIgnoreCase("fid"))
            bFilmID = true; 
        
    }
    

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        
        if (bTitle){
            
            tempGenreInMovie.setTitle(new String(ch, start, length).trim());
            bTitle = false; 
            
        }
            
        else if (bYear){
            
            if (isValidYear(new String(ch, start, length)))
                tempGenreInMovie.setYear(Integer.parseInt(new String(ch, start, length).trim()));
            else
                tempGenreInMovie.setYear(-1); 
                    
            bYear = false; 
            
        }
            
        else if (bDirector){
            
            tempGenreInMovie.setDirector(new String(ch, start, length).trim());
            bDirector = false; 
            
        }
        
        else if (bGenre){
            
            tempGenreInMovie.setGenre(new String(ch, start, length).trim());
            bGenre = false; 
            
        }
        
        else if (bFilmID){
            
            tempGenreInMovie.setMovieIdString(new String(ch, start, length).trim());
            bFilmID = false; 
            
        }
            
        
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
        if (qName.equalsIgnoreCase("film")){
            
            try {
                insertIntoTable(tempGenreInMovie);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } 
            tempGenreInMovie = null; 
            bFilm = false; 
            
        }
        
    }
    
    public HashMap<GenreInMovie, Integer> getMovieMapWithID(){
        
        return movieMapWithID; 
        
    }
    
}




