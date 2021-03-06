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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;


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

        long startTimeTS = System.nanoTime();
        long elapsedTimeTJ = -1;

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
        if (request.getParameter("mobile") == null) {
            // set session part for jump
            HttpSession session = request.getSession();
            JumpSession mySession = new JumpSession();
            System.out.println("*** Sort at URL: " + request.getParameter("sortby1"));
            System.out.println("*** Page Size at URL: " + request.getParameter("pageSize"));
            if (session.getAttribute("temp") == null) {
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
            // First page of list
            else if (session.getAttribute("back") == null) {
                // Genre clicked on first page, new session temp
                if (request.getParameter("genre") != null && request.getParameter("genre") != "" &&
                        request.getParameter("pageSize") == null) {
                    mySession = (JumpSession) session.getAttribute("temp");
                    if (request.getParameter("genre") != null) {
                        mySession.setGenre(request.getParameter("genre").toLowerCase());
                    }
                    System.out.println("SESSION (Build new with genre 1): " + mySession);
                    mySession = (JumpSession) session.getAttribute("temp");
                }
                // Sort form submitted, new session temp
                else if (request.getParameter("sortby1") != null && (request.getParameter("sortby1") != ""
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
                mySession = (JumpSession) session.getAttribute("temp");
                // Genre clicked on other than first page, new session temp
                if (request.getParameter("genre") != null && request.getParameter("genre") != "" &&
                        request.getParameter("pageSize") == "") {
                    if (request.getParameter("genre") != null) {
                        mySession.setGenre(request.getParameter("genre").toLowerCase());
                    }
                    System.out.println("SESSION (Build new with genre 2): " + mySession);
                    mySession = (JumpSession) session.getAttribute("temp");
                }
                // Sort after back
                else if (request.getParameter("sortby1") != null && (
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


            if (mySession.getTitle() != null)
                title = mySession.getTitle();
            if (mySession.getYear() != null)
                year = mySession.getYear();
            if (mySession.getDirector() != null)
                director = mySession.getDirector();
            if (mySession.getStar() != null)
                star = mySession.getStar();
            if (mySession.getGenre() != null)
                genre = mySession.getGenre();
            if (mySession.getLetter() != null)
                letter = mySession.getLetter();
            if (mySession.getSortby1() != null)
                sortby1 = mySession.getSortby1();
            if (mySession.getOrder1() != null)
                order1 = mySession.getOrder1();
            if (mySession.getSortby2() != null)
                sortby2 = mySession.getSortby2();
            if (mySession.getOrder2() != null)
                order2 = mySession.getOrder2();
            if (mySession.getPageSize() != null)
                pageSize = mySession.getPageSize();
            if (mySession.getPageNum() != null)
                pageNum = mySession.getPageNum();

            System.out.println("back:" + mySession);

            pageNum = mySession.getPageNum();
        }
        else{
            title = request.getParameter("title"); // TODO: CHANGE TO SEARCH
            search = true;
            pageNum = request.getParameter("pageNum");
            pageSize = "20";
        }
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

            long startTimeTJ = System.nanoTime();
            // Declare our statement

            JsonArray jsonArray = new JsonArray();
            Statement moviesIds = dbcon.createStatement();
            ResultSet moviesIdrs = null;
            boolean prepareIf = false;

            // Browse by genre option selected
            if(browsByGenre) {
                String genresIdquery = "SELECT id from genres where name = ?";

                PreparedStatement genresIds = dbcon.prepareStatement(genresIdquery);
                genresIds.setString(1, genre);
                ResultSet genresIdrs = genresIds.executeQuery();
                genresIdrs.next();
                String genresId_inmovies = genresIdrs.getString("id");

                String moviesIdquery = "Select gim.movieId from genres_in_movies gim, movies m, ratings r " +
                        "where r.movieId = m.id and gim.movieId = m.id and gim.genreId = ? ";
                // Sort
                if(!sortby1.equals("") && !order1.equals("") && !sortby2.equals("") && !order2.equals("")){
                    moviesIdquery += " ORDER BY ?  ? ,  ?  ? ";
                    prepareIf = true;
                }

                // Pagination
                moviesIdquery += " LIMIT ? OFFSET ? ";
                PreparedStatement genresIdrs2 = dbcon.prepareStatement(moviesIdquery);
                genresIdrs2.setString(1, genresId_inmovies);
                if (prepareIf == true){
                    genresIdrs2.setString(2, sortby1);
                    genresIdrs2.setString(3, order1);
                    genresIdrs2.setString(4, sortby2);
                    genresIdrs2.setString(5, order2);
                    genresIdrs2.setInt(6, pageSizeInt);
                    genresIdrs2.setInt(7, offset);
                }
                else{
                    genresIdrs2.setInt(2, pageSizeInt);
                    genresIdrs2.setInt(3, offset);
                }
                moviesIdrs = genresIdrs2.executeQuery();
                prepareIf = false; // reset boolean


            }
            // Browse by letter option selected
            else if (browsByLetter) {
                System.out.println("Movie: browse by letter");
                if(letter.length() > 3) {
//                    String temp = "^[A-Z0-9a-z]";
                    String letterIdquery = "select distinct m.id as movieId from movies m, ratings r " +
                            "where m.id = r.movieId and m.title not regexp '" + "^[A-Z0-9a-z]' ";

                    moviesIdrs = moviesIds.executeQuery(letterIdquery);
                }
                else {
                    String letterIdquery = "SELECT distinct m.id as movieId, m.title, r.rating " +
                            "from movies m, ratings r where m.id = r.movieId and lower(m.title) like '?%' ";

                    // Sort
                    if(!sortby1.equals("") && !order1.equals("") && !sortby2.equals("") && !order2.equals("")){
                        letterIdquery += " ORDER BY ? ? , ? ? ";
                        prepareIf = true;
                    }
                    // Pagination
                    letterIdquery += " LIMIT ? OFFSET ? ";
                    PreparedStatement letterIdrs = dbcon.prepareStatement(letterIdquery);
                    letterIdrs.setString(1, letter.toLowerCase());
                    if(prepareIf == true){
                        letterIdrs.setString(2, sortby1);
                        letterIdrs.setString(3, order1);
                        letterIdrs.setString(4, sortby2);
                        letterIdrs.setString(5, order2);
                        letterIdrs.setInt(6, pageSizeInt);
                        letterIdrs.setInt(7, offset);
                    }
                    else{
                        letterIdrs.setInt(2, pageSizeInt);
                        letterIdrs.setInt(3, offset);
                    }
                    moviesIdrs = moviesIds.executeQuery(letterIdquery);
                }
            }

            // Search option selected
            else if(search) {
                String moviesIdquery = "SELECT distinct m.id as movieId, r.rating, m.title from movies m, " +
                        "stars_in_movies sim, ratings r, " +
                        "stars s where m.id = sim.movieId and r.movieId = m.id and s.id = sim.starId ";
                int tempCount = 1;
                List<String> tempString = null;
                if(!year.equals("")){
                    moviesIdquery += "and m.year = '" + year + "' ";
                }
                if(!director.equals("")){
                    moviesIdquery += "and lower(m.director) like '%" + director + "%' ";
                }
                if(!star.equals("")) {
                    moviesIdquery += "and lower(s.name) like '%" + star + "%' ";
                }
                if(!sortby1.equals("") && !order1.equals("") && !sortby2.equals("") && !order2.equals("")){
                    moviesIdquery += "ORDER BY " + sortby1 + " " + order1 + ", " + sortby2 + " " + order2;
                }
                if(!title.equals("")){
                    // full text search
                    moviesIdquery += "and match (title) against ('";
                    String[] arr = title.split("\\s+");
                    for(String ss : arr){
                        moviesIdquery += "+" + ss + "* ";

                    }
                    moviesIdquery += "'" + " in boolean mode)";
                    System.out.println("Now query: " + moviesIdquery);

                    // like search
//                    moviesIdquery += "and lower(m.title) like '%" + title + "%' ";
                }
                // Pagination
                moviesIdquery += " LIMIT " + pageSizeInt + " OFFSET " + offset;
                moviesIdrs = moviesIds.executeQuery(moviesIdquery);
            }

            int checkMore = 0;
            // Populate JSON Array
            while (moviesIdrs.next()) {
                String movies_id = moviesIdrs.getString("movieId");

                // Perform the query
                String query = "SELECT id, title, year, director, rating FROM movies " +
                        "AS m, ratings AS r WHERE m.id = ? AND r.movieId = ?";
                PreparedStatement statement = dbcon.prepareStatement(query);
                statement.setString(1, movies_id);
                statement.setString(2, movies_id);
                ResultSet rs = statement.executeQuery();
                checkMore = moviesIdrs.getRow();
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
                            " sim.starId IN (SELECT starId FROM stars_in_movies WHERE movieId = ?)" +
                            " GROUP BY sim.starId" +
                            " ORDER BY COUNT(DISTINCT sim.movieId) DESC, s.name ASC" +
                            " LIMIT 3";
                    PreparedStatement statement1 = dbcon.prepareStatement(star_query1);
                    statement1.setString(1, movie_id);
                    ResultSet temp = statement1.executeQuery();

                    int i = 1;
                    while (temp.next()) {
                        String star1 = temp.getString("starID");
                        String query2 = "SELECT name FROM stars WHERE id = ? ";
                        PreparedStatement statement2 = dbcon.prepareStatement(query2);
                        statement2.setString(1, star1);
                        ResultSet temp1 = statement2.executeQuery();
                        temp1.next();
                        String star_name = temp1.getString("name");
                        jsonObject.addProperty("star_name" + i, star_name);
                        jsonObject.addProperty("star_id" + i, star1);
                        i++;
                    }
                    temp.close();
                    statement1.close();

                    // Add three genre here
                    String genre_query = "SELECT gim.genreId FROM genres_in_movies gim, genres g" +
                            " WHERE gim.genreId = g.id AND gim.movieId = ? " +
                            " ORDER BY g.name ASC LIMIT 3";
                    PreparedStatement statement3 = dbcon.prepareStatement(genre_query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    statement3.setString(1, movie_id);
                    ResultSet temp2 = statement3.executeQuery();

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
                            String query2 = "SELECT name FROM genres WHERE id = ? ";
                            PreparedStatement statement4 = dbcon.prepareStatement(query2);
                            statement4.setString(1, genre1);
                            ResultSet temp3 = statement4.executeQuery();
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
                    temp2.close();
                    statement3.close();

                    // Create a JsonObject based on the data we retrieve from rs

                    jsonObject.addProperty("movie_id", movie_id);
                    jsonObject.addProperty("movie_name", movie_name);
                    jsonObject.addProperty("movie_year", movie_year);
                    jsonObject.addProperty("movie_dir", movie_dir);
                    jsonObject.addProperty("rating", movie_rat);

                    jsonArray.add(jsonObject);
                }
                rs.close();
                statement.close();

            }
            moviesIdrs.close();
            moviesIds.close();

            // check more
            JsonObject checkMoreJson = new JsonObject();

            System.out.println("checkmore:" + checkMore);
            System.out.println("pageSize:" + pageSizeInt);
            if(checkMore < pageSizeInt){
                checkMoreJson.addProperty("more", false);
            }
            else{
                checkMoreJson.addProperty("more", true);
            }
            jsonArray.add(checkMoreJson);
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);
            dbcon.close();

            long endTimeTJ = System.nanoTime();
            elapsedTimeTJ = endTimeTJ - startTimeTJ;
            long endTimeTS = System.nanoTime();
            long elapsedTimeTS = endTimeTS - startTimeTS; // elapsed time in nano seconds. Note: print the values in nano seconds
            System.out.println("Path:  " + request.getSession().getServletContext().getRealPath("/"));
            File file = new File( "/home/ubuntu/tomcat/webapps/log.txt");
            if(!file.exists()){
                file.createNewFile();
                FileWriter myWriter = new FileWriter(file);
                myWriter.write("TS : " + elapsedTimeTS + ", TJ : " + elapsedTimeTJ + "\n");
                myWriter.close();
            }
            else{
                FileWriter myWriter = new FileWriter("/home/ubuntu/tomcat/webapps/log.txt",true);
                myWriter.write("TS : " + elapsedTimeTS + ", TJ : " + elapsedTimeTJ + "\n");
                myWriter.close();
            }

        } catch (Exception e) {

            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set response status to 500 (Internal Server Error)
            response.setStatus(300);

        } finally {
            out.close();
        }
    }
}
