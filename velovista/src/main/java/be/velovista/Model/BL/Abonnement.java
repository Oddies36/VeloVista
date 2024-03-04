package be.velovista.Model.BL;

public class Abonnement {
    
    private int idAbonnement;
    private String nomAbonnement;
    private String descriptionAbonnement;

    public String getDescriptionAbonnement() {
        return descriptionAbonnement;
    }
    public void setDescriptionAbonnement(String descriptionAbonnement) {
        this.descriptionAbonnement = descriptionAbonnement;
    }
    public int getIdAbonnement() {
        return idAbonnement;
    }
    public void setIdAbonnement(int idAbonnement) {
        this.idAbonnement = idAbonnement;
    }
    public String getNomAbonnement() {
        return nomAbonnement;
    }

    public Abonnement(int idAbonnement, String nomAbonnement, String descriptionAbonnement){
        this.idAbonnement = idAbonnement;
        this.nomAbonnement = nomAbonnement;
        this.descriptionAbonnement = descriptionAbonnement;
    }
}
