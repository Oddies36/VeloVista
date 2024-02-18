package be.velovista.Model.BL;

public class Abonnement {
    
    private int idAbonnement;
    private String nomAbonnement;

    public int getIdAbonnement() {
        return idAbonnement;
    }
    public void setIdAbonnement(int idAbonnement) {
        this.idAbonnement = idAbonnement;
    }
    public String getNomAbonnement() {
        return nomAbonnement;
    }

    public Abonnement(int idAbonnement, String nomAbonnement){
        this.idAbonnement = idAbonnement;
        this.nomAbonnement = nomAbonnement;
    }
}
