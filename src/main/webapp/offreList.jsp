<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Offres d'Emploi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h1 class="mb-4">Offres d'Emploi</h1>

    <a href="offres?action=create" class="btn btn-primary mb-3">Créer une nouvelle offre</a>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Titre</th>
            <th>Type de Contrat</th>
            <th>Date Publication</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="offre" items="${offres}">
            <tr>
                <td>${offre.id}</td>
                <td>${offre.titre}</td>
                <td>${offre.typeContrat}</td>
                <td><fmt:formatDate value="${offre.datePublication}" pattern="dd/MM/yyyy"/></td>
                <td>
                    <a href="offres?action=edit&id=${offre.id}" class="btn btn-sm btn-warning">Modifier</a>
                    <a href="offres?action=delete&id=${offre.id}" class="btn btn-sm btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette offre?')">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>