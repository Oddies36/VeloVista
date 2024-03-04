package be.velovista.Model.DAL.DAO.Reservation;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import be.velovista.Model.BL.Reservation;
import be.velovista.Model.DAL.DBConnection;

public class ReservationDAO implements IReservationDAO {
        public ReservationDAO(){
        DBConnection.getConnection();
    }

    public void createReservationTable(){
        String sqlString = "CREATE TABLE IF NOT EXISTS Reservation (idReservation SERIAL PRIMARY KEY, id_utilisateur INTEGER, id_velo INTEGER, datedebut DATE, datefin DATE, choixabonnement VARCHAR(30), FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (iduser), FOREIGN KEY (id_velo) REFERENCES velo(idvelo));";

        try(Statement stat = DBConnection.conn.createStatement()){
            stat.executeUpdate(sqlString);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public int checkDispo(int idVelo, int idUser, String dateDebut, String dateFin){
        String sqlString = "SELECT COUNT(idreservation) FROM reservation WHERE datedebut <= ? AND datefin >= ? AND id_utilisateur = ? AND id_velo = ?;";
        int count = 0;
        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setDate(1, Date.valueOf(dateFin));
            pstat.setDate(2, Date.valueOf(dateDebut));
            pstat.setInt(3, idUser);
            pstat.setInt(4, idVelo);
            try(ResultSet rset = pstat.executeQuery()){
                if(rset.next()){
                    count = rset.getInt(1);
                }
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return count;
    }

    public void insertReservation(int idVelo, int idUser, String dateDebut, String dateFin, String choixAboUserString){
        String sqlString = "INSERT INTO reservation (id_utilisateur, id_velo, datedebut, datefin, choixabonnement, isactif) VALUES (?, ?, ?, ?, ?, true)";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setInt(1, idUser);
            pstat.setInt(2, idVelo);
            pstat.setDate(3, Date.valueOf(dateDebut));
            pstat.setDate(4, Date.valueOf(dateFin));
            pstat.setString(5, choixAboUserString);
            pstat.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Reservation> getListeReservations(int idUser){
        ArrayList<Reservation> listeReservations = new ArrayList<>();
        String sqlString = "SELECT idreservation, id_velo, datedebut, datefin, choixabonnement FROM reservation WHERE id_utilisateur = ? AND isactif = true";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setInt(1, idUser);
            try(ResultSet rset = pstat.executeQuery()){
                while(rset.next()){
                    Reservation r = new Reservation(rset.getInt(1), rset.getInt(2), idUser, rset.getDate(3).toLocalDate(), rset.getDate(4).toLocalDate(), rset.getString(5));
                    listeReservations.add(r);
                }
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return listeReservations;
    }

    public void annulerReservation(int idReservation){
        String sqlString = "UPDATE reservation SET isactif = false WHERE idreservation = ?";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setInt(1, idReservation);
            pstat.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
