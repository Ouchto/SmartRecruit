<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modifier une Offre d'Emploi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h1 class="mb-4">Modifier l'Offre d'Emploi</h1>

    <form action="offres" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${offre.id}">

        <div class="mb-3">
            <label for="titre" class="form-label">Titre</label>
            <input type="text" class="form-control" id="titre" name="titre" value="${offre.titre}" required>
        </div>

        <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <textarea class="form-control" id="description" name="description" rows="4" required>${offre.description}</textarea>
        </div>

        <div class="mb-3">
            <label for="typeContrat" class="form-label">Type de Contrat</label>
            <select class="form-select" id="typeContrat" name="typeContrat" required>
                <option value="CDI" ${offre.typeContrat == 'CDI' ? 'selected' : ''}>CDI</option>
                <option value="CDD" ${offre.typeContrat == 'CDD' ? 'selected' : ''}>CDD</option>
                <option value="Stage" ${offre.typeContrat == 'Stage' ? 'selected' : ''}>Stage</option>
                <option value="Freelance" ${offre.typeContrat == 'Freelance' ? 'selected' : ''}>Freelance</option>
            </select>
        </div>

        <div class="mb-3">
            <label for="datePublication" class="form-label">Date de Publication</label>
            <input type="date" class="form-control" id="datePublication" name="datePublication"
                   value="<fmt:formatDate value="${offre.datePublication}" pattern="yyyy-MM-dd"/>" required>
        </div>

        <button type="submit" class="btn btn-primary">Mettre à jour</button>
        <a href="offres" class="btn btn-secondary">Annuler</a>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>