import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpSession;


@WebServlet(name = "DashboardServlet", urlPatterns = "/_dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 3L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type
        System.out.println("Dashboard: In servlet");

        // Initialize star
        String name = "";
        int birthYear = 0;
        System.out.println("Dashboard: Checking if name is null");
        if (request.getParameter("name") != null) {
            System.out.println("Dashboard: Name not null");
            // Retrieve star parameter from url request.
            name = request.getParameter("name");
            birthYear = 0;
            if (request.getParameter("birthYear") != "") {
                birthYear = Integer.parseInt(request.getParameter("birthYear"));
                System.out.println("Dashboard: Birth year entered: " + birthYear);
            }
        }

        // Initialize movie
        String title = "";
        int year  = 0;
        String director = "";
        String star = "";
        String genre = "";
        float rating = -1;
        System.out.println("Dashboard: Checking if title is null");
        if (request.getParameter("title") != null) {
            System.out.println("Dashboard: Title not null");
            // Retrieve star parameter from url request.
            title = request.getParameter("title");
            director = request.getParameter("director");
            star = request.getParameter("star");
            genre = request.getParameter("genre");
            year = Integer.parseInt(request.getParameter("year"));
            rating = Float.parseFloat(request.getParameter("rating"));
        }

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        System.out.println("Insert star values: " + name + ", " + birthYear);
        System.out.println("Insert movie values: " + title + ", " + director + ", " + star + ", " +
                genre + ", " + year);

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            System.out.println("Dashboard: Database connected");

            JsonArray jsonArray = new JsonArray();
            // Inserting star
            if (name != "" && title == "") {
                System.out.println("Dashboard: Insert star");
                // Get id from stored procedure get_id
                String getIdString = "CALL get_star_id()";
                CallableStatement statement_id = dbcon.prepareCall(getIdString);
                ResultSet rs_id = statement_id.executeQuery();
                rs_id.next();
                String id = rs_id.getString("id");
                System.out.println("Dashboard: Insert star - Parsed id from stored procedure: " + id);

                // Insert star into database
                String insertStarString = "INSERT stars(id, name, birthYear) VALUES (?, ?, ?)";
                PreparedStatement statement_insert = dbcon.prepareStatement(insertStarString);
                statement_insert.setString(1, id);
                statement_insert.setString(2, name);
                if (birthYear != 0) {
                    statement_insert.setInt(3, birthYear);
                    int row = statement_insert.executeUpdate();
                    System.out.println("Dashboard: Insert star - Star with dob inserted into table " + row);
                } else {
                    statement_insert.setString(3, null);
                    int row = statement_insert.executeUpdate();
                    System.out.println("Dashboard: Insert star - Star without dob inserted into table: " + row);
                }
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("message", "Star inserted successfully!\n" +
                        "Star ID: " + id);
                jsonArray.add(jsonObject);
            }
            else{
                System.out.println("Dashboard: No insert star");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("message", "No star inserted.");
                jsonArray.add(jsonObject);
            }

            // Inserting movie
            if (title != "" && name == "") {
                System.out.println("Dashboard: Insert movie");
                // Check if movie exists, then return error message
//                String getMovieString = "SELECT id FROM movies WHERE title = '" + title
//                        + "' AND director = '" + director + "' AND year = " + year;
                String getMovieString = "SELECT id FROM movies WHERE title = ? AND director = ? AND year = ?";
                PreparedStatement statement_movie = dbcon.prepareStatement(getMovieString);
                statement_movie.setString(1, title);
                statement_movie.setString(2, director);
                statement_movie.setInt(3, year);

                ResultSet rs_movie = statement_movie.executeQuery();

                // Duplicated movie
                if (rs_movie.next()){
                    System.out.println("Dashboard: Existing movie");
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("message_movie", "Duplicated movie. No movie inserted.");
                    jsonArray.add(jsonObject);
                }
                // New movie
                else {
                    System.out.println("Dashboard: New movie");
                    // Check if star is new, then create star
                    String getStarString = "SELECT id FROM stars WHERE name = ?";
                    PreparedStatement statement_star = dbcon.prepareStatement(getStarString);
                    statement_star.setString(1,star);
                    ResultSet rs_star = statement_star.executeQuery();
                    //rs_star.next();
                    //System.out.println("Dashboard: Insert movie - after rs_star");
                    String star_id = "";
                    // New star
                    if (!rs_star.next()){
                        // Get id from stored procedure get_id
                        String getIdString = "CALL get_star_id()";
                        CallableStatement statement_id = dbcon.prepareCall(getIdString);
                        ResultSet rs_id = statement_id.executeQuery();
                        rs_id.next();
                        star_id = rs_id.getString("id");
                        System.out.println("Dashboard: Insert movie - Parsed star id from stored procedure for add movie: " + star_id);

                        // Insert star into database
                        String insertStarString = "INSERT stars(id, name, birthYear) VALUES (?, ?, ?)";
                        PreparedStatement statement_insert = dbcon.prepareStatement(insertStarString);
                        statement_insert.setString(1, star_id);
                        statement_insert.setString(2, star);
                        statement_insert.setString(3, null);
                        int row = statement_insert.executeUpdate();
                        System.out.println("Dashboard: Insert movie - Star with dob inserted into table for add movie " + row);
                    }
                    // Star existed
                    else{
                        star_id = rs_star.getString("id");
                        System.out.println("Dashboard: Star existed in database");
                    }

                    // Check if genre is new, then create genre
                    String getGenreString = "SELECT id FROM genres WHERE name = ?";
                    PreparedStatement statement_genre = dbcon.prepareStatement(getGenreString);
                    statement_genre.setString(1, genre);
                    ResultSet rs_genre = statement_genre.executeQuery();
                    //rs_genre.next();
                    String genre_id = "";
                    // New star
                    if (!rs_genre.next()){
                        // Get id from stored procedure get_genre_id
                        String getGenreIdString = "CALL get_genre_id()";
                        CallableStatement statement_genre_id = dbcon.prepareCall(getGenreIdString);
                        ResultSet rs_new_genre_id = statement_genre_id.executeQuery();
                        rs_new_genre_id.next();
                        genre_id = rs_new_genre_id.getString("id");
                        System.out.println("Dashboard: Insert movie - Parsed genre id from stored procedure for add movie: " + genre_id);

                        // Insert genre into database
                        String insertGenreString = "INSERT genres(id, name) VALUES (?, ?)";
                        PreparedStatement statement_insert_genre = dbcon.prepareStatement(insertGenreString);
                        statement_insert_genre.setString(1, genre_id);
                        statement_insert_genre.setString(2, genre);
                        int row = statement_insert_genre.executeUpdate();
                        System.out.println("Dashboard: Insert movie - Genre inserted into table for add movie " + row);
                    }
                    // Genre existed
                    else{
                        genre_id = rs_genre.getString("id");
                        System.out.println("Dashboard: Insert movie - Genre existed in database");
                    }

                    // Create new movie
                    // Get id from stored procedure get_genre_id
                    String getMovieIdString = "CALL get_movie_id()";
                    CallableStatement statement_movie_id = dbcon.prepareCall(getMovieIdString);
                    ResultSet rs_new_movie_id = statement_movie_id.executeQuery();
                    rs_new_movie_id.next();
                    String movie_id = rs_new_movie_id.getString("id");
                    System.out.println("Dashboard: Insert movie - Parsed movie id from stored procedure for add movie: " + movie_id);

                    // Call stored procedure insert_movie
                    String insertMovieString = "CALL add_movie('" + movie_id + "', '" + title + "', "
                            + year + ", '" + director + "')";
                    CallableStatement statement_insert_movie = dbcon.prepareCall(insertMovieString);
                    statement_insert_movie.executeQuery();
                    System.out.println("Dashboard: Insert movie - Insert movie id from stored procedure for add movie: " + movie_id);

                    // Add ratings
                    String insertRatingString = "INSERT ratings(movieId, rating, numVotes) VALUES (?, ?, 0)";
                    PreparedStatement statement_insertRating = dbcon.prepareStatement(insertRatingString);
                    statement_insertRating.setString(1, movie_id);
                    statement_insertRating.setFloat(2, rating);
                    int row = statement_insertRating.executeUpdate();
                    System.out.println("Dashboard: Insert movie - ratings inserted into table for add movie " + row);

                    // Link star to movie
                    String insertSIMString = "INSERT stars_in_movies(starId, movieId) VALUES (?, ?)";
                    PreparedStatement statement_insertSIM = dbcon.prepareStatement(insertSIMString);
                    statement_insertSIM.setString(1, star_id);
                    statement_insertSIM.setString(2, movie_id);
                    row = statement_insertSIM.executeUpdate();
                    System.out.println("Dashboard: Insert movie - stars_in_movies inserted into table for add movie " + row);

                    // Link genre to movie
                    String insertGIMString = "INSERT genres_in_movies(genreId, movieId) VALUES (?, ?)";
                    PreparedStatement statement_insertGIM = dbcon.prepareStatement(insertGIMString);
                    statement_insertGIM.setString(1, genre_id);
                    statement_insertGIM.setString(2, movie_id);
                    row = statement_insertGIM.executeUpdate();
                    System.out.println("Dashboard: Insert movie - genres_in_movies inserted into table for add movie " + row);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("message_movie", "Movie inserted successfully! \n" +
                            "Movie ID: " + movie_id + ", Genre ID: " + genre_id + ", Star ID: " + star_id);
                    jsonArray.add(jsonObject);
                }
            }
            else{
                System.out.println("Dashboard: No insert movie");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("message_movie", "No movie inserted.");
                jsonArray.add(jsonObject);
            }

            // Get metadata
            System.out.println("Dashboard: Metadata - Preparing metadata from database.");
            String metadataString = "SELECT DISTINCT t1.table_name, c1.column_name, c1.data_type " +
                    "FROM information_schema.tables t1, information_schema.columns c1 " +
                    "WHERE t1.table_schema = 'moviedb' AND t1.table_name = c1.table_name";
            PreparedStatement statement_meta = dbcon.prepareStatement(metadataString);
            ResultSet rs_meta = statement_meta.executeQuery();
            System.out.println("Dashboard: Metadata - Prepared statement executed.");
            System.out.println("Dashboard: Metadata - Getting metadata from database.");
            while (rs_meta.next()) {
                JsonObject jsonObject = new JsonObject();
                String tableName = rs_meta.getString("table_name");
                String columnName = rs_meta.getString("column_name");
                String dataType = rs_meta.getString("data_type");
                jsonObject.addProperty("table_name", tableName);
                jsonObject.addProperty("column_name", columnName);
                jsonObject.addProperty("data_type", dataType);
                jsonArray.add(jsonObject);
            }
            System.out.println("Dashboard: Metadata - Done obtaining metadata from database.");
            // Set response status to 200 (OK)
            out.write(jsonArray.toString());
            response.setStatus(200);

            dbcon.close();
        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        }
        out.close();
    }
}