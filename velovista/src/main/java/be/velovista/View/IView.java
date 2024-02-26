package be.velovista.View;

import java.time.LocalDate;
import java.util.ArrayList;

import be.velovista.Controller.Controller;
import be.velovista.Model.BL.Abonnement;
import be.velovista.Model.BL.Accessoire;
import be.velovista.Model.BL.Velo;
import javafx.scene.control.Alert.AlertType;

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
  public void showListeAboDispo(ArrayList<Abonnement> listeAbo);
  public void showChoixVeloUtilisateur(Velo v);
  public void showChoixAccessoires(Velo v, ArrayList<Accessoire> listeAccessoires);
  public void showChoixAbonnements(Velo v, ArrayList<String> listeAccessoires, ArrayList<Abonnement> listeAbo);
  public void showAlert(AlertType alertType, String title, String context);
  public void showRecapView(Velo v, double prixAbo, double prixAcc, double prixTotal, String nomAbo, LocalDate dateDebut, LocalDate dateFin, ArrayList<String> listeAccessoires);
  public void showProfilePage(ArrayList<ArrayList<String>> listeStringVeloUser);
}
