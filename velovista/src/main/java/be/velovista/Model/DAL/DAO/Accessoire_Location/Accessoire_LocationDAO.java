package be.velovista.Model.DAL.DAO.Accessoire_Location;

import java.sql.SQLException;
import java.sql.Statement;

import be.velovista.Model.DAL.DBConnection;

public class Accessoire_LocationDAO implements IAccessoire_LocationDAO {
    
    public Accessoire_LocationDAO(){
        DBConnection.getConnection();
    }

    public void createAccessoire_LocationTable(){
        String sqlString = "CREATE TABLE IF NOT EXISTS Accessoire_Location (idaccessoire int, id_location_velo int, PRIMARY KEY (idaccessoire, id_location_velo), FOREIGN KEY (idaccessoire) REFERENCES accessoire(idaccessoire), FOREIGN KEY (id_location_velo) REFERENCES LocationVelo(id_location_velo))";

        try(Statement stat = DBConnection.conn.createStatement()){
            stat.executeQuery(sqlString);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
