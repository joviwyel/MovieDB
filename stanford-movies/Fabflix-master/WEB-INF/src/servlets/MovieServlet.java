package servlets;

import models.Movie;
import models.MoviesDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MovieServlet extends HttpServlet {

    private final String movieIdParam = "id";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            int movieId = Integer.parseInt(request.getParameter(movieIdParam));
            Movie movie = MoviesDAO.getMovieById(movieId);
            forwardMovie(request, response, movie);
        } catch (NumberFormatException e) {
            forwardMovie(request, response, null); // TODO this might be a bad way to handle this.
        }

    }

    private void forwardMovie(HttpServletRequest request, HttpServletResponse response, Movie movie)
            throws ServletException, IOException {

        //TODO Remove logging
        if (movie != null) {
            System.out.printf("Movie: %s\n", movie.toString());
            System.out.printf("\tStars on record: %d\n", movie.getStars().size());
            System.out.printf("\tGenres on record: %d\n", movie.getGenres().size());
        }

        request.setAttribute("movie", movie);

        String nextJSP = "/jsp/movie.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);

        dispatcher.forward(request, response);
    }
}
