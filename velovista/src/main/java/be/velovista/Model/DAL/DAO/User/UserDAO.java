package be.velovista.Model.DAL.DAO.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import be.velovista.Model.BL.User;
import be.velovista.Model.DAL.DBConnection;

public class UserDAO implements IUserDAO{
    
    public UserDAO(){
        
        DBConnection.getConnection();
    }

    public void createUserTable(){
        String sqlString = "CREATE TABLE IF NOT EXISTS Utilisateur (idUser SERIAL PRIMARY KEY, Nom VARCHAR(50), Prenom VARCHAR(50), Email VARCHAR(120), MotDePasse VARCHAR(100), NumTelephone VARCHAR(50), totalkm INTEGER, pointsbonus DECIMAL(10,2));";

        try(Statement stat =  DBConnection.conn.createStatement()){
            stat.executeUpdate(sqlString);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public User getUser(String email){
        User u = null;
        String sqlString = "SELECT iduser, nom, prenom, email, numtelephone, totalkm FROM Utilisateur WHERE email = ?";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setString(1, email);
            try(ResultSet rset = pstat.executeQuery()){
                while(rset.next()){
                    u = new User(rset.getInt(1), rset.getString(2), rset.getString(3), rset.getString(4), rset.getString(5), rset.getInt(6));
                }
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return u;
    }

    public String getUserPassword(String email){
        String hashedPassword = "";
        String sqlString = "SELECT motdepasse FROM Utilisateur WHERE email = ?";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setString(1, email);
            try(ResultSet rset = pstat.executeQuery()){
                while(rset.next()){
                    hashedPassword = rset.getString(1);
                }
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return hashedPassword;
    }

    public void getUserEmail(){

    }

    public int getEmailCount(String Email){
        String sqlString = "SELECT count(email) FROM utilisateur WHERE email = ?";
        int result = -1;

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setString(1, Email);
            try(ResultSet rset = pstat.executeQuery()){
                while(rset.next()){
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

    public void createUserAccount(String nom, String prenom, String Email, String numTel, String hashedPassword){
        String sqlString = "INSERT INTO utilisateur (nom, prenom, email, motdepasse, numtelephone, totalkm, pointsbonus) VALUES (?, ?, ?, ?, ?, 0, 0)";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setString(1, nom);
            pstat.setString(2, prenom);
            pstat.setString(3, Email);
            pstat.setString(4, hashedPassword);
            pstat.setString(5, numTel);
            pstat.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void changeUserPassword(String hashedPassword, String Email){
        String sqlString = "UPDATE utilisateur SET motdepasse = ? WHERE email = ?";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setString(1, hashedPassword);
            pstat.setString(2, Email);
            pstat.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void updateTotalKMUser(int newKmUser, int idUser){
        String sqlString = "UPDATE utilisateur SET totalkm = ? WHERE iduser = ?";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setInt(1, newKmUser);
            pstat.setInt(2, idUser);
            pstat.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void updatePointsBonus(double pointsBonus, int idUser){
        String sqlString = "UPDATE utilisateur SET pointsbonus = ? WHERE iduser = ?";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setDouble(1, pointsBonus);
            pstat.setInt(2, idUser);
            pstat.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public double getPointsBonus(int idUser){
        String sqlString = "SELECT pointsbonus FROM utilisateur WHERE iduser = ?";
        double pointsBonus = -1;

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setInt(1, idUser);
            try(ResultSet rset = pstat.executeQuery()){
                if(rset.next()){
                    pointsBonus = rset.getDouble(1);
                }
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return pointsBonus;
    }

    public void updateNomUser(int userId, String nouveauNom){
        String sqlString = "UPDATE utilisateur SET nom = ? WHERE iduser = ?";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setString(1, nouveauNom);
            pstat.setInt(2, userId);
            pstat.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void updatePrenomUser(int userId, String nouveauPrenom){
        String sqlString = "UPDATE utilisateur SET prenom = ? WHERE iduser = ?";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setString(1, nouveauPrenom);
            pstat.setInt(2, userId);
            pstat.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void updateEmailUser(int userId, String nouveauMail){
        String sqlString = "UPDATE utilisateur SET email = ? WHERE iduser = ?";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setString(1, nouveauMail);
            pstat.setInt(2, userId);
            pstat.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void updateNumTelUser(int userId, String nouveauTel){
        String sqlString = "UPDATE utilisateur SET numtelephone = ? WHERE iduser = ?";

        try(PreparedStatement pstat = DBConnection.conn.prepareStatement(sqlString)){
            pstat.setString(1, nouveauTel);
            pstat.setInt(2, userId);
            pstat.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
