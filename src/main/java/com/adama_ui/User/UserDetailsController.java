package com.adama_ui.User;

import com.adama_ui.User.DTO.UserResponse;
import com.adama_ui.auth.SessionManager;
import com.adama_ui.style.AppTheme;
import com.adama_ui.util.ViewManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lombok.Setter;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.adama_ui.auth.SessionManager.API_BASE_URL;
import static com.adama_ui.auth.SessionManager.HTTP_CLIENT;

public class UserDetailsController {

    private static final String DELETE_USER = "Eliminar usuario";
    private static final String CANCEL = "Cancelar";

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField fieldUsername;
    @FXML
    private TextField fieldFirstName;
    @FXML
    private TextField fieldLastName;
    @FXML
    private TextField fieldDepartment;
    @FXML
    private TextField fieldRole;

    @FXML
    private Button btnModify;
    @FXML
    private Button btnAccept;
    @FXML
    private Button btnDelete;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UserResponse currentUser;

    private final ViewManager viewManager = ViewManager.getInstance();
    @Setter
    private StackPane contentArea;

    @FXML
    public void initialize() {
        AppTheme.applyThemeTo(rootPane);
        if (currentUser != null) {
            populateFields();
        }
    }

    public void setUser(UserResponse user) {
        this.currentUser = user;
        populateFields();
    }

    @FXML
    private void onModify() {
        setEditable(true);
        updateButtonsForEditMode();
    }

    @FXML
    private void onAccept() {
        if (!updateUserFromFields()) return;

        try {
            String json = objectMapper.writeValueAsString(currentUser);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/users/" + currentUser.getId()))
                    .header("Content-Type", "application/json")
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                showAlert("Usuario actualizado con éxito.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error al actualizar el usuario.", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            showAlert("Error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }

        setEditable(false);
        updateButtonsForViewMode();
    }

    private boolean updateUserFromFields() {
        currentUser.setFirstName(fieldFirstName.getText());
        currentUser.setLastName(fieldLastName.getText());
        currentUser.setDepartment(fieldDepartment.getText());
        currentUser.setRole(fieldRole.getText());
        // You can add validation here if needed
        return true;
    }

    @FXML
    private void onCancelOrDelete() {
        if (CANCEL.equals(btnDelete.getText())) {
            populateFields();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Esta acción no se puede deshacer.",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText("¿Eliminar este usuario?");
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                deleteUser();
            }
        });
    }

    private void deleteUser() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/users/" + currentUser.getId()))
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .DELETE()
                    .build();

            HttpResponse<String> resp = HTTP_CLIENT.send(req, HttpResponse.BodyHandlers.ofString());

            if (resp.statusCode() == 200 || resp.statusCode() == 204) {
                showAlert("Usuario eliminado con éxito.", Alert.AlertType.INFORMATION);
                viewManager.goBack();
            } else {
                showAlert("Error al eliminar el usuario.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void setEditable(boolean editable) {
        // Username is typically not editable
        fieldFirstName.setEditable(editable);
        fieldLastName.setEditable(editable);
        fieldDepartment.setEditable(editable);
        fieldRole.setEditable(editable);
    }

    private void populateFields() {
        if (currentUser == null) return;

        fieldUsername.setText(nonNull(currentUser.getUsername()));
        fieldFirstName.setText(nonNull(currentUser.getFirstName()));
        fieldLastName.setText(nonNull(currentUser.getLastName()));
        fieldDepartment.setText(nonNull(currentUser.getDepartment()));
        fieldRole.setText(nonNull(currentUser.getRole()));

        setEditable(false);
        updateButtonsForViewMode();
    }

    private void updateButtonsForViewMode() {
        btnModify.setVisible(true);
        btnAccept.setVisible(false);
        btnDelete.setText(DELETE_USER);
    }

    private void updateButtonsForEditMode() {
        btnModify.setVisible(false);
        btnAccept.setVisible(true);
        btnDelete.setText(CANCEL);
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Error" : "Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String nonNull(String s) {
        return s == null ? "" : s;
    }

}