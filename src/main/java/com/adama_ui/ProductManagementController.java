package com.adama_ui;

import com.adama_ui.util.Brands;
import com.adama_ui.util.ProductType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.List;

public class ProductManagementController {

    @FXML private TextField fieldIdOrTag;
    @FXML private ComboBox<ProductType> comboProductType;
    @FXML private ComboBox<Brands> comboBrand;
    @FXML private ListView<Product> listViewProducts;

    private final ProductService productService = new ProductService();

    @FXML
    public void initialize() {
        comboProductType.getItems().add(null);
        comboProductType.getItems().addAll(ProductType.values());
        comboProductType.setPromptText("-- Select product type --");

        comboBrand.getItems().add(null);
        comboBrand.getItems().addAll(Brands.values());
        comboBrand.setPromptText("-- Select brand --");

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

                    // ● Estado con punto de color más grande y visible
                    String status = item.getStatus();

                    Label statusDot = new Label();
                    statusDot.setPrefSize(12, 12);
                    statusDot.setStyle("-fx-background-radius: 6em; -fx-background-color: " + getColorForStatus(status) + ";");

                    Label statusLabel = new Label(" " + status);
                    statusLabel.setStyle("-fx-text-fill: white;");

                    HBox statusBox = new HBox(5, statusDot, statusLabel);
                    statusBox.setPrefWidth(150);

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    Button viewButton = new Button("Ver detalles");
                    viewButton.setStyle("-fx-background-color: #00c000; -fx-text-fill: white;");
                    viewButton.setOnAction(e -> ViewManager.loadView("/com/adama_ui/ProductDetail.fxml", item));

                    cell.getChildren().addAll(name, type, brand, statusBox, spacer, viewButton);
                    setGraphic(cell);
                }
            }

            private String getColorForStatus(String status) {
                if (status == null) return "gray";
                return switch (status.trim().toLowerCase()) {
                    case "disponible", "activo" -> "limegreen";
                    case "en reparación", "reparación" -> "gold";
                    case "no operativo", "inactivo" -> "red";
                    case "pendiente" -> "dodgerblue";
                    default -> "gray";
                };
            }
        });
    }

    @FXML
    private void onSearchByIdOrTag() {
        String idOrTag = fieldIdOrTag.getText().trim();
        if (idOrTag.isEmpty()) return;

        try {
            Product product = productService.getProductById(idOrTag);
            if (product != null) {
                ViewManager.loadView("/com/adama_ui/ProductDetail.fxml", product);
            } else {
                showAlert("Sin resultados", "No se encontró un producto con ese ID o Tag.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Ocurrió un error al buscar el producto.");
        }
    }

    @FXML
    private void onSearchByFilters() {
        ProductType selectedType = comboProductType.getValue();
        Brands selectedBrand = comboBrand.getValue();

        String type = selectedType != null ? selectedType.toString() : null;
        String brand = selectedBrand != null ? selectedBrand.toString() : null;

        try {
            List<Product> filteredProducts;

            if (type != null && brand == null) {
                filteredProducts = productService.getProductsByType(type);
            } else if (type == null && brand != null) {
                filteredProducts = productService.getProductsByBrand(brand);
            } else if (type != null && brand != null) {
                filteredProducts = productService.getProductsByFilters(type, brand);
            } else {
                showAlert("Filtros vacíos", "Por favor, selecciona al menos un filtro.");
                return;
            }

            if (filteredProducts == null || filteredProducts.isEmpty()) {
                showAlert("Sin resultados", "No se encontraron productos con los filtros seleccionados.");
                listViewProducts.setVisible(false);
            } else {
                listViewProducts.getItems().setAll(filteredProducts);
                listViewProducts.setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Ocurrió un error al buscar productos.");
        }
    }

    @FXML
    private void onBack() {
        ViewManager.loadView("/com/adama_ui/MainScreen.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
