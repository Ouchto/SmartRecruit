package org.example.smartrecruit.model;

import java.util.Date;

public class OffreEmploi {
    private int id;
    private String titre;
    private String description;
    private String typeContrat; // CDI, CDD, Stage, etc.
    private Date datePublication;



    public OffreEmploi() {}

    public OffreEmploi(int id, String titre, String description, String typeContrat, Date datePublication
                      ) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.typeContrat = typeContrat;
        this.datePublication = datePublication;

    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(String typeContrat) {
        this.typeContrat = typeContrat;
    }

    public Date getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Date datePublication) {
        this.datePublication = datePublication;
    }




}
