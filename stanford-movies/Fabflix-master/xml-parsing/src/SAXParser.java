import java.sql.SQLException;

public class SAXParser {
    
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
        
        long MovieParserStart;
        long MovieParserEnd;
        long StarParserStart; 
        long StarParserEnd; 
        
        SAXMovieParser movieParser = new SAXMovieParser();
        SAXStarParser starParser = new SAXStarParser();
        try {
            
            MovieParserStart = System.currentTimeMillis(); 
            movieParser.run();
            MovieParserEnd = System.currentTimeMillis(); 
            System.out.println("Time in Seconds for Movie Parser: " + ((MovieParserEnd - MovieParserStart) / 1000.0));
            
            StarParserStart = System.currentTimeMillis(); 
            starParser.run(movieParser.getMovieMapWithID());
            StarParserEnd = System.currentTimeMillis(); 
            System.out.println("Time in Seconds for Star Parser: " + ((StarParserEnd - StarParserStart) / 1000.0));
            
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }   
    }
}
