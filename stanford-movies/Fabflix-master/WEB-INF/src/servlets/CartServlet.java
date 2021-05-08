package servlets;

import models.SessionCart;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by kbendick on 4/20/16.
 */

public class CartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        try {
            Integer movieId = Integer.parseInt(request.getParameter("movieId"));
            Integer quantity = Integer.parseInt(request.getParameter("quantity"));
            Boolean addToCart = Boolean.valueOf(request.getParameter("addToCart"));
            String movieTitle = request.getParameter("movieTitle");
            HttpSession session = request.getSession(false);

            if (session != null) {

                SessionCart cart = (SessionCart) session.getAttribute("cart");

                cart.updateQuantityOfItemInCart(movieId, quantity, addToCart, movieTitle);
            }
        } catch (NumberFormatException e) {
            // Pass.
        } finally {
            String nextJSP = "/cart";
            response.sendRedirect(getServletContext().getContextPath() + nextJSP);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nextJSP = "/jsp/cart.jsp";
        getServletContext().getRequestDispatcher(nextJSP).forward(request, response);
    }
}

