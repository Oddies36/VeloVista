package be.velovista.Model.DAL.DAO.Velo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        try(Statement stat =  DBConnection.conn.createStatement()){
            stat.executeUpdate(sqlString);
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
        String sqlString = "select * from velo where typevelo = 'Classique' ORDER BY idvelo ASC";
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
        String sqlString = "select * from velo where typevelo = 'Electrique' ORDER BY idvelo ASC";
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
        String sqlString = "select * from velo where typevelo = 'Enfant' ORDER BY idvelo ASC";
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
                            v = new VeloEnfant(rset.getInt(1), rset.getString(2), rset.getString(5), rset.getString(3), rset.getBoolean(7), rset.getString(12), rset.getInt(6), rset.getInt(4), rset.getDouble(8), rset.getString(9));
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

    public int checkDonneesExiste(){
        String sqlString = "SELECT count(idvelo) from velo";
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
    public void insertDonneesClassique(){
        String sqlString = "INSERT INTO velo (numeroserie, typevelo, anneefabrication, marque, taille, estdisponible, prix, photo, vitesses, couleur) VALUES \r\n" +
                        "('e25f6def', 'Classique', 2021, 'Commuter 7', 56, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-Commuter_7_noir.png?raw=true', 1, 'Noir'),\r\n" +
                        "('06b2156c', 'Classique', 2019, 'Commuter 9', 58, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-Commuter_9_gris.png?raw=true', 7, 'Gris'),\r\n" +
                        "('51b398e4', 'Classique', 2012, 'Pathlite 7', 48, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-Pathlite_7_blanc.png?raw=true', 14, 'Blanc'),\r\n" +
                        "('b70603fa', 'Classique', 2016, 'Pathlite 7', 50, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-Pathlite_7_bleu.png?raw=true', 21, 'Bleu'),\r\n" +
                        "('130c52c9', 'Classique', 2014, 'Pathlite 7', 56, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-Pathlite_7_gris.png?raw=true', 10, 'Gris'),\r\n" +
                        "('b9ea25d9', 'Classique', 2023, 'Pathlite 7', 56, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-Pathlite_7_noir.png?raw=true', 14, 'Noir'),\r\n" +
                        "('548057ae', 'Classique', 2022, 'Pathlite 7', 56, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-Pathlite_7_vert.png?raw=true', 1, 'Vert'),\r\n" +
                        "('7f418ee9', 'Classique', 2021, 'Roadlite 4', 50, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-Roadlite_4_bleu.png?raw=true', 7, 'Bleu'),\r\n" +
                        "('b38eece3', 'Classique', 2014, 'Roadlite 6', 56, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-Roadlite_6_bleu.png?raw=true', 10, 'Bleu'),\r\n" +
                        "('e0aaed3a', 'Classique', 2017, 'Roadlite 7', 52, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-Roadlite_7_gris.png?raw=true', 14, 'Gris'),\r\n" +
                        "('15cc710e', 'Classique', 2012, 'Commuter 5', 60, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-commuter_5_blanc.png?raw=true', 7, 'Blanc'),\r\n" +
                        "('c35cc9aa', 'Classique', 2015, 'Commuter 5', 54, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-commuter_5_jaune.png?raw=true', 7, 'Jaune'),\r\n" +
                        "('e2664c34', 'Classique', 2015, 'Pathlite 4', 58, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-pathlite_4_bleu.png?raw=true', 14, 'Bleu'),\r\n" +
                        "('59107fea', 'Classique', 2010, 'Pathlite 4', 50, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-pathlite_4_vert.png?raw=true', 14, 'Vert'),\r\n" +
                        "('3137262a', 'Classique', 2016, 'Pathlite 6', 52, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-pathlite_6_bleu.png?raw=true', 1, 'Bleu'),\r\n" +
                        "('befc6230', 'Classique', 2013, 'Pathlite 6', 50, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-pathlite_6_vert.png?raw=true', 10, 'Vert'),\r\n" +
                        "('8597cc45', 'Classique', 2019, 'Roadlite 5', 48, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-roadlite_5_rose.png?raw=true', 3, 'Rose'),\r\n" +
                        "('7b768170', 'Classique', 2022, 'Roadlite 6', 52, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-roadlite_6_gris.png?raw=true', 3, 'Gris'),\r\n" +
                        "('4dc77563', 'Classique', 2013, 'Roadlite 8', 54, TRUE, 5, 'https://github.com/Oddies36/photoVelo2/blob/main/class-roadlite_8_gris.png?raw=true', 10, 'Gris');";

        try(Statement stat = DBConnection.conn.createStatement()){
            stat.executeUpdate(sqlString);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public void insertDonneesElectrique(){
        String sqlString = "INSERT INTO velo (numeroserie, typevelo, anneefabrication, marque, taille, estdisponible, prix, photo, autonomie, couleur) VALUES \r\n" +
                        "('ab1a231b', 'Electrique', 2012, 'Pathlite ON 6 SUV', 50, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON_6_SUV_gris.png?raw=true', 50, 'Gris'), \r\n" +
                        "('46a41691', 'Electrique', 2010, 'Pathlite ON 7 SUV', 58, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON_7_SUV_bleu.png?raw=true', 65, 'Bleu'), \r\n" +
                        "('6f439889', 'Electrique', 2018, 'Pathlite ON 7 SUV', 48, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON_7_SUV_gris.png?raw=true', 50, 'Gris'), \r\n" +
                        "('64310748', 'Electrique', 2016, 'Pathlite ON 8 SUV', 52, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON_8_SUV_bleu.png?raw=true', 90, 'Bleu'), \r\n" +
                        "('429e33df', 'Electrique', 2011, 'Pathlite ON 9 SUV', 48, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON_9_SUV_vert.png?raw=true', 60, 'Vert'), \r\n" +
                        "('14f7c709', 'Electrique', 2022, 'Pathlite ON SL 4', 50, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON_SL_4_beige.png?raw=true', 80, 'Beige'), \r\n" +
                        "('562c9366', 'Electrique', 2015, 'Pathlite ON SL 4', 52, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON_SL_4_bleu.png?raw=true', 40, 'Bleu'), \r\n" +
                        "('45d2ec3b', 'Electrique', 2019, 'Pathlite ON SL 6', 52, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON_SL_6_beige.png?raw=true', 60, 'Beige'), \r\n" +
                        "('6b0904ea', 'Electrique', 2013, 'Pathlite ON SL 6', 48, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON_SL_6_bleu.png?raw=true', 60, 'Bleu'), \r\n" +
                        "('405c6a15', 'Electrique', 2017, 'Pathlite ON SL 8', 58, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON_SL_8_blanc.png?raw=true', 95, 'Blanc'), \r\n" +
                        "('d3f1882d', 'Electrique', 2021, 'Pathlite ON 4 SUV', 50, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON__4_SUV_blanc.png?raw=true', 100, 'Blanc'), \r\n" +
                        "('838a1c28', 'Electrique', 2011, 'Pathlite ON 4 SUV', 54, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON__4_SUV_vert.png?raw=true', 120, 'Vert'), \r\n" +
                        "('daecb5f3', 'Electrique', 2019, 'Pathlite ON 5 SUV', 56, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON__5_SUV_gris.png?raw=true', 100, 'Gris'), \r\n" +
                        "('38879209', 'Electrique', 2021, 'Pathlite ON 5 SUV', 58, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON__5_SUV_orange.png?raw=true', 60, 'Orange'), \r\n" +
                        "('ebb2287e', 'Electrique', 2016, 'Pathlite ON 6 SUV', 60, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-Pathlite_ON__6_SUV_bleu.png?raw=true', 110, 'Bleu'), \r\n" +
                        "('a335c06d', 'Electrique', 2020, 'Pathlite ON 8 SUV', 48, TRUE, 8, 'https://github.com/Oddies36/photoVelo2/blob/main/elec-pathlite_on_8_SUV_gris.png?raw=true', 50, 'Gris');";

        try(Statement stat = DBConnection.conn.createStatement()){
            stat.executeUpdate(sqlString);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public void insertDonneesEnfant(){
        String sqlString = "INSERT INTO velo (numeroserie, typevelo, anneefabrication, marque, taille, estdisponible, prix, photo, couleur) VALUES \r\n" +
                        "('ab1a231b', 'Enfant', 2012, 'Scool xtrix', 24, TRUE, 2, 'https://github.com/Oddies36/photoVelo2/blob/main/enf-Scool%20xtrix%20mauve.jpg?raw=true', 'Mauve'), \r\n" +
                        "('46a41691', 'Enfant', 2010, 'Scool xtrix', 28, TRUE, 2, 'https://github.com/Oddies36/photoVelo2/blob/main/enf-Scool%20xtrix%20noir.jpg?raw=true', 'Noir'), \r\n" +
                        "('6f439889', 'Enfant', 2018, 'Vermont Kapitan', 30, TRUE, 2, 'https://github.com/Oddies36/photoVelo2/blob/main/enf-Vermont%20Kapitan%20bleu.jpg?raw=true', 'Bleu'), \r\n" +
                        "('64310748', 'Enfant', 2016, 'Vermont', 22, TRUE, 2, 'https://github.com/Oddies36/photoVelo2/blob/main/enf-Vermont%20blanc.jpg?raw=true', 'Blanc'), \r\n" +
                        "('429e33df', 'Enfant', 2011, 'Vermont', 26, TRUE, 2, 'https://github.com/Oddies36/photoVelo2/blob/main/enf-Vermont%20bleu.jpg?raw=true', 'Bleu'), \r\n" +
                        "('14f7c709', 'Enfant', 2022, 'Cubie 160 RT', 35, TRUE, 2, 'https://github.com/Oddies36/photoVelo2/blob/main/enf-cubie%20160%20RT%20vert.jpg?raw=true', 'Vert'), \r\n" +
                        "('562c9366', 'Enfant', 2015, 'Winora', 25, TRUE, 2, 'https://github.com/Oddies36/photoVelo2/blob/main/enf-winora%20rage%20rouge.jpg?raw=true', 'Rouge');";

        try(Statement stat = DBConnection.conn.createStatement()){
            stat.executeUpdate(sqlString);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
}

