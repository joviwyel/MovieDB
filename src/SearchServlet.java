import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
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

/**
 * A servlet that takes input from a html <form> and talks to MySQL moviedb,
 * generates output as a html <table>
 */

// Declaring a WebServlet called FormServlet, which maps to url "/search"
@WebServlet(name = "SearchServlet", urlPatterns = "/search")
public class SearchServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // Use http GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Building page head with title
        out.println("<html><head><title>MovieDBExample: Found Records</title></head>");

        // Building page body
        out.println("<body><h1>MovieDBExample: Found Records</h1>");


        try {

            // Create a new connection to database
            Connection dbCon = dataSource.getConnection();

            // Declare a new statement
            Statement statement = dbCon.createStatement();

            // Retrieve parameter "name" from the http request, which refers to the value of <input name="name"> in index.html
            String title = request.getParameter("title").toLowerCase();
            String year = request.getParameter("year");
            String director = request.getParameter("director").toLowerCase();
            String star = request.getParameter("star").toLowerCase();
            String query = String.format("SELECT * from movies m, stars_in_movies sim, stars s where m.id = sim.movieId and s.id = sim.starId ");

            // Generate a SQL query
            if(title != ""){
                query += "and lower(m.title) like '" + title + "' ";
            }
            if(year != ""){
                query += "and m.year = '" + year + "' ";
            }
            if(director != ""){
                query += "and lower(m.director) like '" + director + "' ";
            }
            if(star != "") {
                query += "and lower(s.name) like '" + star + "' ";
            }

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            // Create a html <table>
            out.println("<table border>");

            // Iterate through each row of rs and create a table row <tr>
            out.println("<tr><td>ID</td><td>Title</td><td>Year</td><td>Director</td><td>Star</td></tr>");
            while (rs.next()) {
                String movie_id = rs.getString("id");
                String movie_title = rs.getString("title");
                String movie_year = rs.getString("year");
                String movie_dir = rs.getString("director");
                String star_name = rs.getString("name");

                out.println(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                        movie_id, movie_title, movie_year, movie_dir, star_name));
            }
            out.println("</table>");


            // Close all structures
            rs.close();
            statement.close();
            dbCon.close();

        } catch (Exception ex) {
            ex.printStackTrace();

            // Output Error Massage to html
            out.println(String.format("<html><head><title>MovieDB: Error</title></head>\n<body><p>SQL error in doGet: %s</p></body></html>", ex.getMessage()));
            return;
        }
        out.close();
    }
}