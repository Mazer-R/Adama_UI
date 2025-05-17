package com.adama_ui.Product;

import com.adama_ui.Product.DTO.Product;
import com.adama_ui.Product.DTO.ProductMapper;
import com.adama_ui.Product.DTO.ProductRequestDTO;
import com.adama_ui.Product.DTO.ProductResponseDTO;
import com.adama_ui.auth.SessionManager;
import com.adama_ui.util.Brands;
import com.adama_ui.util.ProductType;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.adama_ui.auth.SessionManager.API_BASE_URL;
import static com.adama_ui.auth.SessionManager.HTTP_CLIENT;

public class AddProductController {


    @FXML private TextField fieldName;
    @FXML private TextArea fieldDescription;
    @FXML private ComboBox<ProductType> comboType;
    @FXML private ComboBox<Brands> comboBrand;

    @FXML
    public void initialize() {
        comboType.getItems().addAll(ProductType.values());
        comboBrand.getItems().addAll(Brands.values());
    }

    @FXML
    private void onSaveProduct() {
        try {
            ProductRequestDTO requestDto = new ProductRequestDTO(
                    fieldName.getText().trim(),
                    fieldDescription.getText().trim(),
                    comboType.getValue().name(),
                    comboBrand.getValue().name()
            );

            if (requestDto.getName().isEmpty() || requestDto.getType().isEmpty() || requestDto.getBrand().isEmpty()) {
                showAlert("Error", "Campos obligatorios faltantes");
                return;
            }

            String jsonBody = ProductMapper.toJson(requestDto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/products"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                ProductResponseDTO responseDto = ProductMapper.fromJson(response.body());
                Product product = ProductMapper.toProduct(responseDto);
                showAlert("Producto creado", product.toPrettyJson());
            } else {
                showErrorAlert("Error del servidor: " , String.valueOf(response.statusCode()));
            }

        } catch (IllegalArgumentException e) {
            showAlert("Error de validación", e.getMessage());
        } catch (Exception e) {
            showAlert("Error crítico", e.getMessage());
        }
    }

    @FXML
    private void onBack() {
        ViewManager.load("/com/adama_ui/Product/ProductManagement.fxml");
    }

    @FXML
    private void onAddProduct() {
        ViewManager.load("/com/adama_ui/Product/AddProduct.fxml");
    }

    @FXML
    private void onGoToProductManagement() {
        ViewManager.load("/com/adama_ui/Product/ProductManagement.fxml");
    }

    private void showAlert(String title, String message) {
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
}
