package models;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DBStoredProcedures {

    // TODO - Refactor this ot parse the parameter map and then pass in the Movie, Star, Genre - TOO HIGHLY COUPLED.
    public static void addMovie(Map<String, String[]> parametersMap)
            throws SQLException {

        CallableStatement callableStatement = null;
        try(Connection connection = DBConnectionManager.getInstance().getWriteConnection()) {

            // Parse the movie from the parameters map. Must not be null.
            Movie movie = Movie.buildMovieFromStringParameters(parametersMap);

            // Parse the star and genre from the parameters map. Might be null?
            Star star = Star.buildStarFromStringParameters(parametersMap);
            Genre genre = Genre.buildGenreFromStringParameters(parametersMap);

            callableStatement = connection.prepareCall("{call add_movie(?,?,?,?,?,?,?,?,?,?)}");


            // Set movie details
            callableStatement.setString(1, movie.getTitle());
            callableStatement.setInt(2, movie.getYear());
            callableStatement.setString(3, movie.getDirector());
            callableStatement.setString(4, movie.getBannerUrl());
            callableStatement.setString(5, movie.getTrailerUrl());

            // Set genre details
            // TODO - add try catch with setting to null in catch if this is not required.
            callableStatement.setString(6, genre.getName());

            // TODO - add try catch with setting to null in catch if this is not required
            // Set star details
            callableStatement.setString(7, star.getFirstName());
            callableStatement.setString(8, star.getLastName());
            callableStatement.setDate(9, star.getDob());
            callableStatement.setString(10, star.getPhotoUrl());


            // Execute
            callableStatement.execute();

        } finally {

            try {
                callableStatement.close();
            } catch (Exception e) {
                // pass
            }

        }

    }

}
