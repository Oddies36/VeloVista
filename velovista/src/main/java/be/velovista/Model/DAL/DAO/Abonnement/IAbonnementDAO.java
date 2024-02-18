package be.velovista.Model.DAL.DAO.Abonnement;

import java.util.ArrayList;

import be.velovista.Model.BL.Abonnement;

public interface IAbonnementDAO {
  public void createAbonnementTable();
  public ArrayList<String> getAbonnements();
  public ArrayList<Abonnement> getListeAbonnements();
}
