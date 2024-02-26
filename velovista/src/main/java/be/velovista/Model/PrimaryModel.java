package be.velovista.Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.velovista.Model.BL.Abonnement;
import be.velovista.Model.BL.Accessoire;
import be.velovista.Model.BL.User;
import be.velovista.Model.BL.UserConnected;
import be.velovista.Model.BL.Velo;
import be.velovista.Model.DAL.DAO.Abonnement.AbonnementDAO;
import be.velovista.Model.DAL.DAO.Abonnement.IAbonnementDAO;
import be.velovista.Model.DAL.DAO.AbonnementUtilisateur.AbonnementUtilisateurDAO;
import be.velovista.Model.DAL.DAO.AbonnementUtilisateur.IAbonnementUtilisateurDAO;
import be.velovista.Model.DAL.DAO.Accessoire.AccessoireDAO;
import be.velovista.Model.DAL.DAO.Accessoire.IAccessoireDAO;
import be.velovista.Model.DAL.DAO.Accessoire_Location.Accessoire_LocationDAO;
import be.velovista.Model.DAL.DAO.Accessoire_Location.IAccessoire_LocationDAO;
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
      private IAccessoireDAO iaccessoiredao = new AccessoireDAO();
      private UserConnected userConnected = new UserConnected();
      private IAbonnementUtilisateurDAO iabouser = new AbonnementUtilisateurDAO();
      private IAccessoire_LocationDAO iacclocdao = new Accessoire_LocationDAO();
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

    //Combine 2 ArrayList dans une autre ArrayList
    //Utilisé dans la page profil. Affiche la page via firepropertychange
    public void getInfoProfilePage(){
        ArrayList<ArrayList<String>> comboList = new ArrayList<>();
        ArrayList<String> listeInfoVeloActuel = this.listeStringVeloActuel();
        ArrayList<String> listeInfoUserActuel = this.listeStringUser();

        comboList.add(listeInfoVeloActuel);
        comboList.add(listeInfoUserActuel);

        support.firePropertyChange("show-page-profil", "", comboList);
    }

    //Ajoute chaque field du Users dans une ArrayList de String
    //Utilisé pour la page profil
    public ArrayList<String> listeStringUser(){
        ArrayList<String> listeInfoUserActuel = new ArrayList<>();
        User u = this.iuserdao.getUser(this.userConnected.getUser().geteMail());
        listeInfoUserActuel.add(Integer.toString(u.getIdUser()));
        listeInfoUserActuel.add(u.getNom());
        listeInfoUserActuel.add(u.getPrenom());
        listeInfoUserActuel.add(u.geteMail());
        listeInfoUserActuel.add(u.getNumTelephone());
        listeInfoUserActuel.add(Integer.toString(u.getTotalKM()));

        return listeInfoUserActuel;
    }

    //Ajoute chaque field du vélo en location actuel de l'utilisateur connecté dans une ArrayList de String
    //Utilisé pour la page profil
    public ArrayList<String> listeStringVeloActuel(){
        ArrayList<String> listeInfoVeloActuel = new ArrayList<>();
        int idVelo = this.iabouser.getIdVeloFromIdUser(this.userConnected.getUser().getIdUser());
        Velo v = this.ivelodao.getVelo(idVelo);
        if(v != null){
        listeInfoVeloActuel.add(Integer.toString(v.getIdVelo()));
        listeInfoVeloActuel.add(v.getNumeroSerie());
        listeInfoVeloActuel.add(v.getModele());
        listeInfoVeloActuel.add(v.getType());
        listeInfoVeloActuel.add(Boolean.toString(v.isDisponible()));
        listeInfoVeloActuel.add(v.getCouleur());
        listeInfoVeloActuel.add(Integer.toString(v.getTaille()));
        listeInfoVeloActuel.add(Integer.toString(v.getAge()));
        listeInfoVeloActuel.add(Double.toString(v.getPrix()));
        listeInfoVeloActuel.add(v.getPhoto());
        }
        return listeInfoVeloActuel;
    }

    public void sauvegardeProfil(String nouveauNomUser, String nouveauPrenomUser, String nouveauEmailUser, String nouveauNumTelUser){
        if(!nouveauNomUser.equals("")){
            this.userConnected.getUser().setNom(nouveauNomUser);
            this.support.firePropertyChange("test", "", nouveauNomUser);
        }
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
        this.support.firePropertyChange("resultat-choix-velo-utilisateur", "", v);
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
        support.firePropertyChange("resultat-nom-liste-abonnements", "", this.iabonnementdao.getListeAbonnements());
    }

    public ArrayList<Abonnement> getListeChoixAbo(){
        return this.iabonnementdao.getListeAbonnements();
    }

    //Methodes accessoires

    public ArrayList<Accessoire> getAccessoires(){
        return iaccessoiredao.getAccessoires();
    }


    public LocalDate calculDateFin(LocalDate dateDebut, String nomAbonnement){
        LocalDate dateFin = null;
        switch(nomAbonnement){
            case "Journalier":
            dateFin = dateDebut.plusDays(1);
            break;
            case "Hebdomadaire":
            dateFin = dateDebut.plusWeeks(1);
            break;
            case "Mensuel":
            dateFin = dateDebut.plusMonths(1);
            break;
            case "Semestriel":
            dateFin = dateDebut.plusMonths(6);
            break;
            case "Trimestriel":
            dateFin = dateDebut.plusMonths(3);
            break;
            case "Annuel":
            dateFin = dateDebut.plusYears(1);
            break;
            default:
            break;
        }
        return dateFin;
    }
    public double calculPrixAbonnement(Velo v, String nomAbonnement){
        double prixAbonnement = 0.00;

            switch(nomAbonnement){
                case "Journalier":
                    prixAbonnement = v.getPrix();
                break;
                case "Hebdomadaire":
                    prixAbonnement = v.getPrix() * 6;
                break;
                case "Mensuel":
                    prixAbonnement = v.getPrix() * 27;
                break;
                case "Semestriel":
                    prixAbonnement = v.getPrix() * 27 * 6;
                break;
                case "Trimestriel":
                    prixAbonnement = v.getPrix() * 27 * 3;
                break;
                case "Annuel":
                    prixAbonnement = v.getPrix() * 25 * 12;
                break;
                default:
                break;
            }
        return prixAbonnement;
    }
    public double calculPrixAbonnementParType(Velo v, String nomAbonnement){
        String typeVelo = v.getType();
        double prixAbonnementBase = this.calculPrixAbonnement(v, nomAbonnement);
        double prixAbonnementApresCalcul = prixAbonnementBase;

        switch(typeVelo){
            case "Classique":
                if (v.getAge() < 2020){
                    prixAbonnementApresCalcul = prixAbonnementBase - (prixAbonnementBase * 0.30);
                }
            break;
            case "Electrique":
                if(v.getAutonomieFromVelo() >= 100){
                    prixAbonnementApresCalcul = prixAbonnementBase + (prixAbonnementBase * 0.25);
                }
                else if(v.getAutonomieFromVelo() >= 50 && v.getAutonomieFromVelo() < 100){
                    prixAbonnementApresCalcul = prixAbonnementBase + (prixAbonnementBase * 0.15);
                }
            break;
            case "Enfant":
            break;
            default:
            break;
        }
        return prixAbonnementApresCalcul;
    }

    public double calculPrixTotalAccessoires(ArrayList<String> listeIdAccessoires){
        double prixTotal = 0.00;
        ArrayList<Accessoire> listeAccessoiresChoisis = new ArrayList<>();

        for(String accId : listeIdAccessoires){
            listeAccessoiresChoisis.add(this.iaccessoiredao.getAccessoireFromId(accId));
        }
        for(Accessoire acc : listeAccessoiresChoisis){
            prixTotal += acc.getPrixAccessoire();
        }
        return prixTotal;
    }

    public ArrayList<Double> calculPrixTotalLocation(Velo v, String nomAbonnement, ArrayList<String> listeAccessoires){
        ArrayList<Double> listePrix = new ArrayList<>();
        double prixAbonnement = this.calculPrixAbonnementParType(v, nomAbonnement);
        double prixAccessoires = this.calculPrixTotalAccessoires(listeAccessoires);

        listePrix.add(prixAbonnement);
        listePrix.add(prixAccessoires);
        listePrix.add(prixAbonnement + prixAccessoires);

        return listePrix;
    }

    public int createAbonnement(Velo v, String nomAbo, double prixAbo){
        int idAbo = this.iabonnementdao.getAbonnementId(nomAbo);
        int idAboUser = this.iabouser.insertAbonnementUtilisateur(idAbo, v.getIdVelo(), this.userConnected.getUser().getIdUser(), prixAbo);

        return idAboUser;
    }
    public int createLocation(int idAbonnementUtilisateur, double prixTotal, LocalDate dateDebut, LocalDate dateFin){
        int idLoc = this.ilocationdao.insertLocation(idAbonnementUtilisateur, prixTotal, dateDebut, dateFin);

        return idLoc;
    }
    public void createAccessoireLocation(ArrayList<String> listeAccessoires, int idLoc){
        this.iacclocdao.insertAccessoireLocation(listeAccessoires, idLoc);
    }
    public void updateVeloToDispo(int idVelo){
        this.ivelodao.updateVeloToDispo(idVelo);
    }
    public void updateVeloToIndispo(int idVelo){
        this.ivelodao.updateVeloToIndispo(idVelo);
    }

    // public boolean checkLocationExists(LocalDate dateDebut, LocalDate dateFin){
    //     int exists = ilocationdao.checkCurrentLocation(dateDebut, dateFin);

    //     if(exists > 0){
    //         return true;
    //     }
    //     else{
    //         return false;
    //     }
    // }
    //check si une location est déjà actif pour la dernière étape de la réservation
    public boolean checkAboUserExists(){
        int exists = iabouser.checkCurrentAboUser(this.userConnected.getUser().getIdUser());

        if(exists > 0){
            return true;
        }
        else{
            return false;
        }
    }

    //Methode qui s'active quand on rend le vélo
    public boolean updateVeloActuelStatus(int intIdVelo){
        this.updateTotalKMUser();
        this.updateVeloToDispo(intIdVelo);
        this.updateAbonnementUserToInactif();

        return true;
    }

    //Met l'abonnementUser en statut Inactif quand le vélo a été rendu
    public void updateAbonnementUserToInactif(){
        this.iabouser.updateAbonnementUtilisateurInactif(this.userConnected.getUser().getIdUser());
    }

    //Ajoute les KM au CurrentUser et met a jour la DB avec le total des KM parcouru
    public void updateTotalKMUser(){
        int randomKM = this.generateRandomKM();
        int currentKMUser = this.userConnected.getUser().getTotalKM();
        int newKMUser = currentKMUser + randomKM;

        this.iuserdao.updateTotalKMUser(newKMUser, this.userConnected.getUser().getIdUser());
    }
    //Genere un nombre de KM random pour chaque jour que le vélo a été loué. Par jour de 0 a 30km est généré
    public int generateRandomKM(){
        int nbJours = this.calculNbJoursLocation();
        int kmTotalGenere = 0;
        Random random = new Random();
        for (int i = 0; i < nbJours; i++){
            int randKM = random.nextInt(31);
            kmTotalGenere += randKM;
        }
        return kmTotalGenere;
    }

    //calcul le nombre de jours de la location pour generer un random km réaliste par jour
    public int calculNbJoursLocation(){
        int nombreJours = 0;
        
        LocalDate dateAuj = LocalDate.now();
        LocalDate dateDebutLoc = this.ilocationdao.getDateDebutCurrentLocation(this.userConnected.getUser().getIdUser());
        nombreJours = (int) ChronoUnit.DAYS.between(dateDebutLoc, dateAuj);

        return nombreJours;
    }

}
