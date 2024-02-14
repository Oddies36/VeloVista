package be.velovista.Model.DAL.DAO.AbonnementUtilisateur;

import java.sql.SQLException;
import java.sql.Statement;

import be.velovista.Model.DAL.DBConnection;

public class AbonnementUtilisateurDAO implements IAbonnementUtilisateurDAO {
    
    public AbonnementUtilisateurDAO(){
        DBConnection.getConnection();
    }

    public void createAbonnementUtilisateurTable(){
        String sqlString = "CREATE TABLE IF NOT EXISTS AbonnementUtilisateur (id_abonnement_utilisateur SERIAL, id_abonnement int, id_velo int, id_utilisateur int, coutabonnement DECIMAL(10,2), PRIMARY KEY (id_abonnement_utilisateur), FOREIGN KEY (id_abonnement) REFERENCES abonnement(idabonnement), FOREIGN KEY (id_velo) REFERENCES velo(idvelo), FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(iduser));";

        try(Statement stat =  DBConnection.conn.createStatement()){
            stat.executeUpdate(sqlString);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
