package servlets;

import models.*;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by kbendick on 5/5/16.
 */
public class EmployeeDashboardServlet extends HttpServlet {


    private String employeeDashboardJSP = "/jsp/employee_dashboard.jsp";
    private String dashboardUrl = "/_dashboard";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String postAction = request.getParameter("postAction");


            switch(postAction) {

                // TODO - Deal with this nastiness.
                case "insertStar":
                    try {
                        insertStarPostAction(request.getParameterMap());
                    } catch (Exception e) {

                        System.out.println("ERROR - In EmployeeDashboardServlet.doPost#insertStar - " + e.getStackTrace());
                        //TODO: Something for an error message on the request.

                    }
                    break;
                case "updateMovie":
                    updateMoviePostAction(request.getParameterMap());
                    break;
            }


        } catch(Exception e) {

            if (request.getParameter("postAction") != null) {

            }

        } finally {
            forwardToDashboard(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        forwardToDashboard(request, response);
    }

    private void forwardToDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(employeeDashboardJSP);
        dispatcher.forward(request, response);
    }

    private void redirectToDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Redirecting from " + request.getRequestURI());
        response.sendRedirect(request.getContextPath() + dashboardUrl);
    }

    private void insertStarPostAction(Map<String, String[]> parameterMap) throws SQLException {

        // TODO: Delete these SysOUTS
        /*
        System.out.println(parameterMap.get("starFullName").toString());
        System.out.println(parameterMap.get("starDob").toString());
        System.out.println(parameterMap.get("starPhotoUrl").toString());
        Star star = Star.buildStarFromStringParameters(parameterMap);
        System.out.println(star);
        */

        StarsDAO.insert(Star.buildStarFromStringParameters(parameterMap));
    }

    private void updateMoviePostAction(Map<String, String[]> parameterMap) throws SQLException {

        // TODO - Delete these testing statements.
        Movie updateMovie = Movie.buildMovieFromStringParameters(parameterMap);
        System.out.println("IN EmployeeDashboardServlet#updateMoviePostAction - movie = " + updateMovie.toString());

        Star updateStar = Star.buildStarFromStringParameters(parameterMap);
        System.out.println("IN EmployeeDashboardServlet#updateMoviePostAction - star = " + updateStar.toString());

        Genre updateGenre = Genre.buildGenreFromStringParameters(parameterMap);
        System.out.println("IN EmployeeDashboardServlet#updateMoviePostAction - genre = " + updateGenre.getName());

        DBStoredProcedures.addMovie(parameterMap);

        System.out.println("IN EmployeeDashboardServlet#updateMoviePostAction - Returned from DBStoredProcedures#addMovie w/o exception.");
    }

}
