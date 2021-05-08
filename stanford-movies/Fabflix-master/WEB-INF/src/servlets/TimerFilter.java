package servlets;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Created by kbendick on 6/5/16.
 */
public class TimerFilter implements Filter {

    private static Logger log = Logger.getLogger(MovieListServlet.class.getName());

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String path = httpRequest.getRequestURI();

        System.out.println("In TimerFilter, coming from: " + path);

        // Time an event in a program to nanosecond precision
        long startTime = System.nanoTime();

        /////////////////////////////////
        /// ** part to be measured ** ///
        /////////////////////////////////
        chain.doFilter(servletRequest, servletResponse);

        System.out.println("In TimerFilter, having processed: " + path);
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime; // elapsed time in nano seconds. Note: print the values in nano seconds

        log.info("TS:"+elapsedTime);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
