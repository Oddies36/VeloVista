package be.velovista.Model.DAL.DAO.Accessoire;

import be.velovista.Model.DAL.DBConnection;

public class AccessoireDAO implements IAccessoireDAO {
    

    public AccessoireDAO(){
        DBConnection.getConnection();
    }

    public void createAccessoireTable(){
        String sqlString = "CREATE TABLE IF NOT EXISTS Accessoire (idaccessoire SERIAL, nomaccessoire VARCHAR(50), prixaccessoire DECIMAL(10,2), PRIMARY KEY (idaccessoire))";
    }
}
