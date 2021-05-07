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

        String name = "";
        int birthYear = 0;
        System.out.println("Dashboard: Checking if name is null");
        if (request.getParameter("name") != null) {
            // Retrieve star parameter from url request.
            name = request.getParameter("name");
            birthYear = 0;
            if (request.getParameter("birthYear") != "") {
                birthYear = Integer.parseInt(request.getParameter("birthYear"));
                System.out.println("Dashboard: Birth year entered: " + birthYear);
            }
        }
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            System.out.println("Dashboard: Database connected");
            JsonArray jsonArray = new JsonArray();

            if (name != "") {
                // Get last id from stars
                String getLastIDString = "SELECT MAX(id) AS id from stars";
                PreparedStatement statement_mid = dbcon.prepareStatement(getLastIDString);
                ResultSet rs_mid = statement_mid.executeQuery();
                rs_mid.next();
                String last_id = rs_mid.getString("id");
                System.out.println("Dashboard: Received last id from stars table: " + last_id);

                // Get id from stored procedure get_id
                String getIdString = "CALL get_id('" + last_id + "')";
                CallableStatement statement_id = dbcon.prepareCall(getIdString);
                ResultSet rs_id = statement_id.executeQuery();
                rs_id.next();
                String id = rs_id.getString("id");
                System.out.println("Dashboard: Parsed id from stored procedure: " + id);

                // Insert star into database
                String insertStarString = "INSERT stars(id, name, birthYear) VALUES (?, ?, ?)";
                PreparedStatement statement_insert = dbcon.prepareStatement(insertStarString);
                statement_insert.setString(1, id);
                statement_insert.setString(2, name);
                if (birthYear != 0) {
                    statement_insert.setInt(3, birthYear);
                    int row = statement_insert.executeUpdate();
                    System.out.println("Dashboard: Star with dob inserted into table " + row);
                } else {
                    statement_insert.setString(3, null);
                    int row = statement_insert.executeUpdate();
                    System.out.println("Dashboard: Star without dob inserted into table: " + row);
                }
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("message", "Star inserted successfully!");
                jsonArray.add(jsonObject);
            }
            else{
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("message", "No star inserted.");
                jsonArray.add(jsonObject);
            }

            // Get metadata
            System.out.println("Dashboard: Preparing metadata from database.");
            String metadataString = "SELECT DISTINCT t1.table_name, c1.column_name, c1.data_type " +
                    "FROM information_schema.tables t1, information_schema.columns c1 " +
                    "WHERE t1.table_schema = 'moviedb' AND t1.table_name = c1.table_name";
            PreparedStatement statement_meta = dbcon.prepareStatement(metadataString);
            ResultSet rs_meta = statement_meta.executeQuery();
            System.out.println("Dashboard: Prepared statement executed.");
            System.out.println("Dashboard: Getting metadata from database.");
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
            System.out.println("Dashboard: Done obtaining metadata from database.");
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