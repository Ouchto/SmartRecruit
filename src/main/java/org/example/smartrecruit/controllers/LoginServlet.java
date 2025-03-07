package org.example.smartrecruit.controllers;

import org.example.smartrecruit.dao.UserDAO;
import org.example.smartrecruit.dao.OffreDAO;
import org.example.smartrecruit.model.User;
import org.example.smartrecruit.model.OffreEmploi;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;
    private OffreDAO offreDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
        offreDAO = new OffreDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            List<User> users = userDAO.getAll();
            boolean loginSuccessful = false;

            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    loginSuccessful = true;
                    HttpSession session = request.getSession(true);
                    session.setAttribute("username", username);
                    session.setAttribute("password", password);

                    switch (user.getRole()) {
                        case "admin":

                            List<OffreEmploi> offres = offreDAO.getAll();
                            request.setAttribute("offres", offres);
                            request.getRequestDispatcher("offreList.jsp").forward(request, response);
                            break;

                        case "recruteur":

                            request.getRequestDispatcher("recruteurHome.jsp").forward(request, response);
                            break;

                        case "candidat":
                            List<OffreEmploi> activeOffres = offreDAO.getAll();
                            request.setAttribute("offres", activeOffres);
                            request.getRequestDispatcher("candidatHome.jsp").forward(request, response);
                            break;

                        default:
                            response.sendRedirect("login.jsp");
                    }
                    break;
                }
            }

            // If login was not successful
            if (!loginSuccessful) {
                request.setAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            throw new ServletException("An error occurred processing POST request", e);
        }
    }
}