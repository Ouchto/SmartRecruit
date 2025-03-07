<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mes Candidatures</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h1 class="mb-4">Mes Candidatures</h1>

    <c:if test="${empty candidatures}">
        <div class="alert alert-info" role="alert">
            Vous n'avez pas encore postulé à des offres d'emploi.
        </div>
    </c:if>

    <div class="table-responsive">
        <table class="table table-striped table-hover">
            <thead class="table-dark">
            <tr>
                <th>Offre</th>
                <th>Date de Candidature</th>
                <th>Statut</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="candidature" items="${candidatures}">
                <tr>
                    <td>${candidature.offreTitre}</td>
                    <td><fmt:formatDate value="${candidature.datePostulation}" pattern="dd/MM/yyyy"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${candidature.statut == 'EN_ATTENTE'}">
                                <span class="badge bg-warning text-dark">En attente</span>
                            </c:when>
                            <c:when test="${candidature.statut == 'ACCEPTE'}">
                                <span class="badge bg-success">Acceptée</span>
                            </c:when>
                            <c:when test="${candidature.statut == 'REFUSE'}">
                                <span class="badge bg-danger">Refusée</span>
                            </c:when>
                        </c:choose>
                    </td>
                    <td>
                        <a href="candidature?action=detail&id=${candidature.id}" class="btn btn-sm btn-info">
                            Détails
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="mt-4">
        <a href="candidatHome" class="btn btn-primary">Retour aux Offres</a>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>