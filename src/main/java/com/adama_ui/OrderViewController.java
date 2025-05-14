package com.adama_ui;

import com.adama_ui.util.Brands;
import com.adama_ui.util.ProductStatus;
import com.adama_ui.util.ProductType;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OrderViewController {

    @FXML private ComboBox<ProductType> comboProductType;
    @FXML private ComboBox<Brands> comboBrand;
    @FXML private ListView<Product> listViewProducts;

    private final ProductService productService = new ProductService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {
        comboProductType.getItems().add(null);
        comboProductType.getItems().addAll(ProductType.values());
        comboProductType.setPromptText("Product type");

        comboBrand.getItems().add(null);
        comboBrand.getItems().addAll(Brands.values());
        comboBrand.setPromptText("Brand");

        listViewProducts.setVisible(false);

        listViewProducts.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox cell = new HBox(10);
                    cell.setStyle("-fx-background-color: #1e1e1e; -fx-padding: 10; -fx-border-color: gray; -fx-border-radius: 5;");

                    Label name = new Label(item.getName());
                    name.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                    name.setPrefWidth(200);

                    Label type = new Label(item.getType());
                    type.setStyle("-fx-text-fill: white;");
                    type.setPrefWidth(100);

                    Label brand = new Label(item.getBrand());
                    brand.setStyle("-fx-text-fill: white;");
                    brand.setPrefWidth(100);

                    ProductStatus statusEnum = item.getStatus();
                    String statusLabelText = statusEnum != null ? statusEnum.getLabel() : "Desconocido";
                    String color = statusEnum != null ? statusEnum.getColorHex() : "gray";

                    Label statusDot = new Label();
                    statusDot.setPrefSize(12, 12);
                    statusDot.setStyle("-fx-background-radius: 6em; -fx-background-color: " + color + ";");

                    Label statusLabel = new Label(" " + statusLabelText);
                    statusLabel.setStyle("-fx-text-fill: white;");

                    HBox statusBox = new HBox(5, statusDot, statusLabel);
                    statusBox.setPrefWidth(150);

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    Button requestButton = new Button("Solicitar");
                    requestButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
                    requestButton.setOnAction(e -> saveRequestAsJson(item));

                    cell.getChildren().addAll(name, type, brand, statusBox, spacer, requestButton);
                    setGraphic(cell);
                }
            }
        });

        // Ya no llamamos aquí, se hará desde ProfileViewController
    }

    private void saveRequestAsJson(Product product) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Motivo de la solicitud");
        dialog.setHeaderText("Introduce el motivo por el cual solicitas este producto:");

        ButtonType sendButtonType = new ButtonType("Enviar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sendButtonType, ButtonType.CANCEL);

        TextArea motivoArea = new TextArea();
        motivoArea.setPromptText("Escribe el motivo aquí...");
        motivoArea.setWrapText(true);
        motivoArea.setPrefRowCount(6); // Altura visible
        motivoArea.setPrefColumnCount(40); // Anchura visible

        dialog.getDialogPane().setContent(motivoArea);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == sendButtonType) {
                return motivoArea.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(motivo -> {
            if (motivo.isBlank()) {
                showAlert("Campo vacío", "Debes introducir un motivo para realizar la solicitud.");
                return;
            }

            // Simulación de datos
            String userId = "123456";
            String supervisorId = "Juanito";

            SolicitudRequest solicitud = new SolicitudRequest(userId, supervisorId, motivo);

            try {
                File file = new File("solicitud.json");
                objectMapper.writeValue(file, solicitud);
                showAlert("Solicitud guardada", "El producto ha sido solicitado con éxito.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Error al guardar la solicitud.");
            }
        });
    }


    @FXML
    private void onSearchByFilters() {
        ProductType selectedType = comboProductType.getValue();
        Brands selectedBrand = comboBrand.getValue();

        String type = selectedType != null ? selectedType.toString() : null;
        String brand = selectedBrand != null ? selectedBrand.toString() : null;

        try {
            List<Product> filtered;

            if (type != null && brand != null) {
                filtered = productService.getProductsByFilters(type, brand);
            } else if (type != null) {
                filtered = productService.getProductsByType(type);
            } else if (brand != null) {
                filtered = productService.getProductsByBrand(brand);
            } else {
                loadInStockProducts();
                return;
            }

            List<Product> inStock = filtered.stream()
                    .filter(p -> p.getStatus() == ProductStatus.EN_STOCK)
                    .toList();

            if (inStock.isEmpty()) {
                showAlert("Sin resultados", "No hay productos en stock con esos filtros.");
                listViewProducts.setVisible(false);
            } else {
                listViewProducts.getItems().setAll(inStock);
                listViewProducts.setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Ocurrió un error al filtrar productos.");
        }
    }

    // 👇 Este es el método que necesitas hacer público
    public void loadInStockProducts() {
        try {
            List<Product> allProducts = productService.getAllProducts();
            List<Product> inStock = allProducts.stream()
                    .filter(p -> p.getStatus() == ProductStatus.EN_STOCK)
                    .toList();

            if (inStock.isEmpty()) {
                showAlert("Sin productos", "No hay productos en stock disponibles.");
                listViewProducts.setVisible(false);
            } else {
                listViewProducts.getItems().setAll(inStock);
                listViewProducts.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudieron cargar los productos en stock.");
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
