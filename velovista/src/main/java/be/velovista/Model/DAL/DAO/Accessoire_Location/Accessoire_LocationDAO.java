package be.velovista.Model.DAL.DAO.Accessoire_Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import be.velovista.Model.BL.Accessoire;
import be.velovista.Model.DAL.DBConnection;

public class Accessoire_LocationDAO implements IAccessoire_LocationDAO {
    
    public Accessoire_LocationDAO(){
        DBConnection.getConnection();
    }

    public void createAccessoire_LocationTable(){
        String sqlString = "CREATE TABLE IF NOT EXISTS Accessoire_Location (idaccessoire int, id_location_velo int, prixtotalaccessoires DECIMAL(10,2), PRIMARY KEY (idaccessoire, id_location_velo), FOREIGN KEY (idaccessoire) REFERENCES accessoire(idaccessoire), FOREIGN KEY (id_location_velo) REFERENCES LocationVelo(id_location_velo))";

        try(Statement stat = DBConnection.conn.createStatement()){
            stat.executeQuery(sqlString);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Accessoire> getListeAccessoiresUser(String idLocation){
        String sqlString = "SELECT acc.idaccessoire, acc.nomaccessoire, acc.prixaccessoire, acc.photoaccessoire, acc.descriptionaccessoire from accessoire AS acc INNER JOIN accessoire_location AS accloc ON accloc.idaccessoire = acc.idaccessoire WHERE accloc.id_location_velo = ?";
        ArrayList<Accessoire> listeAccessoireUser = new ArrayList<Accessoire>();
        

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setInt(1, Integer.parseInt(idLocation));;
            try(ResultSet rset = pstat.executeQuery()){
                while(rset.next()){
                    Accessoire a = new Accessoire(rset.getInt(1), rset.getString(2), rset.getDouble(3), rset.getString(4), rset.getString(5));
                    listeAccessoireUser.add(a);
                }
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return listeAccessoireUser;
    }
}
