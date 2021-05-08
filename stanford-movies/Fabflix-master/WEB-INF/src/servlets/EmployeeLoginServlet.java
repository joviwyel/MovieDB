package servlets;

import models.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by kbendick on 5/4/16.
 */
public class EmployeeLoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee employee = (Employee) session.getAttribute("employee");
        String error = (String) session.getAttribute("error");

        // If we're already logged in, we don't need to be at the login page.
        if (employee != null) {

            request.setAttribute("error", null);
            request.setAttribute("invalid_login", null);
            response.sendRedirect(request.getContextPath() + "/_dashboard");

        } else {
            request.getRequestDispatcher("/jsp/employee_login.jsp").forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            login(email, password, request, response);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private synchronized void login(String email, String password, HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {

        HttpSession session = request.getSession();

        Employee employee = EmployeesDAO.verifyAccount(email, password);

        if (employee != null) {

            session.setAttribute("employee", employee);

            // Remove any previous failed attempts at logging in.
            request.setAttribute("error", null);
            request.setAttribute("invalid_login", null);

            // TODO - change web.xml to have this be /home so as to not expose bare jsp. Requires a filter.
            response.sendRedirect(request.getContextPath() + "/_dashboard");

        } else {
            System.out.printf("INVALID LOGIN email=%s password=%s\n", email, password);
            request.setAttribute("invalid_login", "Invalid login details");
            request.getRequestDispatcher("/jsp/employee_login.jsp").forward(request, response);
        }
    }
}


