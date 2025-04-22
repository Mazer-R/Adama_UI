package com.adama_ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class LoginToAppController {

    @FXML
    private void openMainScreen(ActionEvent event) {
        try {
            // ✅ Ruta absoluta (ajusta según tu estructura)
            URL fxmlUrl = getClass().getResource("MainScreen.fxml");
            if (fxmlUrl == null) {
                throw new IOException("Archivo FXML no encontrado.");
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            // stage.close(); // Opcional: cierra la ventana actual
        } catch (IOException e) {
            System.err.println("Error al cambiar de pantalla: " + e.getMessage());
            showErrorAlert("No se pudo cargar la pantalla principal.");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}