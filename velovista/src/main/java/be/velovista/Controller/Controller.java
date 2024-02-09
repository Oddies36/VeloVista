package be.velovista.Controller;

import java.beans.PropertyChangeListener;
import java.security.InvalidParameterException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import be.velovista.Model.IModel;
import be.velovista.Model.PrimaryModel;
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
            case "show-page-profil":
                t = (x) -> this.getInfoProfilePage();
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

    public void getInfoProfilePage(){
        //this.model.getInfoProfilePage();
    }

    public void louerVelo(String id){
        this.view.showChoixAccessoires(Integer.parseInt(id), this.model.getAccessoires());
    }

    public void getVeloChoixUtilisateur(String idVelo){
        this.model.getVeloChoixUtilisateur(Integer.parseInt(idVelo));
    }

    public void showListeAbo(){
        this.model.getListeAbo();
    }

    public void writePassword(String Email, String mdp, String repeatMdp){
        if(this.model.writeNewPassword(Email, mdp, repeatMdp)){
            this.view.showLoginScreen();
        }
    }

    public void showMdpResetAvecMail(String Email){
        if(this.model.checkEmailExiste(Email) == 2){
            this.view.showMdpResetAvecMail(Email);
        }
        else{
            this.model.showAlert(AlertType.ERROR, "Email", "Mauvaise adresse mail");
        }
    }

    public void showMdpOublie(){
        this.view.showMdpOublie();
    }

    public void CreationCompteUtilisateur(String nom, String prenom, String Email, String numTel, String mdpClaire, String mdpClaireRepeat){
        if(this.model.CreationCompteUtilisateur(nom, prenom, Email, numTel, mdpClaire, mdpClaireRepeat)){
            this.view.showLoginScreen();
        }
    }

    public void getListeVeloClassique(String typeVelo){
        this.model.getListeVeloClassique(typeVelo);
    }

    public void getPrixVelos(){
        this.model.getPrixVelos();
    }

    public void checkLoginCreds(String email, String enteredPassword){
        if(this.model.passwordMatch(email, enteredPassword)){
            this.view.showMainScreen();
        }
    }

    public void showAccountCreation(){
        this.view.showAccountCreation();
    }
    public void showLoginScreen(){
        this.view.showLoginScreen();
    }

    
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
