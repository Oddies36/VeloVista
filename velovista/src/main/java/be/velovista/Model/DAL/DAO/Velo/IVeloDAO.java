package be.velovista.Model.DAL.DAO.Velo;

import java.util.ArrayList;

import be.velovista.Model.BL.Velo;

public interface IVeloDAO {
    public void createVeloTable();
    public int getPrixClassique();
    public int getPrixElectrique();
    public int getPrixEnfant();
    public ArrayList<Integer> getVitesses();
    public ArrayList<Velo> getListeVeloClass();
    public ArrayList<Velo> getListeVeloElec();
    public ArrayList<Velo> getListeVeloEnfant();
    public Velo getVelo(int id);
    public void updateVeloToDispo(int idVelo);
    public void updateVeloToIndispo(int idVelo);
}
