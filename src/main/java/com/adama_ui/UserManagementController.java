package com.adama_ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

import java.util.List;

public class UserManagementController {

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

    // Método llamado cuando se hace clic en el botón "FILTER USERS"
    @FXML
    private void onFilterUsers() {
        String username = fieldUsername.getText().trim();
        String role = comboRole.getValue();

        // Validación de campos
        if (username.isEmpty() && role == null) {
            showAlert("Error", "Por favor ingrese al menos un criterio de filtro.");
            return;
        }

        // Aquí deberías realizar la lógica de filtrado, por ejemplo, llamando a un servicio que obtenga los datos de la base de datos
        // Simulación de la obtención de datos filtrados
        var users = getFilteredUsers(username, role);

        // Mostrar los usuarios filtrados en la TableView
        tableUsers.setItems(FXCollections.observableArrayList(users));
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
