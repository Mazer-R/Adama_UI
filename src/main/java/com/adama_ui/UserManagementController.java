package com.adama_ui;

import com.adama_ui.auth.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.adama_ui.LoginToAppController.API_BASE_URL;

public class UserManagementController {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @FXML
    private TextField fieldUsername;

    @FXML
    private ComboBox<String> comboRole;

    @FXML
    private TableView<User> tableUsers;


    @FXML
    private void onAddUser() {
        ViewManager.loadView("/com/adama_ui/AddUser.fxml");
    }

    @FXML
    private void onGoToUserManagement() {
        ViewManager.loadView("/com/adama_ui/UserManagement.fxml");
    }

    @FXML
    private void onFilterUsers() {
        String selectedRole = comboRole.getValue();
        String usernameFilter = fieldUsername.getText();

        try {
            String endpoint = API_BASE_URL + "/users";
            if (selectedRole != null && !selectedRole.isEmpty()) {
                endpoint += "?role=" + URLEncoder.encode(selectedRole, StandardCharsets.UTF_8);
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .GET()
                    .header("Accept", "application/json")
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String json = response.body();

                ObjectMapper mapper = new ObjectMapper();
                List<UserResponse> users = mapper.readValue(json, new TypeReference<>() {});

                // Aplicar filtro por username en frontend (si hay texto)
                if (usernameFilter != null && !usernameFilter.isEmpty()) {
                    users = users.stream()
                            .filter(u -> u.getUsername().toLowerCase().contains(usernameFilter.toLowerCase()))
                            .collect(Collectors.toList());
                }

                updateUserTable(users);
            } else {
                showAlert("Error", "Error al obtener usuarios: " + response.statusCode(), Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            showAlert("Error", "Excepción al filtrar usuarios:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Simulación de un servicio que devuelve usuarios filtrados
    private List<User> getFilteredUsers(String username, String role) {
        // Aquí deberías consultar la base de datos o un servicio REST para obtener los usuarios
        // Este es un ejemplo de cómo podría ser la lógica
        return List.of(new User("johndoe", "John", "Doe", "ROLE_USER", "Sales"));
    }

    // Método para mostrar alertas
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onBack() {
        ViewManager.loadView("/com/adama_ui/SettingsView.fxml");
    }

    // Clase User para simular los datos
    public static class User {
        private String username;
        private String firstName;
        private String lastName;
        private String role;
        private String department;

        public User(String username, String firstName, String lastName, String role, String department) {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.role = role;
            this.department = department;
        }

        public String getUsername() {
            return username;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getRole() {
            return role;
        }

        public String getDepartment() {
            return department;
        }
    }
}
