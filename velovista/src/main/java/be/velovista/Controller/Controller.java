package be.velovista.Controller;

import java.beans.PropertyChangeListener;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import be.velovista.Model.IModel;
import be.velovista.Model.PrimaryModel;
import be.velovista.Model.BL.Velo;
import be.velovista.View.IView;
import be.velovista.View.PrimaryView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.WindowEvent;

public class Controller {
  
    private IModel model;
    private IView view;

    public void initialize(){
        this.model = new PrimaryModel();
        this.view = new PrimaryView();
        if (PropertyChangeListener.class.isAssignableFrom(view.getClass())){
            PropertyChangeListener pcl = (PropertyChangeListener) view;            
            model.addPropertyChangeListener(pcl);
        }
        view.setController(this);
        this.checkDataExists();
    }

    public void start(){
        this.view.launchApp();
    }

    public EventHandler<ActionEvent> generateEventHandlerAction(String action, Supplier<String[]> params){    
        Consumer<String[]> function = this.generateConsumer(action);        
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                function.accept(params.get());;
            }
        };
    }

    private Consumer<String[]> generateConsumer(String action){
        Consumer<String[]> t;
        switch (action) {
            case "show-login-screen":
                t = (x) -> this.showLoginScreen();
                break;
            case "showAccountCreation":
                t = (x) -> this.showAccountCreation();
                break; 
            case "checkLoginCreds":
                t = (x) -> this.checkLoginCreds(x [0], x[1]);
                break;
            case "show-velo-page":
                t = (x) -> this.getPrixVelos();
                break;
            case "show-liste-velo-class":
                t = (x) -> this.getListeVeloClassique(x [0]);
                break;
            case "creation-compte-utilisateur":
                t = (x) -> this.CreationCompteUtilisateur(x [0], x [1], x [2], x [3], x [4], x [5]);
                break;
            case "show-mdp-oublie":
                t = (x) -> this.showMdpOublie();
            break;
            case "reinitialiser-mdp":
                t = (x) -> this.showMdpResetAvecMail(x [0]);
            break;
            case "write-new-password":
                t = (x) -> this.writePassword(x [0], x [1], x [2]);
            break;
            case "show-liste-abo":
                t = (x) -> this.showListeAbo();
            break;
            case "choix-velo-utilisateur":
                t = (x) -> this.getVeloChoixUtilisateur(x [0]);
            break;
            case "louer-velo":
                t = (x) -> this.louerVelo(x [0]);
            break;
            case "reserver-velo":
                t = (x) -> this.reserverVelo(x [0]);
            break;
            case "valider-reservation":
                t = (x) -> this.insertReservation(x [0], x [1], x [2]);
            break;
            case "show-page-profil":
                t = (x) -> this.getInfoProfilePage();
            break;
            case "retour-main-page":
                t = (x) -> this.showMainPage();
            break;
            case "sauvegarde-profil":
                t = (x) -> this.sauvegardeProfil(x [0], x [1], x [2], x [3]);
            break;
            case "retourner-velo":
                t = (x) -> this.retournerVelo(x [0]);
            break;
            case "merites-profil":
                t = (x) -> this.showMerites();
            break;
            case "show-mes-reservations":
                t = (x) -> this.showMesReservations();
            break;
            case "choix-accessoires-reservation":
                t = (x) -> this.showChoixAccessoiresReservation(x [0], x [1], x [2], x [3]);
            break;
            case "show-recap-view-reservation":
                t = (x) -> this.calculDesPrixReservation(x [0], x [1]);
            break;
            case "creation-location":
                t = (x) -> this.createAboAndLocationReservation(x [0], x [1], x [2], x [3]);
            break;
            case "show-page-historique":
                t = (x) -> this.showPageHistorique();
            break;
            case "annuler-reservation":
                t = (x) -> this.annulerReservation(x [0]);
            break;
            default:
                throw new InvalidParameterException(action + " n'existe pas.");
        }
        return t;
    }

    public EventHandler<WindowEvent> generateCloseEvent(){
        return new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                stop();
                System.exit(0);
            }
        };
    }

    //Annule une réservation quand on clique sur le bouton annuler dans "Mes réservations"
    public void annulerReservation(String idReservation){
        this.model.annulerReservation(idReservation);
        this.view.showAlert(AlertType.CONFIRMATION, "Annulation", "Vous avez bien annulée votre réservation");
        this.view.showMainScreen();
    }

    //Montre les accessoires après avoir réservé un vélo
    public void showChoixAccessoiresReservation(String idVelo, String dateDebut, String dateFin, String choixAbonnement){
        this.model.getAccessoiresString(idVelo, dateDebut, dateFin, choixAbonnement);
    }

    //Retourne le vélo quand on clique sur le bouton "retourner" dans "mon profil". Le vélo se met en actif et l'utilisateur
    //reçoit des points bonus + ajoute des KM a son profil
    public void retournerVelo(String idVelo){
        int intIdVelo = Integer.parseInt(idVelo);
        if(this.model.updateVeloActuelStatus(intIdVelo)){
            this.view.showAlert(AlertType.CONFIRMATION, "Retour vélo", "Vous avez bien retourné votre vélo");
            this.view.showAlert(AlertType.CONFIRMATION, "Points", "Des points bonus ont été ajoutés à votre compte. Vous pouvez les consulter dans la partie Mérites de votre profil.");
            this.view.showMainScreen();
        }
    }

    //Montre la page historique quand on clique sur le bouton "Mon historique" dans le main screen
    public void showPageHistorique(){
        this.model.showPageHistorique();
    }

    //Montre la page "Mes réservations" quand on clique sur le bouton "Mes réservations" dans le main screen
    public void showMesReservations(){
        this.model.getReservations();
    }

    //Montre la page "Mérites" quand on clique sur le bouton Mérites dans "Mon profil"
    public void showMerites(){
        this.model.fustionneListeMerites();
    }

    //Sauvegarde les nouvelles informations de l'utilisateur dans la DB et dans UserConnected
    public void sauvegardeProfil(String nouveauNomUser, String nouveauPrenomUser, String nouveauEmailUser, String nouveauNumTelUser){
        this.model.sauvegardeProfil(nouveauNomUser, nouveauPrenomUser, nouveauEmailUser, nouveauNumTelUser);
    }

    //Montre la page main screen
    public void showMainPage(){
        this.view.showMainScreen();
    }

    //Récupère les informations pour afficher dans la page "mon profil"
    public void getInfoProfilePage(){
        this.model.getInfoProfilePage();
    }

    //Montre la view des choix d'accessoires en prennant le choix du vélo et la liste des accessoires en paramètres
    public void louerVelo(String id){
        this.view.showChoixAccessoires(this.model.getVeloChoixUtilisateur(Integer.parseInt(id)), this.model.getAccessoires());
    }

    //Montre la view des abonnements en prennant le choix du vélo et la liste des abonnements en paramètres
    public void reserverVelo(String id){
        this.view.showChoixAbonnementsReservation(this.model.getVeloChoixUtilisateur(Integer.parseInt(id)), this.model.getListeChoixAbo());
    }

    //Récupère le vélo selon le choix de l'utilisateur
    public void getVeloChoixUtilisateur(String idVelo){
        this.model.getVeloChoixUtilisateur(Integer.parseInt(idVelo));
    }

    //Récupère la liste des abonnements
    public void showListeAbo(){
        this.model.getListeAbo();
    }

    //Ecrit le nouveau mot de passe de l'utilisateur quand il va sur "mot de passe perdu"
    public void writePassword(String Email, String mdp, String repeatMdp){
        if(this.model.writeNewPassword(Email, mdp, repeatMdp)){
            this.view.showLoginScreen();
        }
    }

    //vérifie la disponibilité du vélo a une certaine date choisie pour la partie réservation
    public boolean checkDisponiblites(int idVelo, LocalDate date, String choixAboUserString){
        String dateDebut = date.toString();
        String dateFin = this.model.calculDateFin(date, choixAboUserString).toString();
        if(this.model.checkDisponibilitesReservation(idVelo, dateDebut, dateFin)){
            return true;
        }
        return false;
    }

    //Inscrit une réservation dans la DB
    public void insertReservation(String idVelo, String dateDebut, String choixAboUserString){
        LocalDate dateDebutLD = LocalDate.parse(dateDebut);
        String dateFin = this.model.calculDateFin(dateDebutLD, choixAboUserString).toString();
        this.model.insertReservation(idVelo, dateDebut, dateFin, choixAboUserString);

        this.view.showAlert(AlertType.CONFIRMATION, "Reservation", "Votre réservation a bien été enregistrée. Veuillez prendre votre vélo dans la partie 'Mes réservations' à la date du début.");
        this.view.showMainScreen();
    }

    //Check si le mail existe quand l'utilisateur veut faire un reset de mdp
    public void showMdpResetAvecMail(String Email){
        if(this.model.checkEmailExiste(Email) == 2){
            this.view.showMdpResetAvecMail(Email);
        }
        else{
            this.model.showAlert(AlertType.ERROR, "Email", "Mauvaise adresse mail");
        }
    }

    //Montre la view du mot de passe oublié
    public void showMdpOublie(){
        this.view.showMdpOublie();
    }

    //Cree le compte utilisateur et montre la view du login
    public void CreationCompteUtilisateur(String nom, String prenom, String Email, String numTel, String mdpClaire, String mdpClaireRepeat){
        if(this.model.CreationCompteUtilisateur(nom, prenom, Email, numTel, mdpClaire, mdpClaireRepeat)){
            this.view.showLoginScreen();
        }
    }

    //Récupère la liste des vélos
    public void getListeVeloClassique(String typeVelo){
        this.model.getListeVeloClassique(typeVelo);
    }

    //récupère la liste des prix des vélos
    public void getPrixVelos(){
        this.model.getPrixVelos();
    }

    //Vérifie si les informations du login sont correctes. Si ok, montre le main screen et inscrit le user dans UserConnected
    public void checkLoginCreds(String email, String enteredPassword){
        if(this.model.passwordMatch(email, enteredPassword)){
            this.view.showMainScreen();
        }
    }

    //Montre la view de la création du compte
    public void showAccountCreation(){
        this.view.showAccountCreation();
    }

    //Montre la view de login
    public void showLoginScreen(){
        this.view.showLoginScreen();
    }

    //Montre la view des choid abonnements pendant la location
    public void showChoixAbonnements(String idVelo, ArrayList<String> choixAccessoires){
        this.view.showChoixAbonnements(this.model.getVeloChoixUtilisateur(Integer.parseInt(idVelo)), choixAccessoires, this.model.getListeChoixAbo());
    }

    //Appelle les calculs des prix pour une location
    public void calculDesPrix(Velo v, ArrayList<String> listeAccessoires, LocalDate date, String abo){
        if (date.isBefore(LocalDate.now())){
            this.view.showAlert(AlertType.ERROR, "Dates", "La date choisie est dans le passé.");
        }
        else{
            ArrayList<Double> listePrix = this.model.calculPrixTotalLocation(v, abo, listeAccessoires);
            LocalDate dateFin = this.model.calculDateFin(date, abo);
            this.view.showRecapView(v, listePrix.get(0), listePrix.get(1), listePrix.get(2), abo, date, dateFin, listeAccessoires);
        }
    }

    //Appelle les calculs des prix pour la réservation
    public void calculDesPrixReservation(String infoVeloDates, String listeChoixIdAccessoires){
        this.model.calculPrixTotalLocationReservation(infoVeloDates, listeChoixIdAccessoires);
    }

    //Appelle les méthodes qui vont crées les abonnement_utilisateur et locations dans la DB
    public void createAboAndLocation(Velo v, double prixAbo, double prixTotal, String nomAbo, LocalDate dateDebut, LocalDate dateFin, ArrayList<String> listeAccessoires){
        if(this.model.checkAboUserExists()){
            this.view.showAlert(AlertType.ERROR, "Location", "Vous avez déjà une location en cours!");
        }
        else{
            int idAbonnementUtilisateur = this.model.createAbonnement(v, nomAbo, prixAbo);
            int idLoc = this.model.createLocation(idAbonnementUtilisateur, prixTotal, dateDebut, dateFin);
            this.model.createAccessoireLocation(listeAccessoires, idLoc);
            this.model.updateVeloToIndispo(v.getIdVelo());
            this.view.showAlert(AlertType.CONFIRMATION, "Réussi!", "Votre paiement a bien été accepté. Merci pour votre confiance.");
            this.view.showMainScreen();
        }
    }

    /**
     * Appelle les méthodes qui vont crées les abonnement_utilisateur et locations dans la DB, après l'avoir choisi dans réservation
     * @param listeNomsAccessoires La liste des noms des accessoires
     * @param listeInfoEtDates 
     * @param listePrix
     * @param listeInfoVelo
     */
    public void createAboAndLocationReservation(String listeNomsAccessoires, String listeInfoEtDates, String listePrix, String listeInfoVelo){
        if(this.model.checkAboUserExists()){
            this.view.showAlert(AlertType.ERROR, "Location", "Vous avez déjà une location en cours!");
        }
        else{
            this.model.createAboLocation(listeNomsAccessoires, listeInfoEtDates, listePrix, listeInfoVelo);
            this.view.showAlert(AlertType.CONFIRMATION, "Réussi!", "Votre paiement a bien été accepté. Merci pour votre confiance.");
            this.view.showMainScreen();
        }
    }

    //Montre une alerte
    public void showAlert(AlertType alertType, String titre, String contenu){
        this.model.showAlert(alertType, titre, contenu);
    }

    //Appelle les méthodes qui vérifie si des données existent dans la DB. Si pas, les inscrit
    public void checkDataExists(){
        this.model.checkDataExistsVelo();
        this.model.checkDataExistsMerite();
        this.model.checkDataExistsAbonnement();
        this.model.checkDataExistsAccessoire();
    }




    //Setters controller
    public void setModel(IModel model){
        this.model = model;
    }

    public void setView(IView view){
        this.view = view;
    }

    public void stop(){       
        this.view.stopApp();
    }
}
