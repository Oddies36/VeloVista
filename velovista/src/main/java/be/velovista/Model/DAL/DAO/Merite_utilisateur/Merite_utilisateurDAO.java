package be.velovista.Model.DAL.DAO.Merite_utilisateur;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import be.velovista.Model.BL.Merites;
import be.velovista.Model.DAL.DBConnection;

public class Merite_utilisateurDAO implements IMerite_utilisateurDAO {

    public Merite_utilisateurDAO() {
        DBConnection.getConnection();
    }

    // Crée la table merites_utilisateur
    public void createMeritesTable() {
        String sqlString = "CREATE TABLE IF NOT EXISTS Merite_Utilisateur (id_merite int, id_utilisateur int, dateobtenu DATE, PRIMARY KEY (id_merite, id_utilisateur), FOREIGN KEY (id_merite) REFERENCES merite(idmerite), FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(iduser))";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** 
     * Prend la liste des mérites qui n'ont pas encore été écrit dans la table merite_utilisateur avec l'ID de l'utilisateur
     * @param idUser L'ID de l'utilisateur
     * @return Retourne une ArrayList<Merites> avec les mérites qui n'ont pas encore été obtenu par l'utilisateur
     */
    public ArrayList<Merites> getListeMeritesNonObtenu(int idUser) {
        String sqlString = "SELECT mer.idmerite, mer.nommerite, mer.descriptionmerite, mer.critere FROM merite AS mer LEFT JOIN merite_utilisateur AS merut ON mer.idmerite = merut.id_merite AND merut.id_utilisateur = ? WHERE merut.id_merite IS NULL;";
        ArrayList<Merites> listeMeritesNonObtenus = new ArrayList<>();

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setInt(1, idUser);
            try (ResultSet rset = pstat.executeQuery()) {
                while (rset.next()) {
                    Merites m = new Merites(rset.getInt(1), rset.getString(2), rset.getString(3), rset.getInt(4));
                    listeMeritesNonObtenus.add(m);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listeMeritesNonObtenus;
    }

    /** 
     * Prend la liste des mérites qui ont été obtenu avec l'ID de l'utilisateur
     * @param idUser L'ID de l'utilisateur
     * @return Retourne une ArrayList<Merites> avec les mérites qui ont déjà été obtenu par l'utilisateur
     */
    public ArrayList<Merites> getListeMeritesObtenu(int idUser) {
        String sqlString = "SELECT mer.idmerite, mer.nommerite, mer.descriptionmerite, mer.critere, merut.dateobtenu FROM merite AS mer JOIN merite_utilisateur AS merut ON mer.idmerite = merut.id_merite WHERE merut.id_utilisateur = ?;";
        ArrayList<Merites> listeMeritesObtenus = new ArrayList<>();

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setInt(1, idUser);
            try (ResultSet rset = pstat.executeQuery()) {
                while (rset.next()) {
                    Merites m = new Merites(rset.getInt(1), rset.getString(2), rset.getString(3), rset.getInt(4),
                            rset.getDate(5).toLocalDate());
                    listeMeritesObtenus.add(m);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listeMeritesObtenus;
    }

    /** 
     * Prend la liste du dernier mérite obtenu. Les métites ont un niveau et on prend le niveau le plus elevé. On utilise l'ID de l'utilisateur
     * @param idUser L'ID de l'utilisateur
     * @return Retourne le dernier mérite obtenu par l'utilisateur
     */
    public int getDernierMeriteObtenu(int idUser) {
        String sqlString = "select MAX(mer.levelmerite) from merite as mer left join merite_utilisateur as merut on mer.idmerite = merut.id_merite WHERE merut.id_utilisateur = ?";
        int result = 0;

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setInt(1, idUser);
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

    /** 
     * Met à jour la liste des mérites obtenus. Ajoute une ligne quand un mérite a été obtenu
     * @param meriteObtenu L'ID du mérite qui a été obtenu
     * @param idUser L'ID de l'utilisateur
     */
    public void updateMeritesObtenus(int meriteObtenu, int idUser) {
        LocalDate datenow = LocalDate.now();
        String sqlString = "INSERT INTO merite_utilisateur (id_merite, id_utilisateur, dateobtenu) VALUES ((select idmerite from merite where levelmerite = ?), ?, ?)";

        try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
            pstat.setInt(1, meriteObtenu);
            pstat.setInt(2, idUser);
            pstat.setObject(3, datenow);
            pstat.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Récupère le critère d'un mérite avec le level du mérite
    // public int getCritereProchainPalier(int lvlActuel){
    // int prochainLvl = lvlActuel + 1;
    // String sqlString = "SELECT critere FROM merite WHERE levelmerite = ?";
    // int critere = -1;
    // try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
    // pstat.setInt(1, prochainLvl);
    // try(ResultSet rset = pstat.executeQuery()){
    // if(rset.next()){
    // critere = rset.getInt(1);
    // }
    // }
    // catch(SQLException e){
    // System.out.println(e.getMessage());
    // }
    // }
    // catch(SQLException e){
    // System.out.println(e.getMessage());
    // }
    // return critere;
    // }

    // Récupère la liste des critères de chaque mérite et les trie du plus petit au
    // plus grand
    // public ArrayList<Integer> getListeCriteres(){
    // String sqlString = "select critere from merite order by idmerite";
    // ArrayList<Integer> listeCriteres = new ArrayList<>();

    // try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
    // try(ResultSet rset = pstat.executeQuery()){
    // while(rset.next()){
    // listeCriteres.add(rset.getInt(1));
    // }
    // }
    // catch(SQLException e){
    // System.out.println(e.getMessage());
    // }
    // }
    // catch(SQLException e){
    // System.out.println(e.getMessage());
    // }
    // return listeCriteres;
    // }
}
