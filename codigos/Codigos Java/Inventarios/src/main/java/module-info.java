module com.briansystems.inventarios {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.briansystems.inventarios.inventarioscontrolador to javafx.fxml;
    opens com.briansystems.inventarios.inventariosmodelo to javafx.base;

    exports com.briansystems.inventarios.inventarioprincipal;
}