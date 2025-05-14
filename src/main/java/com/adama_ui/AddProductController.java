package com.adama_ui;

import com.adama_ui.util.Brands;
import com.adama_ui.util.ProductType;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AddProductController {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String API_BASE_URL = "http://localhost:8080/api";

    @FXML private TextField fieldName;
    @FXML private TextArea fieldDescription;
    @FXML private ComboBox<ProductType> comboType;
    @FXML private ComboBox<Brands> comboBrand;
    @FXML private TextField fieldModel;

    @FXML
    public void initialize() {
        comboType.getItems().addAll(ProductType.values());
        comboBrand.getItems().addAll(Brands.values());
    }

    @FXML
    private void onSaveProduct() {
        String name = fieldName.getText().trim();
        String description = fieldDescription.getText().trim();
        ProductType type = comboType.getValue();
        Brands brand = comboBrand.getValue();
        String model = fieldModel.getText().trim();

        if (name.isEmpty() || model.isEmpty() || type == null || brand == null) {
            showAlert("Campos obligatorios", "Por favor completa todos los campos obligatorios (*)");
            return;
        }

        // Construir JSON manualmente (ajusta userId si aplica)
        String jsonBody = String.format(
                """
                {
                    "name": "%s",
                    "description": "%s",
                    "type": "%s",
                    "brand": "%s",
                    "model": "%s",
                    "status": "active",
                    "userId": "user123"
                }
                """,
                escapeJson(name),
                escapeJson(description),
                type.name(),
                brand.name(),
                escapeJson(model)
        );

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/products"))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                showInfoAlert("Producto creado", "Respuesta del servidor:\n" + formatJson(response.body()));
            } else {
                showErrorAlert("Error al crear producto", "Código: " + response.statusCode() + "\n" + formatJson(response.body()));
            }

        } catch (Exception e) {
            showErrorAlert("Error al enviar solicitud", e.getMessage());
        }
    }

    private void showInfoAlert(String title, String message) {
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

    private String escapeJson(String value) {
        return value.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }

    private String formatJson(String json) {
        json = json.replace("{", "{\n  ");
        json = json.replace("}", "\n}");
        json = json.replace(",", ",\n  ");
        return json;
    }


    @FXML
    private void onBack() {
        ViewManager.loadView("/com/adama_ui/ProductManagement.fxml");
    }

    @FXML
    private void onAddProduct() {
        ViewManager.loadView("/com/adama_ui/AddProduct.fxml");
    }

    @FXML
    private void onGoToProductManagement() {
        ViewManager.loadView("/com/adama_ui/ProductManagement.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
