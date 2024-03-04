package be.velovista.Model.DAL.DAO.Merites;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import be.velovista.Model.DAL.DBConnection;

public class MeritesDAO implements IMeritesDAO {
    public MeritesDAO(){
        DBConnection.getConnection();
    }

    public void createMeritesTable(){
                String sqlString = "CREATE TABLE IF NOT EXISTS Merite (idMerite SERIAL PRIMARY KEY, NomMerite VARCHAR(50), DescriptionMerite VARCHAR(200), Critere INTEGER, levelmerite INTEGER);";

        try(Statement stat = DBConnection.conn.createStatement()){
            stat.executeUpdate(sqlString);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

        public int checkDonneesExiste(){
        String sqlString = "SELECT count(idmerite) from merite";
        int result = -1;
        try(Statement stat = DBConnection.conn.createStatement()){
            try(ResultSet rset = stat.executeQuery(sqlString)){
                if(rset.next()){
                    result = rset.getInt(1);
                }
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    public void insertDonneesMerite(){
        String sqlString = "INSERT INTO merite (nommerite, descriptionmerite, critere, levelmerite) VALUES\r\n" +
                        "('Fèr', 'Le plus bas des mérites. Un premier pas vers la grandeur', 50, 1),\r\n" +
                        "('Bronze', 'Reconnaissance des efforts initiaux. Un signe de progrès', 100, 2),\r\n" +
                        "('Argent', 'Marque un engagement sérieux envers le cyclisme', 250, 3),\r\n" +
                        "('Or', 'Un jalon important célébrant un dévouement notable', 500, 4),\r\n" +
                        "('Platinum', 'Pour les cyclistes dédiés atteignant des sommets d''excellence', 750, 5),\r\n" +
                        "('Emeraude', 'Distinction d''élite. Pour ceux qui intègrent pleinement le cyclisme dans leur vie', 1000, 6),\r\n" +
                        "('Diamant', 'Le summum des réalisations. Réservé aux plus passionnés et engagés', 1500, 7);";

        try(Statement stat = DBConnection.conn.createStatement()){
            stat.executeUpdate(sqlString);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
