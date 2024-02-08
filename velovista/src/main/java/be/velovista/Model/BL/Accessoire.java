package be.velovista.Model.BL;
import java.util.ArrayList;

public class Accessoire {

    public enum NomAccessoires{
        Casque,
        Cadenas,
        PorteBadge,
        chaiseEnfant
    }

    private int idAccessoires;
    private ArrayList<NomAccessoires> listeAccessoires;

    public int getIdAccessoires(){
        return this.idAccessoires;
    }
    public void setIdAccessoires(int idAccessoire){
        this.idAccessoires = idAccessoire;
    }

    public ArrayList<NomAccessoires> getListeAccessoires(){
        return this.listeAccessoires;
    }

    public Accessoire(int idAccessoires, ArrayList<NomAccessoires> listeAccessoires){
        this.idAccessoires = idAccessoires;
        this.listeAccessoires = listeAccessoires;
    }
}
