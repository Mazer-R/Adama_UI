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
    public static final String API_BASE_URL = "https://touching-deadly-reindeer.ngrok-free.app";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    public void initialize() {
        passwordField.setOnAction(event -> login(new ActionEvent(passwordField, null)));
    }

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
            HttpRequest loginRequest = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/auth/login"))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> loginResponse = HTTP_CLIENT.send(
                    loginRequest,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (loginResponse.statusCode() == 200) {
                String tokenJson = loginResponse.body();
                SessionManager.getInstance().setAuthToken(tokenJson);

                String authHeader = SessionManager.getInstance().getAuthHeader();

                // Obtener datos del usuario
                String userId = getValueFromAPI("/users/myid", authHeader);
                String role = getValueFromAPI("/users/role", authHeader);
                String managerId = getValueFromAPI("/users/myManager", authHeader);

                if (userId == null || role == null) {
                    showAlert("Error", "No se pudo obtener los datos del usuario");
                    return;
                }

                SessionManager.getInstance().setUserData(userId, role, username);


                openMainScreen(event);
            } else {
                showAlert("Error de login", "Usuario/Contraseña incorrectos");
            }

        } catch (IOException | InterruptedException e) {
            showAlert("Error de conexión", "No se pudo conectar al servidor");
            e.printStackTrace();
        }
    }

    private String getValueFromAPI(String path, String authHeader) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + path))
                .header("Authorization", authHeader)
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body().replace("\"", "");
        } else {
            System.err.println("❌ Error al llamar a " + path + ": " + response.statusCode());
            return null;
        }
    }

    private void openMainScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/adama_ui/MainScreen.fxml"));
            Parent mainView = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(mainView, stage.getWidth(), stage.getHeight());

            stage.setScene(newScene);
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            showAlert("Error", "Error al inicializar la pantalla principal");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}