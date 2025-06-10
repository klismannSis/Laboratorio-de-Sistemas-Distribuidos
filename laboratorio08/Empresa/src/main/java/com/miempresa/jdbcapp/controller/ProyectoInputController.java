package com.miempresa.jdbcapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ProyectoInputController {
    @FXML
    private TextField nombreField, fechaInicioField, fechaFinField, idDptoField, ingenierosField, rolesField;

    @FXML
    private void onCancelar(ActionEvent event) {
        // Volver a ProyectosView.fxml
    }
    @FXML
    private void onGuardar(ActionEvent event) {
        // Guardar nuevo proyecto y asignaciones
    }
}
