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

            User myCart = (User) session.getAttribute("user");
            String temp = myCart.getUsername();

            myCart.addToCart(id);

            System.out.println("in add: " + myCart.toString());
            session.setAttribute("user", myCart);

            // for debug
            User debugUser = (User) session.getAttribute("user");
            System.out.print(debugUser);


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


