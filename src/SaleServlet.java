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
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.servlet.http.HttpSession;


// Declaring a WebServlet called StarsServlet, which maps to url "/api/creditCard"
@WebServlet(name = "SaleServlet", urlPatterns = "/api/confirmation")
public class SaleServlet extends HttpServlet {
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
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter(); // Output stream to STDOUT

        try {

            System.out.println("Confirmation.html");

            Connection dbcon = dataSource.getConnection();
//            JsonObject responseJsonObject = new JsonObject();


            // Construct a query with parameter represented by "?"
            HttpSession session = request.getSession();
            User myInfo = (User) session.getAttribute("user");

            JsonArray jsonArray = new JsonArray();

            // get username from session
            String myUsername = myInfo.getUsername();

            String cusIdQuery = "Select id from customers where email ='" + myUsername +"'";

            Statement cusIdSt = dbcon.createStatement();
            ResultSet cusIdRs = cusIdSt.executeQuery(cusIdQuery);
            cusIdRs.next();

            // get customerId which will be recorded into sales
            String cusId = cusIdRs.getString("id");

            // query new salesId
            String salesIdQuery = "select id from sales order by id desc limit 1";
            Statement salesIdSt = dbcon.createStatement();
            ResultSet salesIdRs = salesIdSt.executeQuery(salesIdQuery);
            salesIdRs.next();
            String salesId = salesIdRs.getString("id");

            // get movies title
            for(int i=0; i<myInfo.getMyCartList().size(); i++){
                String movieTitleQuery = "select title from movies where id='"
                        + myInfo.getMyCartList().get(i) + "'";
                Statement movieSt = dbcon.createStatement();
                ResultSet movieRs = movieSt.executeQuery(movieTitleQuery);
                movieRs.next();
                String title = movieRs.getString("title");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("customerId", cusId);
                jsonObject.addProperty("id", salesId);
                jsonObject.addProperty("title", title);
                int qty = myInfo.getMyQty().get(i);
                jsonObject.addProperty("qty", qty);
                int total = myInfo.getMyTotal();
                jsonObject.addProperty("Total", total);

                jsonArray.add(jsonObject);
            }


            out.write(jsonArray.toString());
            response.setStatus(200);

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