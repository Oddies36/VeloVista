package be.velovista.Model.DAL.DAO.Merites;

import java.util.ArrayList;

public interface IMeritesDAO {
    public void createMeritesTable();

    public int getCritereProchainPalier(int lvlActuel);

    public ArrayList<Integer> getListeCriteres();

    public int checkDonneesExiste();

    public void insertDonneesMerite();
}
