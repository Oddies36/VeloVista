module be.velovista {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;

    opens be.velovista to javafx.fxml;
    exports be.velovista;
}
