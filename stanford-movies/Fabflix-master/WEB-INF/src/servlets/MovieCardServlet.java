package servlets;

import models.Movie;
import models.MoviesDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by kbendick on 5/20/16.
 */
public class MovieCardServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("In MovieCardServlet#doGet");

        try {
            Integer id = Integer.parseInt(request.getParameter("id"));
            Movie movie = MoviesDAO.getMovieById(id);

            request.setAttribute("movie", movie);

            request.getRequestDispatcher("jsp/movie_card.jsp").forward(request, response);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("<p>No details found.</p>");


        } catch (Exception e) {
            response.getWriter().write("<p>No details found.</p>");
        }
    }
}
