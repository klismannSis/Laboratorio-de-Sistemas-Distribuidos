package main.java.com.miempresa.jdbcapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DepartamentoInputController {
    @FXML
    private TextField nombreField, telefonoField, faxField;

    @FXML
    private void onCancelar(ActionEvent event) {
        // Volver a DepartamentosView.fxml
    }
    @FXML
    private void onGuardar(ActionEvent event) {
        // Guardar nuevo departamento
    }
}
