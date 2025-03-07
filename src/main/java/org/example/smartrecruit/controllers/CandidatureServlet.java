package org.example.smartrecruit.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.smartrecruit.dao.CandidatureDAO;
import org.example.smartrecruit.dao.OffreDAO;
import org.example.smartrecruit.model.Candidat;
import org.example.smartrecruit.model.Candidature;
import org.example.smartrecruit.model.OffreEmploi;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class CandidatureServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CandidatureDAO candidatureDAO;
    private OffreDAO offreDAO;

    public void init() {
        candidatureDAO = new CandidatureDAO();
        offreDAO = new OffreDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "list") {
                case "postuler":
                    showPostulerForm(request, response);
                    break;
                case "mesPostulations":
                    listCandidatures(request, response);
                    break;
                case "detail":
                    showCandidatureDetail(request, response);
                    break;
                default:
                    response.sendRedirect("candidatHome");
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            submitCandidature(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void showPostulerForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int offreId = Integer.parseInt(request.getParameter("offreId"));
        OffreEmploi offre = offreDAO.getById(offreId);
        request.setAttribute("offre", offre);
        RequestDispatcher dispatcher = request.getRequestDispatcher("candidatureCreate.jsp");
        dispatcher.forward(request, response);
    }

    private void submitCandidature(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        int offreId = Integer.parseInt(request.getParameter("offreId"));
        String nom = request.getParameter("nom");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");
        String lettreMotivation = request.getParameter("lettreMotivation");

        // Handle file upload
        Part cvPart = request.getPart("cv");
        String fileName = getSubmittedFileName(cvPart);
        InputStream cvInputStream = cvPart.getInputStream();

        // Create or get candidat
        Candidat candidat = new Candidat();
        candidat.setNom(nom);
        candidat.setEmail(email);


        // You'll need to implement these methods in your DAO
        int candidatId = candidatureDAO.saveCandidat(candidat, cvInputStream, fileName);

        // Create candidature
        Candidature candidature = new Candidature();
        candidature.setCandidatId(candidatId);
        candidature.setOffreId(offreId);
        candidature.setDatePostulation(new Date());
        candidature.setStatut("EN_ATTENTE");

        candidatureDAO.saveCandidature(candidature, lettreMotivation);

        // Redirect to confirmation page or back to offers
        request.setAttribute("success", true);
        request.setAttribute("message", "Votre candidature a été soumise avec succès!");
        RequestDispatcher dispatcher = request.getRequestDispatcher("candidatureConfirmation.jsp");
        dispatcher.forward(request, response);
    }

    private void listCandidatures(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // This would typically use a user ID from the session
        int candidatId = getCandidatIdFromSession(request);
        List<Candidature> candidatures = candidatureDAO.getCandidaturesByCandidat(candidatId);

        request.setAttribute("candidatures", candidatures);
        RequestDispatcher dispatcher = request.getRequestDispatcher("mesCandidatures.jsp");
        dispatcher.forward(request, response);
    }

    private void showCandidatureDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int candidatureId = Integer.parseInt(request.getParameter("id"));
        Candidature candidature = candidatureDAO.getCandidatureById(candidatureId);

        // Ensure the candidature belongs to the current user
        int candidatId = getCandidatIdFromSession(request);
        if (candidature.getCandidatId() != candidatId) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        OffreEmploi offre = offreDAO.getById(candidature.getOffreId());
        String lettreMotivation = candidatureDAO.getLettreMotivation(candidatureId);

        request.setAttribute("candidature", candidature);
        request.setAttribute("offre", offre);
        request.setAttribute("lettreMotivation", lettreMotivation);

        RequestDispatcher dispatcher = request.getRequestDispatcher("candidatureDetail.jsp");
        dispatcher.forward(request, response);
    }

    // Helper method to get filename from part
    private String getSubmittedFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                return item.substring(item.indexOf("=") + 2, item.length() - 1);
            }
        }
        return "";
    }

    // Helper method to get candidat ID from session
    private int getCandidatIdFromSession(HttpServletRequest request) {
        // In a real application, you would get this from the session
        // For now, we'll return a placeholder value
        // You can implement proper session handling as needed
        Object candidatIdObj = request.getSession().getAttribute("candidatId");
        if (candidatIdObj != null) {
            return (Integer) candidatIdObj;
        }
        return 1; // Default value for testing
    }
}