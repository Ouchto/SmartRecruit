package org.example.smartrecruit.dao;

import org.example.smartrecruit.model.OffreEmploi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OffreDAO {
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/smartrecruit";
        String username = "root";
        String password = "password";
        return DriverManager.getConnection(url, username, password);
    }

    public OffreEmploi getById(int id) {
        OffreEmploi offre = null;
        String sql = "SELECT * FROM offres_emploi WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    offre = new OffreEmploi();
                    offre.setId(rs.getInt("id"));
                    offre.setTitre(rs.getString("titre"));
                    offre.setDescription(rs.getString("description"));
                    offre.setTypeContrat(rs.getString("type_contrat"));
                    offre.setDatePublication(rs.getDate("date_publication"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offre;
    }

    public List<OffreEmploi> getAll() {
        List<OffreEmploi> offres = new ArrayList<>();
        String sql = "SELECT * FROM offres_emploi";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                OffreEmploi offre = new OffreEmploi();
                offre.setId(rs.getInt("id"));
                offre.setTitre(rs.getString("titre"));
                offre.setDescription(rs.getString("description"));
                offre.setTypeContrat(rs.getString("type_contrat"));
                offre.setDatePublication(rs.getDate("date_publication"));
                offres.add(offre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offres;
    }

    public List<OffreEmploi> getActiveOffers() {
        List<OffreEmploi> offres = new ArrayList<>();
        String sql = "SELECT * FROM offres_emploi WHERE status = 'ACTIVE' AND date_expiration >= CURDATE()";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                OffreEmploi offre = new OffreEmploi();
                offre.setId(rs.getInt("id"));
                offre.setTitre(rs.getString("titre"));
                offre.setDescription(rs.getString("description"));
                offre.setTypeContrat(rs.getString("type_contrat"));
                offre.setDatePublication(rs.getDate("date_publication"));

                offres.add(offre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offres;
    }

    public boolean insert(OffreEmploi offre) {
        String sql = "INSERT INTO offres_emploi (titre, description, type_contrat,date_publication) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, offre.getTitre());
            stmt.setString(2, offre.getDescription());
            stmt.setString(3, offre.getTypeContrat());
            stmt.setDate(4, new java.sql.Date(offre.getDatePublication().getTime()));


            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    offre.setId(generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(OffreEmploi offre) {
        String sql = "UPDATE offres_emploi SET titre = ?, description = ?, type_contrat = ?, date_publication = ?" +
                " WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, offre.getTitre());
            stmt.setString(2, offre.getDescription());
            stmt.setString(3, offre.getTypeContrat());
            stmt.setDate(4, new java.sql.Date(offre.getDatePublication().getTime()));
        stmt.setInt(5, offre.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM offres_emploi WHERE id = ?";

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