<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Postuler - ${offre.titre}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="row">
        <div class="col-md-8 offset-md-2">
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h2 class="mb-0">Postuler à l'offre: ${offre.titre}</h2>
                </div>
                <div class="card-body">
                    <div class="mb-4">
                        <h4>Détails de l'Offre</h4>
                        <p><strong>Type de Contrat:</strong> ${offre.typeContrat}</p>
                        <p><strong>Date de Publication:</strong>
                            <fmt:formatDate value="${offre.datePublication}" pattern="dd/MM/yyyy"/>
                        </p>
                        <p><strong>Description:</strong> ${offre.description}</p>
                    </div>

                    <form action="candidature" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="offreId" value="${offre.id}">

                        <div class="mb-3">
                            <label for="nom" class="form-label">Nom Complet</label>
                            <input type="text" class="form-control" id="nom" name="nom" required>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email" required>
                        </div>

                        <div class="mb-3">
                            <label for="telephone" class="form-label">Téléphone</label>
                            <input type="tel" class="form-control" id="telephone" name="telephone" required>
                        </div>

                        <div class="mb-3">
                            <label for="cv" class="form-label">CV (PDF)</label>
                            <input type="file" class="form-control" id="cv" name="cv" accept=".pdf" required>
                        </div>

                        <div class="mb-3">
                            <label for="lettreMotivation" class="form-label">Lettre de Motivation</label>
                            <textarea class="form-control" id="lettreMotivation" name="lettreMotivation" rows="4" required></textarea>
                        </div>

                        <button type="submit" class="btn btn-success">Envoyer ma Candidature</button>
                        <a href="candidatHome" class="btn btn-secondary ms-2">Retour aux Offres</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>