package be.velovista.Model.DAL.DAO.Reservation;

import java.util.ArrayList;

import be.velovista.Model.BL.Reservation;

public interface IReservationDAO {
    public void createReservationTable();
    public int checkDispo(int idVelo, int idUser, String dateDebut, String dateFin);
    public void insertReservation(int idVelo, int idUser, String dateDebut, String dateFin, String choixAboUserString);
    public ArrayList<Reservation> getListeReservations(int idUser);
    public void annulerReservation(int idReservation);
}
