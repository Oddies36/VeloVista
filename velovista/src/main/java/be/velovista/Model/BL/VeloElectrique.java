package be.velovista.Model.BL;

public class VeloElectrique extends Velo {
    
    private int autonomie;

    public int getAutonomie() {
        return autonomie;
    }
    public void setAutonomie(int autonomie) {
        this.autonomie = autonomie;
    }

    public VeloElectrique(int autonomie, int idVelo, String numeroSerie, String marque, String type, boolean disponible, String couleur, int taille, int age, double prix, String photo) {
        super(idVelo, numeroSerie, marque, type, disponible, couleur, taille, age, prix, photo);

        this.autonomie = autonomie;
    }

    @Override
    public int getAutonomieFromVelo(){
        return autonomie;
    }
}
