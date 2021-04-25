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

        if(session.getAttribute("editCart") == null){
            System.out.println("in addtoServlet:");

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
        }
        else{
            String type = "";
            String id = "";
            if(request.getParameter("type")!=null)
                type = request.getParameter("type");
            if(request.getParameter("id")!=null)
                id = request.getParameter("id");

            System.out.println("edit Cart: ---- type :" + type);
            System.out.println("edit Cart: ---- id: " + id);

            if(type.equals("plus"))
                myCart.addToCart(id);
            else if(type.equals("min"))
                myCart.minCart(id);
            else if(type.equals("delete"))
                myCart.removeItem(id);

            session.setAttribute("user", myCart);

            // for debug
            System.out.println("after edit in add:" + myCart);

        }

        out.close();
    }
}


