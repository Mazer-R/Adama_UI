package com.adama_ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.Node;
import javafx.stage.Stage;

public class LoginToAppController {

    @FXML
    private void openMainScreen(ActionEvent event) {
        try {
            Parent mainView = ViewManager.loadViewForScene("/com/adama_ui/MainScreen.fxml");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene newScene = new Scene(mainView, stage.getWidth(), stage.getHeight());
            stage.setScene(newScene);
            stage.setTitle("Adama - Principal");

            stage.setMaximized(true); // ✅ Esto soluciona el problema al volver desde login

            stage.show();

        } catch (Exception e) {
            System.err.println("Error al cambiar de pantalla: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo cargar la pantalla principal");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
