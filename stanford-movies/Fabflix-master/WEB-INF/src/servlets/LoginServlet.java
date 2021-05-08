package servlets;

import models.Customer;
import models.CustomersDAO;
import models.Movie;
import models.SessionCart;
import recaptcha.VerifyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            HttpSession session = request.getSession();
            Customer customer = (Customer) session.getAttribute("customer");
            String error = (String) session.getAttribute("error");
        
            // If we're already logged in, we don't need to be at the login page.
            if (customer != null) {
        
                request.setAttribute("error", null);
                response.sendRedirect(request.getContextPath() + "/home");
            
            } else {
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }
        }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("Made it into LoginServlet.doPost!");
        response.setContentType("text/html");
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Boolean fromAndroid = (request.getParameter("fromAndroid") != null && request.getParameter("fromAndroid").equalsIgnoreCase("true") ? true : false);
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

        if (fromAndroid) {
            System.out.println("Received a request from an android device.");
        } else {
            System.out.println("Received a request from a non-android device.");
        }
        
        try {
            login(email, password, gRecaptchaResponse, fromAndroid, request, response);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    private synchronized void login(String email, String password, String gRecaptchaResponse, Boolean fromAndroid, HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException  {

        HttpSession session = request.getSession();

        boolean validRecaptcha = false;
        if (!fromAndroid) {validRecaptcha = VerifyUtils.verify(gRecaptchaResponse);
        }
        Customer customer = CustomersDAO.verifyAccount(email, password);

        if ((fromAndroid || validRecaptcha) && customer != null) {
            
            session.setAttribute("customer", customer);

            if (session.getAttribute("cart") == null) {
               SessionCart cart = new SessionCart();
               session.setAttribute("cart", cart);
            }
            
            // Remove any previous failed attempts at logging in.
            request.setAttribute("invalid_login", null);

            
            // TODO - change web.xml to have this be /home so as to not expose bare jsp. Requires a filter.
            response.sendRedirect(request.getContextPath() + "/home");
            
        } else {
            if (!validRecaptcha) {
                System.out.printf("INVALID recaptcha\n");
                request.setAttribute("invalid_login", "Recaptcha not recognized - are you a bot?");
            }
            else if (customer == null) {
                System.out.printf("INVALID LOGIN email=%s password=%s\n", email, password);
                request.setAttribute("invalid_login", "Invalid login details");
            }
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}

