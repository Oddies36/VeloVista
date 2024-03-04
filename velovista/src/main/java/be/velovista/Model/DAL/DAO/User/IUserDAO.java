package be.velovista.Model.DAL.DAO.User;

import be.velovista.Model.BL.User;

public interface IUserDAO {
    public void createUserTable();
    public String getUserPassword(String email);
    public User getUser(String email);
    public int getEmailCount(String Email);
    public void getUserEmail();
    public void createUserAccount(String nom, String prenom, String Email, String numTel, String hashedPassword);
    public void changeUserPassword(String hashedPassword, String Email);
    public void updateTotalKMUser(int newKmUser, int idUser);
    public double getPointsBonus(int idUser);
    public void updatePointsBonus(double pointsBonus, int idUser);
    public void updateNomUser(int userId, String nouveauNom);
    public void updatePrenomUser(int userId, String nouveauPrenom);
    public void updateEmailUser(int userId, String nouveauMail);
    public void updateNumTelUser(int userId, String nouveauTel);
}
