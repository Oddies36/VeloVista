package be.velovista.Model.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCreation {

    private static DBProperties dbprop;
    public static Connection conn;

    public static void initializeDatabase(){
        String sqlString = "CREATE DATABASE VeloVista";

        dbprop = new DBProperties();
        String defaultUrl = dbprop.getDefaultUrl();
        String username = dbprop.getUsername();
        String password = dbprop.getPassword();

        try(Connection conn = DriverManager.getConnection(defaultUrl, username, password)){
            try(Statement stat = conn.createStatement()){
                stat.executeUpdate(sqlString);
                System.out.println("Création DB ok");
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}

