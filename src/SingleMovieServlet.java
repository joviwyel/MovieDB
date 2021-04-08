import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
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


// Declaring a WebServlet called SingleMovieServlet, which maps to url "/api/single-movie"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet {
    private static final long serialVersionUID = 3L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

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

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Construct a query with parameter represented by "?"
            String query = "SELECT * from stars_in_movies as sim, movies as m, ratings as r " +
                    "where m.id = sim.movieId and m.id = r.movieId and m.id = ?";

            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statement.setString(1, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {

//                String starId = rs.getString("starId");
//                String starName = rs.getString("name");
//                String starDob = rs.getString("birthYear");

                String movieId = rs.getString("movieId");
                String movieTitle = rs.getString("title");
                String movieYear = rs.getString("year");
                String movieDirector = rs.getString("director");
                String rating = rs.getString("rating");

                // arraylist of genres
                String query2 = "SELECT g.name FROM genres AS g, genres_in_movies AS gim " +
                        "WHERE gim.genreId = g.id AND gim.movieId = '" + movieId + "'";
                Statement statement2 = dbcon.createStatement();
                ResultSet temp1 = statement2.executeQuery(query2);

                ArrayList<String> genreList = new ArrayList<String>();
                while(temp1.next()){
                    genreList.add(temp1.getString("name"));
                }
                // arraylist of strings to JsonArray
                JsonArray genreJA = new Gson().toJsonTree(genreList).getAsJsonArray();

//                // arraylist of arraylist of star_id and name
//                String query1 = "SELECT starId, name FROM stars_in_movies AS sim, stars AS s " +
//                        "WHERE sim.starId = s.id AND sim.movieId = '" + movieId + "'";
//                Statement statement1 = dbcon.createStatement();
//                ResultSet temp = statement1.executeQuery(query1);
//
//                ArrayList<ArrayList<String>> starList = new ArrayList<ArrayList<String>>();
//                while(temp.next()){
//                    ArrayList<String> star1List = new ArrayList<String>();
//                    star1List.add(temp.getString("starId"));
//                    star1List.add(temp.getString("starName"));
//                    starList.add(star1List);
//                }
//
//                // arraylist of string to JsonArray
//                JsonArray starJA = new Gson().toJsonTree(starList).getAsJsonArray();
//                // Create a JsonObject based on the data we retrieve from rs

                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("star_id", starId);
//                jsonObject.addProperty("star_name", starName);
//                jsonObject.addProperty("star_dob", starDob);
                jsonObject.addProperty("movie_id", movieId);
                jsonObject.addProperty("movie_title", movieTitle);
                jsonObject.addProperty("movie_year", movieYear);
                jsonObject.addProperty("movie_dir", movieDirector);
                jsonObject.addProperty("rating", rating);

                jsonObject.add("genre_name", genreJA);

//                jsonObject.add("star_id_name", starJA);

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
