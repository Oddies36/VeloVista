package be.velovista.View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Supplier;

import be.velovista.Controller.Controller;
import be.velovista.Model.BL.Abonnement;
import be.velovista.Model.BL.Accessoire;
import be.velovista.Model.BL.Velo;
import be.velovista.Model.BL.VeloClassique;
import be.velovista.Model.BL.VeloElectrique;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
            case "resultat-choix-velo-utilisateur":
                if (evt.getNewValue() instanceof Velo)
                    this.showChoixVeloUtilisateur((Velo) evt.getNewValue());
                break;
            case "resultat-nom-liste-abonnements":
                if (evt.getNewValue().getClass().isAssignableFrom(String.class))
                    this.showListeAboDispo((String) evt.getNewValue());
                break;
            case "show-page-profil":
                if (evt.getNewValue().getClass().isAssignableFrom(ArrayList.class))
                    this.showProfilePage((ArrayList<ArrayList<String>>) evt.getNewValue());
                break;
            case "retourne-liste-merite":
                if (evt.getNewValue().getClass().isAssignableFrom(ArrayList.class))
                    this.showMeriteView((ArrayList<String>) evt.getNewValue());
                break;
            case "retour-liste-mes-reservations":
                if (evt.getNewValue() instanceof String)
                    this.showMesReservations((String) evt.getNewValue());
                break;
            case "retourne-info-accessoires-reservation":
                if (evt.getNewValue() instanceof ArrayList)
                    this.showChoixAccessoiresReservation((ArrayList<String>) evt.getNewValue());
                break;
            case "retour-recap-reservation":
                if (evt.getNewValue() instanceof ArrayList)
                    this.showRecapReservation((ArrayList<String>) evt.getNewValue());
                break;
            case "retour-page-historique":
                if (evt.getNewValue() instanceof ArrayList)
                    this.showHistorique((ArrayList<String>) evt.getNewValue());
                break;
            default:
                break;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        PrimaryView.stage = stage;
        PrimaryView.stage.setOnCloseRequest(this.control.generateCloseEvent());
        showLoginScreen();
        stage.show();
    }

    public void showLoginScreen() {
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        // vbox pour le titre et text en dessous
        VBox vboxTitre = new VBox();
        vboxTitre.setAlignment(Pos.CENTER);
        vboxTitre.setPadding(new Insets(20, 0, 0, 0));

        // vbox pour le login et mdp
        VBox vboxLoginMdp = new VBox();
        vboxLoginMdp.setAlignment(Pos.CENTER);
        vboxLoginMdp.setPadding(new Insets(150, 100, 0, 100));

        // hbox pour le text email
        HBox hboxEmailText = new HBox();

        // hbox pour le text password
        HBox hboxPasswordText = new HBox();
        hboxPasswordText.setPadding(new Insets(20, 0, 0, 0));

        // hbox pour pas de compte
        HBox hboxPasDeCompte = new HBox();
        hboxPasDeCompte.setAlignment(Pos.CENTER);
        hboxPasDeCompte.setPadding(new Insets(20, 0, 0, 0));

        // titre
        Label titre = new Label("Se connecter");
        titre.setStyle("-fx-font-size: 50;");
        // texte en dessous de titre
        Label textSousTitre = new Label("Entrez votre email et votre mot de passe");

        // email text
        Label emailText = new Label("E-mail");
        // email field
        TextField email = new TextField();
        email.setPromptText("E-mail");

        // password text & forgot password
        Label passwordText = new Label("Mot de passe");
        Hyperlink forgotPassword = new Hyperlink("Mot de passe oublié");

        Supplier<String[]> mdpOublie = () -> new String[] { "" };
        forgotPassword.setOnAction(control.generateEventHandlerAction("show-mdp-oublie", mdpOublie));

        // password field
        PasswordField password = new PasswordField();
        password.setPromptText("Mot de passe");

        // login button
        Button login = new Button("Connexion");
        setButtonStyle(login, "rond");
        login.setDefaultButton(true);

        Supplier<String[]> userLogin = () -> new String[] { email.getText(), password.getText() };
        login.setOnAction(control.generateEventHandlerAction("checkLoginCreds", userLogin));

        // pas de compte text & hyperlink
        Label pasDeCompte = new Label("Pas de compte?");
        Hyperlink creerCompte = new Hyperlink("S'enregistrer");
        creerCompte.setStyle("-fx-font-weight: bold");
        Supplier<String[]> supplier = () -> new String[] { "" };
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


    public void showMainScreen() {
        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        GridPane grid = new GridPane();

        // le logo
        Image logo = new Image(getClass().getResourceAsStream("/images/VeloVistaLogo.png"));
        ImageView viewLogo = new ImageView(logo);
        viewLogo.setFitWidth(200);
        viewLogo.setPreserveRatio(true);

        // l'image background
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/background.jpg"));
        ImageView viewBackgroundImage = new ImageView(backgroundImage);
        viewBackgroundImage.setFitHeight(450);
        GridPane.setMargin(viewBackgroundImage, new Insets(20, 20, 20, 100));
        viewBackgroundImage.setPreserveRatio(true);

        // span l'image sur 2 colonnes
        GridPane.setRowIndex(viewBackgroundImage, 1);
        GridPane.setColumnIndex(viewBackgroundImage, 1);
        GridPane.setColumnSpan(viewBackgroundImage, 2);

        // bouton pour voir les abonnements
        HBox hboxBoutonsAbo = new HBox();
        Button boutonAbo = new Button("Nos abonnements");
        GridPane.setMargin(hboxBoutonsAbo, new Insets(0, 0, 0, 20));
        setButtonStyle(boutonAbo, "Rect");

        Supplier<String[]> showListeAboSupplier = () -> new String[] { "" };
        boutonAbo.setOnAction(control.generateEventHandlerAction("show-liste-abo", showListeAboSupplier));

        // bouton pour voir les vélos
        HBox hboxBoutonVelo = new HBox();
        hboxBoutonVelo.setAlignment(Pos.CENTER_RIGHT);
        Button boutonVelo = new Button("Nos vélos");
        GridPane.setMargin(hboxBoutonVelo, new Insets(0, 20, 0, 0));
        setButtonStyle(boutonVelo, "Rect");

        // event sur bouton "nos velos"
        Supplier<String[]> boutonVeloSupplier = () -> new String[] { "" };
        boutonVelo.setOnAction(control.generateEventHandlerAction("show-velo-page", boutonVeloSupplier));

        // vbox menu et items dans le menu
        VBox vboxMenu = new VBox();
        vboxMenu.setAlignment(Pos.CENTER);
        Button boutonProfil = new Button("Profil");
        setButtonStyle(boutonProfil, "RectRond");

        Supplier<String[]> profileSupplier = () -> new String[] { "" };
        boutonProfil.setOnAction(control.generateEventHandlerAction("show-page-profil", profileSupplier));

        Button boutonHistorique = new Button("Mon historique");

        Supplier<String[]> historiqueSupplier = () -> new String[] { "" };
        boutonHistorique.setOnAction(control.generateEventHandlerAction("show-page-historique", historiqueSupplier));

        setButtonStyle(boutonHistorique, "RectRond");
        Button boutonMesReservations = new Button("Mes réservation");
        setButtonStyle(boutonMesReservations, "RectRond");

        Supplier<String[]> mesReservationsSupplier = () -> new String[] { "" };
        boutonMesReservations
                .setOnAction(control.generateEventHandlerAction("show-mes-reservations", mesReservationsSupplier));

        Button boutonCommentaires = new Button("Commentaires");
        setButtonStyle(boutonCommentaires, "RectRond");
        boutonCommentaires.setDisable(true);
        Button boutonDeconnexion = new Button("Déconnexion");
        setButtonStyle(boutonDeconnexion, "RectRond");

        Supplier<String[]> deconnexionSupplier = () -> new String[] { "" };
        boutonDeconnexion.setOnAction(control.generateEventHandlerAction("show-login-screen", deconnexionSupplier));

        FontAwesomeIconView notificationFont = new FontAwesomeIconView(FontAwesomeIcon.BELL);
        notificationFont.setFill(Color.WHITE);

        Button notificationBouton = new Button();
        setButtonStyle(notificationBouton, "retour");
        notificationBouton.setGraphic(notificationFont);
        GridPane.setMargin(notificationBouton, new Insets(0, 0, 0, 70));
        GridPane.setMargin(hboxBoutonsAbo, new Insets(0, 0, 0, 70));
        notificationBouton.setDisable(true);

        grid.add(viewLogo, 0, 0);
        grid.add(vboxMenu, 0, 1);
        grid.add(viewBackgroundImage, 1, 1);
        grid.add(hboxBoutonsAbo, 1, 2);
        grid.add(hboxBoutonVelo, 2, 2);
        grid.add(notificationBouton, 3, 0);

        vboxMenu.getChildren().addAll(boutonProfil, boutonHistorique, boutonMesReservations, boutonCommentaires,
                boutonDeconnexion);
        hboxBoutonsAbo.getChildren().addAll(boutonAbo);
        hboxBoutonVelo.getChildren().addAll(boutonVelo);
        actualParent.getChildren().addAll(grid);

        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void showHistorique(ArrayList<String> listeLocations) {
        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER);

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Button but = new Button("\u2190");
        setButtonStyle(but, "retour");
        Supplier<String[]> backSupplier = () -> new String[] {};
        but.setOnAction(control.generateEventHandlerAction("retour-main-page", backSupplier));

        Label titre = new Label("Mon historique de locations");
        titre.setStyle("-fx-font-size: 50;");

        // center
        VBox vboxListeHistorique = new VBox();
        ScrollPane scrP = new ScrollPane();
        scrP.setContent(vboxListeHistorique);
        scrP.setStyle("-fx-background: white;");

        for (String s : listeLocations) {
            HBox hboxLigneLocation = new HBox(10);

            String photo = s.split(",")[0];
            String marque = s.split(",")[1];
            String numSerie = s.split(",")[2];
            String prixTotal = s.split(",")[3];
            String dateDebut = s.split(",")[4];
            String dateFin = s.split(",")[5];

            Image img = new Image(photo, true);
            ImageView imgview = new ImageView(img);
            imgview.setFitWidth(100);
            imgview.setPreserveRatio(true);

            VBox vboxMarque = new VBox();
            Label labTitreMarque = new Label("Marque:");
            Label labMarque = new Label(marque);
            labTitreMarque.setStyle("-fx-font-weight: bold");
            vboxMarque.getChildren().addAll(labTitreMarque, labMarque);

            VBox vboxNumSerie = new VBox();
            Label labTitreNumSerie = new Label("Numéro de série:");
            Label labNumSerie = new Label(numSerie);
            labTitreNumSerie.setStyle("-fx-font-weight: bold");
            vboxNumSerie.getChildren().addAll(labTitreNumSerie, labNumSerie);

            VBox vboxPrixTotal = new VBox();
            Label labTitrePrixTotal = new Label("Prix total:");
            Label labPrixTotal = new Label(prixTotal + "€");
            labTitrePrixTotal.setStyle("-fx-font-weight: bold");
            vboxPrixTotal.getChildren().addAll(labTitrePrixTotal, labPrixTotal);

            VBox vboxDateDebut = new VBox();
            Label labTitreDateDebut = new Label("Date début:");
            Label labDateDebut = new Label(dateDebut);
            labTitreDateDebut.setStyle("-fx-font-weight: bold");
            vboxDateDebut.getChildren().addAll(labTitreDateDebut, labDateDebut);

            VBox vboxDateFin = new VBox();
            Label labTitreDateFin = new Label("Date fin:");
            Label labDateFin = new Label(dateFin);
            labTitreDateFin.setStyle("-fx-font-weight: bold");
            vboxDateFin.getChildren().addAll(labTitreDateFin, labDateFin);

            hboxLigneLocation.getChildren().addAll(imgview, vboxMarque, vboxNumSerie, vboxPrixTotal, vboxDateDebut,
                    vboxDateFin);
            vboxListeHistorique.getChildren().add(hboxLigneLocation);
        }

        hboxTitreRetour.getChildren().addAll(but, titre);
        bp.setTop(hboxTitreRetour);
        bp.setCenter(scrP);
        actualParent.getChildren().addAll(bp);
        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void showAccountCreation() {
        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        // vbox pour le titre et text en dessous
        VBox vboxTitre = new VBox();
        vboxTitre.setAlignment(Pos.CENTER);
        vboxTitre.setPadding(new Insets(20, 0, 0, 0));

        // titre
        Label titre = new Label("Créer son compte");
        titre.setStyle("-fx-font-size: 50;");

        // texte en dessous de titre
        Label textSousTitre = new Label("Créez votre compte pour pouvoir louer un vélo en toute sécurité.");

        // vbox pour le formulaire a remplir
        VBox vboxFormulaireInscription = new VBox();
        vboxFormulaireInscription.setAlignment(Pos.CENTER);
        vboxFormulaireInscription.setPadding(new Insets(80, 100, 0, 100));

        // hbox pour le label Nom
        HBox hboxLabelNom = new HBox();
        // label nom
        Label labelNom = new Label("Nom");
        TextField fieldNom = new TextField();
        fieldNom.setPromptText("Nom");
        // hbox prénom
        HBox hboxLabelPrenom = new HBox();
        // label prenom
        Label labelPrenom = new Label("Prénom");
        TextField fieldPrenom = new TextField();
        fieldPrenom.setPromptText("Prénom");
        // hbox numtel
        HBox hboxLabelNumTel = new HBox();
        // label numtel
        Label labelNumTel = new Label("Numéro de téléphone");
        TextField fieldNumTel = new TextField();
        fieldNumTel.setPromptText("Numéro de téléphone");
        // hbox pour le label Email
        HBox hboxLabelEmail = new HBox();
        // label email
        Label labelEmail = new Label("Email");
        TextField fieldEmail = new TextField();
        fieldEmail.setPromptText("Email");
        // hbox pour le label password
        HBox hboxLabelPassword = new HBox();
        // label password
        Label labelPassword = new Label("Mot de passe");
        PasswordField fieldPassword = new PasswordField();
        fieldPassword.setPromptText("Mot de passe");
        // hbox pour le label password
        HBox hboxLabelPasswordRepeat = new HBox();
        // label password
        Label labelPasswordRepeat = new Label("Confirmer le mot de passe");
        PasswordField fieldPasswordRepeat = new PasswordField();
        fieldPasswordRepeat.setPromptText("Confirmez le mot de passe");

        // bouton pour creer le compte
        Button creerCompte = new Button("Créer son compte");
        setButtonStyle(creerCompte, "rond");
        creerCompte.setDefaultButton(true);

        Supplier<String[]> creationCompteSupplier = () -> new String[] { fieldNom.getText(), fieldPrenom.getText(),
                fieldEmail.getText(), fieldNumTel.getText(), fieldPassword.getText(), fieldPasswordRepeat.getText() };
        creerCompte.setOnAction(control.generateEventHandlerAction("creation-compte-utilisateur", creationCompteSupplier));

        // hbox pour message et lien retour login
        HBox hboxRetourLogin = new HBox();
        hboxRetourLogin.setAlignment(Pos.CENTER);
        hboxRetourLogin.setPadding(new Insets(20, 0, 0, 0));
        // label message retour login
        Label dejaUnCompte = new Label("Déjà client?");
        // Lien retour login
        Hyperlink retourLogin = new Hyperlink("Se connecter");
        retourLogin.setStyle("-fx-font-weight: bold");

        Supplier<String[]> supplier = () -> new String[] { "" };
        retourLogin.setOnAction(control.generateEventHandlerAction("show-login-screen", supplier));

        vboxTitre.getChildren().addAll(titre, textSousTitre);
        vboxFormulaireInscription.getChildren().addAll(hboxLabelNom, fieldNom, hboxLabelPrenom, fieldPrenom,
                hboxLabelNumTel, fieldNumTel, hboxLabelEmail, fieldEmail, hboxLabelPassword, fieldPassword,
                hboxLabelPasswordRepeat, fieldPasswordRepeat, creerCompte, hboxRetourLogin);
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


    public void showMdpOublie() {
        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        // vbox pour le titre et text en dessous
        VBox vboxTitre = new VBox();
        vboxTitre.setAlignment(Pos.CENTER);
        vboxTitre.setPadding(new Insets(20, 0, 0, 0));

        // titre
        Label titre = new Label("Mot de passe oublié?");
        titre.setStyle("-fx-font-size: 50;");
        // texte en dessous de titre
        Label textSousTitre = new Label("Veuillez saisir votre email afin de réinitialiser votre mot de passe.");

        // vbox pour le formulaire a remplir
        VBox vboxFormulaireReset = new VBox();
        vboxFormulaireReset.setAlignment(Pos.CENTER);
        vboxFormulaireReset.setPadding(new Insets(100, 100, 0, 100));

        // hbox pour le label Email
        HBox hboxLabelEmail = new HBox();
        // label email
        Label labelEmail = new Label("Email");
        TextField fieldEmail = new TextField();
        fieldEmail.setPromptText("Email");

        // bouton pour valider l'email
        Button reinitialiserMdp = new Button("Réinitialiser");
        setButtonStyle(reinitialiserMdp, "rond");
        reinitialiserMdp.setDefaultButton(true);

        Supplier<String[]> creationCompteSupplier = () -> new String[] { fieldEmail.getText() };
        reinitialiserMdp.setOnAction(control.generateEventHandlerAction("reinitialiser-mdp", creationCompteSupplier));

        // hbox pour message et lien retour login
        HBox hboxRetourLogin = new HBox();
        hboxRetourLogin.setAlignment(Pos.CENTER);
        hboxRetourLogin.setPadding(new Insets(20, 0, 0, 0));

        // Lien retour login
        Hyperlink retourLogin = new Hyperlink("Retour à la page de connexion");
        retourLogin.setStyle("-fx-font-weight: bold");

        Supplier<String[]> supplier = () -> new String[] { "" };
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

    public void showMdpResetAvecMail(String Email) {
        // parent
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

        Supplier<String[]> supplier = () -> new String[] { Email, passwordField.getText(),
                repeatPasswordField.getText() };
        valideNouveauMdp.setOnAction(control.generateEventHandlerAction("write-new-password", supplier));

        actualParent.getChildren().addAll(vBoxPasswordFields);
        hBoxPasswordLabel.getChildren().addAll(passwordLabel);
        hBoxRepeatPasswordLabel.getChildren().addAll(repeatPasswordLabel);
        vBoxPasswordFields.getChildren().addAll(hBoxPasswordLabel, passwordField, hBoxRepeatPasswordLabel,
                repeatPasswordField, valideNouveauMdp);

        stage.setResizable(false);
        scene = new Scene(actualParent, 400, 250);
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    public void showProfilePage(ArrayList<ArrayList<String>> listeStringVeloUser) {
        ArrayList<String> listeStringVeloActuel = listeStringVeloUser.get(0);
        ArrayList<String> listeStringUser = listeStringVeloUser.get(1);

        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        Button but = new Button("\u2190");
        setButtonStyle(but, "retour");
        Supplier<String[]> backSupplier = () -> new String[] {};
        but.setOnAction(control.generateEventHandlerAction("retour-main-page", backSupplier));

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Mon profil");
        titre.setStyle("-fx-font-size: 50;");

        // center
        VBox vboxCentral = new VBox();
        Label titreVeloActuel = new Label("Location en cours: ");
        setLabelStyle(titreVeloActuel);
        VBox veloActuel = new VBox();
        if (listeStringVeloActuel.size() > 0) {
            HBox hboxVeloActBoutonRetour = new HBox(20);
            Image img = new Image(listeStringVeloActuel.get(9), true);
            ImageView imgview = new ImageView(img);
            imgview.setFitWidth(100);
            imgview.setPreserveRatio(true);
            Label modeleVelo = new Label("Modèle: " + listeStringVeloActuel.get(2));
            Label numSerieVelo = new Label("Numéro de série: " + listeStringVeloActuel.get(1));
            Label couleurVelo = new Label("Couleur: " + listeStringVeloActuel.get(5));

            Button retournerVelo = new Button("Retourner le vélo");
            HBox.setMargin(retournerVelo, new Insets(20, 0, 0, 0));

            hboxVeloActBoutonRetour.getChildren().addAll(imgview, retournerVelo);
            veloActuel.getChildren().addAll(hboxVeloActBoutonRetour, modeleVelo, numSerieVelo, couleurVelo);

            Supplier<String[]> retournerVeloSupplier = () -> new String[] { listeStringVeloActuel.get(0) };
            retournerVelo.setOnAction(control.generateEventHandlerAction("retourner-velo", retournerVeloSupplier));

        } else {
            Label pasDeVelo = new Label("Pas de location en cours");
            veloActuel.getChildren().add(pasDeVelo);
        }

        VBox vboxProfilUtilisatuer = new VBox();
        HBox hboxNom = new HBox();
        Label nom = new Label("Nom: " + listeStringUser.get(1));
        nom.setMinWidth(300);
        TextField nomField = new TextField();
        hboxNom.getChildren().addAll(nom, nomField);
        HBox.setMargin(nomField, null);

        HBox hboxPrenom = new HBox();
        Label prenom = new Label("Prénom: " + listeStringUser.get(2));
        prenom.setMinWidth(300);
        TextField prenomField = new TextField();
        hboxPrenom.getChildren().addAll(prenom, prenomField);

        HBox hboxEmail = new HBox();
        Label email = new Label("Email: " + listeStringUser.get(3));
        email.setMinWidth(300);
        TextField emailField = new TextField();
        hboxEmail.getChildren().addAll(email, emailField);

        HBox hboxNumTel = new HBox();
        Label numTel = new Label("Numéro de téléphone: " + listeStringUser.get(4));
        numTel.setMinWidth(300);
        TextField numTelField = new TextField();
        hboxNumTel.getChildren().addAll(numTel, numTelField);

        HBox hboxTotalKM = new HBox();
        Label totalKM = new Label("Total des KM parcouru: " + listeStringUser.get(5) + "KM");
        totalKM.setMinWidth(300);
        hboxTotalKM.getChildren().addAll(totalKM);

        // bottom
        HBox hboxBotButtons = new HBox(30);
        Button saveBut = new Button("Sauvegarder");
        setButtonStyle(saveBut, "rond");

        Supplier<String[]> sauvegardeProfilSupplier = () -> new String[] { nomField.getText(), prenomField.getText(),
                emailField.getText(), numTelField.getText() };
        saveBut.setOnAction(control.generateEventHandlerAction("sauvegarde-profil", sauvegardeProfilSupplier));

        Button achievementBut = new Button("Mérites");
        setButtonStyle(achievementBut, "rond");

        Supplier<String[]> meritesSupplier = () -> new String[] {};
        achievementBut.setOnAction(control.generateEventHandlerAction("merites-profil", meritesSupplier));

        hboxBotButtons.getChildren().addAll(saveBut, achievementBut);

        vboxProfilUtilisatuer.getChildren().addAll(hboxNom, hboxPrenom, hboxEmail, hboxNumTel, hboxTotalKM);
        vboxCentral.getChildren().addAll(titreVeloActuel, veloActuel, vboxProfilUtilisatuer);
        hboxTitre.getChildren().addAll(titre);
        hboxTitreRetour.getChildren().addAll(but, hboxTitre);
        BorderPane.setMargin(hboxBotButtons, new Insets(0, 0, 0, 30));
        BorderPane.setMargin(vboxCentral, new Insets(20, 0, 20, 20));
        VBox.setMargin(vboxProfilUtilisatuer, new Insets(50, 0, 0, 20));
        bp.setTop(hboxTitreRetour);
        bp.setCenter(vboxCentral);
        bp.setBottom(hboxBotButtons);
        actualParent.getChildren().addAll(bp);
        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void showMeriteView(ArrayList<String> listeMerites) {
        String listeMeritesObt = listeMerites.get(0);
        String listeMeritesNonObt = listeMerites.get(1);
        String nombreKMRestant = listeMerites.get(2);
        String pointsBonus = listeMerites.get(3);

        String[] meritesObt = listeMeritesObt.split(";");
        String[] meritesNonObt = listeMeritesNonObt.split(";");

        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");
        BorderPane bp = new BorderPane();

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        Button but = new Button("\u2190");
        setButtonStyle(but, "retour");
        Supplier<String[]> backSupplier = () -> new String[] {};
        but.setOnAction(control.generateEventHandlerAction("show-page-profil", backSupplier));

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Mes mérites");
        titre.setStyle("-fx-font-size: 50;");
        Label labSousTitre = new Label(
                "Recevez des points bonus par KM parcouru en vélo. Vos points augmenteront dépendant du plus haut mérit.");

        // center
        VBox vboxListeToutMerits = new VBox();
        vboxListeToutMerits.setPadding(new Insets(20, 0, 0, 50));
        Label labNonObt = new Label("Mérites à obtenir: ");
        setLabelStyle(labNonObt);

        VBox vboxListeNomsNonObt = new VBox();
        if (meritesNonObt.length > 0 && !meritesNonObt[0].equals("")) {
            for (String mNonObt : meritesNonObt) {
                String nomMerite = mNonObt.split(",")[1] + " : " + mNonObt.split(",")[2];
                String critereMerite = "Critère: " + mNonObt.split(",")[3] + "KM";

                Label lab = new Label(nomMerite);
                lab.setUnderline(true);
                Label labCritere = new Label(critereMerite);
                vboxListeNomsNonObt.getChildren().addAll(lab, labCritere);
                VBox.setMargin(lab, new Insets(10, 0, 0, 0));
            }
        }

        Label labObt = new Label("Mérites obtenus:");
        setLabelStyle(labObt);

        VBox vboxListeNomsObt = new VBox();
        if (meritesObt.length > 0 && !meritesObt[0].equals("")) {
            for (String mObt : meritesObt) {
                String nomMerite = mObt.split(",")[1] + " : " + mObt.split(",")[2];
                String critereMerite = "Critère: " + mObt.split(",")[3] + "KM";
                String dateObt = "Date obtenu: " + mObt.split(",")[4];

                Label lab = new Label(nomMerite);
                lab.setUnderline(true);
                Label labCritere = new Label(critereMerite);
                Label labDateObt = new Label(dateObt);
                vboxListeNomsObt.getChildren().addAll(lab, labCritere, labDateObt);
                VBox.setMargin(lab, new Insets(10, 0, 0, 0));
            }
        }

        Label kmRestant = new Label("Nombre de KM avant le prochain palier: " + nombreKMRestant + "KM");
        setLabelStyle(kmRestant);
        Label labNbPointsBonus = new Label(pointsBonus + " points");

        hboxTitre.getChildren().addAll(titre);
        hboxTitreRetour.getChildren().addAll(but, hboxTitre);
        vboxListeToutMerits.getChildren().addAll(labSousTitre, labNonObt, vboxListeNomsNonObt, labObt, vboxListeNomsObt,
                kmRestant, labNbPointsBonus);
        bp.setTop(hboxTitreRetour);
        bp.setCenter(vboxListeToutMerits);
        actualParent.getChildren().addAll(bp);
        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void showVeloCategory(ArrayList<String> listePrixTypeVelos) {
        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        Button but = new Button("\u2190");
        setButtonStyle(but, "retour");
        Supplier<String[]> butRetourSupplier = () -> new String[] { "" };
        but.setOnAction(control.generateEventHandlerAction("retour-main-page", butRetourSupplier));

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Nos vélos");
        titre.setStyle("-fx-font-size: 50;");

        // center
        Image imgVeloClassique = new Image(getClass().getResourceAsStream("/images/class.png"));
        ImageView viewImgVeloClassique = new ImageView(imgVeloClassique);
        viewImgVeloClassique.setFitHeight(150);
        viewImgVeloClassique.setFitWidth(200);
        Image imgVeloElectrique = new Image(getClass().getResourceAsStream("/images/elec.png"));
        ImageView viewImgVeloElectrique = new ImageView(imgVeloElectrique);
        viewImgVeloElectrique.setFitHeight(150);
        viewImgVeloElectrique.setFitWidth(200);
        Image imgVeloEnfant = new Image(getClass().getResourceAsStream("/images/enf.png"));
        ImageView viewImgVeloEnfant = new ImageView(imgVeloEnfant);
        viewImgVeloEnfant.setFitHeight(150);
        viewImgVeloEnfant.setPreserveRatio(true);
        viewImgVeloElectrique.setPreserveRatio(true);
        viewImgVeloClassique.setPreserveRatio(true);
        viewImgVeloEnfant.setFitWidth(200);

        GridPane.setMargin(viewImgVeloEnfant, new Insets(10, 0, 0, 0));
        GridPane.setMargin(viewImgVeloElectrique, new Insets(10, 0, 0, 0));

        VBox vboxClassique = new VBox(10);
        VBox vboxElectrique = new VBox(10);
        VBox vboxEnfant = new VBox(10);

        Label labTitreClassique = new Label("Vélos classiques");
        setLabelStyle(labTitreClassique);
        Label labTitreElectrique = new Label("Vélos Electriques");
        setLabelStyle(labTitreElectrique);
        Label labTitreEnfant = new Label("Vélos enfants");
        setLabelStyle(labTitreEnfant);

        Label prixVeloClassique = new Label("Prix / jour: " + listePrixTypeVelos.get(0) + "€");
        Label descriptionVeloClassique = new Label(
                "Vélo classique idéal pour des balades en ville ou sur des pistes cyclables, offrant confort et simplicité d'utilisation. Parfait pour les déplacements quotidiens ou les loisirs.");
        descriptionVeloClassique.setWrapText(true);
        descriptionVeloClassique.setMaxWidth(Double.MAX_VALUE);
        Label prixVeloElectrique = new Label("Prix / jour: " + listePrixTypeVelos.get(1) + "€");
        Label descriptionVeloElectrique = new Label(
                "Vélo électrique moderne conçu pour faciliter vos trajets. Offre une assistance électrique pour surmonter les côtes et parcourir de longues distances sans effort. Idéal pour une expérience de cyclisme détendue et rapide.");
        descriptionVeloElectrique.setWrapText(true);
        Label prixVeloEnfant = new Label("Prix / jour: " + listePrixTypeVelos.get(2) + "€");
        Label descriptionVeloEnfant = new Label(
                "Vélo coloré et sécurisé, spécialement conçu pour les jeunes cyclistes. Tailles et modèles adaptés pour encourager l'apprentissage et l'aventure chez les enfants. Parfait pour les premiers tours de roues.");
        descriptionVeloEnfant.setWrapText(true);

        Button choisirVeloClassique = new Button("Voir les vélos classiques");
        Button choisirVeloElectrique = new Button("Voir les vélos éléctriques");
        Button choisirVeloEnfant = new Button("Voir les vélos enfants");

        GridPane.setValignment(prixVeloClassique, VPos.TOP);
        GridPane.setValignment(prixVeloElectrique, VPos.TOP);
        GridPane.setValignment(prixVeloEnfant, VPos.TOP);

        vboxClassique.setMaxWidth(300);
        vboxElectrique.setMaxWidth(300);
        vboxEnfant.setMaxWidth(300);

        GridPane.setMargin(vboxClassique, new Insets(20, 0, 20, 10));
        GridPane.setMargin(vboxElectrique, new Insets(20, 0, 20, 10));
        GridPane.setMargin(vboxEnfant, new Insets(20, 0, 20, 10));

        vboxClassique.getChildren().addAll(labTitreClassique, prixVeloClassique, descriptionVeloClassique);
        vboxElectrique.getChildren().addAll(labTitreElectrique, prixVeloElectrique, descriptionVeloElectrique);
        vboxEnfant.getChildren().addAll(labTitreEnfant, prixVeloEnfant, descriptionVeloEnfant);

        grid.add(viewImgVeloClassique, 0, 0);
        grid.add(vboxClassique, 1, 0);
        grid.add(viewImgVeloElectrique, 0, 1);
        grid.add(vboxElectrique, 1, 1);
        grid.add(viewImgVeloEnfant, 0, 2);
        grid.add(vboxEnfant, 1, 2);
        grid.add(choisirVeloClassique, 2, 0);
        grid.add(choisirVeloElectrique, 2, 1);
        grid.add(choisirVeloEnfant, 2, 2);

        Supplier<String[]> supplierClass = () -> new String[] { "Classique" };
        choisirVeloClassique.setOnAction(control.generateEventHandlerAction("show-liste-velo-class", supplierClass));

        Supplier<String[]> supplierElec = () -> new String[] { "Electrique" };
        choisirVeloElectrique.setOnAction(control.generateEventHandlerAction("show-liste-velo-class", supplierElec));

        Supplier<String[]> supplierEnfant = () -> new String[] { "Enfant" };
        choisirVeloEnfant.setOnAction(control.generateEventHandlerAction("show-liste-velo-class", supplierEnfant));

        hboxTitre.getChildren().addAll(titre);
        hboxTitreRetour.getChildren().addAll(but, hboxTitre);
        bp.setCenter(grid);
        bp.setTop(hboxTitreRetour);
        actualParent.getChildren().addAll(bp);
        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void showListeVeloClassique(ArrayList<Velo> listeVelosClass) {
        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();

        TilePane listeVelos = new TilePane();
        listeVelos.setHgap(20);
        listeVelos.setVgap(20);
        listeVelos.setPrefColumns(4);
        listeVelos.setAlignment(Pos.CENTER);

        ScrollPane scrPane = new ScrollPane();
        scrPane.setFitToWidth(true);
        scrPane.setContent(listeVelos);

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        Button but = new Button("\u2190");
        setButtonStyle(but, "retour");
        Supplier<String[]> butRetourSupplier = () -> new String[] { "" };
        but.setOnAction(control.generateEventHandlerAction("retour-main-page", butRetourSupplier));

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Nos vélos");
        titre.setStyle("-fx-font-size: 50;");

        for (Velo velclass : listeVelosClass) {
            VBox vbox = new VBox();
            Image img = new Image(velclass.getPhoto(), true);
            ImageView imgview = new ImageView();
            imgview.setImage(img);
            imgview.setFitWidth(250);
            imgview.setPreserveRatio(true);

            Label annee = new Label("Année: " + Integer.toString(velclass.getAge()));
            Label taille = new Label("Taille: " + Integer.toString(velclass.getTaille()) + "cm");
            Label marque = new Label(velclass.getModele());
            setLabelStyle(marque);
            Label couleur = new Label("Couleur: " + velclass.getCouleur().toString());
            Label dispo = new Label(this.setDispoText(velclass.isDisponible()));
            setLabelColor(dispo);

            vbox.getChildren().addAll(imgview, marque, taille, annee, couleur);

            if (velclass instanceof VeloClassique) {
                Label vitesses = new Label("Vitesses: " + velclass.getVitessesFromVelo());
                vbox.getChildren().add(vitesses);
            } else if (velclass instanceof VeloElectrique) {
                Label autonomie = new Label("Autonomie: " + velclass.getAutonomieFromVelo() + "km");
                vbox.getChildren().add(autonomie);
            }

            vbox.getChildren().add(dispo);

            Button choixVelo = new Button("Choisir ce vélo");

            Supplier<String[]> choixVeloSupplier = () -> new String[] { Integer.toString(velclass.getIdVelo()) };
            choixVelo.setOnAction(control.generateEventHandlerAction("choix-velo-utilisateur", choixVeloSupplier));

            vbox.getChildren().addAll(choixVelo);
            listeVelos.getChildren().addAll(vbox);
        }

        Supplier<String[]> supplier = () -> new String[] {};
        but.setOnAction(control.generateEventHandlerAction("show-velo-page", supplier));

        hboxTitre.getChildren().addAll(titre);
        hboxTitreRetour.getChildren().addAll(but, hboxTitre);
        bp.setTop(hboxTitreRetour);
        bp.setCenter(scrPane);

        scrPane.setStyle("-fx-background: white; -fx-background-insets: 0; -fx-padding: 0;");
        actualParent.getChildren().addAll(bp);

        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }


    public void showListeAboDispo(String listeAbo) {
        String[] array = listeAbo.split(";");
        BorderPane bp = new BorderPane();

        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        Button but = new Button("\u2190");
        setButtonStyle(but, "retour");
        Supplier<String[]> butRetourSupplier = () -> new String[] { "" };
        but.setOnAction(control.generateEventHandlerAction("retour-main-page", butRetourSupplier));

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Nos abonnements");
        titre.setStyle("-fx-font-size: 50;");

        // Center
        VBox vboxConteneurListe = new VBox(10);

        for (String s : array) {
            String[] arrayListeAbo = s.split(",");
            VBox vboxListeAbo = new VBox();
            Label nomAbo = new Label(arrayListeAbo[0] + " : ");
            setLabelStyle(nomAbo);
            Label descriptionAbo = new Label(arrayListeAbo[1]);
            vboxListeAbo.getChildren().addAll(nomAbo, descriptionAbo);
            vboxConteneurListe.getChildren().add(vboxListeAbo);
        }

        BorderPane.setAlignment(titre, Pos.CENTER);
        BorderPane.setMargin(titre, new Insets(20, 0, 0, 0));
        BorderPane.setMargin(vboxConteneurListe, new Insets(20, 0, 0, 20));

        hboxTitre.getChildren().addAll(titre);
        hboxTitreRetour.getChildren().addAll(but, hboxTitre);
        bp.setTop(hboxTitreRetour);
        bp.setCenter(vboxConteneurListe);
        actualParent.getChildren().addAll(bp);
        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void showChoixVeloUtilisateur(Velo v) {
        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        FontAwesomeIconView backButtonIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_LEFT);
        backButtonIcon.setFill(Color.WHITE);
        Button but = new Button();
        but.setGraphic(backButtonIcon);
        setButtonStyle(but, "retour");
        Supplier<String[]> backSupplier = () -> new String[] { v.getType() };
        but.setOnAction(control.generateEventHandlerAction("show-liste-velo-class", backSupplier));

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Caractéristiques du vélo");
        titre.setStyle("-fx-font-size: 50;");

        hboxTitre.getChildren().addAll(titre);
        hboxTitreRetour.getChildren().addAll(but, hboxTitre);

        // center
        Image img = new Image(v.getPhoto(), true);
        ImageView imgview = new ImageView(img);
        imgview.setFitWidth(400);
        imgview.setPreserveRatio(true);

        VBox veloInfo = new VBox();
        Label marque = new Label("Modèle: " + v.getModele());
        setLabelStyle(marque);
        Label serial = new Label("Numéro de série: " + v.getNumeroSerie());
        setLabelStyle(serial);
        Label couleur = new Label("Couleur: " + v.getCouleur());
        setLabelStyle(couleur);
        Label taille = new Label("Taille: " + Integer.toString(v.getTaille()));
        setLabelStyle(taille);
        Label age = new Label("Année de construction: " + Integer.toString(v.getAge()));
        setLabelStyle(age);
        Label prix = new Label("Prix / jour: " + Double.toString(v.getPrix()) + "€");
        setLabelStyle(prix);

        veloInfo.getChildren().addAll(marque, serial, couleur, taille, age, prix);
        if (v instanceof VeloClassique) {
            Label vitesses = new Label("Vitesses: " + v.getVitessesFromVelo());
            setLabelStyle(vitesses);
            veloInfo.getChildren().add(vitesses);
        } else if (v instanceof VeloElectrique) {
            Label autonomie = new Label("Autonomie: " + v.getAutonomieFromVelo() + "km");
            setLabelStyle(autonomie);
            veloInfo.getChildren().add(autonomie);
        }

        GridPane.setMargin(veloInfo, new Insets(0, 0, 0, 30));

        gp.add(imgview, 0, 0);
        gp.add(veloInfo, 1, 0);

        // bottom
        HBox bottomButs = new HBox();
        bottomButs.setAlignment(Pos.CENTER);
        Button louerBut = new Button("Louer");
        if (!v.isDisponible()) {
            louerBut.setDisable(true);
        }
        setButtonStyle(louerBut, "rond");
        Button reserverBut = new Button("Reserver");
        setButtonStyle(reserverBut, "rond");
        Button avisBut = new Button("Avis Clients");
        setButtonStyle(avisBut, "rond");
        avisBut.setDisable(true);
        bottomButs.getChildren().addAll(louerBut, reserverBut, avisBut);
        HBox.setMargin(louerBut, new Insets(0, 20, 0, 0));
        HBox.setMargin(reserverBut, new Insets(0, 20, 0, 0));

        Supplier<String[]> louerSupplier = () -> new String[] { Integer.toString(v.getIdVelo()) };
        louerBut.setOnAction(control.generateEventHandlerAction("louer-velo", louerSupplier));

        Supplier<String[]> reserverSupplier = () -> new String[] { Integer.toString(v.getIdVelo()) };
        reserverBut.setOnAction(control.generateEventHandlerAction("reserver-velo", reserverSupplier));

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

    public void showChoixAccessoires(Velo v, ArrayList<Accessoire> listeAccessoires) {
        ArrayList<String> choixAccessoiresId = new ArrayList<>();

        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        Button but = new Button("\u2190");
        setButtonStyle(but, "retour");
        Supplier<String[]> backSupplier = () -> new String[] { Integer.toString(v.getIdVelo()) };
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
                if (checkBoxActif) {
                    choixAccessoiresId.add(Integer.toString(acc.getIdAccessoires()));
                } else {
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

        // Bottom
        Button butSelection = new Button("Sélectionner");
        setButtonStyle(butSelection, "rond");
        butSelection.setOnAction(
                event -> control.showChoixAbonnements(Integer.toString(v.getIdVelo()), choixAccessoiresId));

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

    public void showChoixAccessoiresReservation(ArrayList<String> infoVeloAccessoiresDates) {
        String infoAcc = infoVeloAccessoiresDates.get(0);
        String infoVeloDates = infoVeloAccessoiresDates.get(1);

        String[] accListe = infoAcc.split(";");
        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        Button but = new Button("\u2190");
        setButtonStyle(but, "retour");
        Supplier<String[]> backSupplier = () -> new String[] {};
        but.setOnAction(control.generateEventHandlerAction("show-mes-reservations", backSupplier));

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

        StringBuilder SB = new StringBuilder();
        for (String a : accListe) {
            String[] detailsListe = a.split(",");
            VBox vboxAccessoire = new VBox();
            vboxAccessoire.setAlignment(Pos.CENTER);
            CheckBox chkbox = new CheckBox(detailsListe[1]);

            chkbox.selectedProperty().addListener((obs, checkBoxInactif, checkBoxActif) -> {
                if (checkBoxActif) {
                    if (SB.length() > 0) {
                        SB.append(",");
                    }
                    SB.append(detailsListe[0]);
                } else {
                    String selection = SB.toString();
                    String remove = detailsListe[0];
                    String[] parts = selection.split(",");
                    SB.setLength(0);
                    for (String s : parts) {
                        if (!s.equals(remove)) {
                            if (SB.length() > 0) {
                                SB.append(",");
                            }
                            SB.append(s);
                        }
                    }
                }
            });

            Label labPrix = new Label("Prix: " + detailsListe[3] + "€");
            Image img = new Image(detailsListe[4], true);
            ImageView imgview = new ImageView(img);
            Label descLab = new Label(detailsListe[2]);
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

        // Bottom
        Button butSelection = new Button("Sélectionner");
        setButtonStyle(butSelection, "rond");
        butSelection.setOnAction(e -> {
            String listeAccString = SB.toString();
            Supplier<String[]> valideSupplier = () -> new String[] { infoVeloDates, listeAccString };
            control.generateEventHandlerAction("show-recap-view-reservation", valideSupplier).handle(e);
        });

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

    public void showChoixAbonnements(Velo v, ArrayList<String> listeAccessoires, ArrayList<Abonnement> listeAbo) {
        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        Button but = new Button("\u2190");
        setButtonStyle(but, "retour");
        Supplier<String[]> backSupplier = () -> new String[] { Integer.toString(v.getIdVelo()) };
        but.setOnAction(control.generateEventHandlerAction("louer-velo", backSupplier));

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Nos abonnements");
        titre.setStyle("-fx-font-size: 50;");

        // center
        ToggleGroup tog = new ToggleGroup();
        VBox vboxRadioBut = new VBox();
        for (Abonnement abo : listeAbo) {
            RadioButton radioBut = new RadioButton(abo.getNomAbonnement());
            radioBut.setToggleGroup(tog);
            vboxRadioBut.getChildren().add(radioBut);
        }

        // bottom
        HBox hboxBottom = new HBox();
        // DatePicker datepick = new DatePicker(LocalDate.now());
        Button validerBut = new Button("Louez immédiatement");
        validerBut.setOnAction(event -> {
            RadioButton choixAboUser = (RadioButton) tog.getSelectedToggle();
            if (choixAboUser != null) {
                String choixAboUserString = choixAboUser.getText();
                control.calculDesPrix(v, listeAccessoires, LocalDate.now(), choixAboUserString);
            } else {
                control.showAlert(AlertType.WARNING, "Abonnement", "Vous devez choisir un abonnement");
            }
        });

        hboxTitre.getChildren().addAll(titre);
        hboxTitreRetour.getChildren().addAll(but, hboxTitre);
        hboxBottom.getChildren().addAll(validerBut);
        bp.setCenter(vboxRadioBut);
        bp.setTop(hboxTitreRetour);
        bp.setBottom(hboxBottom);
        actualParent.getChildren().addAll(bp);
        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void showChoixAbonnementsReservation(Velo v, ArrayList<Abonnement> listeAbo) {
        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        Button but = new Button("\u2190");
        setButtonStyle(but, "retour");
        Supplier<String[]> backSupplier = () -> new String[] { Integer.toString(v.getIdVelo()) };
        but.setOnAction(control.generateEventHandlerAction("choix-velo-utilisateur", backSupplier));

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Nos abonnements");
        titre.setStyle("-fx-font-size: 50;");

        // center
        ToggleGroup tog = new ToggleGroup();
        VBox vboxRadioBut = new VBox();
        for (Abonnement abo : listeAbo) {
            RadioButton radioBut = new RadioButton(abo.getNomAbonnement());
            radioBut.setToggleGroup(tog);
            vboxRadioBut.getChildren().add(radioBut);
        }

        // bottom
        HBox hboxBottom = new HBox();
        DatePicker datepick = new DatePicker(LocalDate.now());
        Button verifDispo = new Button("Vérifiez la disponibilité");
        Button valider = new Button("Valider ma reservation");
        valider.setDisable(true);

        verifDispo.setOnAction(event -> {
            RadioButton choixAboUser = (RadioButton) tog.getSelectedToggle();
            if (choixAboUser != null) {
                String choixAboUserString = choixAboUser.getText();
                if (control.checkDisponiblites(v.getIdVelo(), datepick.getValue(), choixAboUserString)) {
                    valider.setDisable(false);

                    Supplier<String[]> validerSupplier = () -> new String[] { Integer.toString(v.getIdVelo()),
                            datepick.getValue().toString(), choixAboUserString };
                    valider.setOnAction(control.generateEventHandlerAction("valider-reservation", validerSupplier));
                } else {
                    showAlert(AlertType.ERROR, "Disponibilité",
                            "Ce vélo n'est pas disponible à cette date. Veuillez choisir une autre date.");
                }
            } else {
                control.showAlert(AlertType.WARNING, "Abonnement", "Vous devez choisir un abonnement");
            }
        });

        hboxTitre.getChildren().addAll(titre);
        hboxTitreRetour.getChildren().addAll(but, hboxTitre);
        hboxBottom.getChildren().addAll(datepick, verifDispo, valider);
        bp.setCenter(vboxRadioBut);
        bp.setTop(hboxTitreRetour);
        bp.setBottom(hboxBottom);
        actualParent.getChildren().addAll(bp);
        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void showMesReservations(String reservations) {
        String[] mesReservations = reservations.split(";");

        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER_LEFT);

        Button but = new Button("\u2190");
        setButtonStyle(but, "retour");
        Supplier<String[]> backSupplier = () -> new String[] {};
        but.setOnAction(control.generateEventHandlerAction("retour-main-page", backSupplier));

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Mes réservations");
        titre.setStyle("-fx-font-size: 50;");

        // center
        VBox vboxListeReservations = new VBox();
        if (mesReservations.length > 0 && !mesReservations[0].equals("Pas de réservations")) {
            for (String res : mesReservations) {
                String modeleVelo = res.split(",")[0];
                String serialVelo = res.split(",")[1];
                String dateDebut = res.split(",")[2];
                String dateFin = res.split(",")[3];
                String idVeloString = res.split(",")[4];
                String choixAbo = res.split(",")[5];
                Label labModeleVelo = new Label("Modèle: " + modeleVelo);
                Label labSerialVelo = new Label("Numéro de série: " + serialVelo);
                Label labChoixAbo = new Label("Abonnement: " + choixAbo);
                Label labDateDebut = new Label("Date début: " + dateDebut);
                Label labDateFin = new Label("Date fin: " + dateFin);
                Button reprendre = new Button("Prendre le vélo");
                Button annuler = new Button("Annuler la réservation");
                if (LocalDate.now().isEqual(LocalDate.parse(dateDebut))
                        || LocalDate.now().isBefore(LocalDate.parse(dateDebut))) {
                    reprendre.setDisable(false);
                } else {
                    reprendre.setDisable(false);
                }
                Supplier<String[]> repriseVeloReservationSupplier = () -> new String[] { idVeloString, dateDebut,
                        dateFin, choixAbo };
                reprendre.setOnAction(control.generateEventHandlerAction("choix-accessoires-reservation",
                        repriseVeloReservationSupplier));

                Supplier<String[]> AnnulerReservationSupplier = () -> new String[] { res.split(",")[6] };
                annuler.setOnAction(
                        control.generateEventHandlerAction("annuler-reservation", AnnulerReservationSupplier));

                vboxListeReservations.getChildren().addAll(labModeleVelo, labSerialVelo, labChoixAbo, labDateDebut,
                        labDateFin, reprendre, annuler);
            }
        } else {
            Label pasDeRes = new Label(mesReservations[0]);
            vboxListeReservations.getChildren().add(pasDeRes);
        }

        // bottom
        hboxTitre.getChildren().addAll(titre);
        hboxTitreRetour.getChildren().addAll(but, hboxTitre);

        bp.setTop(hboxTitreRetour);
        bp.setCenter(vboxListeReservations);

        actualParent.getChildren().addAll(bp);
        stage.setResizable(false);
        scene = new Scene(actualParent, 1200, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void showRecapView(Velo v, double prixAbo, double prixAcc, double prixTotal, String nomAbo,
            LocalDate dateDebut, LocalDate dateFin, ArrayList<String> listeAccessoires) {
        DecimalFormat df = new DecimalFormat("0.00");
        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER);

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Récapitulatif");
        titre.setStyle("-fx-font-size: 50;");

        // Center
        VBox vboxRecapContenu = new VBox();
        HBox hboxImgVelo = new HBox();

        Image img = new Image(v.getPhoto(), true);
        ImageView imgview = new ImageView(img);
        imgview.setFitWidth(100);
        imgview.setPreserveRatio(true);

        VBox vboxInfoVelo = new VBox();
        Label labMarque = new Label("Modèle: " + v.getModele());
        Label labType = new Label("Type: " + v.getType());
        Label labCouleur = new Label("Couleur: " + v.getCouleur());
        Label labTaille = new Label("Taille: " + Integer.toString(v.getTaille()) + "cm");
        hboxImgVelo.setAlignment(Pos.CENTER);

        VBox vboxAboInfo = new VBox();
        Label labTypeAbo = new Label(nomAbo);
        Label labDateDebut = new Label("Début de la location: " + dateDebut.toString());
        Label labDateFin = new Label("Fin de la location: " + dateFin.toString());
        vboxAboInfo.setAlignment(Pos.CENTER);

        VBox vboxPrix = new VBox();
        Label labPrixAbo = new Label("Prix de l'abonnement: " + df.format(prixAbo) + "€");
        Label labPrixAcc = new Label("Prix des accessoires: " + df.format(prixAcc) + "€");
        Label labPrixTotal = new Label("Prix total: " + df.format(prixTotal) + "€");
        vboxPrix.setAlignment(Pos.CENTER);

        // Bottom
        HBox hboxBottomButs = new HBox(20);
        hboxBottomButs.setAlignment(Pos.CENTER);
        HBox.setMargin(hboxBottomButs, new Insets(20, 0, 0, 0));
        Button validerBut = new Button("Valider");
        setButtonStyle(validerBut, "rond");
        Button recommencerBut = new Button("Recommencer");
        setButtonStyle(recommencerBut, "rond");

        validerBut.setOnAction(event -> {
            this.control.createAboAndLocation(v, prixAbo, prixTotal, nomAbo, dateDebut, dateFin, listeAccessoires);
        });
        Supplier<String[]> recommencerSupplier = () -> new String[] { "" };
        recommencerBut.setOnAction(control.generateEventHandlerAction("retour-main-page", recommencerSupplier));

        BorderPane.setMargin(hboxBottomButs, new Insets(20, 0, 0, 0));
        vboxPrix.getChildren().addAll(labPrixAbo, labPrixAcc, labPrixTotal);
        vboxAboInfo.getChildren().addAll(labTypeAbo, labDateDebut, labDateFin);
        hboxImgVelo.getChildren().addAll(imgview, vboxInfoVelo);
        vboxInfoVelo.getChildren().addAll(labMarque, labType, labCouleur, labTaille);
        vboxRecapContenu.getChildren().addAll(hboxImgVelo, vboxAboInfo, vboxPrix);
        hboxTitreRetour.getChildren().addAll(titre);
        hboxBottomButs.getChildren().addAll(validerBut, recommencerBut);
        bp.setTop(hboxTitreRetour);
        bp.setCenter(vboxRecapContenu);
        bp.setBottom(hboxBottomButs);
        actualParent.getChildren().addAll(bp);
        stage.setResizable(false);
        scene = new Scene(actualParent, 500, 400);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void showRecapReservation(ArrayList<String> listeInformations) {
        String listeNomsAccessoires = listeInformations.get(0);
        String listeInfoEtDates = listeInformations.get(1);
        String listePrix = listeInformations.get(2);
        String listeInfoVelo = listeInformations.get(3);

        String[] arrayListeInfoEtDates = listeInfoEtDates.split(",");
        String[] arrayListePrix = listePrix.split(",");
        String[] arrayListeInfoVelo = listeInfoVelo.split(",");

        // parent
        actualParent = new VBox();
        actualParent.setStyle("-fx-background-color: #ffffff");

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);

        // top
        HBox hboxTitreRetour = new HBox();
        hboxTitreRetour.setAlignment(Pos.CENTER);

        hboxTitreRetour.setPadding(new Insets(30, 50, 0, 50));

        HBox hboxTitre = new HBox();
        HBox.setHgrow(hboxTitre, Priority.ALWAYS);
        hboxTitre.setAlignment(Pos.CENTER);

        Label titre = new Label("Récapitulatif");
        titre.setStyle("-fx-font-size: 50;");

        // Center
        VBox vboxRecapContenu = new VBox();
        HBox hboxImgVelo = new HBox();

        Image img = new Image(arrayListeInfoVelo[7], true);
        ImageView imgview = new ImageView(img);
        imgview.setFitWidth(100);
        imgview.setPreserveRatio(true);

        VBox vboxInfoVelo = new VBox();
        Label labMarque = new Label(arrayListeInfoVelo[2]);
        Label labType = new Label(arrayListeInfoVelo[3]);
        Label labCouleur = new Label("Couleur: " + arrayListeInfoVelo[4]);
        Label labTaille = new Label("Taille: " + arrayListeInfoVelo[5] + "cm");
        Label labAccessoires = new Label("Accessoires: " + listeNomsAccessoires);
        hboxImgVelo.setAlignment(Pos.CENTER);

        VBox vboxAboInfo = new VBox();
        Label labTypeAbo = new Label(arrayListeInfoEtDates[3]);
        Label labDateDebut = new Label("Début de la location: " + arrayListeInfoEtDates[1]);
        Label labDateFin = new Label("Fin de la location: " + arrayListeInfoEtDates[2]);
        vboxAboInfo.setAlignment(Pos.CENTER);

        VBox vboxPrix = new VBox();
        Label labPrixAbo = new Label("Prix de l'abonnement: " + arrayListePrix[0] + "€");
        Label labPrixAcc = new Label("Prix des accessoires: " + arrayListePrix[1] + "€");
        Label labPrixTotal = new Label("Prix total: " + arrayListePrix[2] + "€");
        vboxPrix.setAlignment(Pos.CENTER);

        // Bottom
        HBox hboxBottomButs = new HBox(20);
        hboxBottomButs.setAlignment(Pos.CENTER);
        Button validerBut = new Button("Valider");
        setButtonStyle(validerBut, "rond");
        Button recommencerBut = new Button("Recommencer");
        setButtonStyle(recommencerBut, "rond");
        BorderPane.setAlignment(hboxBottomButs, Pos.CENTER);

        Supplier<String[]> validerSupplier = () -> new String[] { listeNomsAccessoires, listeInfoEtDates, listePrix,
                listeInfoVelo };
        validerBut.setOnAction(control.generateEventHandlerAction("creation-location", validerSupplier));
        Supplier<String[]> recommencerSupplier = () -> new String[] { "" };
        recommencerBut.setOnAction(control.generateEventHandlerAction("retour-main-page", recommencerSupplier));

        BorderPane.setMargin(hboxBottomButs, new Insets(20, 0, 0, 0));
        vboxPrix.getChildren().addAll(labPrixAbo, labPrixAcc, labPrixTotal);
        vboxAboInfo.getChildren().addAll(labTypeAbo, labDateDebut, labDateFin);
        hboxImgVelo.getChildren().addAll(imgview, vboxInfoVelo);
        vboxInfoVelo.getChildren().addAll(labMarque, labType, labCouleur, labTaille, labAccessoires);
        vboxRecapContenu.getChildren().addAll(hboxImgVelo, vboxAboInfo, vboxPrix);
        hboxTitreRetour.getChildren().addAll(titre);
        hboxBottomButs.getChildren().addAll(validerBut, recommencerBut);
        bp.setTop(hboxTitreRetour);
        bp.setCenter(vboxRecapContenu);
        bp.setBottom(hboxBottomButs);
        actualParent.getChildren().addAll(bp);
        stage.setResizable(false);
        scene = new Scene(actualParent, 500, 400);
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
            nomBouton.setStyle("-fx-font-size: 20px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;");
            nomBouton.setPrefWidth(60);
            VBox.setMargin(nomBouton, new Insets(30, 0, 0, 0));
      
            nomBouton.setOnMouseEntered(e -> nomBouton.setStyle("-fx-font-size: 20px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #46508f; -fx-text-fill: white;"));
            nomBouton.setOnMouseExited(e -> nomBouton.setStyle("-fx-font-size: 20px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;"));
            nomBouton.setOnMousePressed(e -> nomBouton.setStyle("-fx-font-size: 20px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #1f2a50; -fx-text-fill: white;"));
            nomBouton.setOnMouseReleased(e -> nomBouton.setStyle("-fx-font-size: 20px; -fx-background-radius: 20; -fx-padding: 10 20 10 20; -fx-background-color: #323b6f; -fx-text-fill: white;"));
        }
    }

    public void showAlert(AlertType alertType, String title, String context) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(context);
        ButtonType okBut = new ButtonType("Ok");
        alert.getButtonTypes().setAll(okBut);
        alert.showAndWait();
    }

    public void setLabelStyle(Label l) {
        l.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; ");
    }

    public String setDispoText(Boolean bool) {
        if (bool) {
            return "Disponible";
        } else {
            return "Indisponible";
        }
    }

    public void setLabelColor(Label l) {
        if (l.getText().equals("Disponible")) {
            l.setStyle("-fx-text-fill: green");
        } else {
            l.setStyle("-fx-text-fill: red");
        }
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
