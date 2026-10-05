package com.briansystems.inventarios.inventarioscontrolador;

import com.briansystems.inventarios.inventariosmodelo.RegistroInventario;
import com.briansystems.inventarios.inventariosmodelo.SimuladorInventarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class InventariosController {

    @FXML private TextField txtInvInicial;
    @FXML private TextField txtReorden;
    @FXML private TextField txtCantidadOrden;
    @FXML private TextField txtDias;

    @FXML private TableView<RegistroInventario> tablaInventario;
    @FXML private TableColumn<RegistroInventario, Integer> colDia;
    @FXML private TableColumn<RegistroInventario, Integer> colInvInicial;
    @FXML private TableColumn<RegistroInventario, Integer> colDemanda;
    @FXML private TableColumn<RegistroInventario, Integer> colInvFinal;
    @FXML private TableColumn<RegistroInventario, Integer> colFaltante;
    @FXML private TableColumn<RegistroInventario, Integer> colOrden;
    @FXML private TableColumn<RegistroInventario, Integer> colEntrega;

    @FXML private Label lblTotalFaltantes;
    @FXML private Label lblTotalOrdenes;

    private final SimuladorInventarios simulador = new SimuladorInventarios();

    @FXML
    public void initialize() {
        colDia.setCellValueFactory(new PropertyValueFactory<>("dia"));
        colInvInicial.setCellValueFactory(new PropertyValueFactory<>("inventarioInicial"));
        colDemanda.setCellValueFactory(new PropertyValueFactory<>("demanda"));
        colInvFinal.setCellValueFactory(new PropertyValueFactory<>("inventarioFinal"));
        colFaltante.setCellValueFactory(new PropertyValueFactory<>("faltante"));
        colOrden.setCellValueFactory(new PropertyValueFactory<>("orden"));
        colEntrega.setCellValueFactory(new PropertyValueFactory<>("diasParaEntrega"));
    }

    @FXML
    private void onSimularClick() {
        try {
            int invInicial = Integer.parseInt(txtInvInicial.getText());
            int reorden = Integer.parseInt(txtReorden.getText());
            int cantidadOrden = Integer.parseInt(txtCantidadOrden.getText());
            int dias = Integer.parseInt(txtDias.getText());

            List<RegistroInventario> resultados = simulador.simular(invInicial, reorden, cantidadOrden, dias, 10.0, 3.0);
            ObservableList<RegistroInventario> data = FXCollections.observableArrayList(resultados);
            tablaInventario.setItems(data);

            int totalFaltantes = resultados.stream().mapToInt(RegistroInventario::getFaltante).sum();
            long totalOrdenes = resultados.stream().filter(r -> r.getOrden() > 0).count();

            lblTotalFaltantes.setText("Unidades Faltantes Totales: " + totalFaltantes);
            lblTotalOrdenes.setText("Pedidos Realizados: " + totalOrdenes);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor ingresa valores numéricos enteros válidos.");
            alert.show();
        }
    }
}