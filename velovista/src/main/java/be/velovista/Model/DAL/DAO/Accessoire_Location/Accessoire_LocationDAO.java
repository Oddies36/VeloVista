package be.velovista.Model.DAL.DAO.Accessoire_Location;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import be.velovista.Model.DAL.DBConnection;

public class Accessoire_LocationDAO implements IAccessoire_LocationDAO {

    public Accessoire_LocationDAO() {
        DBConnection.getConnection();
    }

    // Crée la table accessoire_location
    public void createAccessoire_LocationTable() {
        String sqlString = "CREATE TABLE IF NOT EXISTS Accessoire_Location (idaccessoire int, id_location_velo int, PRIMARY KEY (idaccessoire, id_location_velo), FOREIGN KEY (idaccessoire) REFERENCES accessoire(idaccessoire), FOREIGN KEY (id_location_velo) REFERENCES LocationVelo(id_location_velo))";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** 
     * Insère une ligne accessoire_location
     * @param listeAccessoires La liste des ID des accessoires en format de String
     * @param idLoc L'ID de la location
     */
    public void insertAccessoireLocation(ArrayList<String> listeAccessoires, int idLoc) {
        String sqlString = "INSERT INTO accessoire_location (idaccessoire, id_location_velo) VALUES (?, ?)";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            for (String a : listeAccessoires) {
                pstat.setInt(1, Integer.parseInt(a));
                pstat.setInt(2, idLoc);
                pstat.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Récupère la liste des accessoires qu'un utilisateur a avec l'ID de la location
    // public ArrayList<Accessoire> getListeAccessoiresUser(String idLocation) {
    //     String sqlString = "SELECT acc.idaccessoire, acc.nomaccessoire, acc.prixaccessoire, acc.photoaccessoire, acc.descriptionaccessoire from accessoire AS acc INNER JOIN accessoire_location AS accloc ON accloc.idaccessoire = acc.idaccessoire WHERE accloc.id_location_velo = ?";
    //     ArrayList<Accessoire> listeAccessoireUser = new ArrayList<Accessoire>();

    //     try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
    //         pstat.setInt(1, Integer.parseInt(idLocation));
    //         ;
    //         try (ResultSet rset = pstat.executeQuery()) {
    //             while (rset.next()) {
    //                 Accessoire a = new Accessoire(rset.getInt(1), rset.getString(2), rset.getDouble(3),
    //                         rset.getString(4), rset.getString(5));
    //                 listeAccessoireUser.add(a);
    //             }
    //         } catch (SQLException e) {
    //             System.out.println(e.getMessage());
    //         }
    //     } catch (SQLException e) {
    //         System.out.println(e.getMessage());
    //     }
    //     return listeAccessoireUser;
    // }
}
