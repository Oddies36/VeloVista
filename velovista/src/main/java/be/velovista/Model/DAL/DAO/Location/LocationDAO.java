package be.velovista.Model.DAL.DAO.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    String sqlString = "CREATE TABLE IF NOT EXISTS LocationVelo (id_location_velo SERIAL, id_abonnement int, id_accessoire int, prixtotal DECIMAL(10,2), DateDebut DATE, DateFin DATE, PRIMARY KEY (id_location_velo),FOREIGN KEY (id_abonnement) REFERENCES abonnement(idabonnement), FOREIGN KEY (id_accessoire) REFERENCES accessoires(idaccessoire))";

    try(Statement stat = DBConnection.conn.createStatement()){
      stat.executeQuery(sqlString);
    }
    catch(SQLException e){
      System.out.println(e.getMessage());
    }
  }
/////////////////
  public Location getLocation(String email){
    String sqString = "SELECT id_location_velo FROM LocationVelo";
    Location l = null;

    try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqString)) {
      try(ResultSet rset = pstat.executeQuery()){
        if(rset.next()){
          l = new Location(0, this.iuserdao.getUser(email), null, null, sqString, sqString);
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
}
