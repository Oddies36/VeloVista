package be.velovista.Model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import be.velovista.Model.BL.Abonnement;
import be.velovista.Model.BL.Accessoire;
import be.velovista.Model.BL.Velo;
import javafx.scene.control.Alert.AlertType;

public interface IModel {
    public void addPropertyChangeListener(PropertyChangeListener pcl);
    public void removePropertyChangeListener(PropertyChangeListener pcl);
    public boolean passwordMatch(String email, String password);
    public String hashPassword(String clearPassword);
    public String getHashedPassword(String email);
    public void getPrixVelos();
    public void getListeVeloClassique(String typeVelo);
    public boolean CreationCompteUtilisateur(String nom, String prenom, String Email, String numTel, String mdpClaire, String mdpClaireRepeat);
    public boolean checkPasswordMatchCreationCompte(String mdpClaire, String mdpClaireRepeat);
    public boolean checkPuissanceMdp(String mdpClaire);
    public boolean writeNewPassword(String Email, String mdp, String repeatMdp);
    public int checkEmailExiste(String Email);
    public void showAlert(AlertType alertType, String title, String context);
    public Velo getVeloChoixUtilisateur(int id);
    public void getListeAbo();
    public ArrayList<Accessoire> getAccessoires();
    public ArrayList<Abonnement> getListeChoixAbo();
    //public void getInfoProfilePage();
}
