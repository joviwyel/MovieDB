
package servlets;

import models.Genre;
import models.GenresDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class GenresServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static Map<Integer, String> genresMap;

    @Override
    public String getServletInfo() {
        return "Servlet initializes an application scoped map of genre ID to genres titles.";
    }


    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        genresMap = GenresDAO.getGenresMap();
        config.getServletContext().setAttribute("genresMap", genresMap); 
    }

}
