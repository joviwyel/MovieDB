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
@WebServlet(name = "AddtoServlet", urlPatterns = "/api/addTo")
public class AddtoServlet extends HttpServlet {
//    private static final long serialVersionUID = 3L;

    // Create a dataSource which registered in web.xml
//    private DataSource dataSource;
//
//    public void init(ServletConfig config) {
//        try {
//            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
//        } catch (NamingException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        User myCart = (User) session.getAttribute("user");

        System.out.println("in addtoServlet:");
//        if(session.getAttribute("back") == null){
//            JumpSession mySession = (JumpSession) session.getAttribute("temp");
//            session.setAttribute("back", mySession);
//            System.out.println("in single: " + mySession);
//        }
//        else{
//            JumpSession mySession = (JumpSession) session.getAttribute("back");
//            System.out.println("in single1: " + mySession);
//        }
        // Get a connection from dataSource
//            Connection dbcon = dataSource.getConnection();

//                String temp = myCart.getUsername();
        String addId = "";
        // Retrieve parameter id from url request.
        if (request.getParameter("addId") != null) {
            addId = request.getParameter("addId");
        }
        myCart.addToCart(addId);

        session.setAttribute("user", myCart);

        // for debug
        User debugUser = (User) session.getAttribute("user");
        System.out.print("in add debug:" + debugUser);

        out.close();
    }
}


