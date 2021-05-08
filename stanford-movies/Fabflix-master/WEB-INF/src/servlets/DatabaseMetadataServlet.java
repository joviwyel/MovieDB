package servlets;

import models.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by kbendick on 5/4/16.
 */
public class DatabaseMetadataServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, List<Map<String, String>>> metaData = DBMetaDataDAO.getSchema();
        forwardMetaData(request, response, metaData);
    }

    // TODO: Should handle error condition here (null value of list)
    private void forwardMetaData(HttpServletRequest req, HttpServletResponse resp, Map<String, List<Map<String, String>>> metaData)
            throws ServletException, IOException {
        String nextJSP = "/jsp/db_metadata.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("metaData", metaData);
        dispatcher.forward(req, resp);
    }

}
