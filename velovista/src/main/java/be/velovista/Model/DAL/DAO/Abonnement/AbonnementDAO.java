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
    String sqlString = "CREATE TABLE IF NOT EXISTS Abonnement (idabonnement SERIAL PRIMARY KEY, nomabonnement VARCHAR(50));";

      try(PreparedStatement pstat =  DBConnection.conn.prepareStatement(sqlString)){
        pstat.executeUpdate();
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
    String sqlString = "SELECT idabonnement, nomabonnement FROM Abonnement;";
    ArrayList<Abonnement> listeAbo = new ArrayList<>();

    try(Statement stat = DBConnection.conn.createStatement()){
      try(ResultSet rset = stat.executeQuery(sqlString)){
        while(rset.next()){
          Abonnement a = new Abonnement(rset.getInt(1), rset.getString(2));
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
}
