module com.briansystems.lineasdeespera {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.briansystems.lineasdeespera.lineaesperacontrolador to javafx.fxml;
    opens com.briansystems.lineasdeespera.lineaesperamodelo to javafx.base;

    exports com.briansystems.lineasdeespera.lineaesperaprincipal;
}