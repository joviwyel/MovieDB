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
import java.util.Locale;
import javax.servlet.http.HttpSession;


// Declaring a WebServlet called StarsServlet, which maps to url "/api/creditCard"
@WebServlet(name = "CreditCardServlet", urlPatterns = "/api/creditCard")
public class CreditCardServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("-- CREDIT CARD SERVLET: POST --");
        //response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter(); // Output stream to STDOUT

        try {
            Connection dbcon = dataSource.getConnection();
            JsonObject responseJsonObject = new JsonObject();

            // Read data from creditCardForm
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String creditNum = request.getParameter("creditNum");
            String expDate = request.getParameter("expDate");

            // Check if match with data from creditcards table
            if(firstName != "" && lastName != "" && creditNum != "" && expDate != ""){
                System.out.println("-- CREDIT CARD SERVLET: INPUT NOT EMPTY --");
                // Get all information of credit card
                String query_cc = "SELECT * from creditcards where id = '" + creditNum + "' ";

                Statement statement_cc = dbcon.createStatement();
                ResultSet rs_cc = statement_cc.executeQuery(query_cc);

                boolean success = false;

                    String firstNameDB = rs_cc.getString("firstName");
                    String lastNameDB = rs_cc.getString("lastName");
                    java.sql.Date expDateOriDB = rs_cc.getDate("expiration"); // Convert date to string
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String expDateDB = dateFormat.format(expDateOriDB);

                    // Return success
                    if (firstName.equals(firstNameDB) && lastName.equals(lastNameDB) &&
                            expDate.equals(expDateDB)) {
                        System.out.println("-- CREDIT CARD SERVLET: SUCCESS --");
                        success = true;
                        responseJsonObject.addProperty("status", "success");
                        responseJsonObject.addProperty("message", "Payment success!");

                    }
                    // Return error
                    else{
                        System.out.println("-- CREDIT CARD SERVLET: FAILED --");
                        responseJsonObject.addProperty("status", "fail");
                        responseJsonObject.addProperty("message", "Payment failed! Please try again.");
                    }

                rs_cc.close();
                statement_cc.close();
                dbcon.close();
            }
            // Return error
            else{
                System.out.println("-- CREDIT CARD SERVLET: FAILED --");
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Payment failed! Please try again.");
            }

            out.write(responseJsonObject.toString());
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