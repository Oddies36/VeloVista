package be.velovista.Model.DAL.DAO.Location;

import be.velovista.Model.BL.Location;

public interface ILocationDAO {
    public Location getLocationByEmail(String email);
}
