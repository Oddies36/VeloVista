package be.velovista.Model.DAL.DAO.Merites;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import be.velovista.Model.DAL.DBConnection;

public class MeritesDAO implements IMeritesDAO {
    public MeritesDAO(){
        DBConnection.getConnection();
    }

    public void createMeritesTable(){
                String sqlString = "CREATE TABLE IF NOT EXISTS Merite (idMerite SERIAL PRIMARY KEY, NomMerite VARCHAR(50), DescriptionMerite VARCHAR(200), Critere INTEGER);";

        try(Statement stat = DBConnection.conn.createStatement()){
            stat.executeQuery(sqlString);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
