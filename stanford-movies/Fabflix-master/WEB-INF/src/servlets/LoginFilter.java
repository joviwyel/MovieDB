package servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginFilter implements Filter {

    private String loginUrl = "/login";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {


        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String path = httpRequest.getRequestURI();

        System.out.println("In filter, coming from: " + path);
        if (path.contains("/css") || path.contains("css") ||
                path.contains("/js") || path.contains("js") ||
                path.contains("/login") || path.contains("login") ||
                path.contains("/_dashboard") || path.contains("_dashboard") ||
                path.contains("api")) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }


        HttpSession session = httpRequest.getSession();

        boolean loggedIn = (session != null) && (session.getAttribute("customer") != null);

        if (!loggedIn) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + loginUrl);
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }

   }

     public void init(FilterConfig filterConfig) {

         // Set all paths for the filter to ignore.
//         String ignorePathsArray = filterConfig.getInitParameter("ignorePaths");
//         this.loginUrl = filterConfig.getInitParameter("loginUrl");
//         ignorePathsArray += "," + loginUrl;
//         this.ignorePaths = ignorePathsArray.split(",");
     } 

     public void destroy() {

     }

}
