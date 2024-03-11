package be.velovista.Model.BL;

public class VeloClassique extends Velo {

    private int vitesses;

    public int getVitesses() {
        return vitesses;
    }

    public void setVitesses(int vitesses) {
        this.vitesses = vitesses;
    }

    public VeloClassique(int vitesses, int idVelo, String numeroSerie, String marque, String type, boolean disponible,
            String couleur, int taille, int age, double prix, String photo) {
        super(idVelo, numeroSerie, marque, type, disponible, couleur, taille, age, prix, photo);

        this.vitesses = vitesses;
    }

    @Override
    public int getVitessesFromVelo() {
        return vitesses;
    }
}
