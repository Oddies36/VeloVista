package be.velovista.Model.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection conn;
    private static DBConnection instance;
    private DBProperties dbprop;

    private DBConnection(){
        try{
            dbprop = new DBProperties();

            String url = dbprop.getUrl();
            String username = dbprop.getUsername();
            String password = dbprop.getPassword();

            DBConnection.conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection ok");
        }    
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static DBConnection getConnection(){
        if(DBConnection.instance == null){
            DBConnection.instance = new DBConnection();
        }
        return DBConnection.instance;
    }

    public static void closeConnection(){
        try{
            if(DBConnection.conn != null){
                DBConnection.conn.close();
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
