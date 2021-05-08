package servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class EmployeeLoginFilter implements Filter {

    private String employeeLoginUrl = "/_dashboard/login";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {


        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String path = httpRequest.getRequestURI();

        System.out.println("In EmployeeLoginFilter, coming from: " + path);
        if (path.contains("/css") || path.contains("css") ||
                path.contains("/js") || path.contains("js") ||
                path.contains("/_dashboard/login") ||
                !path.contains("/_dashboard")) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }


        HttpSession session = httpRequest.getSession(false);

        boolean loggedIn = (session != null) && (session.getAttribute("employee") != null);

        if (!loggedIn) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + employeeLoginUrl);
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }

    }

    public void init(FilterConfig filterConfig) {

    }

    public void destroy() {

    }

}

