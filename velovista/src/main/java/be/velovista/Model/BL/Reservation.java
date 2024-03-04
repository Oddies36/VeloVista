package be.velovista.Model.BL;

import java.time.LocalDate;

public class Reservation {
    private int idReservation;
    public int getIdReservation() {
        return idReservation;
    }
    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    private int idVelo;
    private int idUser;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String choixAbonnement;


    public String getChoixAbonnement() {
        return choixAbonnement;
    }
    public void setChoixAbonnement(String choixAbonnement) {
        this.choixAbonnement = choixAbonnement;
    }
    public int getIdVelo() {
        return idVelo;
    }
    public void setIdVelo(int idVelo) {
        this.idVelo = idVelo;
    }

    public int getIdUser() {
        return idUser;
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Reservation(int idReservation, int idVelo, int idUser, LocalDate dateDebut, LocalDate dateFin, String choixAbonnement){
        this.idReservation = idReservation;
        this.idVelo = idVelo;
        this.idUser = idUser;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.choixAbonnement = choixAbonnement;
    }
}
