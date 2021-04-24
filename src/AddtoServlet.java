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


// Declaring a WebServlet called SingleMovieServlet, which maps to url "/api/addto
// This is only for calculate the backend data when adding to shopping cart
@WebServlet(name = "AddtoServlet", urlPatterns = "/api/addto")
public class AddtoServlet extends HttpServlet {
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
        String id = request.getParameter("addId");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();


        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            System.out.println(id);

//            // Construct a query with parameter represented by "?"
//            String query = "SELECT * from stars_in_movies as sim, movies as m, ratings as r " +
//                    "where m.id = sim.movieId and m.id = r.movieId and m.id = ?";
//
//            // Declare our statement
//            PreparedStatement statement = dbcon.prepareStatement(query);
//
//            // Set the parameter represented by "?" in the query to the id we get from url,
//            // num 1 indicates the first "?" in the query
//            statement.setString(1, id);
//
//            // Perform the query
//            ResultSet rs = statement.executeQuery();
//
//            JsonArray jsonArray = new JsonArray();
//
//            ArrayList<String> starsIdList = new ArrayList<>();
//            ArrayList<String> starsNameList = new ArrayList<>();
//            // Iterate through each row of rs
//            while (rs.next()) {
//
//                String movieId = rs.getString("movieId");
//                String movieTitle = rs.getString("title");
//                String movieYear = rs.getString("year");
//                String movieDirector = rs.getString("director");
//                String rating = rs.getString("rating");
//
//                // arraylist of genres
//                String query2 = "SELECT g.name FROM genres AS g, genres_in_movies AS gim " +
//                        "WHERE gim.genreId = g.id AND gim.movieId = '" + movieId + "' " +
//                        "ORDER BY g.name ASC";
//                Statement statement2 = dbcon.createStatement();
//                ResultSet temp1 = statement2.executeQuery(query2);
//
//                ArrayList<String> genreList = new ArrayList<String>();
//                while(temp1.next()){
//                    genreList.add(temp1.getString("name"));
//                }
//                // arraylist of strings to JsonArray
//                JsonArray genreJA = new Gson().toJsonTree(genreList).getAsJsonArray();
//
//
//                JsonObject jsonObject = new JsonObject();
//
//
//                // add stars ID
//                String starId = rs.getString("starId");
//                starsIdList.add(starId);
//                JsonArray starIDJA = new Gson().toJsonTree(starsIdList).getAsJsonArray();
//
//                // add stars Name
//                String query_star_name = "SELECT name FROM stars WHERE id = '" + starId + "'";
//                Statement statement3 = dbcon.createStatement();
//                ResultSet temp2 = statement3.executeQuery(query_star_name);
//                temp2.next();
//                String star_name = temp2.getString("name");
//                starsNameList.add(star_name);
//                JsonArray starNAMEJA = new Gson().toJsonTree(starsNameList).getAsJsonArray();
//
//                jsonObject.addProperty("movie_id", movieId);
//                jsonObject.addProperty("movie_title", movieTitle);
//                jsonObject.addProperty("movie_year", movieYear);
//                jsonObject.addProperty("movie_dir", movieDirector);
//                jsonObject.addProperty("rating", rating);
//
//                jsonObject.add("genre_name", genreJA);
//                jsonObject.add("starId", starIDJA);
//                jsonObject.add("starName", starNAMEJA);
//
//                jsonArray.add(jsonObject);
//            }
//
//            // write JSON string to output
//            out.write(jsonArray.toString());
//            // set response status to 200 (OK)
//            response.setStatus(200);
//
//            rs.close();
//            statement.close();
//            dbcon.close();
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


