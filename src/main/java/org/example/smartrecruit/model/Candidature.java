package org.example.smartrecruit.model;

import java.util.Date;

public class Candidature {
    private int candidatId;
    private int offreId;
    private Date datePostulation;
    private String statut; // "EN_ATTENTE", "ACCEPTE", "REFUSE"


    // Constructors
    public Candidature() {}

    public Candidature( int candidatId, int offreId, Date datePostulation,
                       String statut, String lettreMotivation, String commentaires) {
        this.candidatId = candidatId;
        this.offreId = offreId;
        this.datePostulation = datePostulation;
        this.statut = statut;

    }



    public int getCandidatId() {
        return candidatId;
    }

    public void setCandidatId(int candidatId) {
        this.candidatId = candidatId;
    }

    public int getOffreId() {
        return offreId;
    }

    public void setOffreId(int offreId) {
        this.offreId = offreId;
    }

    public Date getDatePostulation() {
        return datePostulation;
    }

    public void setDatePostulation(Date datePostulation) {
        this.datePostulation = datePostulation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

}