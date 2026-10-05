package com.briansystems.analisisreportes.analisiscontrolador;

import com.briansystems.analisisreportes.analisismodelo.GeneradorReporte;
import com.briansystems.analisisreportes.analisismodelo.MetricaReporte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AnalisisController {

    @FXML private TextField txtCorridas;
    @FXML private ComboBox<String> cbConfianza;

    @FXML private TableView<MetricaReporte> tablaMetricas;
    @FXML private TableColumn<MetricaReporte, String> colMetrica;
    @FXML private TableColumn<MetricaReporte, Double> colPromedio;
    @FXML private TableColumn<MetricaReporte, Double> colInf;
    @FXML private TableColumn<MetricaReporte, Double> colSup;
    @FXML private TableColumn<MetricaReporte, String> colEstado;

    @FXML private TextArea txtResumen;

    private final GeneradorReporte generador = new GeneradorReporte();

    @FXML
    public void initialize() {
        cbConfianza.setItems(FXCollections.observableArrayList("90%", "95%", "99%"));
        cbConfianza.getSelectionModel().select("95%");

        colMetrica.setCellValueFactory(new PropertyValueFactory<>("nombreMetrica"));
        colPromedio.setCellValueFactory(new PropertyValueFactory<>("valorPromedio"));
        colInf.setCellValueFactory(new PropertyValueFactory<>("limiteInferior"));
        colSup.setCellValueFactory(new PropertyValueFactory<>("limiteSuperior"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoEvaluacion"));
    }

    @FXML
    private void onGenerarReporteClick() {
        try {
            int corridas = Integer.parseInt(txtCorridas.getText());
            String seleccionConfianza = cbConfianza.getValue();
            double confianza = Double.parseDouble(seleccionConfianza.replace("%", "")) / 100.0;

            List<MetricaReporte> resultados = generador.generarAnalisis(corridas, confianza);
            ObservableList<MetricaReporte> data = FXCollections.observableArrayList(resultados);
            tablaMetricas.setItems(data);

            StringBuilder sb = new StringBuilder();
            sb.append("RESUMEN EJECUTIVO DE SIMULACIÓN\n\n");
            sb.append("Se completaron ").append(corridas).append(" corridas de simulación con un nivel de confianza del ").append(seleccionConfianza).append(".\n\n");
            sb.append("Conclusiones Principales:\n");

            for (MetricaReporte m : resultados) {
                if (m.getEstadoEvaluacion().equalsIgnoreCase("Dentro de Parámetro")) {
                    sb.append("• ").append(m.getNombreMetrica()).append(": Se encuentra operando de manera aceptable dentro del rango esperado.\n");
                } else {
                    sb.append("• ").append(m.getNombreMetrica()).append(": Presenta una alerta debido a que sobrepasa el umbral permitido.\n");
                }
            }

            txtResumen.setText(sb.toString());

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor ingresa un número de corridas válido.");
            alert.show();
        }
    }
}