
package models;

import java.sql.*;
import java.util.*;

public class MoviesDAO {

    private static int fullTextMinWordLength = 3;

    public static List<Movie> searchForMovieTitlesByStarId(int starId) {
        String sql = "SELECT movies.title AS title, movies.id AS id, movies.year AS year, movies.director AS director, movies.banner_url AS banner_url, movies.trailer_url AS trailer_url "
                + "FROM stars JOIN stars_in_movies JOIN movies " + "WHERE stars.id = stars_in_movies.star_id AND "
                + "stars_in_movies.movie_id = movies.id AND " + "stars.id = " + String.valueOf(starId) + ";";
        List<Movie> matchingMoviesList = new ArrayList<Movie>();
        try {
            for (Map<String, Object> movieMap : QueryProcessor.processReadOp(sql)) {
                matchingMoviesList.add(Movie.buildMovieFromRowResult(movieMap));
            }
            return matchingMoviesList;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Movie> searchByMatchingTitleArray(String[] titlesToMatch) {
        if (titlesToMatch == null || titlesToMatch.length == 0) {
            return null;
        }

        // Build the base query
        String sql = "SELECT * FROM movies WHERE title LIKE '%" + titlesToMatch[0] + "%' ";

        // Add the remaining title matches.
        for (String titleToMatch: titlesToMatch) {
            sql += " AND title LIKE '%" + titleToMatch + "%' ";
        }

        try {
            return buildMovieListFromRowResults(QueryProcessor.processReadOp(sql));
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Movie> fullTextSearchByTitle(String[] titlesToMatch) {
        if (titlesToMatch == null || titlesToMatch.length == 0) {
            return null;
        }

        // Boolean AND match all keywords except the last, which must only match by prefix
        String matchString = "";
        for (int i = 0; i < titlesToMatch.length; i++) {

            matchString += "+" + titlesToMatch[i].trim();
            if (i == titlesToMatch.length - 1) {
                matchString += "*";
            } else {
                matchString += " ";
            }
        }

        String sql = "SELECT * FROM movies WHERE MATCH(title) AGAINST(? IN BOOLEAN MODE) ORDER BY title DESC;";

        try {

            PreparedStatement preparedStatement = DBConnectionManager.getInstance()
                                                        .getConnection()
                                                        .prepareStatement(sql);
            preparedStatement.setString(1, matchString);
            return buildMovieListFromRowResults(QueryProcessor.processReadOp(preparedStatement));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Movie> searchByName(String name, Integer limit, Integer offset) {

        // Build the sql string.
        String sql =   "SELECT * "
                     + "FROM movies "
                     + "WHERE title='" + name + "' ";
        if (limit != null) {
            sql = sql + " LIMIT " + String.valueOf(limit);
            if (offset != null) {
                sql = sql + " OFFSET " + String.valueOf(offset);
            }
        }

        try {
            return buildMovieListFromRowResults(QueryProcessor.processReadOp(sql));
        } catch (Exception e) {
            System.out.println("MoviesDAO.searchByName("+(name != null ? name : "null" )+"): Exception " + e.getClass() + ": " + e.getMessage());
            return null;
        }
    }

    public static List<Movie> searchByYear(Integer year, Integer limit, Integer offset) {
        String sql =   "SELECT title, id, year, director, banner_url, trailer_url "
                      + "FROM movies "
                      + "WHERE year=" + String.valueOf(year);
        if (limit != null) {
            sql = sql + " LIMIT " + String.valueOf(limit);
            if (offset != null) {
                sql = sql + " OFFSET " + String.valueOf(offset);
            }
        }

        try {
            return buildMovieListFromRowResults(QueryProcessor.processReadOp(sql));
        } catch (Exception e) {
            System.out.println("MoviesDAO.searchByYear("+(year != null ? String.valueOf(year) : "null" )+"): Exception " + e.getClass() + ": " + e.getMessage());
            return null;
        }
    }

    public static List<Movie> searchByGenre(String genre, Integer limit, Integer offset) {
        String sql =   "SELECT movies.title AS title, movies.id AS id, movies.year AS year, movies.director AS director, movies.banner_url AS banner_url, movies.trailer_url AS trailer_url "
                     + "FROM genres JOIN genres_in_movies JOIN movies "
                     + "WHERE genres.id = genres_in_movies.genres_id AND genres_in_movies.movie_id = movies.id AND genres.name = '" + genre + "' ";
        if (limit != null) {
            sql = sql + " LIMIT " + String.valueOf(limit);
            if (offset != null) {
                sql = sql + " OFFSET " + String.valueOf(offset);
            }
        }

        try {
            return buildMovieListFromRowResults(QueryProcessor.processReadOp(sql));
        } catch (Exception e) {
            System.out.println("MoviesDAO.searchByGenre("+(genre != null ? genre : "null" )+"): Exception " + e.getClass() + ": " + e.getMessage());
            return null;
        }
    }

    public static List<Movie> searchByGenreName(String genreId) {
        String sqlFmtString = "SELECT movies.id, movies.title, movies.year, movies.director, movies.banner_url, movies.trailer_url, "
                +        "stars.id AS star_id, stars.first_name AS star_first_name, stars.last_name AS star_last_name, "
                +        "genres.id AS genre_id, genres.name AS genre_name "
                + "FROM movies "
                + "JOIN stars_in_movies ON stars_in_movies.movie_id = movies.id "
                + "JOIN stars ON stars_in_movies.star_id = stars.id "
                + "JOIN genres_in_movies ON genres_in_movies.movie_id = movies.id "
                + "JOIN genres ON genres.id = genres_in_movies.genre_id "
                + "WHERE 1=1 AND genres.id = %d ";
        String sql = String.format(sqlFmtString, genreId);

        try {
            return buildMovieListWithStarsAndGenres(QueryProcessor.processReadOp(sql));
        } catch (Exception e) {
            System.out.println("MoviesDAO.searchByGenre("+(genreId != null ? genreId : "null" )+"): Exception " + e.getClass() + ": " + e.getMessage());
            return null;
        }
    }

    public static List<Movie> searchByGenreId(Integer genreId) {
        String sqlFmtString = "SELECT movies.id, movies.title, movies.year, movies.director, movies.banner_url, movies.trailer_url, "
                              +        "stars.id AS star_id, stars.first_name AS star_first_name, stars.last_name AS star_last_name, "
                              +        "genres.id AS genre_id, genres.name AS genre_name "
                              + "FROM movies "
                              + "JOIN stars_in_movies ON stars_in_movies.movie_id = movies.id "
                              + "JOIN stars ON stars_in_movies.star_id = stars.id "
                              + "JOIN genres_in_movies ON genres_in_movies.movie_id = movies.id "
                              + "JOIN genres ON genres.id = genres_in_movies.genre_id "
                              + "WHERE 1=1 AND genres.id = %d ";
        String sql = String.format(sqlFmtString, genreId);

        try {
            return buildMovieListWithStarsAndGenres(QueryProcessor.processReadOp(sql));
        } catch (Exception e) {
            System.out.println("MoviesDAO.searchByGenre("+(genreId != null ? genreId : "null" )+"): Exception " + e.getClass() + ": " + e.getMessage());
            return null;
        }
    }


    /* Uses PreparedStatement not */
    public static Movie getMovieById(Integer id) {
//      String sqlFmtString = "SELECT movies.id, movies.title, movies.year, movies.director, movies.banner_url, movies.trailer_url, "
//                          +        "stars.id AS star_id, stars.first_name AS star_first_name, stars.last_name AS star_last_name, "
//                          +        "genres.id AS genre_id, genres.name AS genre_name "
//                          + "FROM movies "
//                          + "INNER JOIN stars_in_movies ON stars_in_movies.movie_id = movies.id "
//                          + "INNER JOIN stars ON stars_in_movies.star_id = stars.id "
//                          + "INNER JOIN genres_in_movies ON genres_in_movies.movie_id = movies.id "
//                          + "INNER JOIN genres ON genres.id = genres_in_movies.genre_id "
//                          + "WHERE movies.id = %d ";
//      String sql = String.format(sqlFmtString, id);

        String sql= "SELECT movies.id, movies.title, movies.year, movies.director, movies.banner_url, movies.trailer_url, "
                +        "stars.id AS star_id, stars.first_name AS star_first_name, stars.last_name AS star_last_name, "
                +        "genres.id AS genre_id, genres.name AS genre_name "
                + "FROM movies "
                + "INNER JOIN stars_in_movies ON stars_in_movies.movie_id = movies.id "
                + "INNER JOIN stars ON stars_in_movies.star_id = stars.id "
                + "INNER JOIN genres_in_movies ON genres_in_movies.movie_id = movies.id "
                + "INNER JOIN genres ON genres.id = genres_in_movies.genre_id "
                + "WHERE movies.id = ?;";

        try {

            PreparedStatement preparedStmnt = DBConnectionManager.getInstance().getConnection().prepareStatement(sql);
            preparedStmnt.setInt(1, id);
            return buildMovieWithStarsAndGenres(QueryProcessor.processReadOp(preparedStmnt));
        } catch (Exception e) {
            System.out.println("MoviesDAO.searchById("+(id != null ? id : "null" )+"): Exception " + e.getClass() + ": " + e.getMessage());
            return null;
        }
    }

    private static List<Movie> buildMovieListWithStarsAndGenres(List<Map<String, Object>> rowResults) {

        Map<Integer, Movie> movieIdToMovieMap = new HashMap<Integer, Movie>();
        try {

            for (Map<String, Object> row : rowResults) {

                Star starFromRow = MoviesDAO.buildStarFromMovieResultRow(row);
                Genre genreFromRow = MoviesDAO.buildGenreFromMovieResultRow(row);

                // TODO DELETE ME.
                Movie movie = Movie.buildMovieFromRowResult(row);

                if (!movieIdToMovieMap.containsKey(movie.getId())) {
                    movieIdToMovieMap.put(movie.getId(), movie);
                }

                // Use the running copy of the movie.
                movie = movieIdToMovieMap.get(movie.getId());

                movie.addStarIfAbsent(starFromRow);
                movie.addGenreIfAbsent(genreFromRow);

            }

            return new ArrayList<Movie>(movieIdToMovieMap.values());

        } catch (Exception e) {
            return null;
        }
    }

    private static Star buildStarFromMovieResultRow(Map<String, Object> rowResult) {
        String starFirstName = (String) rowResult.get("star_first_name");
        String starLastName = (String) rowResult.get("star_last_name");
        Integer starId = (Integer) rowResult.get("star_id");
        if (starId != null) {
            Star star = new Star();
            star.setFirstName(starFirstName);
            star.setLastName(starLastName);
            star.setId(starId);
            return star;
        } else {
            return null;
        }
    }

    private static Genre buildGenreFromMovieResultRow(Map<String, Object> rowResult) {
        String genreName = (String) rowResult.get("genre_name");
        Integer genreId = (Integer) rowResult.get("genre_id");

        if (genreId != null && genreName != null) {
            Genre genre = new Genre();
            genre.setName(genreName);
            genre.setId(genreId);
            return genre;
        } else {
            return null;
        }
    }


    private static Movie buildMovieWithStarsAndGenres(List<Map<String, Object>> rowResults) {

        Set<Integer> genresSet = new HashSet<Integer>();
        Set<Integer> starsSet = new HashSet<Integer>();

        try {

            // Build the star info
            Movie result = Movie.buildMovieFromRowResult(rowResults.get(0));

            // Add the genres and stars to their respective map.
            // Use Sets to eliminate possible duplicates between rows.
            for (Map<String, Object> rowResult : rowResults) {

                String starFirstName = (String) rowResult.get("star_first_name");
                String starLastName = (String) rowResult.get("star_last_name");
                Integer starId = (Integer) rowResult.get("star_id");

                if (starId != null && !starsSet.contains(starId)) {
                    starsSet.add(starId);
                    Star star = new Star();
                    star.setFirstName(starFirstName);
                    star.setLastName(starLastName);
                    star.setId(starId);
                    result.addStar(star);
                }

                String genreName = (String) rowResult.get("genre_name");
                Integer genreId = (Integer) rowResult.get("genre_id");


                // TODO should more gracefully handle duplicate genres.
                if (genreId != null && !genresSet.contains(genreId)) {
                    genresSet.add(genreId);
                    Genre genre = new Genre();
                    genre.setName(genreName);
                    genre.setId(genreId);
                    result.addGenre(genre);
                }

            }

            return result;

        } catch (NullPointerException e) {
            System.out.println("MoviesDAO.buildMovieWithStarsAndGenres: NullPointerException");
            return null;
        }
    }

    private static List<Movie> buildMovieListFromRowResults(List<Map<String, Object>> rowResults) {

        List<Movie> result = new ArrayList<Movie>();
        try {
            for (Map<String, Object> movieMap : rowResults) {
                result.add(Movie.buildMovieFromRowResult(movieMap));
            }
            return result;
        } catch (NullPointerException e) {
            System.out.println("MoviesDAO.buildMovieListFromRowResults: NullPointerException");
            return null;
        }
    }






    // This method will search for the movie
    public static List<Movie> searchMovie(String id, String title, String year, String director, String genre,
                                            String starFirstName, String starLastName, String order,
                                            String firstLetter, Integer page, Integer resultsPerPage) throws SQLException {

        Connection connection = DBConnectionManager.getInstance().getConnection();
        String whereCondition = "WHERE 1 = 1 ";
        String sortCondition =  "ORDER BY movies.title ASC ";
        String limitCondition = "LIMIT " + String.valueOf((page-1) * resultsPerPage) + ", " + String.valueOf(resultsPerPage);                   // ASSUMES 5 resultsPerPage.

        if (id != null) {
            whereCondition = whereCondition + "AND movies.id = " + id + " ";
        }

        if (title != null) {
            whereCondition = whereCondition + "AND title LIKE '%" + title + "%' ";
        }

        if (year != null) {
            whereCondition = whereCondition + "AND year = " + year + " ";
        }

        if (director != null) {
            whereCondition = whereCondition + "AND director LIKE '%" + director + "%' ";
        }

        if (genre != null) {
            whereCondition = whereCondition + "AND genres.id = " + genre + " ";
        }

        if (starFirstName != null) {
            whereCondition = whereCondition + "AND stars.first_name LIKE '%" + starFirstName + "%' ";
        }

        if (starLastName != null) {
            whereCondition = whereCondition + "AND stars.last_name LIKE '%" + starLastName + "%' ";
        }

        if (firstLetter != null) {
            whereCondition = whereCondition + "AND title LIKE '" + firstLetter + "%' ";
        }

        if (order != null && !order.isEmpty()) {
            if (order.equals("titleAscending")) {
                sortCondition = " ORDER BY movies.title ASC ";
            }
            else if (order.equals("titleDescending")) {
                sortCondition = " ORDER BY movies.title DESC ";
            }
            else if (order.equals("yearAscending")) {
                sortCondition = " ORDER BY movies.year ASC ";
            }
            else if (order.equals("yearDescending")) {
                sortCondition = " ORDER BY movies.year DESC ";
            }
        }

        List<Integer> movieIdsFound = executeMovieQuery(whereCondition, sortCondition, limitCondition, connection);
        int totalNumberOfResults = movieIdsFound.size();

        List<Movie> moviesWithDetails = new ArrayList<>();

        int numPages = 1;
        if (totalNumberOfResults > resultsPerPage) {
            numPages = (int) Math.ceil( ((float) totalNumberOfResults) / resultsPerPage);
        }

        try {
            int start = (page-1) * resultsPerPage;
            int end = start + resultsPerPage;
            if ((end > start) && (end > movieIdsFound.size())) {
                end = movieIdsFound.size();
            }
            for (Integer movieId : movieIdsFound.subList(start, end)) {
                moviesWithDetails.add(getMovieById(movieId));
            }
        } catch (Exception e) {

        }

        return moviesWithDetails;
    }

    // This method will search for the movie
    public static List<Movie> searchMovieWithPreparedStatement(String id, String title, String year, String director, String genre,
                                          String starFirstName, String starLastName, String order,
                                          String firstLetter, Integer page, Integer resultsPerPage) throws SQLException {

        Connection connection = DBConnectionManager.getInstance().getConnection();
        String whereCondition = "WHERE 1 = 1 ";
        String sortCondition =  "ORDER BY movies.title ASC ";
        String limitCondition = "LIMIT " + String.valueOf((page-1) * resultsPerPage) + ", " + String.valueOf(resultsPerPage);                   // ASSUMES 5 resultsPerPage.

        if (id != null) {
            whereCondition = whereCondition + "AND movies.id = " + id + " ";
        }

        if (title != null) {
            whereCondition = whereCondition + "AND title LIKE '%" + title + "%' ";
        }

        if (year != null) {
            whereCondition = whereCondition + "AND year = " + year + " ";
        }

        if (director != null) {
            whereCondition = whereCondition + "AND director LIKE '%" + director + "%' ";
        }

        if (genre != null) {
            whereCondition = whereCondition + "AND genres.id = " + genre + " ";
        }

        if (starFirstName != null) {
            whereCondition = whereCondition + "AND stars.first_name LIKE '%" + starFirstName + "%' ";
        }

        if (starLastName != null) {
            whereCondition = whereCondition + "AND stars.last_name LIKE '%" + starLastName + "%' ";
        }

        if (firstLetter != null) {
            whereCondition = whereCondition + "AND title LIKE '" + firstLetter + "%' ";
        }

        if (order != null && !order.isEmpty()) {
            if (order.equals("titleAscending")) {
                sortCondition = " ORDER BY movies.title ASC ";
            }
            else if (order.equals("titleDescending")) {
                sortCondition = " ORDER BY movies.title DESC ";
            }
            else if (order.equals("yearAscending")) {
                sortCondition = " ORDER BY movies.year ASC ";
            }
            else if (order.equals("yearDescending")) {
                sortCondition = " ORDER BY movies.year DESC ";
            }
        }

        List<Integer> movieIdsFound = executeMovieQuery(whereCondition, sortCondition, limitCondition, connection);
        int totalNumberOfResults = movieIdsFound.size();

        List<Movie> moviesWithDetails = new ArrayList<>();

        int numPages = 1;
        if (totalNumberOfResults > resultsPerPage) {
            numPages = (int) Math.ceil( ((float) totalNumberOfResults) / resultsPerPage);
        }

        try {
            int start = (page-1) * resultsPerPage;
            int end = start + resultsPerPage;
            if ((end > start) && (end > movieIdsFound.size())) {
                end = movieIdsFound.size();
            }
            for (Integer movieId : movieIdsFound.subList(start, end)) {
                moviesWithDetails.add(getMovieById(movieId));
            }
        } catch (Exception e) {

        }

        return moviesWithDetails;
    }

    private static List<Integer> executeMovieQuery(String whereCondition, String sortCondition,
                                                   String limitCondition, Connection connection) throws SQLException {
//              String query = "SELECT stars.id, stars.first_name, stars.last_name, stars.dob, stars.photo_url, movies.id, movies.title, movies.year, movies.director, movies.banner_url, movies.trailer_url, "
//                   + "genres.id, genres.name FROM movies "
//                   + "INNER JOIN stars_in_movies ON stars_in_movies.movie_id = movies.id "
//                   + "INNER JOIN stars ON stars_in_movies.star_id = stars.id "
//                   + "INNER JOIN genres_in_movies ON genres_in_movies.movie_id = movies.id "
//                   + "INNER JOIN genres ON genres.id = genres_in_movies.genre_id "
//                   + whereCondition + sortCondition + limitCondition;

        String query = "SELECT movies.id "
                + "FROM movies "
                + "INNER JOIN stars_in_movies ON stars_in_movies.movie_id = movies.id "
                + "INNER JOIN stars ON stars_in_movies.star_id = stars.id "
                + "INNER JOIN genres_in_movies ON genres_in_movies.movie_id = movies.id "
                + "INNER JOIN genres ON genres.id = genres_in_movies.genre_id "
                + whereCondition + sortCondition;

        query = QueryProcessor.preprocess(query);

        // create statement and result
//      Statement statement = connection.createStatement();


        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery(query);

        Set<Integer> movieIdsFoundSoFar = new HashSet<Integer>();
        ArrayList<Integer> orderedMovieIds = new ArrayList<Integer>();

        while (resultSet.next()) {

            Integer nextResult = resultSet.getInt(1);
            if (movieIdsFoundSoFar.add(nextResult)) {
                orderedMovieIds.add(nextResult);
            }
        }

        return orderedMovieIds;

    }

    // This method is a helper method to execute the movie query
//  private static List<Movie> executeMovieQuery(String whereCondition, String sortCondition,
//                                               String limitCondition, String offsetCondition, Connection connection) throws SQLException {
//      String query = "SELECT stars.id, stars.first_name, stars.last_name, stars.dob, stars.photo_url, movies.id, movies.title, movies.year, movies.director, movies.banner_url, movies.trailer_url, "
//                   + "genres.id, genres.name FROM movies "
//                   + "INNER JOIN stars_in_movies ON stars_in_movies.movie_id = movies.id "
//                   + "INNER JOIN stars ON stars_in_movies.star_id = stars.id "
//                   + "INNER JOIN genres_in_movies ON genres_in_movies.movie_id = movies.id "
//                   + "INNER JOIN genres ON genres.id = genres_in_movies.genre_id "
//                   + whereCondition + sortCondition + limitCondition + offsetCondition;
//
//      // create statement and result
//      Statement statement = connection.createStatement();
//      ResultSet resultSet = statement.executeQuery(query);
//
//      LinkedHashMap<Integer, Movie> movieMap = new LinkedHashMap<Integer, Movie>();
//
//      while (resultSet.next()) {
//          Star star = new Star(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getDate(4), resultSet.getString(5));
//          Genre genre = new Genre(resultSet.getInt(12), resultSet.getString(13));
//          Movie movie;
//
//          if (movieMap.containsKey(resultSet.getInt(6))) {
//              movie = movieMap.get(resultSet.getInt(6));
//          }
//          else {
//              movie = new Movie(resultSet.getInt(6), resultSet.getString(7), resultSet.getInt(8), resultSet.getString(9), resultSet.getString(10), resultSet.getString(11));
//          }
//
//          movie.addStarIfAbsent(star);
//          movie.addGenreIfAbsent(genre);
//          movieMap.put(movie.getId(), movie);
//      }
//
//      ArrayList<Movie> movieList = new ArrayList<Movie>(movieMap.values());
//
//      return movieList;
//  }

    public static void addMovieStoredProcedure(Movie movie, Star star, Genre genre) {

    }


}
