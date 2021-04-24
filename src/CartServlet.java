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


// Declaring a WebServlet called SingleMovieServlet, which maps to url "/api/single-movie"
@WebServlet(name = "CartServlet", urlPatterns = "/api/shoppingcart")
public class CartServlet extends HttpServlet {
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


        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        System.out.println(session.getAttribute("temp"));
        session.setAttribute("back", true);

        User myCart = (User) session.getAttribute("user");
        System.out.println(myCart);

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            ArrayList<String> temp = myCart.getMyCartList();
            ArrayList<Integer> myQty = myCart.getMyQty();
            ArrayList<Double> myprice = myCart.getMyPrice();
            JsonArray jsonArray = new JsonArray();
            // Construct a query with parameter represented by "?"
            for(int i=0; i<temp.size(); i++) {
                String movieId = temp.get(i);
                String query = "SELECT title from movies where id = '" + movieId + "'";

                Statement cartS = dbcon.createStatement();
                ResultSet rs = cartS.executeQuery(query);

                JsonObject jsonObject = new JsonObject();
                // Iterate through each row of rs
                rs.next();
                String movieTitle = rs.getString("title");
                System.out.println("title is:" + movieTitle);
                jsonObject.addProperty("title", movieTitle);
                System.out.println("title is:1" + movieTitle);
                jsonObject.addProperty("qty", myQty.get(i).toString() );
                System.out.println("title is:2" + movieTitle);
                jsonObject.addProperty("price", myprice.get(i).toString());
                System.out.println("title is:3" + movieTitle);

                jsonArray.add(jsonObject);
            }
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

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


