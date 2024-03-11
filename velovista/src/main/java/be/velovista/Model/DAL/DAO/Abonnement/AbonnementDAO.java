package be.velovista.Model.DAL.DAO.Abonnement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import be.velovista.Model.BL.Abonnement;
import be.velovista.Model.DAL.DBConnection;

public class AbonnementDAO implements IAbonnementDAO {

  public AbonnementDAO() {
    DBConnection.getConnection();
  }

  // Crée la table abonnement
  public void createAbonnementTable() {
    String sqlString = "CREATE TABLE IF NOT EXISTS Abonnement (idabonnement SERIAL PRIMARY KEY, nomabonnement VARCHAR(50), descriptionabonnement VARCHAR(200));";

    try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
      pstat.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /** 
   * Récupère la liste des abonnements et crée un objet abonnement pour chaque retour
   * @return Retourne une ArrayList<Abonnement> avec tout les abonnements dans la DB
   */
  public ArrayList<Abonnement> getListeAbonnements() {
    String sqlString = "SELECT idabonnement, nomabonnement, descriptionabonnement FROM Abonnement;";
    ArrayList<Abonnement> listeAbo = new ArrayList<>();
    
    try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
      try (ResultSet rset = pstat.executeQuery()) {
        while (rset.next()) {
          Abonnement a = new Abonnement(rset.getInt(1), rset.getString(2), rset.getString(3));
          listeAbo.add(a);
        }
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return listeAbo;
  }
  
  /** 
   * Récupère l'ID de l'abonnement par le nom
   * @param nomAbo Le nom de l'abonnement
   * @return Retourne l'ID de l'abonnement
   */
  public int getAbonnementId(String nomAbo) {
    String sqlString = "SELECT idabonnement FROM Abonnement WHERE nomabonnement = ?;";
    int idAbo = -1;
    
    try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
      pstat.setString(1, nomAbo);
      try (ResultSet rset = pstat.executeQuery()) {
        if (rset.next()) {
          idAbo = rset.getInt(1);
        }
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return idAbo;
  }

  
  /** 
   * Vérifie si une donnée existe dans la table abonnement en retournant le total des lignes
   * @return Retourne le nombre de lignes existant dans la table abonnement
   */
  public int checkDonneesExiste() {
    String sqlString = "SELECT count(idabonnement) FROM abonnement";
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

  // Va inserer les données de base dans la base de données
  public void insertDonneesAbonnement() {
    String sqlString = "INSERT INTO abonnement (nomabonnement, descriptionabonnement) VALUES\r\n" +
    "('Journalier', 'Profitez d''un accès complet pour une journée sans réduction.'),\r\n" +
    "('Hebdomadaire', 'Abonnez-vous pour une semaine et payez seulement 6 jours au lieu de 7 et profitant ainsi d''une journée offerte.'),\r\n"
    +
    "('Mensuel', 'Optez pour un abonnement mensuel avec un tarif avantageux équivalent à 27 jours de service au prix de 30.'),\r\n"
    +
    "('Semestriel', 'Bénéficiez d''une offre semestrielle calculée sur une base de 27 jours par mois pendant 6 mois pour plus d''économies.'),\r\n"
    +
    "('Trimestriel', 'Souscrivez pour un trimestre avec une tarification basée sur 27 jours par mois multipliée par 3 pour un compromis idéal entre flexibilité et économie.'),\r\n"
    +
    "('Annuel', 'L''abonnement annuel vous offre le meilleur tarif avec un calcul basé sur 25 jours par mois sur 12 mois maximisant vos économies sur l''année.');";
    
    try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
      pstat.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // Récupère la liste des noms des abonnements
  // public ArrayList<String> getAbonnements() {
  //   String sqlString = "SELECT nomabonnement FROM Abonnement;";
  //   ArrayList<String> listeNomAbo = new ArrayList<>();
  
  //   try (PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)) {
  //     try (ResultSet rset = pstat.executeQuery()) {
  //       while (rset.next()) {
  //         listeNomAbo.add(rset.getString(1));
  //       }
  //     } catch (SQLException e) {
  //       System.out.println(e.getMessage());
  //     }
  //   } catch (SQLException e) {
  //     System.out.println(e.getMessage());
  //   }
  //   return listeNomAbo;
  // }
}
