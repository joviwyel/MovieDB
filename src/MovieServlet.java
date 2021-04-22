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
import java.sql.ResultSet;
import java.sql.Statement;


// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "MovieServlet", urlPatterns = "/api/movie")
public class MovieServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean search = false;
        boolean browsByGenre = false;
        boolean browsByLetter = false;
        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        String title = "";
        String year = "";
        String director = "";
        String star = "";
        String genre = "";
        String letter = "";

        if(request.getParameter("title") != null) {
            title = request.getParameter("title").toLowerCase();
            year = request.getParameter("year");
            director = request.getParameter("director").toLowerCase();
            star = request.getParameter("star").toLowerCase();
            search = true;
        }
        else {
            genre = request.getParameter("genre");
            letter = request.getParameter("letter");
            if(genre != null)
                browsByGenre = true;
            else
                browsByLetter = true;
        }

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Declare our statement

            JsonArray jsonArray = new JsonArray();
            Statement moviesIds = dbcon.createStatement();
            ResultSet moviesIdrs = null;

            // Browse by letter option selected
            if(browsByGenre) {
                String genresIdquery = "SELECT id from genres where name = '" + genre + "'";

                Statement genresIds = dbcon.createStatement();
                ResultSet genresIdrs = genresIds.executeQuery(genresIdquery);
                genresIdrs.next();
                String genresId_inmovies = genresIdrs.getString("id");

                String moviesIdquery = "Select movieId from genres_in_movies " +
                        "where genreId ='" + genresId_inmovies + "'";
                moviesIdrs = moviesIds.executeQuery(moviesIdquery);
            }
            else if (browsByLetter) {
                if(letter.length() > 3) {
//                    String temp = "^[A-Z0-9a-z]";
                    String letterIdquery = "select distinct id as movieId from movies where title "+
                            "not regexp '" + "^[A-Z0-9a-z]'";
                    moviesIdrs = moviesIds.executeQuery(letterIdquery);
                }
                else {
                    String letterIdquery = "SELECT distinct id as movieId from movies where lower(title) like '"
                            + letter.toLowerCase() + "%'";
                    moviesIdrs = moviesIds.executeQuery(letterIdquery);
                }
            }

            // Search option selected
            else if(search) {
                String moviesIdquery = String.format("SELECT distinct m.id as movieId from movies m, stars_in_movies sim, " +
                        "stars s where m.id = sim.movieId and s.id = sim.starId ");

                if(!title.equals("")){
                    moviesIdquery += "and lower(m.title) like '" + title + "' ";
                }
                if(!year.equals("")){
                    moviesIdquery += "and m.year = '" + year + "' ";
                }
                if(!director.equals("")){
                    moviesIdquery += "and lower(m.director) like '" + director + "' ";
                }
                if(!star.equals("")) {
                    moviesIdquery += "and lower(s.name) like '" + star + "' ";
                }
                moviesIdrs = moviesIds.executeQuery(moviesIdquery);
            }

            // Populate JSON Array
            while (moviesIdrs.next()) {
                String movies_id = moviesIdrs.getString("movieId");

                // Perform the query
                String query = "SELECT id, title, year, director, rating from movies " +
                        "AS m, ratings as r WHERE m.id ='" + movies_id + "'" + "And r.movieId " +
                        "= '" + movies_id + "'";
                Statement statement = dbcon.createStatement();
                ResultSet rs = statement.executeQuery(query);

                // Iterate through each row of rs
                while (rs.next()) {
                    String movie_id = rs.getString("id");
                    String movie_name = rs.getString("title");
                    String movie_year = rs.getString("year");
                    String movie_dir = rs.getString("director");
                    String movie_rat = rs.getString("rating");

                    JsonObject jsonObject = new JsonObject();

                    // Add three stars here
                    String query1 = "SELECT starId FROM stars_in_movies WHERE movieId = '" +
                            movie_id + "'" + " LIMIT 3";
                    Statement statement1 = dbcon.createStatement();
                    ResultSet temp = statement1.executeQuery(query1);

                    int i = 1;
                    while (temp.next()) {
                        String star1 = temp.getString("starID");
                        String query2 = "SELECT name FROM stars WHERE id = '" + star1 + "'";
                        Statement statement2 = dbcon.createStatement();
                        ResultSet temp1 = statement2.executeQuery(query2);
                        temp1.next();
                        String star_name = temp1.getString("name");
                        jsonObject.addProperty("star_name" + i, star_name);
                        jsonObject.addProperty("star_id" + i, star1);
                        i++;
                    }

                    // Add three genre here
                    String genre_query = "SELECT genreId FROM genres_in_movies WHERE movieId = '" +
                            movie_id + "'" + " LIMIT 3";
                    Statement statement3 = dbcon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet temp2 = statement3.executeQuery(genre_query);

                    temp2.last();
                    int genresnum = temp2.getRow();
                    temp2.beforeFirst();

                    int loopnum = 3;
                    int j = 1;
                    while (loopnum != 0) {
                        temp2.next();
                        genresnum--;
                        if (genresnum >= 0) {
                            String genre1 = temp2.getString("genreId");
                            String query2 = "SELECT name FROM genres WHERE id = '" + genre1 + "'";
                            Statement statement4 = dbcon.createStatement();
                            ResultSet temp3 = statement4.executeQuery(query2);
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

                    // Create a JsonObject based on the data we retrieve from rs

                    jsonObject.addProperty("movie_id", movie_id);
                    jsonObject.addProperty("movie_name", movie_name);
                    jsonObject.addProperty("movie_year", movie_year);
                    jsonObject.addProperty("movie_dir", movie_dir);
                    jsonObject.addProperty("rating", movie_rat);

                    jsonArray.add(jsonObject);
                }
            }
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            dbcon.close();


        } catch (Exception e) {

            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);

        } finally {
            out.close();
        }
    }
}
