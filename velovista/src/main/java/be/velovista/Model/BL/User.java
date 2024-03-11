package be.velovista.Model.BL;

public class User {

    private int idUser;
    private String nom;
    private String prenom;
    private String eMail;
    private String numTelephone;
    private int totalKM;

    public int getTotalKM() {
        return totalKM;
    }

    public void setTotalKM(int totalKM) {
        this.totalKM = totalKM;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getNumTelephone() {
        return numTelephone;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    public User(int idUser, String nom, String prenom, String eMail, String numTelephone, int totalKM) {
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        this.eMail = eMail;
        this.numTelephone = numTelephone;
        this.totalKM = totalKM;
    }

}
