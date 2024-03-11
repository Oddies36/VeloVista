package be.velovista.Model.DAL.DAO.AbonnementUtilisateur;

public interface IAbonnementUtilisateurDAO {
    public void createAbonnementUtilisateurTable();

    public int insertAbonnementUtilisateur(int idAbo, int idVelo, int idUser, double prixAbo);

    public int getIdVeloFromIdUser(int idUser);

    public int checkCurrentAboUser(int idUser);

    public void updateAbonnementUtilisateurInactif(int idUser);
}
