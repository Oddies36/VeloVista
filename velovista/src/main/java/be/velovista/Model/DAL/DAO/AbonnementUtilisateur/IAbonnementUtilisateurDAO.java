package be.velovista.Model.DAL.DAO.AbonnementUtilisateur;

public interface IAbonnementUtilisateurDAO {
    public void createAbonnementUtilisateurTable();
    public int insertAbonnementUtilisateur(int idAbo, int idVelo, int idUser, double prixAbo);
}
