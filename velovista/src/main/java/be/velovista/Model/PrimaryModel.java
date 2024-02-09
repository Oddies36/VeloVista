package be.velovista.Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.velovista.Model.BL.Accessoire;
import be.velovista.Model.BL.ClasseConteneur;
import be.velovista.Model.BL.Location;
import be.velovista.Model.BL.UserConnected;
import be.velovista.Model.BL.Velo;
import be.velovista.Model.DAL.DAO.Abonnement.AbonnementDAO;
import be.velovista.Model.DAL.DAO.Abonnement.IAbonnementDAO;
import be.velovista.Model.DAL.DAO.Location.ILocationDAO;
import be.velovista.Model.DAL.DAO.Location.LocationDAO;
import be.velovista.Model.DAL.DAO.User.IUserDAO;
import be.velovista.Model.DAL.DAO.User.UserDAO;
import be.velovista.Model.DAL.DAO.Velo.IVeloDAO;
import be.velovista.Model.DAL.DAO.Velo.VeloDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PrimaryModel implements IModel {
      private PropertyChangeSupport support;
      private IUserDAO iuserdao = new UserDAO();
      private IVeloDAO ivelodao = new VeloDAO();
      private IAbonnementDAO iabonnementdao = new AbonnementDAO();
      private ILocationDAO ilocationdao = new LocationDAO();
      private UserConnected userConnected = new UserConnected();
      Alert alert;


    public PrimaryModel(){
        this.support = new PropertyChangeSupport(this);
        //this.iSectionDAO = new SectionDAO("jdbc:postgresql://192.168.1.13/test1", "postgres", "password");
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    //Profile methods

    public void getInfoProfilePage(){
        ClasseConteneur classcon = new ClasseConteneur(null, this.userConnected);

        this.ilocationdao.getLocation(this.userConnected.getUser().geteMail());

        support.firePropertyChange("retour-info-profile", "", classcon);


    }

    //User methods

    public boolean passwordMatch(String email, String password){
        String hashedPassword = hashPassword(password);
        String dbPassword = getHashedPassword(email);

        if(hashedPassword.equals(dbPassword)){
            this.userConnected.setUser(iuserdao.getUser(email));

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Connexion");
            alert.setHeaderText("Connexion réussie!");
            alert.setContentText("Connexion réussie. Bienvenu "+userConnected.getUser().getNom()+" "+userConnected.getUser().getPrenom());
            alert.showAndWait();
            return true;
        }
        else{
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Connexion");
            alert.setHeaderText("Connexion échouée");
            alert.setContentText("Email ou mot de passe incorrect. Veuillez réessayer.");
            alert.showAndWait();
            return false;
        }
    }

    public boolean CreationCompteUtilisateur(String nom, String prenom, String Email, String numTel, String mdpClaire, String mdpClaireRepeat){

        int mailExiste = this.checkEmailExiste(Email);
        if(checkNom(nom)){
            if(checkEmail(Email)){
                if(checkPuissanceMdp(mdpClaire)){
                    if(checkPasswordMatchCreationCompte(mdpClaire, mdpClaireRepeat)){
                        if(mailExiste == 0){
                            this.iuserdao.createUserAccount(nom, prenom, Email, numTel, this.hashPassword(mdpClaire));
                            alert = new Alert(AlertType.CONFIRMATION);
                            alert.setTitle("Création réussie!");
                            alert.setContentText("Votre compte a été créé. Vous pouvez maintenant utiliser votre compte pour vous connecter.");
                            alert.showAndWait();
                            return true;
                        }
                        else if(mailExiste == -1){
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Erreur");
                            alert.setContentText("Problème de vérification Email.");
                            alert.showAndWait();
                            return false;
                        }
                        else{
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Erreur");
                            alert.setContentText("L'adresse mail est déjà utilisée.");
                            alert.showAndWait();
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean checkPuissanceMdp(String mdpClaire){
        if(mdpClaire.length() >= 6){
            return true;
        }
        else{
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Mot de passe");
            alert.setContentText("Le mot de passe doit contenir au moins 6 caractères.");
            alert.showAndWait();
            return false;
        }
    }

    public boolean checkNom(String nom){
        if(nom.isEmpty()){
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Création de compte");
            alert.setContentText("Un ou plusieurs champs sont vides!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public boolean checkEmail(String Email){
        String regexEmail = "^[^@]+@[^@\\.]+(\\.[^@\\.]+)+$";
        Pattern pat = Pattern.compile(regexEmail);
        Matcher match = pat.matcher(Email);

        if(match.matches()){
            return true;
        }
        else{
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Email");
            alert.setContentText("Le format de l'adresse mail n'est pas valide");
            alert.showAndWait();
            return false;
        }

    }

    public int checkEmailExiste(String Email){
        int existingAccounts = this.iuserdao.getEmailCount(Email);
        if(existingAccounts == 0){
            return 0;
        }
        else if(existingAccounts == -1){
            return -1;
        }
        else{
            return 2;
        }
    }

    public boolean checkPasswordMatchCreationCompte(String mdpClaire, String mdpClaireRepeat){
        if(mdpClaire.equals(mdpClaireRepeat)){
            return true;
        }
        else{
            showAlert(AlertType.ERROR, "Mot de passe", "Les mots de passe ne sont pas identiques!");
            return false;
        }
    }

    public String hashPassword(String clearPassword){
        int hash = 7;
        for (int i = 0; i < clearPassword.length(); i++) {
            hash = hash * 31 + clearPassword.charAt(i);
        }
        return Integer.toHexString(hash);
    }

    public String getHashedPassword(String email){
        return iuserdao.getUserPassword(email);
    }

    public boolean writeNewPassword(String Email, String mdp, String repeatMdp){

        if(this.checkPasswordMatchCreationCompte(mdp, repeatMdp)){
            if(this.checkPuissanceMdp(mdp)){
                this.iuserdao.changeUserPassword(this.hashPassword(repeatMdp), Email);
                this.showAlert(AlertType.CONFIRMATION, "Mot de passe", "Votre mot de passe a été réinitialisé");
                return true;
            }
        }
        return false;
    }

    //Methodes Velos

    public void louerVelo(int id){
        System.out.println(this.userConnected.getUser().getNom());
    }

    public void getPrixVelos(){
        ArrayList<String> listePrixTypeVelos = new ArrayList<>();

        listePrixTypeVelos.add(Integer.toString(this.ivelodao.getPrixClassique()));
        listePrixTypeVelos.add(Integer.toString(this.ivelodao.getPrixElectrique()));
        listePrixTypeVelos.add(Integer.toString(this.ivelodao.getPrixEnfant()));

        this.support.firePropertyChange("retour-liste-prix-type-velos", "", listePrixTypeVelos);
    }

    public void getVitessesVelosClassique(){
        ArrayList<Integer> listeIntVitesses = new ArrayList<>();
        ArrayList<String> listeVitesses = new ArrayList<>();

        listeIntVitesses = this.ivelodao.getVitesses();
        for(int vitesse : listeIntVitesses){
            listeVitesses.add(Integer.toString(vitesse));
        }

        this.support.firePropertyChange("retour-liste-vitesses-velos", "", listeVitesses);
    }

    public void getListeVeloClassique(String typeVelo){
        ArrayList<Velo> listeVeloClassique = new ArrayList<>();

        switch (typeVelo){
            case "Classique":
                listeVeloClassique = this.ivelodao.getListeVeloClass();
                this.support.firePropertyChange("retour-liste-velos-class", "", listeVeloClassique);
                break;
            case "Electrique":
                listeVeloClassique = this.ivelodao.getListeVeloElec();
                this.support.firePropertyChange("retour-liste-velos-class", "", listeVeloClassique);
                break;
            case "Enfant":
                listeVeloClassique = this.ivelodao.getListeVeloEnfant();
                this.support.firePropertyChange("retour-liste-velos-class", "", listeVeloClassique);
                break;
            default:
                break;
        }
    }

    public Velo getVeloChoixUtilisateur(int id){
        Velo v = ivelodao.getVelo(id);
        return v;
    }



    public void showAlert(AlertType alertType, String title, String context){
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(context);
        alert.showAndWait();
    }


    //Methodes abonnements

    public void getListeAbo(){
        support.firePropertyChange("resultat-nom-liste-abonnements", "", this.iabonnementdao.getAbonnements());
    }
}
