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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;


// Declaring a WebServlet called SingleMovieServlet, which maps to url "/api/single-movie"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet {
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

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        System.out.println("in single:");
        if(session.getAttribute("back") == null){
            JumpSession mySession = (JumpSession) session.getAttribute("temp");
            session.setAttribute("back", mySession);
            System.out.println("Single movie: " + mySession);
        }
        else{
            JumpSession mySession = (JumpSession) session.getAttribute("back");
            System.out.println("in single1: " + mySession);
        }

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Construct a query with parameter represented by "?"
            String query = "SELECT * " +
                    "from ratings r, stars_in_movies sim, movies m  " +
                    "where m.id = sim.movieId and m.id = r.movieId " +
                    "and m.id = ? ";

            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statement.setString(1, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            ArrayList<String> starsIdList = new ArrayList<>();
            ArrayList<String> starsNameList = new ArrayList<>();
            // Iterate through each row of rs
            while (rs.next()) {

                String movieId = rs.getString("movieId");
                String movieTitle = rs.getString("title");
                String movieYear = rs.getString("year");
                String movieDirector = rs.getString("director");
                String rating = rs.getString("rating");

                JsonObject jsonObject = new JsonObject();

                // arraylist of genres
                String genre_query = "SELECT gim.genreId FROM genres_in_movies gim, genres g" +
                        " WHERE gim.genreId = g.id AND gim.movieId = '" + movieId + "'" +
                        " ORDER BY g.name ASC";
                Statement statement4 = dbcon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet temp4 = statement4.executeQuery(genre_query);

                temp4.last();
                int genresnum = temp4.getRow();
                temp4.beforeFirst();

                int loopnum = 3;
                int j = 1;
                while (loopnum != 0) {
                    temp4.next();
                    genresnum--;
                    if (genresnum >= 0) {
                        String genre1 = temp4.getString("genreId");
                        String query2 = "SELECT name FROM genres WHERE id = '" + genre1 + "'";
                        Statement statement5 = dbcon.createStatement();
                        ResultSet temp3 = statement5.executeQuery(query2);
                        temp3.next();
                        String genre_name = temp3.getString("name");
                        jsonObject.addProperty("genre_name" + j, genre_name);
                        j++;
                        loopnum--;
                    } else {
                        jsonObject.addProperty("genre_name" + j, "N/A");
                        j++;
                        loopnum--;
                    }

                }


                String star_query1 = "SELECT DISTINCT sim.starId, s.name FROM stars s, stars_in_movies sim " +
                        "WHERE s.id = sim.starId AND" +
                        " sim.starId IN (SELECT starId FROM stars_in_movies WHERE movieId = '" +
                        movieId + "')" +
                        " GROUP BY sim.starId" +
                        " ORDER BY COUNT(DISTINCT sim.movieId) DESC, s.name ASC";
                Statement statement1_star = dbcon.createStatement();
                ResultSet temp_star = statement1_star.executeQuery(star_query1);


                // Add star
                while(temp_star.next()) {
                    String starId = temp_star.getString("starId");
                    starsIdList.add(starId);
                    String star_name = temp_star.getString("name");
                    starsNameList.add(star_name);
                }
                JsonArray starIDJA = new Gson().toJsonTree(starsIdList).getAsJsonArray();
                JsonArray starNAMEJA = new Gson().toJsonTree(starsNameList).getAsJsonArray();

                jsonObject.addProperty("movie_id", movieId);
                jsonObject.addProperty("movie_title", movieTitle);
                jsonObject.addProperty("movie_year", movieYear);
                jsonObject.addProperty("movie_dir", movieDirector);
                jsonObject.addProperty("rating", rating);

                jsonObject.add("starId", starIDJA);
                jsonObject.add("starName", starNAMEJA);

                jsonArray.add(jsonObject);
            }

            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
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


