package be.velovista.Model.DAL.DAO.Merite_utilisateur;

import java.util.ArrayList;

import be.velovista.Model.BL.Merites;

public interface IMerite_utilisateurDAO {
    public void createMeritesTable();
    public ArrayList<Merites> getListeMeritesNonObtenu(int idUser);
    public ArrayList<Merites> getListeMeritesObtenu(int idUser);
    public int getDernierMeriteObtenu(int idUser);
    public int getCritereProchainPalier(int lvlActuel);
    public ArrayList<Integer> getListeCriteres();
    public void updateMeritesObtenus(int meriteObtenu, int idUser);
}
