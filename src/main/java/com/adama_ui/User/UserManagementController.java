package com.adama_ui.User;

import com.adama_ui.User.DTO.UserDTO;
import com.adama_ui.User.DTO.UserResponse;
import com.adama_ui.User.DTO.UserService;
import com.adama_ui.auth.SessionManager;
import com.adama_ui.style.GenericCellFactory;
import com.adama_ui.style.renderer.UserCellRenderer;
import com.adama_ui.util.ViewManager;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.adama_ui.Product.DTO.ProductMapper.mapper;
import static com.adama_ui.auth.SessionManager.API_BASE_URL;
import static com.adama_ui.auth.SessionManager.HTTP_CLIENT;

@Slf4j
public class UserManagementController {

    @FXML
    private TextField fieldUsername;
    @FXML
    private ComboBox<String> comboRole;
    @FXML
    private ListView<UserResponse> listViewUsers;

    private final UserService userService = new UserService();
    private final ViewManager viewManager = ViewManager.getInstance();


    @FXML
    public void initialize() {
        comboRole.setPromptText("Select role");
        listViewUsers.setVisible(false);
        listViewUsers.setCellFactory(new GenericCellFactory<>(UserCellRenderer::render));
    }

    @FXML
    private void onFilterUsers() {
        String selectedRole = comboRole.getValue();
        String usernameFilter = fieldUsername.getText().trim();

        try {
            List<UserResponse> users;

            if (!usernameFilter.isEmpty()) {
                users = searchByUsername(usernameFilter);

                if (selectedRole != null && !selectedRole.isEmpty()) {
                    users = users.stream()
                            .filter(u -> selectedRole.equalsIgnoreCase(u.getRole()))
                            .toList();
                }
            } else if (selectedRole != null && !selectedRole.isEmpty()) {
                users = searchByRole(selectedRole);
            } else {
                showAlert("Filtros vacíos", "Introduce al menos un nombre de usuario o selecciona un rol.");
                listViewUsers.setVisible(false);
                return;
            }

            if (users == null || users.isEmpty()) {
                showAlert("Sin resultados", "No se encontraron usuarios con esos criterios.");
                listViewUsers.setVisible(false);
            } else {
                listViewUsers.getItems().setAll(users);
                listViewUsers.setVisible(true);
            }

        } catch (Exception e) {
            log.error("Error al filtrar usuarios", e);
            showAlert("Error", "Excepción al filtrar:\n" + e.getMessage());
            listViewUsers.setVisible(false);
        }
    }

    private List<UserResponse> searchByUsername(String username) throws IOException, InterruptedException {
        String endpoint = API_BASE_URL + "/users/username/" + URLEncoder.encode(username, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .header("Accept", "application/json")
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            UserResponse user = mapper.readValue(response.body(), UserResponse.class);
            return List.of(user);
        } else if (response.statusCode() == 404) {
            showAlert("No encontrado", "Usuario '" + username + "' no existe.");
            return List.of();
        } else {
            showAlert("Error", "Código: " + response.statusCode());
            return List.of();
        }
    }

    private List<UserResponse> searchByRole(String role) throws IOException, InterruptedException {
        String endpoint = API_BASE_URL + "/users?role=" + URLEncoder.encode(role, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .header("Accept", "application/json")
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<>() {
            });
        } else {
            showAlert("Error", "Código: " + response.statusCode());
            return List.of();
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}