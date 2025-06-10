package main.java.com.miempresa.jdbcapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class IngenieroInputController {
    @FXML
    private TextField idDptoField, nombreField, apellidoField, especialidadField, cargoField;

    @FXML
    private void onCancelar(ActionEvent event) {
        // Volver a IngenierosView.fxml
    }
    @FXML
    private void onGuardar(ActionEvent event) {
        // Guardar nuevo ingeniero
    }
}
