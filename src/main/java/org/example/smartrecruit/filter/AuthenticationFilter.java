package org.example.smartrecruit.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;


@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation du filtre
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getServletPath();


        if ("/".equals(path) || "/login".equals(path) || "/login.jsp".equals(path)) {
            chain.doFilter(request, response);
        }else {
            HttpSession session = httpRequest.getSession(false);
            boolean isAuthenticated = (session != null && session.getAttribute("username") != null);

            if (isAuthenticated) {
                chain.doFilter(request, response);
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            }

        }

    }


    @Override
    public void destroy() {
        // Nettoyage des ressources
    }

}
