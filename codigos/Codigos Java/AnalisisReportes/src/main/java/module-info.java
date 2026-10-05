module com.briansystems.analisisreportes {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.briansystems.analisisreportes.analisiscontrolador to javafx.fxml;

    exports com.briansystems.analisisreportes.analisisprincipal;
    exports com.briansystems.analisisreportes.analisiscontrolador;
    exports com.briansystems.analisisreportes.analisismodelo;
}