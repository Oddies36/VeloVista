package be.velovista.Model;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;

import be.velovista.Model.BL.Abonnement;
import be.velovista.Model.BL.Accessoire;
import be.velovista.Model.BL.Velo;
import javafx.scene.control.Alert.AlertType;

public interface IModel {
    public void addPropertyChangeListener(PropertyChangeListener pcl);

    public void removePropertyChangeListener(PropertyChangeListener pcl);

    public void showAlert(AlertType alertType, String title, String context);

    public String hashPassword(String clearPassword);

    public String getHashedPassword(String email);

    public boolean passwordMatch(String email, String password);

    public boolean CreationCompteUtilisateur(String nom, String prenom, String Email, String numTel, String mdpClaire,
            String mdpClaireRepeat);

    public boolean checkNom(String nom);

    public boolean checkNomUpdate(String nom);

    public boolean checkNumTel(String numTel);

    public boolean checkEmail(String Email);

    public int checkEmailExiste(String Email);

    public boolean checkPasswordMatchCreationCompte(String mdpClaire, String mdpClaireRepeat);

    public boolean checkPuissanceMdp(String mdpClaire);

    public boolean writeNewPassword(String Email, String mdp, String repeatMdp);

    public void getInfoProfilePage();

    public ArrayList<String> listeStringUser();

    public ArrayList<String> listeStringVeloActuel();

    public void sauvegardeProfil(String nouveauNomUser, String nouveauPrenomUser, String nouveauEmailUser,
            String nouveauNumTelUser);

    public boolean updateVeloActuelStatus(int intIdVelo);

    public void updateTotalKMUser();

    public int generateRandomKM();

    public void getPrixVelos();

    public void getListeVeloClassique(String typeVelo);

    public Velo getVeloChoixUtilisateur(int id);

    public void updateVeloToDispo(int idVelo);

    public void updateVeloToIndispo(int idVelo);

    public void getListeAbo();

    public int createAbonnement(Velo v, String nomAbo, double prixAbo);

    public void createAboLocation(String listeNomsAccessoires, String listeInfoEtDates, String listePrix,
            String listeInfoVelo);

    public ArrayList<Abonnement> getListeChoixAbo();

    public boolean checkAboUserExists();

    public void updateAbonnementUserToInactif();

    public ArrayList<Accessoire> getAccessoires();

    public String getNomsAccessoires(String choixIdAccessoires);

    public void getAccessoiresString(String idVelo, String dateDebut, String dateFin, String choixAbonnement);

    public void calculPrixTotalLocationReservation(String infoVeloDates, String listeChoixIdAccessoires);

    public void insertReservation(String idVelo, String dateDebut, String dateFin, String choixAboUserString);

    public void annulerReservation(String idReservation);

    public void getReservations();

    public boolean checkDisponibilitesReservation(int idVelo, String dateDebut, String dateFin);

    public LocalDate calculDateFin(LocalDate dateDebut, String nomAbonnement);

    public double calculPrixAbonnement(Velo v, String nomAbonnement);

    public double calculPrixAbonnementParType(Velo v, String nomAbonnement);

    public double calculPrixTotalAccessoires(ArrayList<String> listeIdAccessoires);

    public ArrayList<Double> calculPrixTotalLocation(Velo v, String nomAbonnement, ArrayList<String> listeAccessoires);

    public int createLocation(int idAbonnementUtilisateur, double prixTotal, LocalDate dateDebut, LocalDate dateFin);

    public void createAccessoireLocation(ArrayList<String> listeAccessoires, int idLoc);

    public int calculNbJoursLocation();

    public String getMeritesObtenuString();

    public String getMeritesNonObtenuString();

    public void fustionneListeMerites();

    public int checkDernierMeriteObtenu();

    public int getCritereProchainPalier();

    public int calculNbKmRestants();

    public ArrayList<Integer> meritesObtenus();

    public double calculPointsBonus(int enregistrementKM);

    public void updatePointsBonus(double pointsBonus);

    public void updateMerites(ArrayList<Integer> lvlMeritesObtenus);

    public void showPageHistorique();

    public void checkDataExistsVelo();

    public void checkDataExistsMerite();

    public void checkDataExistsAbonnement();

    public void checkDataExistsAccessoire();
}
