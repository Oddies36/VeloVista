package be.velovista.Model.DAL.DAO.Accessoire;

import java.util.ArrayList;

import be.velovista.Model.BL.Accessoire;

public interface IAccessoireDAO {
    public void createAccessoireTable();
    public ArrayList<Accessoire> getAccessoires();
}
