package org.example.smartrecruit.dao;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.smartrecruit.model.Candidat;
import org.example.smartrecruit.model.Candidature;


public class CandidatureDAO {

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/smartrecruit";
        String username = "root";
        String password = "admin";
        return DriverManager.getConnection(url, username, password);
    }

    public int saveCandidat(Candidat candidat, InputStream cvInputStream, String fileName) throws SQLException {
        // First check if the candidate already exists with the same email
        String checkSql = "SELECT id FROM candidats WHERE email = ?";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, candidat.getEmail());

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // Candidate exists, update info
                    int candidatId = rs.getInt("id");
                    updateCandidat(candidatId, candidat, cvInputStream, fileName);
                    return candidatId;
                } else {
                    // New candidate, insert
                    return insertCandidat(candidat, cvInputStream, fileName);
                }
            }
        }
    }

    private int insertCandidat(Candidat candidat, InputStream cvInputStream, String fileName) throws SQLException {
        String sql = "INSERT INTO candidats (nom, email, telephone, cv_file_name) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, candidat.getNom());
            stmt.setString(2, candidat.getEmail());
            stmt.setString(4, fileName);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating candidat failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int candidatId = generatedKeys.getInt(1);

                    // Save the CV file to the database or file system
                    saveCvFile(candidatId, cvInputStream);

                    return candidatId;
                } else {
                    throw new SQLException("Creating candidat failed, no ID obtained.");
                }
            }
        }
    }

    private void updateCandidat(int candidatId, Candidat candidat, InputStream cvInputStream, String fileName) throws SQLException {
        String sql = "UPDATE candidats SET nom = ?, email = ?, telephone = ?, cv_file_name = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, candidat.getNom());
            stmt.setString(2, candidat.getEmail());
            stmt.setString(4, fileName);
            stmt.setInt(5, candidatId);

            stmt.executeUpdate();

            // Update the CV file
            saveCvFile(candidatId, cvInputStream);
        }
    }

    private void saveCvFile(int candidatId, InputStream cvInputStream) throws SQLException {
        // This method would save the CV file either to the database as a BLOB
        // or to the file system with a reference in the database

        String sql = "UPDATE candidats SET cv_data = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBinaryStream(1, cvInputStream);
            stmt.setInt(2, candidatId);

            stmt.executeUpdate();
        }
    }

    public int saveCandidature(Candidature candidature, String lettreMotivation) throws SQLException {
        String sql = "INSERT INTO candidatures (candidat_id, offre_id, date_postulation, statut, lettre_motivation) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, candidature.getCandidatId());
            stmt.setInt(2, candidature.getOffreId());
            stmt.setTimestamp(3, new Timestamp(candidature.getDatePostulation().getTime()));
            stmt.setString(4, candidature.getStatut());
            stmt.setString(5, lettreMotivation);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating candidature failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int candidatureId = generatedKeys.getInt(1);
                    return candidatureId;
                } else {
                    throw new SQLException("Creating candidature failed, no ID obtained.");
                }
            }
        }
    }

    public List<Candidature> getCandidaturesByCandidat(int candidatId) {
        List<Candidature> candidatures = new ArrayList<>();

        String sql = "SELECT c.*, o.titre as offre_titre FROM candidatures c " +
                "JOIN offres_emploi o ON c.offre_id = o.id " +
                "WHERE c.candidat_id = ? ORDER BY c.date_postulation DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, candidatId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Candidature candidature = new Candidature();
                    candidature.setCandidatId(rs.getInt("candidat_id"));
                    candidature.setOffreId(rs.getInt("offre_id"));
                    candidature.setDatePostulation(rs.getTimestamp("date_postulation"));
                    candidature.setStatut(rs.getString("statut"));

                    candidatures.add(candidature);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return candidatures;
    }

    public Candidature getCandidatureById(int candidatureId) {
        Candidature candidature = null;

        String sql = "SELECT * FROM candidatures WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, candidatureId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    candidature = new Candidature();
                    candidature.setCandidatId(rs.getInt("candidat_id"));
                    candidature.setOffreId(rs.getInt("offre_id"));
                    candidature.setDatePostulation(rs.getTimestamp("date_postulation"));
                    candidature.setStatut(rs.getString("statut"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return candidature;
    }

    public List<Candidature> getCandidaturesByOffre(int offreId) {
        List<Candidature> candidatures = new ArrayList<>();

        String sql = "SELECT c.*, ca.nom, ca.email FROM candidatures c " +
                "JOIN candidats ca ON c.candidat_id = ca.id " +
                "WHERE c.offre_id = ? ORDER BY c.date_postulation";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, offreId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Candidature candidature = new Candidature();
                    candidature.setCandidatId(rs.getInt("candidat_id"));
                    candidature.setOffreId(rs.getInt("offre_id"));
                    candidature.setDatePostulation(rs.getTimestamp("date_postulation"));
                    candidature.setStatut(rs.getString("statut"));

                    candidatures.add(candidature);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return candidatures;
    }

    public boolean updateStatut(int candidatureId, String statut) {
        String sql = "UPDATE candidatures SET statut = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, statut);
            stmt.setInt(2, candidatureId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getLettreMotivation(int candidatureId) {
        String lettreMotivation = "";

        String sql = "SELECT lettre_motivation FROM candidatures WHERE id = ?";

        try (Connection conn =getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, candidatureId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lettreMotivation = rs.getString("lettre_motivation");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lettreMotivation;
    }
}