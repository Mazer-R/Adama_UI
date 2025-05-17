package com.adama_ui;

import com.adama_ui.auth.SessionManager;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static com.adama_ui.LoginToAppController.API_BASE_URL;

public class SettingsViewController {

    @FXML
    private TextField productIdField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField brandField;

    @FXML
    private TextArea resultArea;

    private final HttpClient httpClient = HttpClient.newBuilder().build();

    /**
     * Retrieves all products from the API
     */
    @FXML
    private void getAllProducts() {
        try {
            System.out.println(SessionManager.getInstance().getAuthHeader());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/products"))
                    .GET()
                    .header("Authorization", "Bearer " + SessionManager.getInstance().getAuthToken())
                    .build();
            System.out.println(request);

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                resultArea.setText("All Products:\n" + formatJson(response.body()));
            } else {
                resultArea.setText("Error retrieving products: " + response.statusCode() + "\n" + response.body());
            }
        } catch (Exception e) {
            showErrorAlert("Error retrieving products", e.getMessage());
        }
    }

    /**
     * Retrieves a product by ID from the API
     */
    @FXML
    private void getProductById() {
        String productId = productIdField.getText().trim();

        if (productId.isEmpty()) {
            showErrorAlert("Input Error", "Please enter a product ID");
            return;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/products/" + productId))
                    .GET()
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                resultArea.setText("Product Details:\n" + formatJson(response.body()));
            } else {
                resultArea.setText("Error retrieving product: " + response.statusCode() + "\n" + response.body());
            }
        } catch (Exception e) {
            showErrorAlert("Error retrieving product", e.getMessage());
        }
    }

    /**
     * Creates a new product via the API
     */
    @FXML
    private void createProduct() {
        try {
            String jsonBody = showProductInputDialog("Create New Product", null);
            if (jsonBody == null) return;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/products"))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .header("Content-Type", "application/json")
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                resultArea.setText("Product Created Successfully:\n" + formatJson(response.body()));
            } else {
                resultArea.setText("Error creating product: " + response.statusCode() + "\n" + response.body());
            }
        } catch (Exception e) {
            showErrorAlert("Error creating product", e.getMessage());
        }
    }

    /**
     * Updates an existing product via the API
     */
    @FXML
    private void updateProduct() {
        String productId = productIdField.getText().trim();

        if (productId.isEmpty()) {
            showErrorAlert("Input Error", "Please enter a product ID to update");
            return;
        }

        try {
            String jsonBody = showProductInputDialog("Update Product", "patch");
            if (jsonBody == null) return;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/products/" + productId))
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody))
                    .header("Content-Type", "application/json")
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                resultArea.setText("Product Updated Successfully:\n" + formatJson(response.body()));
            } else {
                resultArea.setText("Error updating product: " + response.statusCode() + "\n" + response.body());
            }
        } catch (Exception e) {
            showErrorAlert("Error updating product", e.getMessage());
        }
    }

    /**
     * Deletes a product via the API
     */
    @FXML
    private void deleteProduct() {
        String productId = productIdField.getText().trim();

        if (productId.isEmpty()) {
            showErrorAlert("Input Error", "Please enter a product ID to delete");
            return;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/products/" + productId))
                    .DELETE()
                    .header("Content-Type", "application/json")
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                resultArea.setText("Product Deleted Successfully (ID: " + productId + ")");
            } else {
                resultArea.setText("Error deleting product: " + response.statusCode() + "\n" + response.body());
            }
        } catch (Exception e) {
            showErrorAlert("Error deleting product", e.getMessage());
        }
    }

    /**
     * Shows an input dialog for product creation or update
     */
    private String showProductInputDialog(String title, String mode) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText("Enter Product Details (JSON format)");

        String template;
        if ("patch".equals(mode)) {
            template = "{\n" +
                    "  \"name\": \"Product Name\",\n" +
                    "  \"description\": \"Product Description\",\n" +
                    "  \"type\": \"Product Type\",\n" +
                    "  \"brand\": \"Product Brand\",\n" +
                    "  \"model\": \"Product Model\",\n" +
                    "  \"status\": \"active\"\n" +
                    "}";
        } else {

            template = "{\n" +
                    "  \"name\": \"Product Name\",\n" +
                    "  \"description\": \"Product Description\",\n" +
                    "  \"type\": \"Product Type\",\n" +
                    "  \"brand\": \"Product Brand\",\n" +
                    "  \"model\": \"Product Model\",\n" +
                    "  \"status\": \"active\",\n" +
                    "  \"userId\": \"user123\"\n" +
                    "}";
        }

        dialog.getEditor().setText(template);
        dialog.getEditor().setPrefHeight(200);
        dialog.getEditor().setPrefWidth(400);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     * Shows an error alert with the given title and message
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Simple JSON formatter for better readability
     */
    private String formatJson(String json) {
        json = json.replace("{", "{\n  ");
        json = json.replace("}", "\n}");
        json = json.replace(",", ",\n  ");
        return json;
    }
    @FXML
    private void getFilteredProducts() {
        String type = typeField.getText().trim();
        String brand = brandField.getText().trim();

        StringBuilder uriBuilder = new StringBuilder(API_BASE_URL + "/products?");
        if (!type.isEmpty()) {
            uriBuilder.append("type=").append(type);
        }
        if (!brand.isEmpty()) {
            if (uriBuilder.charAt(uriBuilder.length() - 1) != '?') {
                uriBuilder.append("&");
            }
            uriBuilder.append("brand=").append(brand);
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uriBuilder.toString()))
                    .GET()
                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                resultArea.setText("Filtered Products:\n" + formatJson(response.body()));
            } else {
                resultArea.setText("Error retrieving filtered products: " + response.statusCode() + "\n" + response.body());
            }
        } catch (Exception e) {
            showErrorAlert("Error retrieving filtered products", e.getMessage());
        }
    }


}
