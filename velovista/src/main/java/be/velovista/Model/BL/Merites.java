package be.velovista.Model.BL;

import java.time.LocalDate;

public class Merites {
    private int idMerite;
    private String nomMerite;
    private String descriptionMerite;
    private int critere;
    private LocalDate dateObtenu;

    public LocalDate getDateObtenu() {
        return dateObtenu;
    }

    public void setDateObtenu(LocalDate dateObtenu) {
        this.dateObtenu = dateObtenu;
    }

    public int getIdMerite() {
        return idMerite;
    }

    public void setIdMerite(int idMerite) {
        this.idMerite = idMerite;
    }

    public String getNomMerite() {
        return nomMerite;
    }

    public void setNomMerite(String nomMerite) {
        this.nomMerite = nomMerite;
    }

    public String getDescriptionMerite() {
        return descriptionMerite;
    }

    public void setDescriptionMerite(String descriptionMerite) {
        this.descriptionMerite = descriptionMerite;
    }

    public int getCritere() {
        return critere;
    }

    public void setCritere(int critere) {
        this.critere = critere;
    }

    public Merites(int idMerite, String nomMerite, String descriptionMerite, int critere) {
        this.idMerite = idMerite;
        this.nomMerite = nomMerite;
        this.descriptionMerite = descriptionMerite;
        this.critere = critere;
    }

    public Merites(int idMerite, String nomMerite, String descriptionMerite, int critere, LocalDate dateObtenu) {
        this.idMerite = idMerite;
        this.nomMerite = nomMerite;
        this.descriptionMerite = descriptionMerite;
        this.critere = critere;
        this.dateObtenu = dateObtenu;
    }
}
