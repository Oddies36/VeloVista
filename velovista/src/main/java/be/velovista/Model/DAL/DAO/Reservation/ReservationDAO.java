package be.velovista.Model.DAL.DAO.Reservation;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import be.velovista.Model.BL.Reservation;
import be.velovista.Model.DAL.DBConnection;

public class ReservationDAO implements IReservationDAO {
    public ReservationDAO() {
        DBConnection.getConnection();
    }

    // Crée la table reservation
    public void createReservationTable() {
        String sqlString = "CREATE TABLE IF NOT EXISTS Reservation (idReservation SERIAL PRIMARY KEY, id_utilisateur INTEGER, id_velo INTEGER, datedebut DATE, datefin DATE, choixabonnement VARCHAR(30), FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (iduser), FOREIGN KEY (id_velo) REFERENCES velo(idvelo));";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Vérifie si une réservation existe déjà entre 2 dates.
     * @param idVelo    L'ID du vélo en question
     * @param dateDebut La date de début de la réservation
     * @param dateFin   La date de fin de la réservation
     * @return Retourne le nombre de réservations trouvé
     */
    public int checkDispo(int idVelo, String dateDebut, String dateFin) {
        String sqlString = "SELECT COUNT(idreservation) FROM reservation WHERE datedebut <= ? AND datefin >= ? AND id_velo = ?;";
        int count = 0;
        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setDate(1, Date.valueOf(dateFin));
            pstat.setDate(2, Date.valueOf(dateDebut));
            pstat.setInt(3, idVelo);
            try (ResultSet rset = pstat.executeQuery()) {
                if (rset.next()) {
                    count = rset.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }

    /**
     * Insère une ligne dans une réservation
     * @param idVelo             L'ID du vélo à réserver
     * @param idUser             L'ID de l'utilisateur qui réserve le vélo
     * @param dateDebut          La date de début de la réservation
     * @param dateFin            La date de fin de la réservation
     * @param choixAboUserString Le choix de l'abonnement
     */
    public void insertReservation(int idVelo, int idUser, String dateDebut, String dateFin, String choixAboUserString) {
        String sqlString = "INSERT INTO reservation (id_utilisateur, id_velo, datedebut, datefin, choixabonnement, isactif) VALUES (?, ?, ?, ?, ?, true)";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setInt(1, idUser);
            pstat.setInt(2, idVelo);
            pstat.setDate(3, Date.valueOf(dateDebut));
            pstat.setDate(4, Date.valueOf(dateFin));
            pstat.setString(5, choixAboUserString);
            pstat.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Cherche la liste des réservations qui sont actives avec l'ID de l'utilisateur
     * @param idUser L'ID de l'utilisateur
     * @return Retourne un ArrayList<Reservation> avec la liste des réservations en cours
     */
    public ArrayList<Reservation> getListeReservations(int idUser) {
        ArrayList<Reservation> listeReservations = new ArrayList<>();
        String sqlString = "SELECT idreservation, id_velo, datedebut, datefin, choixabonnement FROM reservation WHERE id_utilisateur = ? AND isactif = true";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setInt(1, idUser);
            try (ResultSet rset = pstat.executeQuery()) {
                while (rset.next()) {
                    Reservation r = new Reservation(rset.getInt(1), rset.getInt(2), idUser,
                            rset.getDate(3).toLocalDate(), rset.getDate(4).toLocalDate(), rset.getString(5));
                    listeReservations.add(r);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listeReservations;
    }

    /**
     * Met à jour une réservation en actif et le met en inactif
     * @param idReservation L'ID de la réservation
     */
    public void annulerReservation(int idReservation) {
        String sqlString = "UPDATE reservation SET isactif = false WHERE idreservation = ?";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setInt(1, idReservation);
            pstat.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
