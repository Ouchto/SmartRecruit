package org.example.smartrecruit.controllers;

import org.example.smartrecruit.dao.UserDAO;
import org.example.smartrecruit.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        System.out.println(action);
        try {
            switch (action) {
                case "/logout":
                    request.getSession().invalidate();
                    response.sendRedirect("login.jsp");
                    break;
                default:
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");



                    List<User> user =  userDAO.getAll();
                    user.forEach(user1 -> {

                        if(user1.getUsername().equals(username) && user1.getPassword().equals(password)) {

                            HttpSession session = request.getSession(true);
                            session.setAttribute("username", username);
                            session.setAttribute("password", password);
                            // response.sendRedirect("home");

                            if (user1.getRole().equals("admin")) {
                                try {
                                    request.getRequestDispatcher("index.jsp").forward(request, response);

                                } catch (ServletException e) {
                                    throw new RuntimeException(e);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else if (user1.getRole().equals("recruteur")) {
                                try {
                                    request.getRequestDispatcher("recruteurHome.jsp").forward(request, response);

                                } catch (ServletException e) {
                                    throw new RuntimeException(e);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }else {
                                try {
                                    request.getRequestDispatcher("candidatHome.jsp").forward(request, response);

                                } catch (ServletException e) {
                                    throw new RuntimeException(e);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }


                        }
                    });
                    request.setAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect");

                    try {
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                    } catch (ServletException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("An error occurred processing POST request", e);
        }



    }
}