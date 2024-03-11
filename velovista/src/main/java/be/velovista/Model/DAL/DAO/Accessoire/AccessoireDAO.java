package be.velovista.Model.DAL.DAO.Accessoire;

import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import be.velovista.Model.BL.Accessoire;
import be.velovista.Model.DAL.DBConnection;

public class AccessoireDAO implements IAccessoireDAO {

    public AccessoireDAO() {
        DBConnection.getConnection();
    }

    // Crée la table accessoire
    public void createAccessoireTable() {
        String sqlString = "CREATE TABLE IF NOT EXISTS Accessoire (idaccessoire SERIAL, nomaccessoire VARCHAR(50), prixaccessoire DECIMAL(10,2), photoaccessoire TEXT, descriptionaccessoire TEXT, PRIMARY KEY (idaccessoire))";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** 
     * Récupère la liste des accessoires et les insère dans une liste d'objets accessoires
     * @return Retourne une ArrayList<Accessoire>
     */
    public ArrayList<Accessoire> getAccessoires() {
        ArrayList<Accessoire> listeAccessoires = new ArrayList<>();
        String sqlString = "SELECT idaccessoire, nomaccessoire, prixaccessoire, photoaccessoire, descriptionaccessoire FROM accessoire;";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            try (ResultSet rset = pstat.executeQuery()) {
                while (rset.next()) {
                    Accessoire acc = new Accessoire(rset.getInt(1), rset.getString(2), rset.getDouble(3),
                            rset.getString(4), rset.getString(5));
                    listeAccessoires.add(acc);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listeAccessoires;
    }

    /** 
     * Récupè un accessoire avec l'ID de cet accessoire et met ça dans un objet accessoire
     * @param idAccessoire L'ID de l'accessoire
     * @return Retourne un accessoire
     */
    public Accessoire getAccessoireFromId(String idAccessoire) {
        Accessoire a = null;
        String sqlString = "SELECT idaccessoire, nomaccessoire, prixaccessoire, photoaccessoire, descriptionaccessoire FROM accessoire WHERE idaccessoire = ?;";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setInt(1, Integer.parseInt(idAccessoire));
            try (ResultSet rset = pstat.executeQuery()) {
                if (rset.next()) {
                    a = new Accessoire(rset.getInt(1), rset.getString(2), rset.getDouble(3), rset.getString(4),
                            rset.getString(5));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return a;
    }

    /** 
     * Récupère un accessoire avec le nom de cet accessoire et met ça dans un objet accessoire
     * @param nomAccessoire Le nom de l'accessoire
     * @return Retourne un Accessoire
     */
    public Accessoire getAccessoiresIdFromName(String nomAccessoire) {
        Accessoire a = null;
        String sqlString = "SELECT idaccessoire, nomaccessoire, prixaccessoire, photoaccessoire, descriptionaccessoire FROM accessoire WHERE nomaccessoire = ?;";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setString(1, nomAccessoire);
            try (ResultSet rset = pstat.executeQuery()) {
                if (rset.next()) {
                    a = new Accessoire(rset.getInt(1), rset.getString(2), rset.getDouble(3), rset.getString(4),
                            rset.getString(5));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return a;
    }

    /** 
     * Vérifie si les données existent dans la table
     * @return Retourne le nombre de lignes présent dans la table accessoire
     */
    public int checkDonneesExiste() {
        String sqlString = "SELECT count(idaccessoire) from accessoire";
        int result = -1;
        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            try (ResultSet rset = pstat.executeQuery()) {
                if (rset.next()) {
                    result = rset.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    // S'il n'y a pas de données, cette méthode les insère au démarrage
    public void insertDonneesAccessoire() {
        String sqlString = "INSERT INTO accessoire (nomaccessoire, prixaccessoire, photoaccessoire) VALUES\r\n" +
                "('Casque', 5, 'https://www.lnlm.fr/Files/134538/Img/18/A-03932-KASK-URBAN-LIFESTYLE-brillant_1x1200.jpg'),\r\n"
                +
                "('Porte-bagage', 7, 'https://m.media-amazon.com/images/I/81A0wuIcyJL._AC_SX679_.jpg'),\r\n" +
                "('Siege enfant', 10, 'https://www.lidl.be/assets/gcp34203ea5c8b64021b259bd28117764bc.jpeg'),\r\n" +
                "('Cadenas', 3, 'https://contents.mediadecathlon.com/p1250932/k$4502b761aaee4a2f8039c06ebce5a371/sq/6191b046-7c3b-44e0-9510-0828d1d88824.jpg?format=auto&f=800x0');";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
