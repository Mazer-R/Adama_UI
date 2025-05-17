package com.adama_ui;

import com.adama_ui.auth.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.adama_ui.LoginToAppController.API_BASE_URL;

public class AddUserController {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String API_BASE_URL = "http://localhost:8080/api";

    // Campos del formulario
    @FXML private TextField fieldUsername;
    @FXML private PasswordField fieldPassword;
    @FXML private TextField fieldFirstName;
    @FXML private TextField fieldLastName;
    @FXML private TextField fieldDepartment;
    @FXML private TextField fieldSupervisorId;
    @FXML private ComboBox<String> comboRole;

    // Método que se ejecuta cuando se presiona el botón "ADD USER"
    @FXML
    private void goAddUser() {
        ViewManager.loadView("/com/adama_ui/AddUser.fxml");
    }

    // Método que se ejecuta cuando se presiona el botón "USER MANAGEMENT"
    @FXML
    private void onGoToUserManagement() {
        ViewManager.loadView("/com/adama_ui/UserManagement.fxml");
    }

    @FXML
    public void onSaveUser() {
        String username = fieldUsername.getText().trim();
        String password = fieldPassword.getText().trim();
        String firstName = fieldFirstName.getText().trim();
        String lastName = fieldLastName.getText().trim();
        String department = fieldDepartment.getText().trim();
        String supervisorId = fieldSupervisorId.getText().trim();
        String role = comboRole.getValue();  // Puede ser ROLE_ADMIN, ROLE_USER, ROLE_WAREHOUSE, ROLE_MANAGER

        // Validación de campos obligatorios
        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || department.isEmpty() || role == null) {
            showAlert("Campos obligatorios", "Por favor completa todos los campos obligatorios (*)");
            return;
        }

        // Construir JSON manualmente
        String jsonBody = String.format(
                """
                {
                    "username": "%s",
                    "password": "%s",
                    "firstName": "%s",
                    "lastName": "%s",
                    "department": "%s",
                    "supervisorId": "%s",
                    "role": "%s"
                }
                """,
                escapeJson(username),
                escapeJson(password),
                escapeJson(firstName),
                escapeJson(lastName),
                escapeJson(department),
                escapeJson(supervisorId),
                role
        );

        try {
            // Realizar la solicitud HTTP POST para crear el usuario
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/users"))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .header("Content-Type", "application/json")
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                showInfoAlert("Usuario creado", "Respuesta del servidor:\n" + formatJson(response.body()));
            } else {
                showErrorAlert("Error al crear usuario", "Código: " + response.statusCode() + "\n" + formatJson(response.body()));
            }

        } catch (Exception e) {
            showErrorAlert("Error al enviar solicitud", e.getMessage());
        }
    }

    @FXML
    private void onBack() {
        ViewManager.loadView("/com/adama_ui/SettingsView.fxml");
    }

    private String escapeJson(String value) {
        return value.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String formatJson(String json) {
        json = json.replace("{", "{\n  ");
        json = json.replace("}", "\n}");
        json = json.replace(",", ",\n  ");
        return json;
    }
}