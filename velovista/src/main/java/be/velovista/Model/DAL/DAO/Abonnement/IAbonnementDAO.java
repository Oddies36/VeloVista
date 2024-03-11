package be.velovista.Model.DAL.DAO.Abonnement;

import java.util.ArrayList;

import be.velovista.Model.BL.Abonnement;

public interface IAbonnementDAO {
  public void createAbonnementTable();

  public ArrayList<Abonnement> getListeAbonnements();
  
  public int getAbonnementId(String nomAbo);
  
  public int checkDonneesExiste();
  
  public void insertDonneesAbonnement();
  
  // public ArrayList<String> getAbonnements();
}
