import java.sql.SQLException;


public class SAXParserCasts {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

        SAXParserMovies speMovies = new SAXParserMovies();
        SAXParserStars speStars = new SAXParserStars();
        long insertStart;
        long insertEnd;
        long insertMovieStart;
        long insertMovieEnd;
        try {
            insertMovieStart = System.currentTimeMillis();
            speStars.run();
            insertMovieEnd = System.currentTimeMillis();

            insertStart = System.currentTimeMillis();
            speMovies.run();
            insertEnd = System.currentTimeMillis();
            System.out.println("Time in Seconds for insert stars Parser: " + ((insertEnd - insertStart) / 1000.0));
            System.out.println("Time in Seconds for insert Movie Parser: " + ((insertMovieEnd - insertMovieStart) / 1000.0));
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}

