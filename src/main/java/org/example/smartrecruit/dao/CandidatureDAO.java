package org.example.smartrecruit.dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidatureDAO {
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/smartrecruit";
        String username = "root";
        String password = "admin";
        return DriverManager.getConnection(url, username, password);
    }


}