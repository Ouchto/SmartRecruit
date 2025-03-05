package org.example.smartrecruit.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.smartrecruit.dao.OffreDAO;
import org.example.smartrecruit.model.OffreEmploi;


public class OffreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OffreDAO offreDAO;

    public void init() {
        offreDAO = new OffreDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "list") {
                case "create":
                    showCreateForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteOffre(request, response);
                    break;
                default:
                    listOffres(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        System.out.println(action);

        try {
            switch (action) {
                case "create":
                    insertOffre(request, response);
                    break;
                case "update":
                    updateOffre(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listOffres(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<OffreEmploi> offres = offreDAO.getAll();
        request.setAttribute("offres", offres);
        RequestDispatcher dispatcher = request.getRequestDispatcher("offreList.jsp");
        dispatcher.forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("offreCreate.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        OffreEmploi existingOffre = offreDAO.getById(id);
        request.setAttribute("offre", existingOffre);
        RequestDispatcher dispatcher = request.getRequestDispatcher("offreEdit.jsp");
        dispatcher.forward(request, response);
    }

    private void insertOffre(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, SQLException {
        String titre = request.getParameter("titre");
        String description = request.getParameter("description");
        String typeContrat = request.getParameter("typeContrat");
        Date datePublication = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("datePublication"));
        OffreEmploi newOffre = new OffreEmploi();
        newOffre.setTitre(titre);
        newOffre.setDescription(description);
        newOffre.setTypeContrat(typeContrat);
        newOffre.setDatePublication(datePublication);

        offreDAO.insert(newOffre);
        response.sendRedirect("offres");
    }

    private void updateOffre(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        int id = Integer.parseInt(request.getParameter("id"));
        String titre = request.getParameter("titre");
        String description = request.getParameter("description");
        String typeContrat = request.getParameter("typeContrat");
        Date datePublication = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("datePublication"));

        OffreEmploi offre = new OffreEmploi();
        offre.setId(id);
        offre.setTitre(titre);
        offre.setDescription(description);
        offre.setTypeContrat(typeContrat);
        offre.setDatePublication(datePublication);

        offreDAO.update(offre);
        response.sendRedirect("offres");
    }

    private void deleteOffre(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        offreDAO.delete(id);
        response.sendRedirect("offres");
    }
}