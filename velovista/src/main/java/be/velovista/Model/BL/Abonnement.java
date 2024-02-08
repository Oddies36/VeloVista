package be.velovista.Model.BL;

public class Abonnement {
    
    private int idAbonnement;
    private String nomAbonnement;
    private double prixAbonnement;

    public int getIdAbonnement() {
        return idAbonnement;
    }
    public void setIdAbonnement(int idAbonnement) {
        this.idAbonnement = idAbonnement;
    }
    public String getNomAbonnement() {
        return nomAbonnement;
    }
    public double getPrixAbonnement(){
        return prixAbonnement;
    }
    public void setPrixAbonnement(double prixAbonnement){
        this.prixAbonnement = prixAbonnement;
    }

    public Abonnement(int idAbonnement, String nomAbonnement, double prixAbonnement){
        this.idAbonnement = idAbonnement;
        this.nomAbonnement = nomAbonnement;
        this.prixAbonnement = prixAbonnement;
    }
}
