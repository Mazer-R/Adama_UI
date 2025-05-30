package com.adama_ui.User;

import com.adama_ui.auth.SessionManager;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.adama_ui.auth.SessionManager.API_BASE_URL;
import static com.adama_ui.auth.SessionManager.HTTP_CLIENT;

public class AddUserController {


    @FXML
    private TextField fieldUsername;
    @FXML
    private PasswordField fieldPassword;
    @FXML
    private TextField fieldFirstName;
    @FXML
    private TextField fieldLastName;
    @FXML
    private TextField fieldDepartment;
    @FXML
    private TextField fieldSupervisorId;
    @FXML
    private ComboBox<String> comboRole;
    ViewManager viewManager = ViewManager.getInstance();

    @FXML
    private void onGoToUserManagement() {
        viewManager.load("/com/adama_ui/User/UserManagement.fxml");
    }

    @FXML
    public void onSaveUser() {
        String username = fieldUsername.getText().trim();
        String password = fieldPassword.getText().trim();
        String firstName = fieldFirstName.getText().trim();
        String lastName = fieldLastName.getText().trim();
        String department = fieldDepartment.getText().trim();
        String supervisorId = fieldSupervisorId.getText().trim();
        String role = comboRole.getValue();

        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || department.isEmpty() || role == null) {
            showAlert();
            return;
        }

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
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/users"))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .header("Content-Type", "application/json")
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                showInfoAlert("Respuesta del servidor:\n" + formatJson(response.body()));
            } else {
                showErrorAlert("Error al crear usuario", "Código: " + response.statusCode() + "\n" + formatJson(response.body()));
            }

        } catch (Exception e) {
            showErrorAlert("Error al enviar solicitud", e.getMessage());
        }
    }


    private String escapeJson(String value) {
        return value.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Campos obligatorios");
        alert.setHeaderText(null);
        alert.setContentText("Por favor completa todos los campos obligatorios (*)");
        alert.showAndWait();
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Usuario creado");
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