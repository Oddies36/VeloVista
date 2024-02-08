package be.velovista.Model.BL;

import java.util.ArrayList;

public class ClasseConteneur {
    private ArrayList<Location> location;
    private UserConnected userConnected;

    public ArrayList<Location> getLocation() {
        return location;
    }
    public void setLocation(ArrayList<Location> location) {
        this.location = location;
    }

    public UserConnected getUserConnected() {
        return userConnected;
    }
    public void setUserConnected(UserConnected userConnected) {
        this.userConnected = userConnected;
    }

    public ClasseConteneur(ArrayList<Location> location, UserConnected userConnected){
        this.location = location;
        this.userConnected = userConnected;
    }
    
}
