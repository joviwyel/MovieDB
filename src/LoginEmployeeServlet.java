import com.google.gson.JsonObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

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

@WebServlet(name = "LoginEmployeeServlet", urlPatterns = "/api/login_employee")
public class LoginEmployeeServlet extends HttpServlet {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
        // Verify reCAPTCHA
        try {
            RecaptchaVerifyUtils.verify(gRecaptchaResponse);
        } catch (Exception e) {
            JsonObject responseJsonObject = new JsonObject();

            responseJsonObject.addProperty("status", "fail");
            // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.

            responseJsonObject.addProperty("message", "Invalid reCAPTCHA. Please try again.");

            out.write(responseJsonObject.toString());

            out.close();
            return;
        }
        try{
            String email = request.getParameter("username");
            String password = request.getParameter("password");

            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Declare our statement
            Statement statement = dbcon.createStatement();

            String query = "SELECT password from employees WHERE email = '" + email +"'";
            ResultSet rs = statement.executeQuery(query);

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */
            JsonObject responseJsonObject = new JsonObject();
            boolean success = false;
            while(rs.next()) {
                String encryptedPasswordDb = rs.getString("password");
                success = new StrongPasswordEncryptor().checkPassword(password, encryptedPasswordDb);
            }
            if (success) {
                // Login success:
                // set this user into the session
                request.getSession().setAttribute("user", new User(email));
                request.getSession().setAttribute("employee", new User(email));

                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");
            } else {
                // Login fail
                responseJsonObject.addProperty("status", "fail");
                // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.
                if (password.equals("")) {
                    responseJsonObject.addProperty("message", "employee " + email + " doesn't exist");
                } else {
                    responseJsonObject.addProperty("message", "incorrect password");
                }
            }

            out.write(responseJsonObject.toString());
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

        } finally {
            out.close();
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doGet(request, response);
    }
}

