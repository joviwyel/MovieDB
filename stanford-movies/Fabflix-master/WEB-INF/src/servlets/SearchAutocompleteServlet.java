package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import models.Movie;
import models.MoviesDAO;


/**
 * Created by kbendick on 5/15/16.
 */
public class SearchAutocompleteServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String searchString = request.getParameter("title");
            System.out.printf("In SearchAutocompleteServlet: %s\n", searchString);

            String[] searchStrings = searchString.split("\\s+");

            List<Movie> movieResults = MoviesDAO.fullTextSearchByTitle(searchStrings);

            // Turn into an array of titles
            String[] movieTitles = new String[movieResults.size()];
            for (int i = 0; i < movieResults.size(); ++i) {
                movieTitles[i] = movieResults.get(i).getTitle();
            }

            Arrays.sort(movieTitles);
            response.setContentType("text/json");
            response.getWriter().write(new Gson().toJson(movieTitles));

        } catch (final Exception e) {
            e.printStackTrace();
            String[] testMovieTitles = {"the good the bad and the ugly", "puppy daniels", "the next one is an empty string", ""};
            response.setContentType("text/json");
            response.getWriter().write(new Gson().toJson(testMovieTitles));
        }
    }
}
