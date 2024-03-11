package be.velovista.View;

import java.time.LocalDate;
import java.util.ArrayList;

import be.velovista.Controller.Controller;
import be.velovista.Model.BL.Abonnement;
import be.velovista.Model.BL.Accessoire;
import be.velovista.Model.BL.Velo;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public interface IView {
  public void setController(Controller control);

  public void showLoginScreen();

  public void showMainScreen();

  public void showHistorique(ArrayList<String> listeLocations);

  public void showAccountCreation();

  public void showMdpOublie();

  public void showMdpResetAvecMail(String Email);

  public void showProfilePage(ArrayList<ArrayList<String>> listeStringVeloUser);

  public void showMeriteView(ArrayList<String> listeMerites);

  public void showVeloCategory(ArrayList<String> listePrixTypeVelos);

  public void showListeVeloClassique(ArrayList<Velo> listeVelosClass);

  public void showListeAboDispo(String listeAbo);

  public void showChoixVeloUtilisateur(Velo v);

  public void showChoixAccessoires(Velo v, ArrayList<Accessoire> listeAccessoires);

  public void showChoixAccessoiresReservation(ArrayList<String> infoVeloAccessoiresDates);

  public void showChoixAbonnements(Velo v, ArrayList<String> listeAccessoires, ArrayList<Abonnement> listeAbo);

  public void showChoixAbonnementsReservation(Velo v, ArrayList<Abonnement> listeAbo);

  public void showMesReservations(String reservations);

  public void showRecapView(Velo v, double prixAbo, double prixAcc, double prixTotal, String nomAbo,
      LocalDate dateDebut, LocalDate dateFin, ArrayList<String> listeAccessoires);

  public void setButtonStyle(Button nomBouton, String type);

  public void showAlert(AlertType alertType, String title, String context);

  public void setLabelStyle(Label l);

  public String setDispoText(Boolean bool);

  public void setLabelColor(Label l);

  public void launchApp();

  public void stopApp();
}
