<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Administration - Candidatures</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h1>Gestion des Candidatures</h1>
    <a href="offres" class="btn btn-primary">Gérer les Offres</a>
  </div>

  <div class="mb-4">
    <form action="admin" method="get" class="row g-3">
      <input type="hidden" name="action" value="candidatures">
      <div class="col-md-4">
        <select name="offreId" class="form-select">
          <option value="">Toutes les offres</option>
          <c:forEach var="offre" items="${offres}">
            <option value="${offre.id}" ${offreId == offre.id ? 'selected' : ''}>
                ${offre.titre}
            </option>
          </c:forEach>
        </select>
      </div>
      <div class="col-md-4">
        <select name="statut" class="form-select">
          <option value="">Tous les statuts</option>
          <option value="EN_ATTENTE" ${statut == 'EN_ATTENTE' ? 'selected' : ''}>En attente</option>
          <option value="ACCEPTE" ${statut == 'ACCEPTE' ? 'selected' : ''}>Accepté</option>
          <option value="REFUSE" ${statut == 'REFUSE' ? 'selected' : ''}>Refusé</option>
        </select>
      </div>
      <div class="col-md-4">
        <button type="submit" class="btn btn-primary">Filtrer</button>
      </div>
    </form>
  </div>

  <c:if test="${empty candidatures}">
  <div class="alert alert-info" role="alert">
    Aucune candidature ne correspond à vos critères.
  </div>
  </c:if>

  <c:if test="${not empty candidatures}">
  <div class="table-responsive">
    <table class="table table-striped">
      <thead class="table-dark">
      <tr>
        <th>Candidat</th>
        <th>Offre</th>
        <th>Date de Candidature</th>
        <th>Statut</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="candidature" items="${candidatures}">
        <tr>
          <td>${candidature.candidatNom}</td>
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
            <div class="btn-group">
              <a href="admin?action=detailCandidature&id=${candidature.id}" class="btn btn-sm btn-info">
                Détails
              </a>
              <button type="button" class="btn btn-sm btn-success" data-bs-toggle="modal" data-bs-target="#changeStatusModal${candidature.id}">
                Changer Statut
              </button>
            </div>

            <!-- Modal for changing status -->
            <div class="modal fade" id="changeStatusModal${candidature.id}" tabindex="-1" aria-hidden="true">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title">Changer le Statut</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <form action="admin" method="post">
                    <input type="hidden" name="action" value="updateStatus">
                    <input type="hidden" name="candidatureId" value="${candidature.id}">
                    <div class="modal-body">
                      <div class="mb-3">
                        <label for="statut" class="form-label">Nouveau Statut</label>
                        <select name="statut" class="form-select" required>
                          <option value="EN_ATTENTE" ${candidature.statut == 'EN_ATTENTE' ? 'selected' : ''}>En attente</option>
                          <option value="ACCEPTE" ${candidature.statut == 'ACCEPTE' ? 'selected' : ''}>Accepté</option>
                          <option value="REFUSE" ${candidature.statut == 'REFUSE' ? 'selected' : ''}>Refusé</option>
                        </select>
                      </div>
                      <div class="mb-3">
                        <label for="commentaire" class="form-label">Commentaire (optionnel)</label>
                        <textarea name="commentaire" class="form-control" rows="3"></textarea>
                      </div>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                      <button type="submit" class="btn btn-primary">Enregistrer</button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </td>
        </tr>
      </c:forEach>
      </tbody>