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
import java.util.Locale;
import javax.servlet.http.HttpSession;


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
        String sortby1 = "";
        String sortby2 = "";
        String order1 = "";
        String order2 = "";
        String pageNum = "";
        String pageSize = "";

        // set session part for jump
        HttpSession session = request.getSession();
        JumpSession mySession = new JumpSession() ;
        System.out.println("*** Sort at URL: " + request.getParameter("sortby1"));
        System.out.println("*** Page Size at URL: " + request.getParameter("pageSize"));
        if(session.getAttribute("temp") == null) {
            // New movie list (without sort)
            if (request.getParameter("title") != null)
                mySession.setTitle(request.getParameter("title").toLowerCase());
            if (request.getParameter("year") != null)
                mySession.setYear(request.getParameter("year").toLowerCase());
            if (request.getParameter("director") != null)
                mySession.setDirector(request.getParameter("director").toLowerCase());
            if (request.getParameter("star") != null)
                mySession.setStar(request.getParameter("star").toLowerCase());
            if (request.getParameter("genre") != null)
                mySession.setGenre(request.getParameter("genre").toLowerCase());
            if (request.getParameter("letter") != null)
                mySession.setLetter(request.getParameter("letter").toLowerCase());
            session.setAttribute("temp", mySession);
            System.out.println("SESSION (Build new): " + mySession);
            mySession = (JumpSession) session.getAttribute("temp");
        }
        // 进list后的第一页
        else if(session.getAttribute("back") == null) {
            // Sort form submitted, new session temp
            if (request.getParameter("sortby1") != null && (request.getParameter("sortby1") != ""
                    || request.getParameter("pageSize") != "")) {
                mySession = (JumpSession) session.getAttribute("temp");
                if (request.getParameter("sortby1") != null)
                    mySession.setSortby1(request.getParameter("sortby1"));
                if (request.getParameter("order1") != null)
                    mySession.setOrder1(request.getParameter("order1"));
                if (request.getParameter("sortby2") != null)
                    mySession.setSortby2(request.getParameter("sortby2"));
                if (request.getParameter("order2") != null)
                    mySession.setOrder2(request.getParameter("order2"));
                if (request.getParameter("pageSize") != null)
                    mySession.setPageSize(request.getParameter("pageSize"));
                if (request.getParameter("pageNum") != null)
                    mySession.setPageNum(request.getParameter("pageNum"));
                System.out.println("SESSION (Build new with sort): " + mySession);
                mySession = (JumpSession) session.getAttribute("temp");
            }
            // Next/Prev (after page 0), update and set "back"
            else {
                mySession = (JumpSession) session.getAttribute("temp");
                if (request.getParameter("pageNum") != null)
                    mySession.setPageNum(request.getParameter("pageNum"));

                session.setAttribute("back", mySession);
                System.out.println("SESSION (Update next/prev): " + mySession);
                mySession = (JumpSession) session.getAttribute("back");
            }
        }
        // back != null
        else {
            // Sort after back
            if (request.getParameter("sortby1") != null && (
                    request.getParameter("sortby1") != "" || request.getParameter("pageSize") != "")) {
                mySession = (JumpSession) session.getAttribute("temp");
                if (request.getParameter("sortby1") != null)
                    mySession.setSortby1(request.getParameter("sortby1"));
                if (request.getParameter("order1") != null)
                    mySession.setOrder1(request.getParameter("order1"));
                if (request.getParameter("sortby2") != null)
                    mySession.setSortby2(request.getParameter("sortby2"));
                if (request.getParameter("order2") != null)
                    mySession.setOrder2(request.getParameter("order2"));
                if (request.getParameter("pageSize") != null)
                    mySession.setPageSize(request.getParameter("pageSize"));
                if (request.getParameter("pageNum") != null)
                    mySession.setPageNum(request.getParameter("pageNum"));
                System.out.println("SESSION (Build new with sort, after back): " + mySession);
                mySession = (JumpSession) session.getAttribute("temp");
            }
            // Return to main / prevNext after back
            else {
                mySession = (JumpSession) session.getAttribute("back");
                System.out.println("after:" + mySession);
                System.out.println(request.getParameter("pageNum"));
                int temp = Integer.parseInt((request.getParameter("pageNum")));
                System.out.println("temp" + temp);
                if (temp != 0) {
                    System.out.println(request.getParameter("pageNum"));
                    mySession.setPageNum(request.getParameter("pageNum"));
                    session.setAttribute("back", mySession);
                }
                System.out.println("SESSION (Use back session): " + mySession);
                mySession = (JumpSession) session.getAttribute("back");
            }
        }
        //mySession = (JumpSession) session.getAttribute("back");


        if(mySession.getTitle() != null)
            title = mySession.getTitle();
        if(mySession.getYear() != null)
            year = mySession.getYear();
        if(mySession.getDirector() != null)
            director = mySession.getDirector();
        if(mySession.getStar() != null)
            star = mySession.getStar();
        if(mySession.getGenre() != null)
            genre = mySession.getGenre();
        if(mySession.getLetter() != null)
            letter = mySession.getLetter();
        if(mySession.getSortby1() != null)
            sortby1 = mySession.getSortby1();
        if(mySession.getOrder1() != null)
            order1 = mySession.getOrder1();
        if(mySession.getSortby2() != null)
            sortby2 = mySession.getSortby2();
        if(mySession.getOrder2() != null)
            order2 = mySession.getOrder2();
        if(mySession.getPageSize() != null)
            pageSize = mySession.getPageSize();
        if(mySession.getPageNum() != null)
             pageNum = mySession.getPageNum();

        System.out.println("back:" + mySession);

        pageNum = mySession.getPageNum();

        // Set boolean for search/browse
        if(genre != ""){
            browsByGenre = true;
        }
        else if(letter != ""){
            browsByLetter = true;
        }
        else
            search = true;

        // Get page num
        int pageNumInt = 0;
        if(pageNum == null || pageNum == ""){
            pageNumInt = 0;
        }
        else {
            pageNumInt = Integer.parseInt(pageNum);
        }
        // Get page size
        int pageSizeInt;
        if(pageSize == null || pageSize == ""){
            pageSizeInt = 25;
        }
        else{
            pageSizeInt = Integer.parseInt(pageSize);
        }
        int offset = (pageNumInt * pageSizeInt);
        System.out.print("Offset: " + offset + " ");
        System.out.print("Page size: " + pageSizeInt + "\n");


        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Declare our statement

            JsonArray jsonArray = new JsonArray();
            Statement moviesIds = dbcon.createStatement();
            ResultSet moviesIdrs = null;

            // Browse by genre option selected
            if(browsByGenre) {
                String genresIdquery = "SELECT id from genres where name = '" + genre + "'";

                Statement genresIds = dbcon.createStatement();
                ResultSet genresIdrs = genresIds.executeQuery(genresIdquery);
                genresIdrs.next();
                String genresId_inmovies = genresIdrs.getString("id");

                String moviesIdquery = "Select gim.movieId from genres_in_movies gim, movies m, ratings r " +
                        "where r.movieId = m.id and gim.movieId = m.id and gim.genreId ='" + genresId_inmovies + "' ";
                // Sort
                if(!sortby1.equals("") && !order1.equals("") && !sortby2.equals("") && !order2.equals("")){
                    moviesIdquery += "ORDER BY " + sortby1 + " " + order1 + ", " + sortby2 + " " + order2 + " ";

                }
                // Pagination
                moviesIdquery += " LIMIT " + pageSizeInt + " OFFSET " + offset;

                moviesIdrs = moviesIds.executeQuery(moviesIdquery);
            }
            // Browse by letter option selected
            else if (browsByLetter) {
                if(letter.length() > 3) {
//                    String temp = "^[A-Z0-9a-z]";
                    String letterIdquery = "select distinct m.id as movieId from movies m, ratings r " +
                            "where m.id = r.movieId and m.title not regexp '" + "^[A-Z0-9a-z]' ";

                    moviesIdrs = moviesIds.executeQuery(letterIdquery);
                }
                else {
                    String letterIdquery = "SELECT distinct m.id as movieId, m.title, r.rating " +
                            "from movies m, ratings r where m.id = r.movieId and lower(m.title) like '"
                            + letter.toLowerCase() + "%' ";

                    // Sort
                    if(!sortby1.equals("") && !order1.equals("") && !sortby2.equals("") && !order2.equals("")){
                        letterIdquery += "ORDER BY " + sortby1 + " " + order1 + ", " + sortby2 + " " + order2;

                    }
                    // Pagination
                    letterIdquery += " LIMIT " + pageSizeInt + " OFFSET " + offset;

                    moviesIdrs = moviesIds.executeQuery(letterIdquery);
                }
            }

            // Search option selected
            else if(search) {
                String moviesIdquery = "SELECT distinct m.id as movieId, r.rating, m.title from movies m, " +
                        "stars_in_movies sim, ratings r, " +
                        "stars s where m.id = sim.movieId and r.movieId = m.id and s.id = sim.starId ";

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
                if(!sortby1.equals("") && !order1.equals("") && !sortby2.equals("") && !order2.equals("")){
                    moviesIdquery += "ORDER BY " + sortby1 + " " + order1 + ", " + sortby2 + " " + order2;

                }
                // Pagination
                moviesIdquery += " LIMIT " + pageSizeInt + " OFFSET " + offset;

                moviesIdrs = moviesIds.executeQuery(moviesIdquery);
            }



            // Populate JSON Array
            while (moviesIdrs.next()) {
                String movies_id = moviesIdrs.getString("movieId");

                // Perform the query
                String query = "SELECT id, title, year, director, rating FROM movies " +
                        "AS m, ratings AS r WHERE m.id ='" + movies_id + "'" + "AND r.movieId " +
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

                    // Add three stars here order by # of movies
                    String star_query1 = "SELECT DISTINCT sim.starId FROM stars s, stars_in_movies sim " +
                            "WHERE s.id = sim.starId AND" +
                            " sim.starId IN (SELECT starId FROM stars_in_movies WHERE movieId = '" +
                            movie_id + "')" +
                            " GROUP BY sim.starId" +
                            " ORDER BY COUNT(DISTINCT sim.movieId) DESC, s.name ASC" +
                            " LIMIT 3";
                    Statement statement1 = dbcon.createStatement();
                    ResultSet temp = statement1.executeQuery(star_query1);

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
                    String genre_query = "SELECT gim.genreId FROM genres_in_movies gim, genres g" +
                            " WHERE gim.genreId = g.id AND gim.movieId = '" + movie_id + "'" +
                            " ORDER BY g.name ASC LIMIT 3";
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
