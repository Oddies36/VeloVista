package be.velovista.Model.DAL.DAO.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import be.velovista.Model.DAL.DBConnection;

public class LocationDAO implements ILocationDAO {

  public LocationDAO() {
    DBConnection.getConnection();
  }

  // Crée la table locationvelo
  public void createLocationTable() {
    String sqlString = "CREATE TABLE IF NOT EXISTS LocationVelo (id_location_velo SERIAL, id_abonnementutilisateur integer, prixtotal DECIMAL(10,2), DateDebut DATE, DateFin DATE, PRIMARY KEY (id_location_velo),FOREIGN KEY (id_abonnementutilisateur) REFERENCES abonnementutilisateur(id_abonnement_utilisateur))";

    try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
      pstat.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /** 
   * Insère une location et retourne l'ID de cette location
   * @param idAbonnementUtilisatuer L'ID de l'abonnement_utilisateur
   * @param prixTotal Le prix total de la location
   * @param dateDebut La date de début de la location
   * @param dateFin La date de fin de la location
   * @return Retourne l'ID de la ligne qui vient d'être crée
   */
  public int insertLocation(int idAbonnementUtilisatuer, double prixTotal, LocalDate dateDebut, LocalDate dateFin) {
    String sqlString = "INSERT INTO locationvelo (id_abonnementutilisateur, prixtotal, datedebut, datefin) VALUES (?, ?, ?, ?) RETURNING id_location_velo;";
    int idLoc = -1;

    try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
      pstat.setInt(1, idAbonnementUtilisatuer);
      pstat.setDouble(2, prixTotal);
      pstat.setObject(3, dateDebut);
      pstat.setObject(4, dateFin);
      try (ResultSet rset = pstat.executeQuery()) {
        if (rset.next()) {
          idLoc = rset.getInt(1);
        }
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return idLoc;
  }

  /** 
   * Prend la date de début de la location qui est actuellement actif avec l'ID de l'utilisateur
   * @param idUser L'ID de l'utilisateur
   * @return Retourne la date de début de la location
   */
  public LocalDate getDateDebutCurrentLocation(int idUser) {
    String sqlString = "select datedebut from locationvelo as loc inner join abonnementutilisateur as abouser ON abouser.id_abonnement_utilisateur = loc.id_abonnementutilisateur where abouser.isactif = true AND abouser.id_utilisateur = ?";
    String dateDebutString = "";
    LocalDate dateDebut = null;

    try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
      pstat.setInt(1, idUser);
      try (ResultSet rset = pstat.executeQuery()) {
        if (rset.next()) {
          dateDebutString = rset.getString(1);
          dateDebut = LocalDate.parse(dateDebutString);
        }
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return dateDebut;
  }

  /** 
   * Prend les informations des locations en insère tout dans une liste de String. On prend l'ID de l'utilisateur comme paramètre
   * @param idUser L'ID de l'utilisateur
   * @return Retourne un ArrayList<String> qui contient plusieurs lignes avec les informations de la location (photo, marque, numSerie, prixtotal, datedebut, datefin) dans cet ordre
   */
  public ArrayList<String> getLocationsById(int idUser) {
    String sqlString = "select vel.photo, vel.marque, vel.numeroserie, loc.prixtotal, loc.datedebut, loc.datefin from locationvelo as loc inner join abonnementutilisateur as abouti on loc.id_abonnementutilisateur = abouti.id_abonnement_utilisateur inner join velo as vel on abouti.id_velo = vel.idvelo where abouti.id_utilisateur = ?";
    ArrayList<String> listeLocations = new ArrayList<>();

    try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
      pstat.setInt(1, idUser);
      try (ResultSet rset = pstat.executeQuery()) {
        while (rset.next()) {
          String ligneLocation = rset.getString(1) + "," +
              rset.getString(2) + "," +
              rset.getString(3) + "," +
              rset.getDouble(4) + "," +
              rset.getDate(5) + "," +
              rset.getDate(6);
          listeLocations.add(ligneLocation);
        }
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return listeLocations;
  }

  // Récupère une location avec l'email de l'utilisateur
  // A enlever?
  // public Location getLocationByEmail(String email){
  // String sqString = "SELECT id_location_velo, id_abonnement, id_accessoire FROM
  // LocationVelo";
  // Location l = null;

  // try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqString)) {
  // try(ResultSet rset = pstat.executeQuery()){
  // if(rset.next()){
  // l = new Location(rset.getInt(1), null, null, null, email, sqString, 0);
  // }
  // }
  // catch(SQLException e){
  // System.out.println(e.getMessage());
  // }
  // }
  // catch(SQLException e){
  // System.out.println(e.getMessage());
  // }
  // return l;
  // }

  // Never used?
  // public ArrayList<String> getLocationInfo(String email){
  // ArrayList<String> result = new ArrayList<>();
  // String sqlString = "SELECT locv.id_location_velo, usr.iduser, v.idvelo,
  // locv.datedebut, locv.datefin, locv.prixtotal FROM locationvelo AS locv INNER
  // JOIN abonnementutilisateur AS abou ON locv.id_abonnementutilisateur =
  // abou.id_abonnement_utilisateur INNER JOIN utilisateur AS usr ON usr.iduser =
  // abou.id_utilisateur INNER JOIN velo AS v ON v.idvelo = abou.id_velo WHERE
  // usr.email = ?";

  // try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
  // pstat.setString(1, email);
  // try(ResultSet rset = pstat.executeQuery()){
  // while(rset.next()){
  // result.add(Integer.toString(rset.getInt(1)));
  // result.add(Integer.toString(rset.getInt(2)));
  // result.add(Integer.toString(rset.getInt(3)));
  // result.add(rset.getString(4));
  // result.add(rset.getString(5));
  // result.add(Double.toString(rset.getDouble(6)));
  // }
  // }
  // catch(SQLException e){
  // System.out.println(e.getMessage());
  // }
  // }
  // catch(SQLException e){
  // System.out.println(e.getMessage());
  // }
  // return result;
  // }

  // not used?
  // public int checkCurrentLocation(LocalDate dateDebut, LocalDate dateFin){
  // int count = -1;
  // LocalDate now = LocalDate.now();
  // String sqlString = "SELECT count(id_location_velo) FROM locationvelo WHERE ?
  // BETWEEN ? AND ?;";

  // try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
  // pstat.setObject(1, now);
  // pstat.setObject(2, dateDebut);
  // pstat.setObject(3, dateFin);
  // try(ResultSet rset = pstat.executeQuery()){
  // if(rset.next()){
  // count = rset.getInt(1);
  // }
  // }
  // catch(SQLException e){
  // System.out.println(e.getMessage());
  // }
  // }
  // catch(SQLException e){
  // System.out.println(e.getMessage());
  // }
  // return count;
  // }
}
