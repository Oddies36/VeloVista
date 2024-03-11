package be.velovista.Model.DAL;

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

public class DBTableInitializer {

    static IAbonnementDAO iabodao = new AbonnementDAO();
    static IAbonnementUtilisateurDAO iabouserdao = new AbonnementUtilisateurDAO();
    static IAccessoireDAO iaccdao = new AccessoireDAO();
    static IAccessoire_LocationDAO iacclocdao = new Accessoire_LocationDAO();
    static ILocationDAO ilocdao = new LocationDAO();
    static IMerite_utilisateurDAO imerutdao = new Merite_utilisateurDAO();
    static IMeritesDAO imeritesdao = new MeritesDAO();
    static IReservationDAO ireservationdao = new ReservationDAO();
    static IUserDAO iuserdao = new UserDAO();
    static IVeloDAO ivelodao = new VeloDAO();

    public static void createAllTables() {
        iuserdao.createUserTable();
        ivelodao.createVeloTable();
        imeritesdao.createMeritesTable();
        iabodao.createAbonnementTable();
        iaccdao.createAccessoireTable();
        iabouserdao.createAbonnementUtilisateurTable();
        ilocdao.createLocationTable();
        iacclocdao.createAccessoire_LocationTable();
        imerutdao.createMeritesTable();
        ireservationdao.createReservationTable();
    }
}
