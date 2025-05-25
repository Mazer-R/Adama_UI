package com.adama_ui.Product;

import com.adama_ui.Product.DTO.Product;
import com.adama_ui.User.DTO.UserService;
import com.adama_ui.auth.SessionManager;
import com.adama_ui.util.ProductStatus;
import com.adama_ui.util.ViewManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.adama_ui.auth.SessionManager.API_BASE_URL;
import static com.adama_ui.auth.SessionManager.HTTP_CLIENT;

public class ProductDetailController {

    private static final String NO_USER_ASSIGNED = "SIN USUARIO ASIGNADO";
    private static final String DELETE_PRODUCT = "Eliminar producto";
    private static final String CANCEL = "Cancelar";

    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldType;
    @FXML
    private TextField fieldBrand;
    @FXML
    private TextField fieldStatus;
    @FXML
    private TextField fieldUser;
    @FXML
    private TextArea fieldDescription;

    @FXML
    private Button btnModify;
    @FXML
    private Button btnAccept;
    @FXML
    private Button btnDelete;

    private final UserService userService = new UserService();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Product currentProduct;

    @FXML
    public void initialize() {
        if (SessionManager.getInstance().getRole().equals("ROLE_WAREHOUSE")) {
            btnDelete.setVisible(false);
        }
    }

    public void setProduct(Product product) {
        this.currentProduct = product;
        populateFields();
    }

    private void populateFields() {
        if (currentProduct == null) return;

        fieldName.setText(nonNull(currentProduct.getName()));
        fieldType.setText(nonNull(currentProduct.getType()));
        fieldBrand.setText(nonNull(currentProduct.getBrand()));

        fieldStatus.setText(
                currentProduct.getStatus() != null
                        ? currentProduct.getStatus().getLabel()
                        : ""
        );

        if (currentProduct.getUserId() == null) {
            fieldUser.setText(NO_USER_ASSIGNED);
        } else {
            fieldUser.setText(getUserDisplayName());
        }

        fieldDescription.setText(nonNull(currentProduct.getDescription()));

        setEditable(false);
        updateButtonsForViewMode();
    }

    private String getUserDisplayName() {
        try {
            if (SessionManager.getInstance().getRole().equals("ROLE_ADMIN")) {
                return userService.getUsernameById(currentProduct.getUserId());
            } else {
                return currentProduct.getUserId();
            }
        } catch (Exception e) {
            // Log or handle exception if needed
            return currentProduct.getUserId();
        }
    }

    @FXML
    private void onModify() {
        setEditable(true);
        updateButtonsForEditMode();
    }

    @FXML
    private void onAccept() {
        if (!updateProductFromFields()) return;

        try {
            String json = objectMapper.writeValueAsString(currentProduct);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/products/" + currentProduct.getId()))
                    .header("Content-Type", "application/json")
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> resp = HTTP_CLIENT.send(req, HttpResponse.BodyHandlers.ofString());

            if (resp.statusCode() == 200) {
                showAlert("Producto actualizado con éxito.", AlertType.INFORMATION);
            } else {
                showAlert("Error al actualizar el producto.", AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error inesperado: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }

        setEditable(false);
        updateButtonsForViewMode();
    }

    private boolean updateProductFromFields() {
        currentProduct.setName(fieldName.getText());
        currentProduct.setType(fieldType.getText());
        currentProduct.setBrand(fieldBrand.getText());

        ProductStatus newStatus = ProductStatus.fromText(fieldStatus.getText());
        if (newStatus == null) {
            showAlert("El estado introducido no es válido.", AlertType.WARNING);
            return false;
        }
        currentProduct.setStatus(newStatus);

        currentProduct.setUserId(fieldUser.getText());
        currentProduct.setDescription(fieldDescription.getText());

        return true;
    }

    @FXML
    private void onCancelOrDelete() {
        if (CANCEL.equals(btnDelete.getText())) {
            populateFields();
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION,
                "Esta acción no se puede deshacer.",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText("¿Eliminar este producto?");
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                deleteProduct();
            }
        });
    }

    private void deleteProduct() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/products/" + currentProduct.getId()))
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .DELETE()
                    .build();

            HttpResponse<String> resp = HTTP_CLIENT.send(req, HttpResponse.BodyHandlers.ofString());

            if (resp.statusCode() == 200 || resp.statusCode() == 204) {
                showAlert("Producto eliminado con éxito.", AlertType.INFORMATION);
                ViewManager.getInstance().goBack();
            } else {
                showAlert("Error al eliminar el producto.", AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error inesperado: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void setEditable(boolean editable) {
        fieldName.setEditable(editable);
        fieldType.setEditable(editable);
        fieldBrand.setEditable(editable);
        fieldStatus.setEditable(editable);
        fieldUser.setEditable(editable);
        fieldDescription.setEditable(editable);
    }

    private void updateButtonsForViewMode() {
        btnModify.setVisible(true);
        btnAccept.setVisible(false);
        btnDelete.setText(DELETE_PRODUCT);
    }

    private void updateButtonsForEditMode() {
        btnModify.setVisible(false);
        btnAccept.setVisible(true);
        btnDelete.setText(CANCEL);
    }

    private void showAlert(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type == AlertType.ERROR ? "Error" : "Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String nonNull(String s) {
        return s == null ? "" : s;
    }
}