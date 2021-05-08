
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StarsDAO {
    
    // Will return -1 on null input.
    public static int insert(Star star) throws SQLException {
        
        if (star == null) {
            return -1;
        }

        Connection connection = null;
        PreparedStatement preparedStmnt = null;
        int numUpdates = -1;
        String sqlString = "INSERT INTO stars (first_name, last_name, dob, photo_url) VALUES (?, ?, ?, ?);";
        
        try {
            connection = DBConnectionManager.getInstance().getWriteConnection();
            preparedStmnt = connection.prepareStatement(sqlString);
            
            preparedStmnt.setString(1,  star.getFirstName());
            preparedStmnt.setString(2, star.getLastName());
            preparedStmnt.setDate(3, star.getDob());
            preparedStmnt.setString(4, star.getPhotoUrl());
            
            
            
            numUpdates = preparedStmnt.executeUpdate();
            return numUpdates;
            
        } catch(final Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                preparedStmnt.close();
            } catch (SQLException e) { }

            try {
                connection.close();
            } catch (SQLException e) { }
        }
    }
    
    public static List<Star> searchByFirstOrLastName(String name) {
        String sql = String.format("SELECT * FROM stars WHERE first_name='%s' OR last_name='%s';", name, name); 
        List<Star> matchingStarsList = new ArrayList<Star>();
        try {
            
            // TODO: Remove this
            // System.out.println("In StarDAO.searchByFirstOrLastName search results = " + searchResults);
            for (Map<String, Object> starMap : QueryProcessor.processReadOp(sql)) {
                matchingStarsList.add(Star.buildStarFromRowResult(starMap));
            }

            //TODO: Remove this
            // System.out.println("In StarDAO.searchByFirstOrLastName stars list = " + matchingStarIdsList.toString());;
            
            return matchingStarsList;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static List<Star> searchByFullName(String fullName) {
        Utils.FullName name = new Utils.FullName();
        name.parse(fullName);
        String sql = String.format("SELECT * FROM stars WHERE first_name='%s' AND last_name='%s';", name.firstName, name.lastName); 
        List<Star> matchingStarsList = new ArrayList<Star>();
        try {
            for (Map<String, Object> starMap : QueryProcessor.processReadOp(sql)) {
                matchingStarsList.add(Star.buildStarFromRowResult(starMap));
            }
            return matchingStarsList;
        } catch (Exception e) {
            return null;
        }
    }

    public static Star searchById(Integer id) {
        String sqlFmtString = "SELECT movies.id AS movie_id, movies.title AS movie_title, stars.id, stars.first_name, stars.last_name, stars.dob, stars.photo_url FROM stars"
                            + " INNER JOIN stars_in_movies ON stars_in_movies.star_id=stars.id"
                            + " INNER JOIN movies ON stars_in_movies.movie_id=movies.id"
                            + " WHERE stars.id = %d";
        String sql = String.format(sqlFmtString, id);

        try {
            return buildStarWithMovieListFromRowResults(QueryProcessor.processReadOp(sql));
        } catch (Exception e) {
            System.out.println("StarsDAO.searchById("+(id != null ? id : "null" )+"): Exception " + e.getClass() + ": " + e.getMessage());
            return null;
        }
    }

    private static Star buildStarWithMovieListFromRowResults(List<Map<String, Object>> rowResults) {

        try {

            // Build the star info
            Star result = Star.buildStarFromRowResult(rowResults.get(0));
            System.out.printf("Star: %s\n", result.toString());

            // Add the list of movies, only including the title and the id.
            for (Map<String, Object> rowResult : rowResults) {
                Integer movieId = (Integer) rowResult.get("movie_id");
                String movieTitle = (String) rowResult.get("movie_title");

                if (movieId != null && movieTitle != null)
                    result.addMovie(new Movie(movieId, movieTitle));
            }

            return result;
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("StarsDAO.buildStarWithMovieListFromRowResults: NullPointerException");
            return null;
        }
    }
}
