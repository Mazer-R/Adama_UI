package com.adama_ui;

import com.adama_ui.auth.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginToAppController {

    public static final String API_BASE_URL = "http://localhost:8080";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();


    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;


    @FXML
    private void login(ActionEvent event) {
        var username = usernameField.getText();
        var password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Campos requeridos", "Usuario y contraseña son obligatorios");
            return;
        }

        String jsonBody = String.format(
                "{\"username\": \"%s\", \"password\": \"%s\"}",
                username,
                password
        );
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/auth/login"))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 200) {
                var token = response.body();
                SessionManager.getInstance().setAuthToken(token);
                openMainScreen(event);
            } else {
                showAlert("Error de login", "Usuario/Contraseña incorrectos");
            }

        } catch (IOException | InterruptedException e) {
            showAlert("Error de conexión", "No se pudo conectar al servidor");
            e.printStackTrace();
        }


    }

    private void openMainScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/adama_ui/MainScreen.fxml"));
            Parent mainView = loader.load();

            MainScreenController controller = loader.getController();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(mainView, stage.getWidth(), stage.getHeight());

            stage.setScene(newScene);
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            showAlert("Error", "Error al inicializar");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
