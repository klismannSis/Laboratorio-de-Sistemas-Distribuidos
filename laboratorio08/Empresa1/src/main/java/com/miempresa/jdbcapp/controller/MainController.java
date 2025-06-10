package main.java.com.miempresa.jdbcapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;
import javafx.scene.control.Button;
import main.java.com.miempresa.jdbcapp.view.App;

public class MainController {

    @FXML
    private Button btnDepartamentos;

    @FXML
    private Button btnIngenieros;

    @FXML
    private Button btnProyectos;

    @FXML
    private void onDepartamentos(ActionEvent event) {
        // Lógica para cambiar a la vista de Departamentos
        try {
            App.setRoot("departamentos");  // "departamentos" es el nombre de la vista FXML de Departamentos
        } catch (IOException e) {
            e.printStackTrace();
            // Aquí podrías mostrar un mensaje de error usando una utilidad de diálogo
        }
    }

    @FXML
    private void onIngenieros(ActionEvent event) {
        // Lógica para cambiar a la vista de Ingenieros
        try {
            App.setRoot("ingenieros");  // "ingenieros" es el nombre de la vista FXML de Ingenieros
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onProyectos(ActionEvent event) {
        // Lógica para cambiar a la vista de Proyectos
        try {
            App.setRoot("proyectos");  // "proyectos" es el nombre de la vista FXML de Proyectos
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}