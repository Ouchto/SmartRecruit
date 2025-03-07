package org.example.smartrecruit.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.smartrecruit.dao.OffreDAO;
import org.example.smartrecruit.model.OffreEmploi;

public class CandidatHomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OffreDAO offreDAO;

    public void init() {
        offreDAO = new OffreDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get active job offers
        List<OffreEmploi> offres = offreDAO.getActiveOffers();
        request.setAttribute("offres", offres);

        RequestDispatcher dispatcher = request.getRequestDispatcher("candidatHome.jsp");
        dispatcher.forward(request, response);
    }
}