module com.briansystems.pruebasparametricas {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.briansystems.pruebasparametricas.pruebascontrolador to javafx.fxml;
    opens com.briansystems.pruebasparametricas.pruebasmodelo to javafx.base;

    exports com.briansystems.pruebasparametricas.pruebasprincipal;
}