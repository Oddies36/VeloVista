package be.velovista.Model.DAL.DAO.Accessoire_Location;

import java.util.ArrayList;

public interface IAccessoire_LocationDAO {
    public void createAccessoire_LocationTable();

    public void insertAccessoireLocation(ArrayList<String> listeAccessoires, int idLoc);

    // public ArrayList<Accessoire> getListeAccessoiresUser(String idLocation);
}
