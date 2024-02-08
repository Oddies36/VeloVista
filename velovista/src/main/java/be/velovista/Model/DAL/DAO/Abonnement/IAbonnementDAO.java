package be.velovista.Model.DAL.DAO.Abonnement;

import java.util.ArrayList;

public interface IAbonnementDAO {
  public void createAbonnementTable();
  public ArrayList<String> getAbonnements();
}
