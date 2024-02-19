package be.velovista.Model.DAL.DAO.Accessoire;

import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import be.velovista.Model.BL.Accessoire;
import be.velovista.Model.DAL.DBConnection;

public class AccessoireDAO implements IAccessoireDAO {
    

    public AccessoireDAO(){
        DBConnection.getConnection();
    }

    public void createAccessoireTable(){
        String sqlString = "CREATE TABLE IF NOT EXISTS Accessoire (idaccessoire SERIAL, nomaccessoire VARCHAR(50), prixaccessoire DECIMAL(10,2), photoaccessoire TEXT, descriptionaccessoire TEXT, PRIMARY KEY (idaccessoire))";

        try(Statement stat = DBConnection.conn.createStatement()){
            stat.executeQuery(sqlString);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Accessoire> getAccessoires(){
        ArrayList<Accessoire> listeAccessoires = new ArrayList<>();
        String sqlString = "SELECT idaccessoire, nomaccessoire, prixaccessoire, photoaccessoire, descriptionaccessoire FROM accessoire;";

        try(Statement stat = DBConnection.conn.createStatement()){
            try(ResultSet rset = stat.executeQuery(sqlString)){
                while(rset.next()){
                    Accessoire acc = new Accessoire(rset.getInt(1), rset.getString(2), rset.getDouble(3), rset.getString(4), rset.getString(5));
                    listeAccessoires.add(acc);
                }
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return listeAccessoires;
    }

    public Accessoire getAccessoireFromId(String idAccessoire){
        Accessoire a = null;
        String sqlString = "SELECT idaccessoire, nomaccessoire, prixaccessoire, photoaccessoire, descriptionaccessoire FROM accessoire WHERE idaccessoire = ?;";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setInt(1, Integer.parseInt(idAccessoire));
            try(ResultSet rset = pstat.executeQuery()){
                if(rset.next()){
                    a = new Accessoire(rset.getInt(1), rset.getString(2), rset.getDouble(3), rset.getString(4), rset.getString(5));
                }
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return a;
    }
}
