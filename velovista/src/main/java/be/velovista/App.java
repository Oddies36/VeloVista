package be.velovista;

import be.velovista.Controller.Controller;
import be.velovista.Model.DAL.DBCreation;
import be.velovista.Model.DAL.DBTableInitializer;

public class App {

    public static void main(String[] args) {
        DBCreation.initializeDatabase();
        DBTableInitializer.createAllTables();
        Controller controller = new Controller();
        controller.initialize();
        controller.start();
    }

}