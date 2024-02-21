package be.velovista.Model.DAL.DAO.Velo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import be.velovista.Model.BL.Velo;
import be.velovista.Model.BL.VeloClassique;
import be.velovista.Model.BL.VeloElectrique;
import be.velovista.Model.BL.VeloEnfant;
import be.velovista.Model.DAL.DBConnection;

public class VeloDAO implements IVeloDAO{
    
    public VeloDAO(){
        DBConnection.getConnection();
    }

    public void createVeloTable(){
        String sqlString = "CREATE TABLE IF NOT EXISTS velo (idvelo SERIAL PRIMARY KEY, numeroserie VARCHAR(50), typevelo VARCHAR(20), anneefabrication INTEGER, marque VARCHAR(50), taille INTEGER, estdisponible BOOL, prix DECIMAL(10,2), photo VARCHAR(200), vitesses INTEGER, autonomie INTEGER, couleur VARCHAR(50));";

        try(PreparedStatement pstat =  DBConnection.conn.prepareStatement(sqlString)){
            pstat.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public int getPrixClassique(){
        String sqlString = "select prix from velo where typevelo = 'Classique' limit 1;";
        int result = 0;

        try(PreparedStatement pstat =  DBConnection.conn.prepareStatement(sqlString)){
            ResultSet rset = pstat.executeQuery();
            while(rset.next())
            result = rset.getInt(1);
            return result;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Erreur de données");
        }
    }
    public int getPrixElectrique(){
        String sqlString = "select prix from velo where typevelo = 'Electrique' limit 1;";
        int result = 0;

        try(PreparedStatement pstat =  DBConnection.conn.prepareStatement(sqlString)){
            ResultSet rset = pstat.executeQuery();
            while(rset.next())
            result = rset.getInt(1);
            return result;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Erreur de données");
        }
    }
    public int getPrixEnfant(){
        String sqlString = "select prix from velo where typevelo = 'Enfant' limit 1;";
        int result = 0;

        try(PreparedStatement pstat =  DBConnection.conn.prepareStatement(sqlString)){
            ResultSet rset = pstat.executeQuery();
            while(rset.next())
            result = rset.getInt(1);
            return result;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Erreur de données");
        }
    }

    public ArrayList<Integer> getVitesses(){
        String sqlString = "select distinct vitesse from velo where typevelo = 'Classique';";
        ArrayList<Integer> result = new ArrayList<>();

        try(PreparedStatement pstat =  DBConnection.conn.prepareStatement(sqlString)){
            ResultSet rset = pstat.executeQuery();
            while(rset.next())
            result.add(rset.getInt(1));
            return result;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Erreur de données");
        }
    }

    public ArrayList<Velo> getListeVeloClass(){
        String sqlString = "select * from velo where typevelo = 'Classique'";
        ArrayList<Velo> result = new ArrayList<>();
        VeloClassique vc;

        try(PreparedStatement pstat =  DBConnection.conn.prepareStatement(sqlString)){
            ResultSet rset = pstat.executeQuery();
            while(rset.next()){
                vc = new VeloClassique(rset.getInt(10), rset.getInt(1), rset.getString(2), rset.getString(5), rset.getString(3), rset.getBoolean(7), rset.getString(12), rset.getInt(6), rset.getInt(4), rset.getDouble(8), rset.getString(9));
                result.add(vc);
            }
            return result;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Erreur de données");
        }
    }

    public ArrayList<Velo> getListeVeloElec(){
        String sqlString = "select * from velo where typevelo = 'Electrique'";
        ArrayList<Velo> result = new ArrayList<>();
        VeloElectrique vc;

        try(PreparedStatement pstat =  DBConnection.conn.prepareStatement(sqlString)){
            ResultSet rset = pstat.executeQuery();
            while(rset.next()){
                vc = new VeloElectrique(rset.getInt(11), rset.getInt(1), rset.getString(2), rset.getString(5), rset.getString(3), rset.getBoolean(7), rset.getString(12), rset.getInt(6), rset.getInt(4), rset.getDouble(8), rset.getString(9));
                result.add(vc);
            }
            return result;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Erreur de données");
        }
    }

    public ArrayList<Velo> getListeVeloEnfant(){
        String sqlString = "select * from velo where typevelo = 'Enfant'";
        ArrayList<Velo> result = new ArrayList<>();
        VeloEnfant vc;

        try(PreparedStatement pstat =  DBConnection.conn.prepareStatement(sqlString)){
            ResultSet rset = pstat.executeQuery();
            while(rset.next()){
                vc = new VeloEnfant(rset.getInt(1), rset.getString(2), rset.getString(5), rset.getString(3), rset.getBoolean(7), rset.getString(12), rset.getInt(6), rset.getInt(4), rset.getDouble(8), rset.getString(9));
                result.add(vc);
            }
            return result;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Erreur de données");
        }
    }

    public Velo getVelo(int id){
        String sqlString = "SELECT * FROM velo WHERE idvelo = ?";
        Velo v = null;

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setInt(1, id);
            ResultSet rset = pstat.executeQuery();
                while(rset.next()){
                    String type = rset.getString(3);
                    switch (type){
                        case "Classique":
                            v = new VeloClassique(rset.getInt(10), rset.getInt(1), rset.getString(2), rset.getString(5), rset.getString(3), rset.getBoolean(7), rset.getString(12), rset.getInt(6), rset.getInt(4), rset.getDouble(8), rset.getString(9));
                            break;
                        case "Electrique":
                            v = new VeloElectrique(rset.getInt(11), rset.getInt(1), rset.getString(2), rset.getString(5), rset.getString(3), rset.getBoolean(7), rset.getString(12), rset.getInt(6), rset.getInt(4), rset.getDouble(8), rset.getString(9));
                            break;
                        case "Enfant":
                            v = new VeloEnfant(rset.getInt(1), rset.getString(5), rset.getString(2), rset.getString(3), rset.getBoolean(7), rset.getString(12), rset.getInt(6), rset.getInt(4), rset.getDouble(8), rset.getString(9));
                            break;

                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return v;
    }

    public void updateVeloToDispo(int idVelo){
        String sqString ="UPDATE velo SET estdisponible = true WHERE idvelo = ?";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqString)){
            pstat.setInt(1, idVelo);
            pstat.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void updateVeloToIndispo(int idVelo){
        String sqString ="UPDATE velo SET estdisponible = false WHERE idvelo = ?";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqString)){
            pstat.setInt(1, idVelo);
            pstat.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}

