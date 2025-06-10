package com.miempresa.jdbcapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.miempresa.jdbcapp.model.Departamento;
import com.miempresa.jdbcapp.dao.DepartamentoDAO;

import java.io.IOException;
import java.sql.SQLException;

public class DepartamentosController {

    @FXML
    private TableView<Departamento> tablaDepartamentos;
    @FXML
    private TableColumn<Departamento, String> colNombre;
    @FXML
    private TableColumn<Departamento, String> colTelefono;
    @FXML
    private TableColumn<Departamento, String> colFax;
    @FXML
    private TableColumn<Departamento, Void> colAcciones;

    private ObservableList<Departamento> departamentos;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colFax.setCellValueFactory(new PropertyValueFactory<>("fax"));

        // Botones "Eliminar" y "Proyectos" en cada fila
        colAcciones.setCellFactory(getAccionesCellFactory());

        cargarDepartamentos();
    }

    private Callback<TableColumn<Departamento, Void>, TableCell<Departamento, Void>> getAccionesCellFactory() {
        return param -> new TableCell<>() {
            private final Button btnEliminar = new Button("Eliminar");
            private final Button btnProyectos = new Button("Proyectos");
            private final HBox pane = new HBox(5, btnEliminar, btnProyectos);

            {
                btnEliminar.setOnAction(event -> onEliminar(getTableView().getItems().get(getIndex())));
                btnProyectos.setOnAction(event -> onVerProyectos(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        };
    }

    private void cargarDepartamentos() {
        try {
        departamentos = FXCollections.observableArrayList(DepartamentoDAO.obtenerTodos());
        tablaDepartamentos.setItems(departamentos);
        } catch (SQLException e) {
            e.printStackTrace();
            // Aquí puedes mostrar un mensaje de error al usuario si lo deseas
        }
    }

    private void onEliminar(Departamento departamento) {
        try {
            DepartamentoDAO.eliminar(departamento.getId());
            cargarDepartamentos();
        } catch (SQLException e) {
            e.printStackTrace();
            // Aquí puedes mostrar un mensaje de error al usuario si lo deseas
        }
    }

    private void onVerProyectos(Departamento departamento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miempresa/jdbcapp/view/ProyectosPorDepartamentoView.fxml"));
            Parent root = loader.load();
            // Puedes pasar el departamento al controlador de proyectos si es necesario
            Stage stage = (Stage) tablaDepartamentos.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAtras(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miempresa/jdbcapp/view/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tablaDepartamentos.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onActualizar(ActionEvent event) {
        cargarDepartamentos();
    }

    @FXML
    private void onIngresar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miempresa/jdbcapp/view/DepartamentoInputView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tablaDepartamentos.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}