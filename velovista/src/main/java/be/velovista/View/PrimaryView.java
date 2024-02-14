package be.velovista.View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Supplier;

import be.velovista.Controller.Controller;
import be.velovista.Model.BL.Accessoire;
import be.velovista.Model.BL.Velo;
import be.velovista.Model.BL.VeloClassique;
import be.velovista.Model.BL.VeloElectrique;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PrimaryView extends Application implements IView, PropertyChangeListener {
    
    private static Scene scene;
    private static Stage stage;
    private Pane actualParent; 
    private Controller control;

    public void setController(Controller control) {
        this.control = control;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "retour-liste-prix-type-velos":
                if (evt.getNewValue().getClass().isAssignableFrom(ArrayList.class))
                    this.showVeloCategory((ArrayList<String>) evt.getNewValue());
                break;
            case "retour-liste-velos-class":
                if (evt.getNewValue().getClass().isAssignableFrom(ArrayList.class))
                    this.showListeVeloClassique((ArrayList<Velo>) evt.getNewValue());
                break;
            default:
            case "resultat-choix-velo-utilisateur":
                if(evt.getNewValue() instanceof Velo)
                    this.showChoixVeloUtilisateur((Velo) evt.getNewValue());
                break;
            case "resultat-nom-liste-abonnements":
                if(evt.getNewValue().getClass().isAssignableFrom(ArrayList.class))
                    this.showListeAboDispo((ArrayList<String>) evt.getNewValue());
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        PrimaryView.stage = stage;
        // Préparation du stage pour gérer la fermeture du programme.
        PrimaryView.stage.setOnCloseRequest(this.control.generateCloseEvent());
        //Velo v = new VeloClassique(10, 1, "zebi", "Classique", true, "blanc", 54, 2022, 500.00, "https://www.statebicycle.com/cdn/shop/products/6061-eBikeCommuter-MatteBlack_1.jpg?v=1684443969");
        //ArrayList<String> test = new ArrayList<>();
        // Préparation de la première fenêtre
        showLoginScreen();
        stage.show();
    }

    public void showLoginScreen(){
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        //parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        //vbox pour le titre et text en dessous
        VBox vboxTitre = new VBox();
        vboxTitre.setAlignment(Pos.CENTER);
        vboxTitre.setPadding(new Insets(20, 0, 0, 0));
        
        //vbox pour le login et mdp
        VBox vboxLoginMdp = new VBox();
        vboxLoginMdp.setAlignment(Pos.CENTER);
        vboxLoginMdp.setPadding(new Insets(150, 100, 0, 100));

        //hbox pour le text email
        HBox hboxEmailText = new HBox();

        //hbox pour le text password
        HBox hboxPasswordText = new HBox();
        hboxPasswordText.setPadding(new Insets(20, 0, 0, 0));

        //hbox pour pas de compte
        HBox hboxPasDeCompte = new HBox();
        hboxPasDeCompte.setAlignment(Pos.CENTER);
        hboxPasDeCompte.setPadding(new Insets(20, 0, 0, 0));
        
        //titre
        Label titre = new Label("Se connecter");
        titre.setStyle("-fx-font-size: 50;");
        //texte en dessous de titre
        Label textSousTitre = new Label("Entrez votre email et votre mot de passe");

        //email text
        Label emailText = new Label("E-mail");
        //email field
        TextField email = new TextField();
        email.setPromptText("E-mail");

        //password text & forgot password
        Label passwordText = new Label("Mot de passe");
        Hyperlink forgotPassword = new Hyperlink("Mot de passe oublié");

        Supplier<String[]> mdpOublie = () -> new String[] {""};
        forgotPassword.setOnAction(control.generateEventHandlerAction("show-mdp-oublie", mdpOublie));

        //password field
        PasswordField password = new PasswordField();
        password.setPromptText("Mot de passe");

        //login button
        Button login = new Button("Connexion");
        setButtonStyle(login, "rond");
        login.setDefaultButton(true);

        Supplier<String[]> userLogin = () -> new String[] {email.getText(), password.getText()};
        login.setOnAction(control.generateEventHandlerAction("checkLoginCreds", userLogin));

        //pas de compte text & hyperlink
        Label pasDeCompte = new Label("Pas de compte?");
        Hyperlink creerCompte = new Hyperlink("S'enregistrer");
        creerCompte.setStyle("-fx-font-weight: bold");
        Supplier<String[]> supplier = () -> new String[] {""};
        creerCompte.setOnAction(control.generateEventHandlerAction("showAccountCreation", supplier));

        vboxTitre.getChildren().addAll(titre, textSousTitre);
        hboxEmailText.getChildren().addAll(emailText);
        hboxPasswordText.getChildren().addAll(passwordText, spacer, forgotPassword);
        hboxPasDeCompte.getChildren().addAll(pasDeCompte, creerCompte);
        vboxLoginMdp.getChildren().addAll(hboxEmailText, email, hboxPasswordText, password, login, hboxPasDeCompte);
        actualParent.getChildren().addAll(vboxTitre, vboxLoginMdp);

        stage.setResizable(false);
        scene = new Scene(actualParent, 500, 600);
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    public void showMainScreen(){

        //parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        GridPane grid = new GridPane();

        //le logo
        Image logo = new Image(getClass().getResourceAsStream("/images/VeloVistaLogo.png"));
        ImageView viewLogo = new ImageView(logo);
        viewLogo.setFitWidth(200);
        viewLogo.setPreserveRatio(true);

        //l'image background
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/newerFaded.png"));
        ImageView viewBackgroundImage = new ImageView(backgroundImage);
        viewBackgroundImage.setFitWidth(800);
        GridPane.setMargin(viewBackgroundImage, new Insets(20, 20, 20, 20));
        viewBackgroundImage.setPreserveRatio(true);

        //span l'image sur 2 colonnes
        GridPane.setRowIndex(viewBackgroundImage, 1);
        GridPane.setColumnIndex(viewBackgroundImage, 1);
        GridPane.setColumnSpan(viewBackgroundImage, 2);

        //bouton pour voir les abonnements
        HBox hboxBoutonsAbo = new HBox();
        Button boutonAbo = new Button("Nos abonnements");
        GridPane.setMargin(hboxBoutonsAbo, new Insets(0, 0, 0, 20));
        setButtonStyle(boutonAbo, "Rect");

        Supplier<String[]> showListeAboSupplier = () -> new String[] {""};
        boutonAbo.setOnAction(control.generateEventHandlerAction("show-liste-abo", showListeAboSupplier));

        //bouton pour voir les vélos
        HBox hboxBoutonVelo = new HBox();
        hboxBoutonVelo.setAlignment(Pos.CENTER_RIGHT);
        Button boutonVelo = new Button("Nos vélos");
        GridPane.setMargin(hboxBoutonVelo, new Insets(0, 20, 0, 0));
        setButtonStyle(boutonVelo, "Rect");

        //event sur bouton "nos velos"
        Supplier<String[]> boutonVeloSupplier = () -> new String[] {""};
        boutonVelo.setOnAction(control.generateEventHandlerAction("show-velo-page", boutonVeloSupplier));

        //vbox menu et items dans le menu
        VBox vboxMenu = new VBox();
        vboxMenu.setAlignment(Pos.CENTER);
        Button boutonProfil = new Button("Profil");
        setButtonStyle(boutonProfil, "RectRond");

        Supplier<String[]> profileSupplier = () -> new String[] {""};
        boutonProfil.setOnAction(control.generateEventHandlerAction("show-page-profil", profileSupplier));

        Button boutonHistorique = new Button("Mon historique");
        setButtonStyle(boutonHistorique, "RectRond");
        Button boutonCommentaires = new Button("Commentaires");
        setButtonStyle(boutonCommentaires, "RectRond");
        Button boutonDeconnexion = new Button("Déconnexion");
        setButtonStyle(boutonDeconnexion, "RectRond");

        Button notificationBouton = new Button("Notif");
        notificationBouton.setStyle("-fx-background-color: red;");
        GridPane.setMargin(notificationBouton, new Insets(0, 0, 0, 0));

        grid.add(viewLogo, 0, 0);
        grid.add(vboxMenu, 0, 1);
        grid.add(viewBackgroundImage, 1, 1);
        grid.add(hboxBoutonsAbo, 1, 2);
        grid.add(hboxBoutonVelo, 2, 2);
        grid.add(notificationBouton, 3, 0);

        grid.setGridLinesVisible(true);



        
        vboxMenu.getChildren().addAll(boutonProfil, boutonHistorique, boutonCommentaires, boutonDeconnexion);
        hboxBoutonsAbo.getChildren().addAll(boutonAbo);
        hboxBoutonVelo.getChildren().addAll(boutonVelo);
        actualParent.getChildren().addAll(grid);

        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    public void showAccountCreation(){

        //parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");
        

        //vbox pour le titre et text en dessous
        VBox vboxTitre = new VBox();
        vboxTitre.setAlignment(Pos.CENTER);
        vboxTitre.setPadding(new Insets(20, 0, 0, 0));

        //titre
        Label titre = new Label("Créer son compte");
        titre.setStyle("-fx-font-size: 50;");
        //texte en dessous de titre
        Label textSousTitre = new Label("Créez votre compte pour pouvoir louer un vélo en toute sécurité.");



        //vbox pour le formulaire a remplir
        VBox vboxFormulaireInscription = new VBox();
        vboxFormulaireInscription.setAlignment(Pos.CENTER);
        vboxFormulaireInscription.setPadding(new Insets(80, 100, 0, 100));

        //hbox pour le label Nom
        HBox hboxLabelNom = new HBox();
        //label nom
        Label labelNom = new Label("Nom");
        TextField fieldNom = new TextField();
        fieldNom.setPromptText("Nom");
        //hbox prénom
        HBox hboxLabelPrenom = new HBox();
        //label prenom
        Label labelPrenom = new Label("Prénom");
        TextField fieldPrenom = new TextField();
        fieldPrenom.setPromptText("Prénom");
        //hbox numtel
        HBox hboxLabelNumTel = new HBox();
        //label numtel
        Label labelNumTel = new Label("Numéro de téléphone");
        TextField fieldNumTel = new TextField();
        fieldNumTel.setPromptText("Numéro de téléphone");
        //hbox pour le label Email
        HBox hboxLabelEmail = new HBox();
        //label email
        Label labelEmail = new Label("Email");
        TextField fieldEmail = new TextField();
        fieldEmail.setPromptText("Email");
        //hbox pour le label password
        HBox hboxLabelPassword = new HBox();
        //label password
        Label labelPassword = new Label("Mot de passe");
        PasswordField fieldPassword = new PasswordField();
        fieldPassword.setPromptText("Mot de passe");
        //hbox pour le label password
        HBox hboxLabelPasswordRepeat = new HBox();
        //label password
        Label labelPasswordRepeat = new Label("Confirmer le mot de passe");
        PasswordField fieldPasswordRepeat = new PasswordField();
        fieldPasswordRepeat.setPromptText("Confirmez le mot de passe");

        //bouton pour creer le compte
        Button creerCompte = new Button("Créer son compte");
        setButtonStyle(creerCompte, "rond");
        creerCompte.setDefaultButton(true);

        Supplier<String[]> creationCompteSupplier = () -> new String[] {fieldNom.getText(), fieldPrenom.getText(), fieldEmail.getText(), fieldNumTel.getText(), fieldPassword.getText(), fieldPasswordRepeat.getText()};
        creerCompte.setOnAction(control.generateEventHandlerAction("creation-compte-utilisateur", creationCompteSupplier));

        //hbox pour message et lien retour login
        HBox hboxRetourLogin = new HBox();
        hboxRetourLogin.setAlignment(Pos.CENTER);
        hboxRetourLogin.setPadding(new Insets(20, 0, 0, 0));
        //label message retour login
        Label dejaUnCompte = new Label("Déjà client?");
        //Lien retour login
        Hyperlink retourLogin = new Hyperlink("Se connecter");
        retourLogin.setStyle("-fx-font-weight: bold");

        Supplier<String[]> supplier = () -> new String[] {""};
        retourLogin.setOnAction(control.generateEventHandlerAction("show-login-screen", supplier));

        vboxTitre.getChildren().addAll(titre, textSousTitre);
        vboxFormulaireInscription.getChildren().addAll(hboxLabelNom, fieldNom, hboxLabelPrenom, fieldPrenom, hboxLabelNumTel, fieldNumTel, hboxLabelEmail, fieldEmail, hboxLabelPassword, fieldPassword, hboxLabelPasswordRepeat, fieldPasswordRepeat, creerCompte, hboxRetourLogin);
        hboxLabelNom.getChildren().addAll(labelNom);
        hboxLabelPrenom.getChildren().addAll(labelPrenom);
        hboxLabelNumTel.getChildren().addAll(labelNumTel);
        hboxLabelPassword.getChildren().addAll(labelPassword);
        hboxLabelEmail.getChildren().addAll(labelEmail);
        hboxLabelPasswordRepeat.getChildren().addAll(labelPasswordRepeat);
        hboxRetourLogin.getChildren().addAll(dejaUnCompte, retourLogin);
        actualParent.getChildren().addAll(vboxTitre, vboxFormulaireInscription);
        
        stage.setResizable(false);
        scene = new Scene(actualParent, 500, 600);
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    public void showMdpOublie(){

        //parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");
        

        //vbox pour le titre et text en dessous
        VBox vboxTitre = new VBox();
        vboxTitre.setAlignment(Pos.CENTER);
        vboxTitre.setPadding(new Insets(20, 0, 0, 0));

        //titre
        Label titre = new Label("Mot de passe oublié?");
        titre.setStyle("-fx-font-size: 50;");
        //texte en dessous de titre
        Label textSousTitre = new Label("Veuillez saisir votre email afin de réinitialiser votre mot de passe.");



        //vbox pour le formulaire a remplir
        VBox vboxFormulaireReset = new VBox();
        vboxFormulaireReset.setAlignment(Pos.CENTER);
        vboxFormulaireReset.setPadding(new Insets(100, 100, 0, 100));

        //hbox pour le label Email
        HBox hboxLabelEmail = new HBox();
        //label email
        Label labelEmail = new Label("Email");
        TextField fieldEmail = new TextField();
        fieldEmail.setPromptText("Email");

        //bouton pour valider l'email
        Button reinitialiserMdp = new Button("Réinitialiser");
        setButtonStyle(reinitialiserMdp, "rond");
        reinitialiserMdp.setDefaultButton(true);

        /////////////////////////////////////
        Supplier<String[]> creationCompteSupplier = () -> new String[] {fieldEmail.getText()};
        reinitialiserMdp.setOnAction(control.generateEventHandlerAction("reinitialiser-mdp", creationCompteSupplier));
        /////////////////////////////////////

        //hbox pour message et lien retour login
        HBox hboxRetourLogin = new HBox();
        hboxRetourLogin.setAlignment(Pos.CENTER);
        hboxRetourLogin.setPadding(new Insets(20, 0, 0, 0));

        //Lien retour login
        Hyperlink retourLogin = new Hyperlink("Retour à la page de connexion");
        retourLogin.setStyle("-fx-font-weight: bold");

        Supplier<String[]> supplier = () -> new String[] {""};
        retourLogin.setOnAction(control.generateEventHandlerAction("show-login-screen", supplier));

        vboxTitre.getChildren().addAll(titre, textSousTitre);
        vboxFormulaireReset.getChildren().addAll(hboxLabelEmail, fieldEmail, reinitialiserMdp, hboxRetourLogin);

        hboxLabelEmail.getChildren().addAll(labelEmail);

        hboxRetourLogin.getChildren().addAll(retourLogin);
        actualParent.getChildren().addAll(vboxTitre, vboxFormulaireReset);
        
        stage.setResizable(false);
        scene = new Scene(actualParent, 500, 600);
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    public void showMdpResetAvecMail(String Email){

        //parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        VBox vBoxPasswordFields = new VBox();
        vBoxPasswordFields.setAlignment(Pos.CENTER);
        vBoxPasswordFields.setPadding(new Insets(30, 50, 0, 50));

        HBox hBoxPasswordLabel = new HBox();
        Label passwordLabel = new Label("Introduisez votre nouveau mot de passe:");
        PasswordField passwordField = new PasswordField();

        HBox hBoxRepeatPasswordLabel = new HBox();
        Label repeatPasswordLabel = new Label("Répétez votre nouveau mot de passe:");
        PasswordField repeatPasswordField = new PasswordField();

        VBox.setMargin(hBoxRepeatPasswordLabel, new Insets(10, 0, 0, 0));

        Button valideNouveauMdp = new Button("Valider");
        setButtonStyle(valideNouveauMdp, "rond");
        valideNouveauMdp.setDefaultButton(true);

        Supplier<String[]> supplier = () -> new String[] {Email, passwordField.getText(), repeatPasswordField.getText()};
        valideNouveauMdp.setOnAction(control.generateEventHandlerAction("write-new-password", supplier));

        actualParent.getChildren().addAll(vBoxPasswordFields);
        hBoxPasswordLabel.getChildren().addAll(passwordLabel);
        hBoxRepeatPasswordLabel.getChildren().addAll(repeatPasswordLabel);
        vBoxPasswordFields.getChildren().addAll(hBoxPasswordLabel, passwordField, hBoxRepeatPasswordLabel, repeatPasswordField, valideNouveauMdp);

        stage.setResizable(false);
        scene = new Scene(actualParent, 400, 250);
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    public void showProfilePage(){

        //parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        //vbox pour le titre et text en dessous
        VBox vboxTitre = new VBox();
        vboxTitre.setAlignment(Pos.CENTER);
        vboxTitre.setPadding(new Insets(20, 0, 0, 0));

        //titre profil
        Label titreProfil = new Label("Mon profil");
        titreProfil.setStyle("-fx-font-size: 50;");

        //vbox pour velo actuel
        VBox veloActuel = new VBox();
        //Image img = new Image(null);

        vboxTitre.getChildren().addAll(titreProfil);
        actualParent.getChildren().addAll(vboxTitre);

        stage.setResizable(false);
        scene = new Scene(actualParent, 800, 700);
        stage.setScene(scene);
        stage.centerOnScreen();

    }


    public void showVeloCategory(ArrayList<String> listePrixTypeVelos){

        //parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);

        Button butRetour = new Button("retour");
        setButtonStyle(butRetour, "rond");

        Supplier<String[]> butRetourSupplier = () -> new String[] {""};
        butRetour.setOnAction(control.generateEventHandlerAction("retour-main-page", butRetourSupplier));

        Label titre = new Label("Nos vélos");
        titre.setStyle("-fx-font-size: 50;");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        Image imgVeloClassique = new Image(getClass().getResourceAsStream("/images/veloClassique.png"));
        ImageView viewImgVeloClassique = new ImageView(imgVeloClassique);
        viewImgVeloClassique.setFitHeight(200);
        viewImgVeloClassique.setFitWidth(200);
        Image imgVeloElectrique = new Image(getClass().getResourceAsStream("/images/veloElectrique.png"));
        ImageView viewImgVeloElectrique = new ImageView(imgVeloElectrique);
        viewImgVeloElectrique.setFitHeight(200);
        viewImgVeloElectrique.setFitWidth(200);
        Image imgVeloEnfant = new Image(getClass().getResourceAsStream("/images/veloEnfant.png"));
        ImageView viewImgVeloEnfant = new ImageView(imgVeloEnfant);
        viewImgVeloEnfant.setFitHeight(200);
        viewImgVeloEnfant.setFitWidth(200);

        GridPane.setMargin(viewImgVeloEnfant, new Insets(10, 0, 0, 0));
        GridPane.setMargin(viewImgVeloElectrique, new Insets(10, 0, 0, 0));
        

        Label descriptionVeloClassique = new Label("Vélo classique. Prix: " + listePrixTypeVelos.get(0));
        Label descriptionVeloElectrique = new Label("Vélo éléctrique. Prix: " + listePrixTypeVelos.get(1));
        Label descriptionVeloEnfant = new Label("Vélo enfant. Prix: " + listePrixTypeVelos.get(2));

        Button choisirVeloClassique = new Button("Voir les vélos classiques");
        Button choisirVeloElectrique = new Button("Voir les vélos éléctriques");
        Button choisirVeloEnfant = new Button("Voir les vélos enfants");

        GridPane.setValignment(descriptionVeloClassique, VPos.TOP);

        grid.setGridLinesVisible(true);
        grid.add(viewImgVeloClassique, 0, 0);
        grid.add(descriptionVeloClassique, 1, 0);
        grid.add(viewImgVeloElectrique, 0, 1);
        grid.add(descriptionVeloElectrique, 1, 1);
        grid.add(viewImgVeloEnfant, 0, 2);
        grid.add(descriptionVeloEnfant, 1, 2);
        grid.add(choisirVeloClassique, 2, 0);
        grid.add(choisirVeloElectrique, 2, 1);
        grid.add(choisirVeloEnfant, 2, 2);

        Supplier<String[]> supplierClass = () -> new String[] {"Classique"};
        choisirVeloClassique.setOnAction(control.generateEventHandlerAction("show-liste-velo-class", supplierClass));

        Supplier<String[]> supplierElec = () -> new String[] {"Electrique"};
        choisirVeloElectrique.setOnAction(control.generateEventHandlerAction("show-liste-velo-class", supplierElec));

        Supplier<String[]> supplierEnfant = () -> new String[] {"Enfant"};
        choisirVeloEnfant.setOnAction(control.generateEventHandlerAction("show-liste-velo-class", supplierEnfant));


        content.getChildren().addAll(butRetour, titre, grid);
        actualParent.getChildren().addAll(content);

        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    public void showListeVeloClassique(ArrayList<Velo> listeVelosClass){

        Button back = new Button("Retour");

        //parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        VBox scrollContainer = new VBox();
        scrollContainer.setAlignment(Pos.CENTER);


        TilePane listeVelos = new TilePane();
        listeVelos.setHgap(20);
        listeVelos.setVgap(20);
        listeVelos.setPrefColumns(4);
        listeVelos.setAlignment(Pos.CENTER);
        

        for(Velo velclass : listeVelosClass){
            VBox vbox = new VBox();
            Image img = new Image(velclass.getPhoto(), true);
            ImageView imgview = new ImageView();
            imgview.setImage(img);
            imgview.setFitWidth(250);
            imgview.setPreserveRatio(true);

            Label couleur = new Label("Couleur: " + velclass.getCouleur().toString());
            Label prix = new Label("Prix: " + String.valueOf(velclass.getPrix()) + "€");

            vbox.getChildren().addAll(imgview, couleur, prix);

            if(velclass instanceof VeloClassique){
                Label vitesses = new Label("Vitesses: " + velclass.getVitessesFromVelo());
                vbox.getChildren().add(vitesses);
            }
            else if(velclass instanceof VeloElectrique){
                Label vitesses = new Label("Autonomie: " + velclass.getAutonomieFromVelo() + "km");
                vbox.getChildren().add(vitesses);
            }

            Button choixVelo = new Button("Choisir ce vélo");

            Supplier<String[]> choixVeloSupplier = () -> new String[] {Integer.toString(velclass.getIdVelo())};
            choixVelo.setOnAction(control.generateEventHandlerAction("choix-velo-utilisateur", choixVeloSupplier));

            vbox.getChildren().addAll(choixVelo);
            listeVelos.getChildren().addAll(vbox);
        }

        //ObservableList<VeloClassique> obslist = FXCollections.observableArrayList(listeVelosClass);
        //ListView<VeloClassique> listeVelosClassique = new ListView<>(obslist);

        Supplier<String[]> supplier = () -> new String[] {};
        back.setOnAction(control.generateEventHandlerAction("show-velo-page", supplier));

        ScrollPane scrPane = new ScrollPane(listeVelos);
        scrPane.setStyle("-fx-background: white;");
        scrollContainer.getChildren().addAll(scrPane);
        actualParent.getChildren().addAll(back, scrollContainer);

        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    public void showListeAboDispo(ArrayList<String> listeAbo){

        Button back = new Button("Retour");
        BorderPane bp = new BorderPane();


        //parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        

        //top
        Label titre = new Label("Nos abonnements");
        titre.setStyle("-fx-font-size: 50;");

        //Center
        HBox hboxListeAbo = new HBox();
        for(String nomAbo : listeAbo){
            Label l = new Label(nomAbo);
            hboxListeAbo.getChildren().add(l);
        }
        // VBox vboxJournalier = new VBox();
        // VBox vboxHebdo = new VBox();
        // VBox vboxMensu = new VBox();
        // VBox vboxSemest = new VBox();
        // VBox vboxTrimest = new VBox();
        // VBox vboxAnnu = new VBox();



        bp.setTop(titre);
        bp.setCenter(hboxListeAbo);
        //hboxListeAbo.getChildren().addAll(vboxJournalier, vboxHebdo, vboxMensu, vboxSemest, vboxTrimest, vboxAnnu);
        BorderPane.setAlignment(titre, Pos.CENTER);
        BorderPane.setMargin(titre, new Insets(20, 0, 0, 0));

        actualParent.getChildren().add(bp);
        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();

    }


    public void showChoixVeloUtilisateur(Velo v){
        //parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");
        
        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);

        //top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        Button but =  new Button("<-");
        setButtonStyle(but, "retour");
        Supplier<String[]> backSupplier = () -> new String[] {v.getType()};
        but.setOnAction(control.generateEventHandlerAction("show-liste-velo-class", backSupplier));

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Caractéristiques du vélo");
        //titre.setAlignment(Pos.CENTER);
        titre.setStyle("-fx-font-size: 50;");
        
        hboxTitre.getChildren().addAll(titre);
        hboxTitreRetour.getChildren().addAll(but, hboxTitre);


        //center
        Image img = new Image(v.getPhoto(), true);
        ImageView imgview = new ImageView(img);
        imgview.setFitWidth(400);
        imgview.setPreserveRatio(true);

        VBox veloInfo = new VBox();
        Label marque = new Label("Modèle: " + v.getModele());
        setLabelStyle(marque);
        Label couleur = new Label("Couleur: " + v.getCouleur());
        setLabelStyle(couleur);
        Label taille = new Label("Taille: " + Integer.toString(v.getTaille()));
        setLabelStyle(taille);
        Label age = new Label("Année de construction: " + Integer.toString(v.getAge()));
        setLabelStyle(age);
        Label prix = new Label("Prix: " + Double.toString(v.getPrix()));
        setLabelStyle(prix);

        GridPane.setMargin(veloInfo, new Insets(0, 0, 0, 30));

        veloInfo.getChildren().addAll(marque, couleur, taille, age, prix);
        gp.add(imgview, 0, 0);
        gp.add(veloInfo, 1, 0);

        //bottom
        HBox bottomButs = new HBox();
        bottomButs.setAlignment(Pos.CENTER);
        Button louerBut = new Button("Louer");
        setButtonStyle(louerBut, "rond");
        Button reserverBut = new Button("Reserver");
        setButtonStyle(reserverBut, "rond");
        Button avisBut = new Button("Avis Clients");
        setButtonStyle(avisBut, "rond");
        bottomButs.getChildren().addAll(louerBut, reserverBut, avisBut);
        HBox.setMargin(louerBut, new Insets(0, 20, 0, 0));
        HBox.setMargin(reserverBut, new Insets(0, 20, 0, 0));

        Supplier<String[]> louerSupplier = () -> new String[] {Integer.toString(v.getIdVelo())};
        louerBut.setOnAction(control.generateEventHandlerAction("louer-velo", louerSupplier));

        bp.setTop(hboxTitreRetour);
        bp.setCenter(gp);
        bp.setBottom(bottomButs);

        BorderPane.setAlignment(titre, Pos.CENTER);
        BorderPane.setAlignment(gp, Pos.CENTER);
        BorderPane.setMargin(gp, new Insets(100, 50, 50, 0));
        BorderPane.setMargin(titre, new Insets(30, 0, 0, 0));

        actualParent.getChildren().addAll(bp);
        
        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void showChoixAccessoires(Velo v, ArrayList<Accessoire> listeAccessoires){
        ArrayList<String> choixAccessoiresId = new ArrayList<>();

        //parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);

        //top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        Button but =  new Button("<-");
        setButtonStyle(but, "retour");
        Supplier<String[]> backSupplier = () -> new String[] {Integer.toString(v.getIdVelo())};
        but.setOnAction(control.generateEventHandlerAction("choix-velo-utilisateur", backSupplier));

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Nos accessoires");
        titre.setStyle("-fx-font-size: 50;");
        
        hboxTitre.getChildren().addAll(titre);
        hboxTitreRetour.getChildren().addAll(but, hboxTitre);

        int columnIndex = 0;
        int rowIndex = 0;

        for (Accessoire acc : listeAccessoires) {
            VBox vboxAccessoire = new VBox();
            vboxAccessoire.setAlignment(Pos.CENTER);
            CheckBox chkbox = new CheckBox(acc.getNomAccessoire());

            chkbox.selectedProperty().addListener((obs, checkBoxInactif, checkBoxActif) -> {
                if(checkBoxActif){
                    choixAccessoiresId.add(Integer.toString(acc.getIdAccessoires()));
                }
                else{
                    choixAccessoiresId.remove(Integer.toString(acc.getIdAccessoires()));
                }
            });

            Label labPrix = new Label(String.valueOf("Prix: " + acc.getPrixAccessoire()) + "€");
            Image img = new Image(acc.getPhotoAccessoire(), true);
            ImageView imgview = new ImageView(img);
            Label descLab = new Label(acc.getDescriptionAccessoire());
            descLab.setAlignment(Pos.CENTER);
            descLab.setMaxWidth(200);
            descLab.setWrapText(true);
            imgview.setFitWidth(100);
            imgview.setPreserveRatio(true);

            VBox.setMargin(chkbox, new Insets(0, 60, 0, 0));
            vboxAccessoire.getChildren().addAll(imgview, descLab, labPrix, chkbox);
    
            gp.add(vboxAccessoire, columnIndex, rowIndex);
            columnIndex++;
            if (columnIndex > 1) {
                columnIndex = 0;
                rowIndex++;
            }
        }

        //Bottom
        Button butSelection = new Button("Sélectionner");
        setButtonStyle(butSelection, "rond");
        butSelection.setOnAction(event -> control.showChoixAbonnements(Integer.toString(v.getIdVelo()), choixAccessoiresId));
        
        BorderPane.setAlignment(butSelection, Pos.CENTER);
        bp.setBottom(butSelection);
        bp.setTop(hboxTitreRetour);
        bp.setCenter(gp);
        actualParent.getChildren().addAll(bp);
        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void showChoixAbonnements(Velo v, ArrayList<String> listeAccessoires){
        //parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);

        //top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        Button but =  new Button("<-");
        setButtonStyle(but, "retour");
        Supplier<String[]> backSupplier = () -> new String[] {Integer.toString(v.getIdVelo())};
        but.setOnAction(control.generateEventHandlerAction("louer-velo", backSupplier));

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Nos abonnements");
        titre.setStyle("-fx-font-size: 50;");
        
        hboxTitre.getChildren().addAll(titre);
        hboxTitreRetour.getChildren().addAll(but, hboxTitre);

        bp.setTop(hboxTitreRetour);
        actualParent.getChildren().addAll(bp);
        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    public void setButtonStyle(Button nomBouton, String type){
        switch(type){
            case "rond":
            nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;");
            nomBouton.setPrefWidth(200);
            VBox.setMargin(nomBouton, new Insets(30, 0, 0, 0));
      
            nomBouton.setOnMouseEntered(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #46508f; -fx-text-fill: white;"));
            nomBouton.setOnMouseExited(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;"));
            nomBouton.setOnMousePressed(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #1f2a50; -fx-text-fill: white;"));
            nomBouton.setOnMouseReleased(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;"));
            break;
            case "RectRond":
            nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 0 20 20 0; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;");
            nomBouton.setPrefWidth(200);
            VBox.setMargin(nomBouton, new Insets(30, 0, 0, 0));
      
            nomBouton.setOnMouseEntered(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 0 20 20 0; -fx-padding: 10 20 10 20; -fx-background-color: #46508f; -fx-text-fill: white;"));
            nomBouton.setOnMouseExited(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 0 20 20 0; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;"));
            nomBouton.setOnMousePressed(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 0 20 20 0; -fx-padding: 10 20 10 20; -fx-background-color: #1f2a50; -fx-text-fill: white;"));
            nomBouton.setOnMouseReleased(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 0 20 20 0; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;"));
            break;
            case "Rect":
            nomBouton.setStyle("-fx-font-size: 15px; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;");
            nomBouton.setPrefWidth(200);
            VBox.setMargin(nomBouton, new Insets(30, 0, 0, 0));
      
            nomBouton.setOnMouseEntered(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-padding: 10 20 10 20; -fx-background-color: #46508f; -fx-text-fill: white;"));
            nomBouton.setOnMouseExited(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;"));
            nomBouton.setOnMousePressed(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-padding: 10 20 10 20; -fx-background-color: #1f2a50; -fx-text-fill: white;"));
            nomBouton.setOnMouseReleased(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;"));
            break;
            case "retour":
            nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;");
            nomBouton.setPrefWidth(60);
            VBox.setMargin(nomBouton, new Insets(30, 0, 0, 0));
      
            nomBouton.setOnMouseEntered(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #46508f; -fx-text-fill: white;"));
            nomBouton.setOnMouseExited(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;"));
            nomBouton.setOnMousePressed(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #1f2a50; -fx-text-fill: white;"));
            nomBouton.setOnMouseReleased(e -> nomBouton.setStyle("-fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;"));
        }
    }


    public void setLabelStyle(Label l){
        l.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; ");
    }

    @Override
    public void launchApp() {
        Platform.startup(() -> {
            Stage stage = new Stage();
            try {
                this.start(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void stopApp() {        
        Platform.exit();
    }
}
