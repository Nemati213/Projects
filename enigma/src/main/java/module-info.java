module ru.itmo.nemat.enigma {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.itmo.nemat.enigma to javafx.fxml;
    exports ru.itmo.nemat.enigma;
}