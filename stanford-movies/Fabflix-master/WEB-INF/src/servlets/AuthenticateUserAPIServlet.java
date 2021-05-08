package servlets;

import models.CustomersDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by kbendick on 5/18/16.
 */
public class AuthenticateUserAPIServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("In AuthenticateUserAPIServlet#doPost.");

        response.setContentType("text");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.printf("In AuthenticateUserAPIServelt#doPost with email: %s, password: %s.\n", email, password);

        if ( CustomersDAO.verifyAccount(email, password) != null ) {
            System.out.printf("In AuthenticateUserAPIServelt#doPost with email: %s, password: %s. Returning true.\n", email, password);
            response.getWriter().write("true");
        } else {
            System.out.printf("In AuthenticateUserAPIServelt#doPost with email: %s, password: %s. Returning false.\n", email, password);
            response.getWriter().write("false");
        }
    }
}
