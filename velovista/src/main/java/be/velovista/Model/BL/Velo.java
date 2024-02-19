package be.velovista.Model.BL;

public abstract class Velo {
    
    private int idVelo;
    private String numeroSerie;
    private String marque;
    private String type;
    private boolean disponible;
    private String couleur;
    private int taille;
    private int age;
    private double prix;
    private String photo;

    public int getIdVelo() {
        return idVelo;
    }
    public void setIdVelo(int idVelo) {
        this.idVelo = idVelo;
    }
    public String getNumeroSerie(){
        return numeroSerie;
    }
    public void setNumeroSerie(String numeroSerie){
        this.numeroSerie = numeroSerie;
    }
    public String getModele() {
        return marque;
    }
    public void setModele(String modele) {
        this.marque = modele;
    }

    public String getType(){
        return type;
    }
    public void getType(String type){
        this.type = type;
    }

    public boolean isDisponible() {
        return disponible;
    }
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getCouleur() {
        return couleur;
    }
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public int getTaille() {
        return taille;
    }
    public void setTaille(int taille) {
        this.taille = taille;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public double getPrix(){
        return prix;
    }
    public void setPrix(Double prix){
        this.prix = prix;
    }

    public String getPhoto(){
        return photo;
    }
    public void setPhoto(String photo){
        this.photo = photo;
    }

    public Velo(int idVelo, String numeroSerie, String marque, String type, boolean disponible, String couleur, int taille, int age, double prix, String photo){
        this.idVelo = idVelo;
        this.numeroSerie = numeroSerie;
        this.marque = marque;
        this.type = type;
        this.disponible = disponible;
        this.couleur = couleur;
        this.taille = taille;
        this.age = age;
        this.prix = prix;
        this.photo = photo;
    }


    public int getVitessesFromVelo(){
        return -1;
    }
    public int getAutonomieFromVelo(){
        return -1;
    }
    
}
