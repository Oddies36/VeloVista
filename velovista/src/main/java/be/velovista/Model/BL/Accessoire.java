package be.velovista.Model.BL;

public class Accessoire {

    // public enum NomAccessoires{
    //     Casque,
    //     Cadenas,
    //     PorteBadge,
    //     chaiseEnfant
    // }

    private int idAccessoires;
    private String nomAccessoire;
    private double prixAccessoire;

    public int getIdAccessoires(){
        return this.idAccessoires;
    }
    public void setIdAccessoires(int idAccessoire){
        this.idAccessoires = idAccessoire;
    }
    public String getNomAccessoire(){
        return this.nomAccessoire;
    }
    public void setNomAccessoire(String nomAccessoire){
        this.nomAccessoire = nomAccessoire;
    }
    public double getPrixAccessoire(){
        return this.prixAccessoire;
    }
    public void setPrixAccessoire(double prixAccessoire){
        this.prixAccessoire = prixAccessoire;
    }


    public Accessoire(int idAccessoires, String nomAccessoire, double prixAccessoire){
        this.idAccessoires = idAccessoires;
        this.nomAccessoire = nomAccessoire;
        this.prixAccessoire = prixAccessoire;
    }
}
