package be.velovista.Model.BL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Location {
    
    DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int idLocation;
    private User user;
    private Velo velo;
    private ArrayList<Accessoire> accessoires;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    public int getIdLocation() {
        return idLocation;
    }
    public void setIdLocation(int idLocation) {
        this.idLocation = idLocation;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Velo getVelo() {
        return velo;
    }
    public void setVelo(Velo velo) {
        this.velo = velo;
    }
    public ArrayList<Accessoire> getAccessoires(){
        return accessoires;
    }
    public void setAccessoires(ArrayList<Accessoire> accessoires){
        this.accessoires = accessoires;
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

    public Location(int idLocation, User user, Velo velo, ArrayList<Accessoire> accessoires, String dateDebut, String dateFin){
        this.idLocation = idLocation;
        this.user = user;
        this.velo = velo;
        this.accessoires = accessoires;

        try{
            this.dateDebut = LocalDate.parse(dateDebut, formatDate);
            this.dateFin = LocalDate.parse(dateFin, formatDate);
        }
        catch(DateTimeParseException e){
            throw new IllegalArgumentException("Date invalide. Le format de date est jj/MM/aaaa");
        }
    }
}
