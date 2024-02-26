package be.velovista.Model.DAL.DAO.Merite_utilisateur;

import java.sql.SQLException;
import java.sql.Statement;

import be.velovista.Model.DAL.DBConnection;

public class Merite_utilisateurDAO implements IMerite_utilisateurDAO {
    public Merite_utilisateurDAO(){
        DBConnection.getConnection();
    }

    public void createMeritesTable(){
        String sqlString = "CREATE TABLE IF NOT EXISTS Merite_Utilisateur (id_merite int, id_utilisateur int, dateobtenu DATE, PRIMARY KEY (id_merite, id_utilisateur), FOREIGN KEY (id_merite) REFERENCES merite(idmerite), FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(iduser))";

        try(Statement stat = DBConnection.conn.createStatement()){
            stat.executeQuery(sqlString);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
