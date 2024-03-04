package be.velovista.Model.DAL.DAO.Abonnement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import be.velovista.Model.BL.Abonnement;
import be.velovista.Model.DAL.DBConnection;

public class AbonnementDAO implements IAbonnementDAO{
  
  public AbonnementDAO(){
    DBConnection.getConnection();
  }

  public void createAbonnementTable(){
    String sqlString = "CREATE TABLE IF NOT EXISTS Abonnement (idabonnement SERIAL PRIMARY KEY, nomabonnement VARCHAR(50), descriptionabonnement VARCHAR(200));";

      try(Statement stat =  DBConnection.conn.createStatement()){
        stat.executeUpdate(sqlString);
      }
      catch(SQLException e){
        System.out.println(e.getMessage());
      }
  }

  public ArrayList<String> getAbonnements(){
    String sqlString = "SELECT nomabonnement FROM Abonnement;";
    ArrayList<String> listeNomAbo = new ArrayList<>();

    try(Statement stat = DBConnection.conn.createStatement()){
      try(ResultSet rset = stat.executeQuery(sqlString)){
        while(rset.next()){
          listeNomAbo.add(rset.getString(1));
        }
      }
      catch(SQLException e){
        System.out.println(e.getMessage());
      }
    }
    catch(SQLException e){
      System.out.println(e.getMessage());
    }
    return listeNomAbo;
  }

  public ArrayList<Abonnement> getListeAbonnements(){
    String sqlString = "SELECT idabonnement, nomabonnement, descriptionabonnement FROM Abonnement;";
    ArrayList<Abonnement> listeAbo = new ArrayList<>();

    try(Statement stat = DBConnection.conn.createStatement()){
      try(ResultSet rset = stat.executeQuery(sqlString)){
        while(rset.next()){
          Abonnement a = new Abonnement(rset.getInt(1), rset.getString(2), rset.getString(3));
          listeAbo.add(a);
        }
      }
      catch(SQLException e){
        System.out.println(e.getMessage());
      }
    }
    catch(SQLException e){
      System.out.println(e.getMessage());
    }
    return listeAbo;
  }

  public int getAbonnementId(String nomAbo){
    String sqlString = "SELECT idabonnement FROM Abonnement WHERE nomabonnement = ?;";
    int idAbo = -1;

    try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
      pstat.setString(1, nomAbo);
      try(ResultSet rset = pstat.executeQuery()){
        if(rset.next()){
          idAbo = rset.getInt(1);
        }
      }
      catch(SQLException e){
        System.out.println(e.getMessage());
      }
    }
    catch(SQLException e){
      System.out.println(e.getMessage());
    }
    return idAbo;
  }

  public int checkDonneesExiste(){
    String sqlString = "SELECT count(idabonnement) from abonnement";
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

public void insertDonneesAbonnement(){
    String sqlString = "INSERT INTO abonnement (nomabonnement, descriptionabonnement) VALUES\r\n" +
            "('Journalier', 'Profitez d''un accès complet pour une journée sans réduction.'),\r\n" +
            "('Hebdomadaire', 'Abonnez-vous pour une semaine et payez seulement 6 jours au lieu de 7 et profitant ainsi d''une journée offerte.'),\r\n" +
            "('Mensuel', 'Optez pour un abonnement mensuel avec un tarif avantageux équivalent à 27 jours de service au prix de 30.'),\r\n" +
            "('Semestriel', 'Bénéficiez d''une offre semestrielle calculée sur une base de 27 jours par mois pendant 6 mois pour plus d''économies.'),\r\n" +
            "('Trimestriel', 'Souscrivez pour un trimestre avec une tarification basée sur 27 jours par mois multipliée par 3 pour un compromis idéal entre flexibilité et économie.'),\r\n" +
            "('Annuel', 'L''abonnement annuel vous offre le meilleur tarif avec un calcul basé sur 25 jours par mois sur 12 mois maximisant vos économies sur l''année.');";

    try(Statement stat = DBConnection.conn.createStatement()){
        stat.executeUpdate(sqlString);
    }
    catch(SQLException e){
        System.out.println(e.getMessage());
    }

}
}
