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
import be.velovista.Model.BL.Merites;
import be.velovista.Model.BL.Reservation;
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
import be.velovista.Model.DAL.DAO.Merite_utilisateur.IMerite_utilisateurDAO;
import be.velovista.Model.DAL.DAO.Merite_utilisateur.Merite_utilisateurDAO;
import be.velovista.Model.DAL.DAO.Merites.IMeritesDAO;
import be.velovista.Model.DAL.DAO.Merites.MeritesDAO;
import be.velovista.Model.DAL.DAO.Reservation.IReservationDAO;
import be.velovista.Model.DAL.DAO.Reservation.ReservationDAO;
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
    private IMerite_utilisateurDAO imeriteuserdao = new Merite_utilisateurDAO();
    private IReservationDAO iresdao = new ReservationDAO();
    private IMeritesDAO imeritedao = new MeritesDAO();
    Alert alert;

    public PrimaryModel() {
        this.support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    ///////////////
    //Utilitaires//
    ///////////////

    /**
     * Méthode qui montre une alerte
     * @param alerType Le type d'alerte
     * @param title Le titre de l'alerte
     * @param context Le contenu de l'alerte
     */
    public void showAlert(AlertType alertType, String title, String context) {
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(context);
        alert.showAndWait();
    }

    /**
     * Méthode qui hash un mot de passe
     * @param clearPassword Le mot de passe en claire
     * @return Retourne la chaine avec le mot de passe hashé
     */
    public String hashPassword(String clearPassword) {
        int hash = 7;
        for (int i = 0; i < clearPassword.length(); i++) {
            hash = hash * 31 + clearPassword.charAt(i);
        }
        return Integer.toHexString(hash);
    }

    /**
     * Méthode qui récupère le mot de passe hashé de la base de données. Utilisé pour vérification de connexion
     * @param email L'adresse mail de l'utilisateur
     * @return Retourne la le mot de passe hashé vennant de la DB
     */
    public String getHashedPassword(String email) {
        return iuserdao.getUserPassword(email);
    }

    ////////////////////////////
    //Gestion des utilisateurs//
    ////////////////////////////

    /**
     * Méthode qui vérifie si les 2 mot de passes hashé sont égaux.
     * Si c'est le cas, inscrit l'utilisateur dans userConnected.
     * @param email L'adresse mail de l'utilisateur
     * @param password Le mot de passe hashé que l'utilisateur a entré
     * @return Retourne true si les mot de passes sont égaux
     */
    public boolean passwordMatch(String email, String password) {
        String hashedPassword = hashPassword(password);
        String dbPassword = getHashedPassword(email);

        if (hashedPassword.equals(dbPassword)) {
            this.userConnected.setUser(iuserdao.getUser(email));

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Connexion");
            alert.setHeaderText("Connexion réussie!");
            alert.setContentText("Connexion réussie. Bienvenu " + userConnected.getUser().getNom() + " "
                    + userConnected.getUser().getPrenom());
            alert.showAndWait();
            return true;
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Connexion");
            alert.setHeaderText("Connexion échouée");
            alert.setContentText("Email ou mot de passe incorrect. Veuillez réessayer.");
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Méthode qui crée le compte de l'utilisateur.
     * Appelle chaque méthode de vérification. S'ils passent tous, le compte est crée dans la DB
     * @param nom Le nom de l'utilisateur
     * @param prenom Le prénom de l'utilisateur
     * @param Email L'adresse mail de l'utilisateur
     * @param numTel Le numéro de téléphone de l'utilisateur
     * @param mdpClaire Le mot de passe en claire que l'utilisateur a entré
     * @param mdpClaireRepeat Le même mot de passe en claire que l'utilisateur a entré une 2ème fois
     * @return Si tout est ok, retourne true qui part dans le controller. Le controller affiche alors le main screen
     */
    public boolean CreationCompteUtilisateur(String nom, String prenom, String Email, String numTel, String mdpClaire,
            String mdpClaireRepeat) {
        int mailExiste = this.checkEmailExiste(Email);
        if (checkNom(nom)) {
            if (checkEmail(Email)) {
                if (checkPuissanceMdp(mdpClaire)) {
                    if (checkPasswordMatchCreationCompte(mdpClaire, mdpClaireRepeat)) {
                        if (mailExiste == 0) {
                            this.iuserdao.createUserAccount(nom, prenom, Email, numTel, this.hashPassword(mdpClaire));
                            alert = new Alert(AlertType.CONFIRMATION);
                            alert.setTitle("Création réussie!");
                            alert.setContentText(
                                    "Votre compte a été créé. Vous pouvez maintenant utiliser votre compte pour vous connecter.");
                            alert.showAndWait();
                            return true;
                        } else if (mailExiste == -1) {
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Erreur");
                            alert.setContentText("Problème de vérification Email.");
                            alert.showAndWait();
                            return false;
                        } else {
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

    /**
     * Méthode qui vérifie si le nom n'est pas vide. Utilisé dans la création du compte
     * @param nom Le nom de l'utilisateur
     * @return Retourne true si les conditions sont remplies
     */
    public boolean checkNom(String nom) {
        if (nom.isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Création de compte");
            alert.setContentText("Un ou plusieurs champs sont vides!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Vérifie le nom entré par l'utilisateur pour la modification des informations
     * Règles: Le nom ne peut être null ou vide, la première lettre est une majuscule, le nom ne peut que contenir de A à Z
     * et les 2 caractères ' et -
     * @param nom Le nom entré par l'utilisateur
     * @return Retourne true si les conditions sont remplies et false si elles ne le sont pas
     */
    public boolean checkNomUpdate(String nom) {
        if (nom != null && !nom.isEmpty() && Character.isUpperCase(nom.charAt(0)) && nom.matches("[A-Za-z'-]+")) {
            return true;
        }
        return false;
    }

    /**
     * Vérifie le numéro de téléphone entré par l'utilisateur
     * Règles: Ne peut pas être null ou vide
     * Regex: +? La chaine peut commencer par un + et est optionnel
     * d+ represente une suite de chiffres
     * @param numTel Le numéro de téléphone
     * @return Retourne true si les conditions sont remplies et false si elles ne le sont pas
     */
    public boolean checkNumTel(String numTel) {
        if (numTel != null && !numTel.isEmpty() && numTel.matches("^\\+?\\d+$")) {
            return true;
        }
        return false;
    }

    /**
     * Méthode qui vérifie l'adresse mail. Utilisé dans la création du compte et la mise à jour des données
     * Regex: [^@]+ Signifie que la chaine ne peut pas commencer par un @
     * Le @ Signifie qu'un @ doit être présent dans la chaine
     * [^@\\.]+ Après le @, on peut pas directement avoir un @ ni un .
     * (\\.[^@\\.]+)+ l'ensemble des expression doit apparaitre au moins une fois
     * @param Email L'adresse mail que l'utilisateur a entré
     * @return Retourne vrai si les conditions sont remplies
     */
    public boolean checkEmail(String Email) {
        String regexEmail = "^[^@]+@[^@\\.]+(\\.[^@\\.]+)+$";
        Pattern pat = Pattern.compile(regexEmail);
        Matcher match = pat.matcher(Email);

        if (match.matches()) {
            return true;
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Email");
            alert.setContentText("Le format de l'adresse mail n'est pas valide");
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Méthode qui vérifie que l'adresse mail existe dans la DB. Utilisé pour la création du compte et reset du mdp
     * @param Email L'adresse mail de l'utilisateur
     * @return Retourne 0 si aucun mail a été trouvé, -1 s'il y a une erreur, 2 si une ou plusieurs lignes ont été trouvés
     */
    public int checkEmailExiste(String Email) {
        int existingAccounts = this.iuserdao.getEmailCount(Email);
        if (existingAccounts == 0) {
            return 0;
        } else if (existingAccounts == -1) {
            return -1;
        } else {
            return 2;
        }
    }

    /**
     * Méthode qui vérifie si les 2 mot de passes entrés par l'utilisateur en claire sont égaux
     * @param mdpClaire Le mot de passe en claire
     * @param mdpClaireRepeat Le mot de passe en claire entré une 2ème fois lors de la création du compte
     * @return Retourne true si les mots de passe sont égaux
     */
    public boolean checkPasswordMatchCreationCompte(String mdpClaire, String mdpClaireRepeat) {
        if (mdpClaire.equals(mdpClaireRepeat)) {
            return true;
        } else {
            showAlert(AlertType.ERROR, "Mot de passe", "Les mots de passe ne sont pas identiques!");
            return false;
        }
    }

    /**
     * Méthode qui vérifie la puissance du mot de passe. Est utilisé pour la création du compte et le reset du mdp
     * Le mot de passe doit contenir au moins 6 caractères
     * @param mdpClaire Le mot de passe entré par l'utilisateur
     * @return Retourne true si les conditions sont remplies
     */
    public boolean checkPuissanceMdp(String mdpClaire) {
        if (mdpClaire.length() >= 6) {
            return true;
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Mot de passe");
            alert.setContentText("Le mot de passe doit contenir au moins 6 caractères.");
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Méthode qui appelle les méthodes de vérification de mot de passe. Si tout passe, le mot de passe est mis à jour dans la DB
     * @param Email L'adresse mail de l'utilisateur
     * @param mdp Le mot de passe entré par l'utilisateur dans le changement du mot de passe
     * @param repeatMdp Le mot de passe entré une 2ème fois
     * @return Retourne vrai si le mot de passe à bien été modifié
     */
    public boolean writeNewPassword(String Email, String mdp, String repeatMdp) {
        if (this.checkPasswordMatchCreationCompte(mdp, repeatMdp)) {
            if (this.checkPuissanceMdp(mdp)) {
                this.iuserdao.changeUserPassword(this.hashPassword(repeatMdp), Email);
                this.showAlert(AlertType.CONFIRMATION, "Mot de passe", "Votre mot de passe a été réinitialisé");
                return true;
            }
        }
        return false;
    }

    ///////////////////////////
    //Profil de l'utilisateur//
    ///////////////////////////

    /**
     * Combine 2 ArrayList dans une autre ArrayList
     * Utilisé dans la page profil. Affiche la page via firepropertychange
     */
    public void getInfoProfilePage() {
        ArrayList<ArrayList<String>> comboList = new ArrayList<>();
        ArrayList<String> listeInfoVeloActuel = this.listeStringVeloActuel();
        ArrayList<String> listeInfoUserActuel = this.listeStringUser();

        comboList.add(listeInfoVeloActuel);
        comboList.add(listeInfoUserActuel);

        support.firePropertyChange("show-page-profil", "", comboList);
    }

    /**
     * Ajoute chaque field du Users dans une ArrayList de String
     * Utilisé pour la page profil
     * @return Retourne la liste des informations dans une arraylist dans cet ordre: id, nom, prenom, email, numTel, totalKM
     */
    public ArrayList<String> listeStringUser() {
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

    /**
     * Ajoute chaque field du vélo en location actuel de l'utilisateur connecté dans une ArrayList de String
     * Utilisé pour la page profil
     * @return Retourne une arrayList de String avec les infos du vélo actuellement en location
     */
    public ArrayList<String> listeStringVeloActuel() {
        ArrayList<String> listeInfoVeloActuel = new ArrayList<>();
        int idVelo = this.iabouser.getIdVeloFromIdUser(this.userConnected.getUser().getIdUser());
        Velo v = this.ivelodao.getVelo(idVelo);
        if (v != null) {
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

    /**
     * Méthode qui vérifie si les chaines en paramètres sont vides ou non.
     * Si pas, met à jour les informations de l'utilisateur actuellement connecté avec les nouvelles informations
     * Met à jour userConnected et également le user dans la base de données
     * @param nouveauNomUser Le nouveau nom de l'utilisateur
     * @param nouveauPrenomUser Le nouveau prénom de l'utilisateur
     * @param nouveauEmailUser La nouvelle adresse mail de l'utilisateur
     * @param nouveauNumTelUser Le nouveau numéro de téléphone de l'utilisateur
     */
    public void sauvegardeProfil(String nouveauNomUser, String nouveauPrenomUser, String nouveauEmailUser,
            String nouveauNumTelUser) {
        if (!nouveauNomUser.isEmpty()) {
            if (checkNomUpdate(nouveauNomUser)) {
                this.userConnected.getUser().setNom(nouveauNomUser);
                this.iuserdao.updateNomUser(this.userConnected.getUser().getIdUser(), nouveauNomUser);
                this.getInfoProfilePage();
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Nom invalide");
                alert.setContentText("Nom invalide");
                alert.showAndWait();
            }
        }

        if (!nouveauPrenomUser.isEmpty()) {
            if (checkNomUpdate(nouveauPrenomUser)) {
                this.userConnected.getUser().setPrenom(nouveauPrenomUser);
                this.iuserdao.updatePrenomUser(this.userConnected.getUser().getIdUser(), nouveauPrenomUser);
                this.getInfoProfilePage();
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Prénom invalide");
                alert.setContentText("Prénom invalide");
                alert.showAndWait();
            }
        }

        if (!nouveauEmailUser.isEmpty()) {
            if (checkEmail(nouveauEmailUser)) {
                this.userConnected.getUser().seteMail(nouveauEmailUser);
                ;
                this.iuserdao.updateEmailUser(this.userConnected.getUser().getIdUser(), nouveauEmailUser);
                this.getInfoProfilePage();
            }
        }

        if (!nouveauNumTelUser.isEmpty()) {
            if (checkNumTel(nouveauNumTelUser)) {
                this.userConnected.getUser().setNumTelephone(nouveauNumTelUser);
                this.iuserdao.updateNumTelUser(this.userConnected.getUser().getIdUser(), nouveauNumTelUser);
                this.getInfoProfilePage();
            }
        }
    }

    /**
     * Méthode qui s'active quand on rend le vélo
     * On met a jour le total des KM, on met à jour le vélo en statut disponible, on met l'abonnement actif en inactif
     * @return retourne vrai dans tout les cas
     */
    public boolean updateVeloActuelStatus(int intIdVelo) {
        this.updateTotalKMUser();
        this.updateVeloToDispo(intIdVelo);
        this.updateAbonnementUserToInactif();

        return true;
    }

    //Ajoute les KM au CurrentUser et met a jour la DB avec le total des KM parcouru. Appelle ensuite la partie check de Mérites
    public void updateTotalKMUser() {
        int randomKM = this.generateRandomKM();
        int currentKMUser = this.userConnected.getUser().getTotalKM();
        int newKMUser = currentKMUser + randomKM;
        this.userConnected.getUser().setTotalKM(newKMUser);

        this.iuserdao.updateTotalKMUser(newKMUser, this.userConnected.getUser().getIdUser());
        updateMerites(this.meritesObtenus());
        updatePointsBonus(calculPointsBonus(randomKM));
    }

    /**
     * Génère un nombre de KM random pour chaque jour que le vélo a été loué. Par jour, de 0 a 30km est généré
     * @return Retourne le nombre de KM généré
     */
    public int generateRandomKM() {
        Random random = new Random();
        int nbJours = this.calculNbJoursLocation();
        int kmTotalGenere = random.nextInt(21);
        for (int i = 0; i < nbJours; i++) {
            int randKM = random.nextInt(31);
            kmTotalGenere += randKM;
        }
        return kmTotalGenere;
    }

    /////////////////////
    //Gestion des vélos//
    /////////////////////

    /**
     * Méthode qui cherche les prix de chaque type de vélo et les ajoute dans une ArrayList<String>
     * Utilise le firePropertyChange pour envoyer ces informations a la view
     */
    public void getPrixVelos() {
        ArrayList<String> listePrixTypeVelos = new ArrayList<>();

        listePrixTypeVelos.add(Integer.toString(this.ivelodao.getPrixClassique()));
        listePrixTypeVelos.add(Integer.toString(this.ivelodao.getPrixElectrique()));
        listePrixTypeVelos.add(Integer.toString(this.ivelodao.getPrixEnfant()));

        this.support.firePropertyChange("retour-liste-prix-type-velos", "", listePrixTypeVelos);
    }

    /**
     * Méthode qui utilise le firePropertyChange pour envoyer la bonne liste de vélos vers la view qui montre les vélos
     * La liste des vélos sont: Classique, Electrique, Enfant
     * @param typeVelo Le type de vélo que l'utilisateur a choisi de voir
     */
    public void getListeVeloClassique(String typeVelo) {
        ArrayList<Velo> listeVeloClassique = new ArrayList<>();

        switch (typeVelo) {
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

    /**
     * Méthode qui utilise le firePropertyChange pour envoyer le vélo choisi par l'utilisateur vers une view
     * @param id ID du vélo choisi
     * @return Retourne un objet avec le vélo choisi
     */
    public Velo getVeloChoixUtilisateur(int id) {
        Velo v = ivelodao.getVelo(id);
        this.support.firePropertyChange("resultat-choix-velo-utilisateur", "", v);
        return v;
    }

    /**
     * Méthode qui met à jour le vélo dans DB à disponible
     * @param idVelo L'ID du vélo
     */
    public void updateVeloToDispo(int idVelo) {
        this.ivelodao.updateVeloToDispo(idVelo);
    }

    /**
     * Méthode qui met à jour le vélo dans DB à indisponible
     * @param idVelo L'ID du vélo
     */
    public void updateVeloToIndispo(int idVelo) {
        this.ivelodao.updateVeloToIndispo(idVelo);
    }

    ///////////////////////////
    //Gestion des abonnements//
    ///////////////////////////

    /**
     * Prend la liste des abonnements (nom, description) dans une ArrayList<Abonnement> et le met dans une String
     * Envoi le résultat dans un firePropertyChange qui affiche la liste des abonnements
     */
    public void getListeAbo() {
        ArrayList<Abonnement> listeAbo = new ArrayList<>();
        String listeNomAbo = "";

        listeAbo = this.iabonnementdao.getListeAbonnements();

        for (Abonnement a : listeAbo) {
            listeNomAbo += a.getNomAbonnement() + "," +
                    a.getDescriptionAbonnement() + ";";
        }
        listeNomAbo = listeNomAbo.substring(0, listeNomAbo.length() - 1);
        support.firePropertyChange("resultat-nom-liste-abonnements", "", listeNomAbo);
    }

    /**
     * Méthode qui crée un abonnement dans la table abonnement_utilisateur
     * @param v Le vélo choisi
     * @param nomAbo Le nom de l'abonnement
     * @param prixAbo Le prix de l'abonnement
     * @return Retourne l'ID de l'abonnement_utilisateur qui vient d'être crée
     */
    public int createAbonnement(Velo v, String nomAbo, double prixAbo) {
        int idAbo = this.iabonnementdao.getAbonnementId(nomAbo);
        int idAboUser = this.iabouser.insertAbonnementUtilisateur(idAbo, v.getIdVelo(),
                this.userConnected.getUser().getIdUser(), prixAbo);

        return idAboUser;
    }

    /**
     * Méthode qui crée l'abonnementLocation. Va d'abbord convertir tout les informations en ce qu'il faut
     * @param listeNomsAccessoires La liste des noms des accessoires choisis
     * @param listeInfoEtDates La liste des dates et abonnements
     * @param listePrix La liste des prix
     * @param listeInfoVelo La liste des informations du vélo
     */
    public void createAboLocation(String listeNomsAccessoires, String listeInfoEtDates, String listePrix,
            String listeInfoVelo) {
        String[] arrayListeInfoVelo = listeInfoVelo.split(",");
        String[] arrayListeInfoEtDates = listeInfoEtDates.split(",");
        String[] arrayListePrix = listePrix.split(",");
        String[] arrayListeAccessoires = listeNomsAccessoires.split(",");
        ArrayList<Accessoire> listeAccessoires = new ArrayList<>();
        ArrayList<String> idAccessoires = new ArrayList<>();

        for (String s : arrayListeAccessoires) {
            Accessoire a = this.iaccessoiredao.getAccessoiresIdFromName(s);
            listeAccessoires.add(a);
        }
        if (listeAccessoires.size() > 0 && listeAccessoires.get(0) != null) {
            for (Accessoire a : listeAccessoires) {
                idAccessoires.add(Integer.toString(a.getIdAccessoires()));
            }
        }

        Double prixAbo = Double.parseDouble(arrayListePrix[0]);
        Double prixTotal = Double.parseDouble(arrayListePrix[2]);

        LocalDate dateDebut = LocalDate.parse(arrayListeInfoEtDates[1]);
        LocalDate dateFin = LocalDate.parse(arrayListeInfoEtDates[2]);

        int idVelo = Integer.parseInt(arrayListeInfoVelo[0]);

        Velo v = ivelodao.getVelo(idVelo);

        int idAbonnementUtilisateur = this.createAbonnement(v, arrayListeInfoEtDates[3], prixAbo);
        int idLoc = this.createLocation(idAbonnementUtilisateur, prixTotal, dateDebut, dateFin);

        this.createAccessoireLocation(idAccessoires, idLoc);
        this.updateVeloToIndispo(v.getIdVelo());
    }

    /**
     * Récupère la liste des abonnements
     * @return Retourne une ArrayList<Abonnement>
     */
    public ArrayList<Abonnement> getListeChoixAbo() {
        return this.iabonnementdao.getListeAbonnements();
    }

    /**
     * Check si une location est déjà active pour la dernière étape de la réservation
     * @return Retourne true si une location existe déjà
     */
    public boolean checkAboUserExists() {
        int exists = iabouser.checkCurrentAboUser(this.userConnected.getUser().getIdUser());

        if (exists > 0) {
            return true;
        } else {
            return false;
        }
    }

    //Met l'abonnementUser en statut Inactif quand le vélo a été rendu
    public void updateAbonnementUserToInactif() {
        this.iabouser.updateAbonnementUtilisateurInactif(this.userConnected.getUser().getIdUser());
    }

    ///////////////////////////
    //Gestion des accessoires//
    ///////////////////////////

    /**
     * Méthode qui prend la liste des accessoires de la DB
     * @return Retourne une ArrayList<Accessoire>
     */
    public ArrayList<Accessoire> getAccessoires() {
        return iaccessoiredao.getAccessoires();
    }

    /**
     * Méthode qui prend la liste des id accessoires en String et prend la liste des noms avec les ID
     * @param choixIdAccessoires Liste des ID accessoires en String
     * @return Retourne la liste des String de noms d'accessoires
     */
    public String getNomsAccessoires(String choixIdAccessoires) {
        String[] arrayListeAccessoires = choixIdAccessoires.split(",");
        String listeNomsAccessoiresString = "";

        if (arrayListeAccessoires.length > 0 && !arrayListeAccessoires[0].equals("")) {
            for (String s : arrayListeAccessoires) {
                Accessoire a = this.iaccessoiredao.getAccessoireFromId(s);
                listeNomsAccessoiresString += a.getNomAccessoire() + ",";
            }
            listeNomsAccessoiresString = listeNomsAccessoiresString.substring(0,
                    listeNomsAccessoiresString.length() - 1);
        }
        return listeNomsAccessoiresString;
    }

    /**
     * Méthode qui prend la liste des accessoires, la liste des informations précédentes et les fusionne ensemble
     * Utilise le firePropertyChange pour envoyer cette liste dans une view
     * @param idVelo Id du vélo choisi
     * @param dateDebut La date de début de la location
     * @param dateFin La date de fin de la location
     * @param choixAbonnement Le choix de l'abonnement
     */
    public void getAccessoiresString(String idVelo, String dateDebut, String dateFin, String choixAbonnement) {
        ArrayList<Accessoire> listeAccessoires = this.iaccessoiredao.getAccessoires();
        ArrayList<String> comboListeAccInfoPrecedent = new ArrayList<>();
        String listeAccessoiresString = "";
        String infoPrecedent = idVelo + "," + dateDebut + "," + dateFin + "," + choixAbonnement;

        for (Accessoire a : listeAccessoires) {
            listeAccessoiresString += Integer.toString(a.getIdAccessoires()) + "," +
                    a.getNomAccessoire() + "," +
                    a.getDescriptionAccessoire() + "," +
                    Double.toString(a.getPrixAccessoire()) + "," +
                    a.getPhotoAccessoire() + "," +
                    idVelo + "," +
                    dateDebut + "," +
                    dateFin + ";";
        }
        if (listeAccessoires.size() > 0) {
            listeAccessoiresString = listeAccessoiresString.substring(0, listeAccessoiresString.length() - 1);
        }

        comboListeAccInfoPrecedent.add(listeAccessoiresString);
        comboListeAccInfoPrecedent.add(infoPrecedent);

        support.firePropertyChange("retourne-info-accessoires-reservation", "", comboListeAccInfoPrecedent);
    }

    //////////////////////////
    //Gestion de réservation//
    //////////////////////////

    /**
     * Calcul les prix après la réservation pour afficher le récap après réservation
     * @param infoVeloDates Les infos dans une string dans cet ordre: idvelo, datedebut, datefin, choixabonnement
     * @param listeChoixIdAccessoires La liste des accessoires
     */
    public void calculPrixTotalLocationReservation(String infoVeloDates, String listeChoixIdAccessoires) {
        String[] listeInfoVeloDates = infoVeloDates.split(",");
        String nomAbo = listeInfoVeloDates[3];
        String[] arrayListeAccessoires = listeChoixIdAccessoires.split(",");
        ArrayList<String> listeAccessoires = new ArrayList<>();
        String listePrixString = "";
        String infoVeloString = "";
        double prixAccessoires = 0.00;

        // Info final a donner dans supplier
        ArrayList<String> listePrixEtInfoPrecedent = new ArrayList<>();

        // A mettre dans arraylist
        String listeNomsAccessoires = this.getNomsAccessoires(listeChoixIdAccessoires);

        for (String s : arrayListeAccessoires) {
            listeAccessoires.add(s);
        }
        Velo v = this.ivelodao.getVelo(Integer.parseInt(listeInfoVeloDates[0]));
        double prixAbonnement = this.calculPrixAbonnementParType(v, nomAbo);
        if (listeAccessoires.size() > 0 && !listeAccessoires.get(0).equals("")) {
            prixAccessoires = this.calculPrixTotalAccessoires(listeAccessoires);
        }

        double prixTotal = prixAbonnement + prixAccessoires;

        listePrixString += Double.toString(prixAbonnement) + "," +
                Double.toString(prixAccessoires) + "," +
                Double.toString(prixTotal);

        infoVeloString += Integer.toString(v.getIdVelo()) + "," +
                v.getNumeroSerie() + "," +
                v.getModele() + "," +
                v.getType() + "," +
                v.getCouleur() + "," +
                Integer.toString(v.getTaille()) + "," +
                Integer.toString(v.getAge()) + "," +
                v.getPhoto();

        listePrixEtInfoPrecedent.add(listeNomsAccessoires);
        listePrixEtInfoPrecedent.add(infoVeloDates);
        listePrixEtInfoPrecedent.add(listePrixString);
        listePrixEtInfoPrecedent.add(infoVeloString);

        support.firePropertyChange("retour-recap-reservation", "", listePrixEtInfoPrecedent);
    }

    /**
     * Méthode qui insère une ligne réservation dans la DB
     * @param idVelo L'ID du vélo choisi
     * @param dateDebut La date de début
     * @param dateFin La date de fin
     * @param choixAboUserString Le choix de l'abonnement choisi
     */
    public void insertReservation(String idVelo, String dateDebut, String dateFin, String choixAboUserString) {
        this.iresdao.insertReservation(Integer.parseInt(idVelo), this.userConnected.getUser().getIdUser(), dateDebut,
                dateFin, choixAboUserString);
    }

    /**
     * Méthode qui change le statut d'une réservation en inactif
     * @param idReservation L'ID de la réservation
     */
    public void annulerReservation(String idReservation) {
        this.iresdao.annulerReservation(Integer.parseInt(idReservation));
    }

    /**
     * Récupère la liste des réservations d'un utilisateur, les converti en String et les envoi dans un firePropertyChange
     */
    public void getReservations() {
        ArrayList<Reservation> listeReservations = new ArrayList<>();
        listeReservations = this.iresdao.getListeReservations(this.userConnected.getUser().getIdUser());
        String listeReservationsString = "";
        if (listeReservations.size() > 0) {
            for (Reservation r : listeReservations) {
                Velo v = this.ivelodao.getVelo(r.getIdVelo());
                listeReservationsString += v.getModele() + "," +
                        v.getNumeroSerie() + "," +
                        r.getDateDebut().toString() + "," +
                        r.getDateFin().toString() + "," +
                        Integer.toString(v.getIdVelo()) + "," +
                        r.getChoixAbonnement() + "," +
                        r.getIdReservation() + ";";
            }
            listeReservationsString = listeReservationsString.substring(0, listeReservationsString.length() - 1);
            support.firePropertyChange("retour-liste-mes-reservations", "", listeReservationsString);
        } else {
            support.firePropertyChange("retour-liste-mes-reservations", "", "Pas de réservations");
        }
    }

    /**
     * Méthode qui vérifie la disponibilités d'un vélo sur les réservations
     * @param idVelo L'ID du vélo
     * @param dateDebut La date de début
     * @param dateFin La date de fin
     * @return Retourne true si le résultat de la requête est 0 (vélo disponible)
     */
    public boolean checkDisponibilitesReservation(int idVelo, String dateDebut, String dateFin) {
        int dispo = this.iresdao.checkDispo(idVelo, dateDebut, dateFin);

        if (dispo == 1) {
            return false;
        } else {
            return true;
        }
    }

    ///////////////////////
    //Gestion de location//
    ///////////////////////

    //*****Méthode à tester*****
    /** Méthode qui calcule la date de fin avec la date de début et le nom de l'abonnement
     * @param dateDebut La date de début
     * @param nomAbonnement Le nom de l'abonnement
     * @return Retourne la date de fin
     */
    public LocalDate calculDateFin(LocalDate dateDebut, String nomAbonnement) {
        LocalDate dateFin = null;
        switch (nomAbonnement) {
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

    //*****Méthode à tester*****
    /**
     * Méthode qui calcule le prix de l'abonnement
     * @param v Le vélo choisi
     * @param nomAbonnement Le nom de l'abonnement
     * @return Retourne le prix calculé avec le prix du vélo et le nom de l'abonnement
     */
    public double calculPrixAbonnement(Velo v, String nomAbonnement) {
        double prixAbonnement = 0.00;

        switch (nomAbonnement) {
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

    //*****Méthode à tester*****
    /**
     * Méthode qui calcule le prix de l'abonnement par type de vélo
     * @param v Le vélo choisi
     * @param nomAbonnement Le nom de l'abonnement
     * @return Retourne le prix de l'abonnement calculé avec le type de vélo
     */
    public double calculPrixAbonnementParType(Velo v, String nomAbonnement) {
        String typeVelo = v.getType();
        double prixAbonnementBase = this.calculPrixAbonnement(v, nomAbonnement);
        double prixAbonnementApresCalcul = prixAbonnementBase;

        switch (typeVelo) {
            case "Classique":
                if (v.getAge() < 2020) {
                    prixAbonnementApresCalcul = prixAbonnementBase - (prixAbonnementBase * 0.30);
                }
                break;
            case "Electrique":
                if (v.getAutonomieFromVelo() >= 100) {
                    prixAbonnementApresCalcul = prixAbonnementBase + (prixAbonnementBase * 0.25);
                } else if (v.getAutonomieFromVelo() >= 50 && v.getAutonomieFromVelo() < 100) {
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

    //*****Méthode à tester*****
    /**
     * Méthode qui calcule le total des prix d'accessoires
     * @param listeIdAccessoires Le liste des ID des accessoires
     * @return Retourne le prix total des accessoires
     */
    public double calculPrixTotalAccessoires(ArrayList<String> listeIdAccessoires) {
        double prixTotal = 0.00;
        ArrayList<Accessoire> listeAccessoiresChoisis = new ArrayList<>();

        for (String accId : listeIdAccessoires) {
            listeAccessoiresChoisis.add(this.iaccessoiredao.getAccessoireFromId(accId));
        }
        for (Accessoire acc : listeAccessoiresChoisis) {
            prixTotal += acc.getPrixAccessoire();
        }
        return prixTotal;
    }

    //*****Méthode à tester*****
    /**
     * Méthode qui prend une les de prix différents
     * @param v Le vélo choisi
     * @param nomAbonnement Le nom de l'abonnement
     * @param listeAccessoires La liste des accessoires
     * @return Retourne la liste des prix dans cet ordre: prixAbonnement, prixAccessoires, PrixTotal
     */
    public ArrayList<Double> calculPrixTotalLocation(Velo v, String nomAbonnement, ArrayList<String> listeAccessoires) {
        ArrayList<Double> listePrix = new ArrayList<>();
        double prixAbonnement = this.calculPrixAbonnementParType(v, nomAbonnement);
        double prixAccessoires = this.calculPrixTotalAccessoires(listeAccessoires);

        listePrix.add(prixAbonnement);
        listePrix.add(prixAccessoires);
        listePrix.add(prixAbonnement + prixAccessoires);

        return listePrix;
    }

    /**
     * Méthode qui crée une location dans la base de données
     * @param idAbonnementUtilisateur ID de l'abonnement_utilisateur qui vient d'être crée
     * @param prixTotal Le prix total de la location
     * @param dateDebut La date de début de la location
     * @param dateFin La date de fin de la location
     * @return Retourne l'ID de la location qui vient d'être crée
     */
    public int createLocation(int idAbonnementUtilisateur, double prixTotal, LocalDate dateDebut, LocalDate dateFin) {
        int idLoc = this.ilocationdao.insertLocation(idAbonnementUtilisateur, prixTotal, dateDebut, dateFin);

        return idLoc;
    }

    /**
     * Méthode qui crée une ligne dans accessoireLocation
     * @param listeAccessoires La liste des accessoires
     * @param idLoc l'ID de la location
     */
    public void createAccessoireLocation(ArrayList<String> listeAccessoires, int idLoc) {
        this.iacclocdao.insertAccessoireLocation(listeAccessoires, idLoc);
    }

    /**
     * Calcul le nombre de jours de la location pour generer un random km réaliste par jour
     * @return Retourne le nombre de jours de la location
     */
    public int calculNbJoursLocation() {
        int nombreJours = 0;

        LocalDate dateAuj = LocalDate.now();
        LocalDate dateDebutLoc = this.ilocationdao
                .getDateDebutCurrentLocation(this.userConnected.getUser().getIdUser());
        nombreJours = (int) ChronoUnit.DAYS.between(dateDebutLoc, dateAuj);

        return nombreJours;
    }

    ////////////////////
    //Methodes Merites//
    ////////////////////

    /**
     * Converti chaque Merite obtenu en 1 seul String
     * @return Retourne la String avec tout les mérites dans cet ordre: id, nom, description, critère, dateObtenu
     */
    public String getMeritesObtenuString() {
        ArrayList<Merites> listeMeritesObtenu = this.imeriteuserdao
                .getListeMeritesObtenu(this.userConnected.getUser().getIdUser());
        String listeMeritesObtenuString = "";

        for (Merites m : listeMeritesObtenu) {
            listeMeritesObtenuString += Integer.toString(m.getIdMerite()) + "," +
                    m.getNomMerite() + "," +
                    m.getDescriptionMerite() + "," +
                    Integer.toString(m.getCritere()) + "," +
                    m.getDateObtenu().toString() + ";";
        }
        if (listeMeritesObtenu.size() > 0) {
            listeMeritesObtenuString = listeMeritesObtenuString.substring(0, listeMeritesObtenuString.length() - 1);
        }
        return listeMeritesObtenuString;
    }

    //Converti change Merite Non Obtenu en 1 seul String
    /**
     * Converti chaque mérite non obtenu en 1 seul String
     * @return Retourne le String des mérites dans cet ordre: id, nom, description, critère
     */
    public String getMeritesNonObtenuString() {
        ArrayList<Merites> listeMeritesNonObtenu = this.imeriteuserdao
                .getListeMeritesNonObtenu(this.userConnected.getUser().getIdUser());
        String listeMeritesNonObtenuString = "";

        for (Merites m : listeMeritesNonObtenu) {
            listeMeritesNonObtenuString += Integer.toString(m.getIdMerite()) + "," +
                    m.getNomMerite() + "," +
                    m.getDescriptionMerite() + "," +
                    Integer.toString(m.getCritere()) + ";";
        }
        if (listeMeritesNonObtenu.size() > 0) {
            listeMeritesNonObtenuString = listeMeritesNonObtenuString.substring(0,
                    listeMeritesNonObtenuString.length() - 1);
        }
        return listeMeritesNonObtenuString;
    }

    //Combine les 2 listes de mérites ensemble et lance un FirePropertyChange avec cette liste. Affiche la view Merites
    public void fustionneListeMerites() {
        ArrayList<String> listeMeritesObtNonObt = new ArrayList<>();
        String listeObt = this.getMeritesObtenuString();
        String listeNonObt = this.getMeritesNonObtenuString();
        String kmRestantsProchainMerite = Integer.toString(this.calculNbKmRestants());
        String nombrePointsBonus = Double
                .toString(this.iuserdao.getPointsBonus(this.userConnected.getUser().getIdUser()));

        listeMeritesObtNonObt.add(listeObt);
        listeMeritesObtNonObt.add(listeNonObt);
        listeMeritesObtNonObt.add(kmRestantsProchainMerite);
        listeMeritesObtNonObt.add(nombrePointsBonus);

        support.firePropertyChange("retourne-liste-merite", "", listeMeritesObtNonObt);
    }

    /**
     * Retourne le level du dernier Merite obtenu. Si c'est null, ça retourne 0
     * @return Retourne le niveau du dernier mérite obtendu
     */
    public int checkDernierMeriteObtenu() {
        return this.imeriteuserdao.getDernierMeriteObtenu(this.userConnected.getUser().getIdUser());
    }

    /**
     * Méthode qui cherche le critère du prochain palier
     * @return Retourne le critère du prochain palier
     */
    public int getCritereProchainPalier() {
        int levelActuel = this.checkDernierMeriteObtenu();
        return this.imeritedao.getCritereProchainPalier(levelActuel);
    }

    //*****Méthode à tester*****
    /**
     * Calcul le nombre de KM restants pour atteindre le prochain pallier
     * @return Retourne le nombre de KM restants pour atteindre le prochain palier
     */
    public int calculNbKmRestants() {
        int totalKMUser = this.userConnected.getUser().getTotalKM();
        int critereProchainMerite = this.getCritereProchainPalier();

        return critereProchainMerite - totalKMUser;
    }

    //*****Méthode à tester*****
    /**
     * Check le dernier level reçu et en fonction de la liste des criteres et le total des km du user
     * Vérifie les levels pas encore reçu
     * @return Retourne la liste des levels des mérites obtenus
     */
    public ArrayList<Integer> meritesObtenus() {
        int lvlDernierObtenu = this.checkDernierMeriteObtenu();
        int totalKMUser = this.userConnected.getUser().getTotalKM();
        ArrayList<Integer> listeLevelsObetenu = new ArrayList<>();
        ArrayList<Integer> listeCriteres = this.imeritedao.getListeCriteres();

        for (int i = lvlDernierObtenu; i < listeCriteres.size(); i++) {
            if (totalKMUser >= listeCriteres.get(i)) {
                listeLevelsObetenu.add(i + 1);
            }
        }
        return listeLevelsObetenu;
    }

    //*****Méthode à tester*****
    /**
     * Calcule les points bonus a partir des km enregistrés et la dernière mérite reçu
     * @param enregistrementKM Les KM parcouru
     * @return Retourne les points bonus
     */
    public double calculPointsBonus(int enregistrementKM) {
        double pointsBonusBase = enregistrementKM * 0.1;
        int maxMerite = this.imeriteuserdao.getDernierMeriteObtenu(this.userConnected.getUser().getIdUser());

        switch (maxMerite) {
            case 1:
                pointsBonusBase += pointsBonusBase * 0.1;
                break;
            case 2:
                pointsBonusBase += pointsBonusBase * 0.2;
                break;
            case 3:
                pointsBonusBase += pointsBonusBase * 0.3;
                break;
            case 4:
                pointsBonusBase += pointsBonusBase * 0.4;
                break;
            case 5:
                pointsBonusBase += pointsBonusBase * 0.5;
                break;
            case 6:
                pointsBonusBase += pointsBonusBase * 0.6;
                break;
            case 7:
                pointsBonusBase += pointsBonusBase * 0.7;
                break;
        }
        return pointsBonusBase;
    }

    /**
     * Met à jour le nombre de points bonus reçu
     * @param pointsBonus Les points bonus à ajouter
     */
    public void updatePointsBonus(double pointsBonus) {
        double pointsActuel = this.iuserdao.getPointsBonus(this.userConnected.getUser().getIdUser());
        double pointsTotal = pointsActuel + pointsBonus;
        this.iuserdao.updatePointsBonus(pointsTotal, this.userConnected.getUser().getIdUser());
    }

    /**
     * Met à jour la DB avec les Merites reçu après avoir rendu le vélo
     * @param lvlMeritesObtenus Liste des levels des mérites obtenus
     */
    public void updateMerites(ArrayList<Integer> lvlMeritesObtenus) {
        if (lvlMeritesObtenus.size() > 0) {
            for (int lvlMerite : lvlMeritesObtenus) {
                this.imeriteuserdao.updateMeritesObtenus(lvlMerite, this.userConnected.getUser().getIdUser());
            }
        }
    }

    ///////////////////
    //Page Historique//
    ///////////////////

    // Méthode qui prend la liste des locations du userConnected et les envoi dans un firePropertyChange
    public void showPageHistorique() {
        ArrayList<String> listeLocations = this.ilocationdao.getLocationsById(this.userConnected.getUser().getIdUser());
        this.support.firePropertyChange("retour-page-historique", "", listeLocations);
    }

    ///////////////
    //Méthodes DB//
    ///////////////

    // Vérifie si les données dans la table vélo existent. Si pas, les insère
    public void checkDataExistsVelo() {
        if (this.ivelodao.checkDonneesExiste() == 0) {
            this.ivelodao.insertDonneesClassique();
            this.ivelodao.insertDonneesElectrique();
            this.ivelodao.insertDonneesEnfant();
        }
    }

    // Vérifie si les données dans la table merite existent. Si pas, les insère
    public void checkDataExistsMerite() {
        if (this.imeritedao.checkDonneesExiste() == 0) {
            this.imeritedao.insertDonneesMerite();
        }
    }

    // Vérifie si les données dans la table abonnement existent. Si pas, les insère
    public void checkDataExistsAbonnement() {
        if (this.iabonnementdao.checkDonneesExiste() == 0) {
            this.iabonnementdao.insertDonneesAbonnement();
        }
    }

    // Vérifie si les données dans la table accessoire existent. Si pas, les insère
    public void checkDataExistsAccessoire() {
        if (this.iaccessoiredao.checkDonneesExiste() == 0) {
            this.iaccessoiredao.insertDonneesAccessoire();
        }
    }
}
