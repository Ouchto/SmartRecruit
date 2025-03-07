<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Offres d'Emploi - Candidat</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h1 class="mb-4">Offres d'Emploi Disponibles</h1>

    <c:if test="${empty offres}">
        <div class="alert alert-info" role="alert">
            Aucune offre d'emploi n'est actuellement disponible.
        </div>
    </c:if>

    <div class="row">
        <c:forEach var="offre" items="${offres}">
            <div class="col-md-4 mb-4">
                <div class="card h-100">
                    <div class="card-body">
                        <h5 class="card-title">${offre.titre}</h5>
                        <h6 class="card-subtitle mb-2 text-muted">${offre.typeContrat}</h6>
                        <p class="card-text">
                                ${offre.description.length() > 100 ?
                                        offre.description.substring(0, 100) += '...' :
                                        offre.description}
                        </p>
                        <div class="d-flex justify-content-between align-items-center">
                            <small class="text-muted">
                                Publiée le <fmt:formatDate value="${offre.datePublication}" pattern="dd/MM/yyyy"/>
                            </small>
                            <a href="candidature?action=postuler&offreId=${offre.id}" class="btn btn-primary btn-sm">
                                Postuler
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>