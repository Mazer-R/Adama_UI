package com.adama_ui;

import com.adama_ui.auth.SessionManager;
import com.adama_ui.util.UserResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

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
        ViewManager.load("/com/adama_ui/AddUser.fxml");
    }

    @FXML
    private void onGoToUserManagement() {
        ViewManager.load("/com/adama_ui/UserManagement.fxml");
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

                // Filtrar por username si se ha escrito algo
                if (usernameFilter != null && !usernameFilter.isEmpty()) {
                    users = users.stream()
                            .filter(u -> u.getUsername().toLowerCase().contains(usernameFilter.toLowerCase()))
                            .collect(Collectors.toList());
                }

                // Convertir UserResponse a User
                List<User> userList = users.stream()
                        .map(u -> new User(u.getUsername(), u.getFirstName(), u.getLastName(), u.getRole(), u.getDepartment()))
                        .collect(Collectors.toList());

                updateUserTable(userList);
            } else {
                showAlert("Error", "Error al obtener usuarios: " + response.statusCode());
            }

        } catch (Exception e) {
            showAlert("Error", "Excepción al filtrar usuarios:\n" + e.getMessage());
        }
    }

    private void updateUserTable(List<User> users) {
        tableUsers.setItems(FXCollections.observableArrayList(users));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onBack() {
        ViewManager.load("/com/adama_ui/SettingsView.fxml");
    }

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
