package servlets;

import models.Star;
import models.StarsDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by kbendick on 4/19/16.
 */
public class StarServlet extends HttpServlet {

    private final String starIdParam = "id";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Find star based on id
        try {
            Integer starId = Integer.parseInt(request.getParameter(starIdParam));
            Star star = StarsDAO.searchById(starId);
            forwardStar(request, response, star);
        } catch (NumberFormatException e) {
            forwardStar(request, response, null);  // TODO - This might be a bad way to handle this
        }
    }

    private void forwardStar(HttpServletRequest request, HttpServletResponse response, Star star)
            throws ServletException, IOException {

        //TODO Remove logging
        if (star != null) {
            System.out.printf("Star: %s\n", star.toString());
            System.out.printf("\tFilms on record: %d\n", star.getMovies().size());
        }


        request.setAttribute("star", star);

        String nextJSP = "/jsp/star.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);

        // TODO: Maybe add this error check?
//        if (star == null) {
//            request.setAttribute("error", "No star found matching this id.");
//        }

        dispatcher.forward(request, response);
    }
}
