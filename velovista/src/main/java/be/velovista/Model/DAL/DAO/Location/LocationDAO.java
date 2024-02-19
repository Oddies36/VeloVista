package be.velovista.Model.DAL.DAO.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import be.velovista.Model.BL.Location;
import be.velovista.Model.DAL.DBConnection;
import be.velovista.Model.DAL.DAO.User.IUserDAO;
import be.velovista.Model.DAL.DAO.User.UserDAO;

public class LocationDAO implements ILocationDAO {
  private IUserDAO iuserdao = new UserDAO();
    
  public LocationDAO(){
    DBConnection.getConnection();
  }

  public void createLocationTable(){
    String sqlString = "CREATE TABLE IF NOT EXISTS LocationVelo (id_location_velo SERIAL, id_abonnementutilisateur integer, prixtotal DECIMAL(10,2), DateDebut DATE, DateFin DATE, PRIMARY KEY (id_location_velo),FOREIGN KEY (id_abonnementutilisateur) REFERENCES abonnementutilisateur(id_abonnement_utilisateur))";

    try(Statement stat = DBConnection.conn.createStatement()){
      stat.executeQuery(sqlString);
    }
    catch(SQLException e){
      System.out.println(e.getMessage());
    }
  }
/////////////////
  public Location getLocationByEmail(String email){
    String sqString = "SELECT id_location_velo, id_abonnement, id_accessoire FROM LocationVelo";
    Location l = null;

    try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqString)) {
      try(ResultSet rset = pstat.executeQuery()){
        if(rset.next()){
          l = new Location(rset.getInt(1), null, null, null, email, sqString, 0);
        }
      }
      catch(SQLException e){
        System.out.println(e.getMessage());
      }
    }
    catch(SQLException e){
      System.out.println(e.getMessage());
    }
    return l;
  }

  public ArrayList<String> getLocationInfo(String email){
    ArrayList<String> result = new ArrayList<>();
    String sqlString = "SELECT locv.id_location_velo, usr.iduser, v.idvelo, locv.datedebut, locv.datefin, locv.prixtotal FROM locationvelo AS locv INNER JOIN abonnementutilisateur AS abou ON locv.id_abonnementutilisateur = abou.id_abonnement_utilisateur INNER JOIN utilisateur AS usr ON usr.iduser = abou.id_utilisateur INNER JOIN velo AS v ON v.idvelo = abou.id_velo WHERE usr.email = ?";

    try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
      pstat.setString(1, email);
      try(ResultSet rset = pstat.executeQuery()){
        while(rset.next()){
          result.add(Integer.toString(rset.getInt(1)));
          result.add(Integer.toString(rset.getInt(2)));
          result.add(Integer.toString(rset.getInt(3)));
          result.add(rset.getString(4));
          result.add(rset.getString(5));
          result.add(Double.toString(rset.getDouble(6)));
        }
      }
      catch(SQLException e){
        System.out.println(e.getMessage());
      }
    }
    catch(SQLException e){
      System.out.println(e.getMessage());
    }
    return result;
  }

  public void insertLocation(int idAbonnementUtilisatuer, double prixTotal, LocalDate dateDebut, LocalDate dateFin){
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String dateDebutString = dateDebut.format(dtf);
    String dateFinString = dateFin.format(dtf);
    
    String sqlString = "INSERT INTO locationvelo (id_abonnementutilisateur, prixtotal, datedebut, datefin) VALUES (?, ?, ?, ?);";

    try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
      pstat.setInt(1, idAbonnementUtilisatuer);
      pstat.setDouble(2, prixTotal);
      pstat.setObject(3, dateDebut);
      pstat.setObject(4, dateFin);
      pstat.executeUpdate();
    }
    catch(SQLException e){
      System.out.println(e.getMessage());
    }
  }
  
  public int checkCurrentLocation(LocalDate dateDebut, LocalDate dateFin){
    int count = -1;
    LocalDate now = LocalDate.now();
    String sqlString = "SELECT count(id_location_velo) FROM locationvelo WHERE ? BETWEEN ? AND ?;";

    try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
      pstat.setObject(1, now);
      pstat.setObject(2, dateDebut);
      pstat.setObject(3, dateFin);
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
}
