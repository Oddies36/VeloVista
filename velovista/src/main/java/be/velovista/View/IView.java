package be.velovista.View;

import java.util.ArrayList;

import be.velovista.Controller.Controller;
import be.velovista.Model.BL.Velo;

public interface IView {
  public void setController(Controller control);
  public void launchApp();
  public void stopApp();
  public void showMainScreen();
  public void showLoginScreen();
  public void showAccountCreation();
  public void showVeloCategory(ArrayList<String> listePrixTypeVelos);
  public void showListeVeloClassique(ArrayList<Velo> listeVelosClass);
  public void showMdpOublie();
  public void showMdpResetAvecMail(String Email);
  public void showListeAboDispo(ArrayList<String> listeAbo);
  public void showChoixVeloUtilisateur(Velo v);
  
}
