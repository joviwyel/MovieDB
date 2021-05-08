package servlets;

import models.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by kbendick on 4/20/16.
 */
public class CheckoutServlet extends HttpServlet {

    public static final String ccIdParam = "ccId";
    public static final String firstNameParam = "firstName";
    public static final String lastNameParam = "lastName";
    public static final String ccExpirationParam = "ccExpiration";
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        SessionCart cart = (SessionCart) session.getAttribute("cart");
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || session == null || cart == null || cart.getCartItems().isEmpty()) {
           request.setAttribute("error", "Cart is empty!");
           doGet(request, response);
        }
        try {
            String ccId = request.getParameter(ccIdParam);
            String firstName = request.getParameter(firstNameParam);
            String lastName = request.getParameter(lastNameParam);
            String ccExpiration = request.getParameter(ccExpirationParam);

            // TODO: DELETE THIS LOGGING
            System.out.printf("ccId: %s, firstName: %s, lastName: %s, ccExpiration: %s\n", ccId, firstName, lastName, ccExpiration);

            if (CreditCardsDAO.ccIsOnFile(ccId, firstName, lastName, ccExpiration)) {

                boolean transactionDidProcess = SalesDAO.insert(customer, cart);
                if (transactionDidProcess) {
                    System.out.println("Successfully stored transaction.");
                }
                cart.removeAllItemsFromCart();
                String nextJSP = "/jsp/confirmation.jsp";
                response.sendRedirect(getServletContext().getContextPath() + nextJSP);

            } else {
                request.setAttribute("error", "CC not on file");
                doGet(request, response);
            }

        } catch (NullPointerException e) {
            request.setAttribute("error", "There was a problem connecting with the database.");
            doGet(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nextJSP = "/jsp/checkout.jsp";
        getServletContext().getRequestDispatcher(nextJSP).forward(request, response);

    }
}
