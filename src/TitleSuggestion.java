
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

// server endpoint URL
@WebServlet("/title-suggestion")
public class TitleSuggestion extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json"); // Response mime type

        try {
            // setup the response json array
            JsonArray jsonArray = new JsonArray();
            Connection dbcon = dataSource.getConnection();

            // get the query string from parameter
            String query = request.getParameter("query");
            System.out.println("Query:" + query);
            // return the empty json array if query is null or empty
            if (query == null || query.trim().isEmpty()) {
                response.getWriter().write(jsonArray.toString());
                return;
            }

            // search on superheroes and add the results to JSON Array
            // this example only does a substring match
            // TODO: in project 4, you should do full text search with MySQL to find the matches on movies and stars

            String SuggestQuery = "";
            String[] arr = query.split("\\s+");
            SuggestQuery += "select title, id from movies where match(title) against (" + "'";
            for(String ss : arr){
                System.out.println(ss);
                SuggestQuery += "+" + ss + "* ";
            }

            // for full-text and fuzzy
//            SuggestQuery += "'" + " in boolean mode) OR ed(title, '" + query.toLowerCase() +
//                    "') <= " + (query.length() < 4 ? 0
//                    : query.length() < 5 ? 1
//                    : query.length() < 6 ? 2 : 3) +
//                    " limit 10";

            // can be use for testing full-text only
            SuggestQuery += "'" + " in boolean mode) limit 10";
            System.out.println(SuggestQuery);

            Statement statement = dbcon.createStatement();
            ResultSet rs = statement.executeQuery(SuggestQuery);

            while(rs.next()){
                String title = rs.getString("title");
                String id = rs.getString("id");
                jsonArray.add(generateJsonObject(id, title));
            }

            response.getWriter().write(jsonArray.toString());
            return;
        } catch (Exception e) {
            System.out.println(e);
            response.sendError(500, e.getMessage());
        }
    }

    /*
     * Generate the JSON Object from hero to be like this format:
     * {
     *   "value": "Iron Man",
     *   "data": { "heroID": 11 }
     * }
     *
     */
    private static JsonObject generateJsonObject(String movieId, String movieTitle) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", movieTitle);

        JsonObject additionalDataJsonObject = new JsonObject();
        additionalDataJsonObject.addProperty("id", movieId);

        jsonObject.add("data", additionalDataJsonObject);
        return jsonObject;
    }


}
