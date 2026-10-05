package com.briansystems.pruebasparametricas.pruebascontrolador;

import com.briansystems.pruebasparametricas.pruebasmodelo.ResultadoPrueba;
import com.briansystems.pruebasparametricas.pruebasmodelo.SimuladorPruebas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PruebasController {

    @FXML private TextField txtMuestras;
    @FXML private TextField txtMediaHipotetica;
    @FXML private TextField txtMediaReal;
    @FXML private TextField txtDesviacion;
    @FXML private TextField txtNivelSignificancia;

    @FXML private TableView<ResultadoPrueba> tablaResultados;
    @FXML private TableColumn<ResultadoPrueba, Integer> colId;
    @FXML private TableColumn<ResultadoPrueba, Double> colValor;

    @FXML private Label lblMediaMuestral;
    @FXML private Label lblEstadisticoZ;
    @FXML private Label lblValorCritico;
    @FXML private Label lblResultadoHo;

    private final SimuladorPruebas simulador = new SimuladorPruebas();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idMuestra"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }

    @FXML
    private void onEjecutarPruebaClick() {
        try {
            int n = Integer.parseInt(txtMuestras.getText());
            double mediaHipotetica = Double.parseDouble(txtMediaHipotetica.getText());
            double mediaReal = Double.parseDouble(txtMediaReal.getText());
            double desviacion = Double.parseDouble(txtDesviacion.getText());
            double alpha = Double.parseDouble(txtNivelSignificancia.getText());

            List<ResultadoPrueba> muestras = simulador.generarMuestras(n, mediaReal, desviacion);
            ObservableList<ResultadoPrueba> data = FXCollections.observableArrayList(muestras);
            tablaResultados.setItems(data);

            double mediaMuestral = simulador.calcularMedia();
            double desviacionMuestral = simulador.calcularDesviacionEstandar(mediaMuestral);
            double zCalculado = simulador.calcularEstadisticoZ(mediaMuestral, mediaHipotetica, desviacionMuestral, n);

            double zCritico = 1.96;
            if (alpha == 0.01) {
                zCritico = 2.576;
            } else if (alpha == 0.10) {
                zCritico = 1.645;
            }

            lblMediaMuestral.setText(String.format("Media Muestral (x̄): %.4f", mediaMuestral));
            lblEstadisticoZ.setText(String.format("Estadístico Z: %.4f", zCalculado));
            lblValorCritico.setText(String.format("Valor Crítico (Z α/2): ±%.3f", zCritico));

            if (Math.abs(zCalculado) <= zCritico) {
                lblResultadoHo.setText("Resultado: Se ACEPTA la hipótesis nula (H0)");
                lblResultadoHo.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            } else {
                lblResultadoHo.setText("Resultado: Se RECHAZA la hipótesis nula (H0)");
                lblResultadoHo.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor ingresa valores numéricos válidos.");
            alert.show();
        }
    }
}