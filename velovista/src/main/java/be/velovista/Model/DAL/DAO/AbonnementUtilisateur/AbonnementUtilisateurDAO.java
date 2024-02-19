package be.velovista.Model.DAL.DAO.AbonnementUtilisateur;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public int insertAbonnementUtilisateur(int idAbo, int idVelo, int idUser, double prixAbo){
        String sqlString = "INSERT INTO abonnementutilisateur (id_abonnement, id_velo, id_utilisateur, coutabonnement) VALUES (?, ?, ?, ?) RETURNING id_abonnement_utilisateur;";
        int idAbonnementUtilisateur = -1;

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setInt(1, idAbo);
            pstat.setInt(2, idVelo);
            pstat.setInt(3, idUser);
            pstat.setDouble(4, prixAbo);
            try(ResultSet rset = pstat.executeQuery()){
                if(rset.next()){
                    idAbonnementUtilisateur = rset.getInt(1);
                }
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return idAbonnementUtilisateur;
    }

}
