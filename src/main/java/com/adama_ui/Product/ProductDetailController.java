package com.adama_ui.Product;

import com.adama_ui.Product.DTO.Product;
import com.adama_ui.auth.SessionManager;
import com.adama_ui.util.ProductStatus;
import com.adama_ui.util.ViewManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.adama_ui.auth.SessionManager.API_BASE_URL;
import static com.adama_ui.auth.SessionManager.HTTP_CLIENT;

public class ProductDetailController {

    @FXML private TextField fieldName;
    @FXML private TextField fieldType;
    @FXML private TextField fieldBrand;
    @FXML private TextField fieldStatus;
    @FXML private TextField fieldUser;
    @FXML private TextArea fieldDescription;

    @FXML private Button btnModify;
    @FXML private Button btnAccept;
    @FXML private Button btnDelete;
    @FXML private Button btnBack;

    private Product currentProduct;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setProduct(Product product) {
        this.currentProduct = product;
        populateFields();
    }

    private void populateFields() {
        if (currentProduct == null) return;

        fieldName.setText(currentProduct.getName());
        fieldType.setText(currentProduct.getType());
        fieldBrand.setText(currentProduct.getBrand());

        if (currentProduct.getStatus() != null) {
            fieldStatus.setText(currentProduct.getStatus().getLabel());
        } else {
            fieldStatus.setText("");
        }

        fieldUser.setText(currentProduct.getUserId());
        fieldDescription.setText(currentProduct.getDescription());

        setEditable(false);
        btnModify.setVisible(true);
        btnAccept.setVisible(false);
        btnBack.setVisible(true);
        btnDelete.setText("Eliminar producto");
    }

    @FXML
    private void onModify() {
        setEditable(true);
        btnModify.setVisible(false);
        btnAccept.setVisible(true);
        btnBack.setVisible(false);
        btnDelete.setText("Cancelar");
    }

    @FXML
    private void onAccept() {
        try {
            currentProduct.setName(fieldName.getText());
            currentProduct.setType(fieldType.getText());
            currentProduct.setBrand(fieldBrand.getText());

            ProductStatus newStatus = ProductStatus.fromText(fieldStatus.getText());
            if (newStatus == null) {
                showAlert("El estado introducido no es válido.", AlertType.WARNING);
                return;
            }
            currentProduct.setStatus(newStatus);

            currentProduct.setUserId(fieldUser.getText());
            currentProduct.setDescription(fieldDescription.getText());

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
        btnModify.setVisible(true);
        btnAccept.setVisible(false);
        btnBack.setVisible(true);
        btnDelete.setText("Eliminar producto");
    }

    @FXML
    private void onCancelOrDelete() {
        if ("Cancelar".equals(btnDelete.getText())) {
            populateFields();
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION, "Esta acción no se puede deshacer.", ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText("¿Eliminar este producto?");
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    HttpRequest req = HttpRequest.newBuilder()
                            .uri(URI.create(API_BASE_URL + "/products/" + currentProduct.getId()))
                            .header("Authorization", SessionManager.getInstance().getAuthHeader())
                            .DELETE()
                            .build();

                    HttpResponse<String> resp = HTTP_CLIENT.send(req, HttpResponse.BodyHandlers.ofString());

                    if (resp.statusCode() == 200 || resp.statusCode() == 204) {
                        showAlert("Producto eliminado con éxito.", AlertType.INFORMATION);
                        ViewManager.load("/com/adama_ui/Product/ProductManagement.fxml");
                    } else {
                        showAlert("Error al eliminar el producto.", AlertType.ERROR);
                    }

                } catch (Exception e) {
                    showAlert("Error inesperado: " + e.getMessage(), AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void onBack() {
        ViewManager.load("/com/adama_ui/Product/ProductManagement.fxml");
    }

    private void setEditable(boolean editable) {
        fieldName.setEditable(editable);
        fieldType.setEditable(editable);
        fieldBrand.setEditable(editable);
        fieldStatus.setEditable(editable);
        fieldUser.setEditable(editable);
        fieldDescription.setEditable(editable);
    }

    private void showAlert(String msg, AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(type == AlertType.ERROR ? "Error" : "Información");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
