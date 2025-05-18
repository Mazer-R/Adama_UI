package com.adama_ui.User;

import com.adama_ui.User.DTO.UserDTO;
import com.adama_ui.User.DTO.UserResponse;
import com.adama_ui.auth.SessionManager;
import com.adama_ui.util.ViewManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.adama_ui.Product.DTO.ProductMapper.mapper;
import static com.adama_ui.auth.SessionManager.API_BASE_URL;


public class UserManagementController {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @FXML
    private TextField fieldUsername;

    @FXML
    private ComboBox<String> comboRole;
    @FXML
    private TableView<UserDTO> tableUsers;
    @FXML
    private TableColumn<UserDTO, String> colUsername;
    @FXML
    private TableColumn<UserDTO, String> colFirstName;
    @FXML
    private TableColumn<UserDTO, String> colLastName;
    @FXML
    private TableColumn<UserDTO, String> colRole;
    @FXML
    private TableColumn<UserDTO, String> colDepartment;

    public void initialize() {

        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));

        tableUsers.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        openUserDetails(newSelection.getId());
                    }
                }
        );
    }

    @FXML
    private void onFilterUsers() {
        String selectedRole = comboRole.getValue();
        String usernameFilter = fieldUsername.getText().trim();

        try {
            String endpoint;
            List<UserResponse> users = new ArrayList<>();

            if (!usernameFilter.isEmpty()) {
                endpoint = API_BASE_URL + "/users/username/" + URLEncoder.encode(usernameFilter, StandardCharsets.UTF_8);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint))
                        .GET()
                        .header("Accept", "application/json")
                        .header("Authorization", SessionManager.getInstance().getAuthHeader())
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    UserResponse user = mapper.readValue(response.body(), UserResponse.class);
                    users.add(user);
                } else if (response.statusCode() == 404) {
                    showAlert("No encontrado", "Usuario '" + usernameFilter + "' no existe");
                } else {
                    showAlert("Error", "Código: " + response.statusCode());
                }
            } else {
                endpoint = API_BASE_URL + "/users";
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
                System.out.println(response.body());
                if (response.statusCode() == 200) {
                    users = mapper.readValue(response.body(), new TypeReference<>() {
                    });
                } else {
                    showAlert("Error", "Código: " + response.statusCode());
                }
            }

            if (selectedRole != null && !selectedRole.isEmpty() && !users.isEmpty()) {
                users = users.stream()
                        .filter(u -> selectedRole.equalsIgnoreCase(u.getRole()))
                        .toList();
            }

            List<UserDTO> userList = users.stream()
                    .map(u -> new UserDTO(u.getId(), u.getUsername(), u.getFirstName(), u.getLastName(), u.getRole(), u.getDepartment()))
                    .toList();
            System.out.println(userList);
            updateUserTable(userList);

        } catch (Exception e) {
            showAlert("Error", "Excepción al filtrar:\n" + e.getMessage());
        }
    }

    private void updateUserTable(List<UserDTO> users) {
        tableUsers.setItems(FXCollections.observableArrayList(users));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openUserDetails(String userId) {
        try {
            String endpoint = API_BASE_URL + "/users/" + URLEncoder.encode(userId, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .GET()
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                UserResponse user = new ObjectMapper().readValue(response.body(), UserResponse.class);

                Parent root = ViewManager.loadForSceneWithUser("/com/adama_ui/User/userDetails.fxml", user);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            } else {
                showAlert("Error", "Usuario no encontrado");
            }
        } catch (Exception e) {
            showAlert("Error", "Error al obtener detalles: " + e.getMessage());
        }
    }

}
