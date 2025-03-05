package org.example.smartrecruit.dao;

import org.example.smartrecruit.model.Candidat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidatDAO {
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/smartrecruit";
        String username = "root";
        String password = "admin";
        return DriverManager.getConnection(url, username, password);
    }

    public Candidat getById(int id) {
        Candidat candidat = null;
        String sql = "SELECT * FROM candidats WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    candidat = new Candidat();
                    candidat.setId(rs.getInt("id"));
                    candidat.setNom(rs.getString("nom"));
                    candidat.setPrenom(rs.getString("prenom"));
                    candidat.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return candidat;
    }

    public List<Candidat> getAll() {
        List<Candidat> candidats = new ArrayList<>();
        String sql = "SELECT * FROM candidats";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Candidat candidat = new Candidat();
                candidat.setId(rs.getInt("id"));
                candidat.setNom(rs.getString("nom"));
                candidat.setPrenom(rs.getString("prenom"));
                candidat.setEmail(rs.getString("email"));
                candidats.add(candidat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return candidats;
    }

    public boolean insert(Candidat candidat) {
        String sql = "INSERT INTO candidats (nom, prenom, email) " +
                "VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, candidat.getNom());
            stmt.setString(2, candidat.getPrenom());
            stmt.setString(3, candidat.getEmail());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    candidat.setId(generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Candidat candidat) {
        String sql = "UPDATE candidats SET nom = ?, prenom = ?, email = ?, " +
                " WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, candidat.getNom());
            stmt.setString(2, candidat.getPrenom());
            stmt.setString(3, candidat.getEmail());
            stmt.setInt(4, candidat.getId());

            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM candidats WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}