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
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type


        // Output stream to STDOUT
        PrintWriter out = response.getWriter();



        try {

            Connection dbcon = dataSource.getConnection();

            HttpSession session = request.getSession();

            User myCart = (User) session.getAttribute("user");
            System.out.println("in cart:" + myCart);
            if(session.getAttribute("editCart") == null){
                session.setAttribute("editCart", true);
            }

            int index = 0;
            int qty = 0;
            String convert = "";
            if(request.getParameter("index")!=null){
                System.out.println("index:" + request.getParameter("index"));
                convert =  request.getParameter("index");
                index = Integer.parseInt(convert);
                System.out.println("index in int:" + index);
                if(request.getParameter("qty") == null) {
                    qty = 0;

                }
                else{
                    System.out.println("here");
                    convert = request.getParameter("qty");
                    qty = Integer.parseInt(convert);
                }

                System.out.println("qty in int:" + qty);
                index = index - 1;
                System.out.println("here1");
                myCart.changeQty(index, qty);
                System.out.println(myCart);
            }

            System.out.println(myCart);
            ArrayList<String> temp = myCart.getMyCartList();
            ArrayList<Integer> myQty = myCart.getMyQty();
            ArrayList<Integer> myprice = myCart.getMyPrice();

            double myTotal;
            myTotal = myCart.getMyTotal();

            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < temp.size(); i++) {
                String movieId = temp.get(i);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", movieId);

                String query = "SELECT title from movies where id = '" + movieId + "'";

                Statement cartS = dbcon.createStatement();
                ResultSet rs = cartS.executeQuery(query);


                // Iterate through each row of rs
                rs.next();
                String movieTitle = rs.getString("title");
                jsonObject.addProperty("title", movieTitle);
                jsonObject.addProperty("qty", myQty.get(i).toString());
                System.out.println("qty:" + myQty.get(i));
                jsonObject.addProperty("price", myprice.get(i).toString());
                jsonObject.addProperty("total", myTotal);

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


