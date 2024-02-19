package be.velovista.Model.DAL.DAO.Location;

import java.time.LocalDate;

import be.velovista.Model.BL.Location;

public interface ILocationDAO {
    public Location getLocationByEmail(String email);
    public void insertLocation(int idAbonnementUtilisatuer, double prixTotal, LocalDate dateDebut, LocalDate dateFin);
    public int checkCurrentLocation(LocalDate dateDebut, LocalDate dateFin);
}
