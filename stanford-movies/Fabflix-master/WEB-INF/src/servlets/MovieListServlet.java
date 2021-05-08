package servlets;

import models.Genre;
import models.Movie;
import models.MoviesDAO;

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MovieListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(MovieListServlet.class.getName());
    private static final int moviesPerPage = 50;

    @Override
    public String getServletInfo() {
        return "Servlet retrieves a list of movies based on some criteria.";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Movie> movieList = null;
        try {
            String director = req.getParameter("director");
            String starFirstName = req.getParameter("starFirstName");
            String starLastName = req.getParameter("starLastName");
            String year = req.getParameter("year");
            String genre = req.getParameter("genre");
            String title = req.getParameter("title");
            String sort = req.getParameter("sort");
            String movieId = req.getParameter("movieId");
            String firstLetter = req.getParameter("firstLetter");

            Integer page = 1;
            String pageString = req.getParameter("page");
            if (pageString != null && pageString != "") {
                page = (Integer.parseInt(pageString) >= 1 ? Integer.parseInt(pageString) : page);
            }

            Integer resultsPerPage = 50;
            String resultsPerPageString = req.getParameter("resultsPerPage");
            if (resultsPerPageString != null && resultsPerPageString != "") {
                resultsPerPage = Integer.parseInt(resultsPerPageString);
            }

            System.out.printf("Executing search with page= %d, rpp= %d\n", page, resultsPerPage);

            long tjStartTime = System.nanoTime();  // Logging
            movieList = MoviesDAO.searchMovie(movieId, title, year, director,
                                                genre, starFirstName, starLastName,
                                                sort, firstLetter, page, resultsPerPage);

            long tjEndTime = System.nanoTime();
            log.info("TJ:"+(tjEndTime-tjStartTime));

        } catch (SQLException e) {

        } finally {
            forwardMovieList(req, resp, movieList);
        }
    }


    // TODO: Should handle error condition here (null value of list)
    private void forwardMovieList(HttpServletRequest req, HttpServletResponse resp, List<Movie> movieList)
            throws ServletException, IOException {
        String nextJSP = "/jsp/results.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("movieList", movieList);
        dispatcher.forward(req, resp);
    }
}

