package com.briansystems.lineasdeespera.lineaesperacontrolador;

import com.briansystems.lineasdeespera.lineaesperamodelo.Cliente;
import com.briansystems.lineasdeespera.lineaesperamodelo.SimuladorColas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ColasController {

    @FXML private TextField txtLlegada;
    @FXML private TextField txtServicio;
    @FXML private TextField txtClientes;

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, Double> colLlegada;
    @FXML private TableColumn<Cliente, Double> colServicio;
    @FXML private TableColumn<Cliente, Double> colInicio;
    @FXML private TableColumn<Cliente, Double> colFin;
    @FXML private TableColumn<Cliente, Double> colEspera;
    @FXML private TableColumn<Cliente, Double> colSistema;

    @FXML private Label lblEsperaPromedio;
    @FXML private Label lblSistemaPromedio;

    private final SimuladorColas simulador = new SimuladorColas();

    @FXML
    public void initialize() {
        // Mapear las columnas con los atributos de Cliente
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLlegada.setCellValueFactory(new PropertyValueFactory<>("tiempoLlegada"));
        colServicio.setCellValueFactory(new PropertyValueFactory<>("tiempoServicio"));
        colInicio.setCellValueFactory(new PropertyValueFactory<>("inicioServicio"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("finServicio"));
        colEspera.setCellValueFactory(new PropertyValueFactory<>("tiempoEspera"));
        colSistema.setCellValueFactory(new PropertyValueFactory<>("tiempoEnSistema"));

        // Formatear columnas para mostrar solo 2 decimales
        formatearColumnaDecimal(colLlegada);
        formatearColumnaDecimal(colServicio);
        formatearColumnaDecimal(colInicio);
        formatearColumnaDecimal(colFin);
        formatearColumnaDecimal(colEspera);
        formatearColumnaDecimal(colSistema);
    }

    // Método auxiliar para limitar los decimales a 2 dígitos
    private void formatearColumnaDecimal(TableColumn<Cliente, Double> columna) {
        columna.setCellFactory(tc -> new TableCell<Cliente, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", value));
                }
            }
        });
    }

    @FXML
    private void onSimularClick() {
        try {
            double tLlegada = Double.parseDouble(txtLlegada.getText());
            double tServicio = Double.parseDouble(txtServicio.getText());
            int numClientes = Integer.parseInt(txtClientes.getText());

            List<Cliente> resultados = simulador.simular(tLlegada, tServicio, numClientes);
            ObservableList<Cliente> data = FXCollections.observableArrayList(resultados);
            tablaClientes.setItems(data);

            // Métricas
            double totalEspera = resultados.stream().mapToDouble(Cliente::getTiempoEspera).sum();
            double totalSistema = resultados.stream().mapToDouble(Cliente::getTiempoEnSistema).sum();

            lblEsperaPromedio.setText(String.format("Tiempo prom. espera: %.2f min", totalEspera / numClientes));
            lblSistemaPromedio.setText(String.format("Tiempo prom. en sistema: %.2f min", totalSistema / numClientes));

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor ingresa números válidos.");
            alert.show();
        }
    }
}