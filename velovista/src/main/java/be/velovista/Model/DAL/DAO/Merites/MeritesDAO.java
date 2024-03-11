package be.velovista.Model.DAL.DAO.Merites;

import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import be.velovista.Model.DAL.DBConnection;

public class MeritesDAO implements IMeritesDAO {
    
    public MeritesDAO() {
        DBConnection.getConnection();
    }

    //Crée la table merites
    public void createMeritesTable() {
        String sqlString = "CREATE TABLE IF NOT EXISTS Merite (idMerite SERIAL PRIMARY KEY, NomMerite VARCHAR(50), DescriptionMerite VARCHAR(200), Critere INTEGER, levelmerite INTEGER);";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** 
     * Récupère le critère d'un mérite avec le level du mérite
     * @param lvlActuel Le level auquel l'utilisateur se trouve +1 (dans la méthode)
     * @return Retourne le critère du mérite ou l'utilisateur se trouve +1 (dans la méthode)
     */
    public int getCritereProchainPalier(int lvlActuel) {
        int prochainLvl = lvlActuel + 1;
        String sqlString = "SELECT critere FROM merite WHERE levelmerite = ?";
        int critere = -1;
        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setInt(1, prochainLvl);
            try (ResultSet rset = pstat.executeQuery()) {
                if (rset.next()) {
                    critere = rset.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return critere;
    }

    /** 
     * Récupère la liste des critères de chaque mérite et les trie du plus petit au plus grand
     * @return Retourne une ArrayList<Integer> de chaque critère
     */
    public ArrayList<Integer> getListeCriteres() {
        String sqlString = "select critere from merite order by idmerite";
        ArrayList<Integer> listeCriteres = new ArrayList<>();

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            try (ResultSet rset = pstat.executeQuery()) {
                while (rset.next()) {
                    listeCriteres.add(rset.getInt(1));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listeCriteres;
    }

    /** 
     * Vérifie si les données existe déjà dans la table mérite
     * @return Retourne le nombre de lignes existants dans la table merite
     */
    public int checkDonneesExiste() {
        String sqlString = "SELECT count(idmerite) from merite";
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

    // Insère les données quand c'est pas encore fait au lancement du programme
    public void insertDonneesMerite() {
        String sqlString = "INSERT INTO merite (nommerite, descriptionmerite, critere, levelmerite) VALUES\r\n" +
                "('Fèr', 'Le plus bas des mérites. Un premier pas vers la grandeur', 50, 1),\r\n" +
                "('Bronze', 'Reconnaissance des efforts initiaux. Un signe de progrès', 100, 2),\r\n" +
                "('Argent', 'Marque un engagement sérieux envers le cyclisme', 250, 3),\r\n" +
                "('Or', 'Un jalon important célébrant un dévouement notable', 500, 4),\r\n" +
                "('Platinum', 'Pour les cyclistes dédiés atteignant des sommets d''excellence', 750, 5),\r\n" +
                "('Emeraude', 'Distinction d''élite. Pour ceux qui intègrent pleinement le cyclisme dans leur vie', 1000, 6),\r\n"
                +
                "('Diamant', 'Le summum des réalisations. Réservé aux plus passionnés et engagés', 1500, 7);";

        try (Statement stat = DBConnection.conn.createStatement()) {
            stat.executeUpdate(sqlString);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
