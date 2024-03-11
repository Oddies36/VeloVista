package be.velovista.Model.BL;

public class Accessoire {

    private int idAccessoires;
    private String nomAccessoire;
    private double prixAccessoire;
    private String photoAccessoire;
    private String descriptionAccessoire;

    public int getIdAccessoires() {
        return this.idAccessoires;
    }

    public void setIdAccessoires(int idAccessoire) {
        this.idAccessoires = idAccessoire;
    }

    public String getNomAccessoire() {
        return this.nomAccessoire;
    }

    public void setNomAccessoire(String nomAccessoire) {
        this.nomAccessoire = nomAccessoire;
    }

    public double getPrixAccessoire() {
        return this.prixAccessoire;
    }

    public void setPrixAccessoire(double prixAccessoire) {
        this.prixAccessoire = prixAccessoire;
    }

    public String getPhotoAccessoire() {
        return this.photoAccessoire;
    }

    public void setPhotoAccessoire(String photoAccessoire) {
        this.photoAccessoire = photoAccessoire;
    }

    public String getDescriptionAccessoire() {
        return this.descriptionAccessoire;
    }

    public void setDescriptionAccessoire(String descriptionAccessoire) {
        this.descriptionAccessoire = descriptionAccessoire;
    }

    public Accessoire(int idAccessoires, String nomAccessoire, double prixAccessoire, String photoAccessoire,
            String descriptionAccessoire) {
        this.idAccessoires = idAccessoires;
        this.nomAccessoire = nomAccessoire;
        this.prixAccessoire = prixAccessoire;
        this.photoAccessoire = photoAccessoire;
        this.descriptionAccessoire = descriptionAccessoire;
    }
}
