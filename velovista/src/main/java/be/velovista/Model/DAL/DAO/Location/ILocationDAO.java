package be.velovista.Model.DAL.DAO.Location;

import java.time.LocalDate;
import java.util.ArrayList;

import be.velovista.Model.BL.Location;

public interface ILocationDAO {
    public void createLocationTable();
    public Location getLocationByEmail(String email);
    public int insertLocation(int idAbonnementUtilisatuer, double prixTotal, LocalDate dateDebut, LocalDate dateFin);
    public int checkCurrentLocation(LocalDate dateDebut, LocalDate dateFin);
    public LocalDate getDateDebutCurrentLocation(int idUser);
    public ArrayList<String> getLocationsById(int idUser);
}
