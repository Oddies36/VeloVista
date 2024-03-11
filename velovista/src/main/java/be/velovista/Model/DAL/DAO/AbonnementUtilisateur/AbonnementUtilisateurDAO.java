package be.velovista.Model.DAL.DAO.AbonnementUtilisateur;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import be.velovista.Model.DAL.DBConnection;

public class AbonnementUtilisateurDAO implements IAbonnementUtilisateurDAO {

    public AbonnementUtilisateurDAO() {
        DBConnection.getConnection();
    }

    // Crée la table abonnement_utilisateur
    public void createAbonnementUtilisateurTable() {
        String sqlString = "CREATE TABLE IF NOT EXISTS AbonnementUtilisateur (id_abonnement_utilisateur SERIAL, id_abonnement int, id_velo int, id_utilisateur int, coutabonnement DECIMAL(10,2), isactif BOOLEAN, PRIMARY KEY (id_abonnement_utilisateur), FOREIGN KEY (id_abonnement) REFERENCES abonnement(idabonnement), FOREIGN KEY (id_velo) REFERENCES velo(idvelo), FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(iduser));";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** 
     * Insère une ligne dans abonnement_utilisateur et retourne l'ID qui est crée
     * @param idAbo L'ID de l'abonnement
     * @param idVelo L'ID du vélo
     * @param idUser L'ID de l'utilisateur
     * @param prixAbo Le prix de l'abonnement
     * @return Retourne l'ID de la ligne qui vient d'être crée
     */
    public int insertAbonnementUtilisateur(int idAbo, int idVelo, int idUser, double prixAbo) {
        String sqlString = "INSERT INTO abonnementutilisateur (id_abonnement, id_velo, id_utilisateur, coutabonnement, isactif) VALUES (?, ?, ?, ?, true) RETURNING id_abonnement_utilisateur;";
        int idAbonnementUtilisateur = -1;

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setInt(1, idAbo);
            pstat.setInt(2, idVelo);
            pstat.setInt(3, idUser);
            pstat.setDouble(4, prixAbo);
            try (ResultSet rset = pstat.executeQuery()) {
                if (rset.next()) {
                    idAbonnementUtilisateur = rset.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return idAbonnementUtilisateur;
    }

    /** 
     * Récupère l'ID du vélo avec l'ID du user et seulement si l'abonnement_utilisateur est actif
     * @param idUser L'ID de l'utilisateur
     * @return Retourne l'ID du vélo
     */
    public int getIdVeloFromIdUser(int idUser) {
        String sqlString = "SELECT id_velo FROM abonnementutilisateur where isactif = true AND id_utilisateur = ?";
        int idVelo = -1;
        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setInt(1, idUser);
            try (ResultSet rset = pstat.executeQuery()) {
                if (rset.next()) {
                    idVelo = rset.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return idVelo;
    }

    /** 
     * Vérifie si un abonnement_utilisateur ACTIF existe pour un utilisateur
     * @param idUser L'ID de l'utilisateur
     * @return Retourne le nombre de lignes trouvé. Si une ligne est trouvé, c'est que l'utilisateur a déjà un abonnement actif
     */
    public int checkCurrentAboUser(int idUser) {
        int count = -1;
        String sqlString = "SELECT count(id_abonnement_utilisateur) FROM abonnementutilisateur WHERE id_utilisateur = ? AND isactif = true;";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setInt(1, idUser);
            try (ResultSet rset = pstat.executeQuery()) {
                if (rset.next()) {
                    count = rset.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }

    /** 
     * Met à jour un abonnement_utilisateur en inactif
     * @param idUser L'ID de l'utilisateur
     */
    public void updateAbonnementUtilisateurInactif(int idUser) {
        String sqlString = "UPDATE abonnementutilisateur SET isactif = false WHERE id_utilisateur = ?";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setInt(1, idUser);
            pstat.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
