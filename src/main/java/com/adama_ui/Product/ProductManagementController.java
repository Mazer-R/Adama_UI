package com.adama_ui.Product;

import com.adama_ui.Product.DTO.Product;
import com.adama_ui.util.Brands;
import com.adama_ui.util.ProductStatus;
import com.adama_ui.util.ProductType;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ProductManagementController {

    @FXML
    private TextField fieldIdOrTag;
    @FXML
    private ComboBox<ProductType> comboProductType;
    @FXML
    private ComboBox<Brands> comboBrand;
    @FXML
    private ListView<Product> listViewProducts;
    ViewManager viewManager = ViewManager.getInstance();

    private final ProductService productService = new ProductService();

    @FXML
    public void initialize() {
        comboProductType.getItems().add(null);
        comboProductType.getItems().addAll(ProductType.values());
        comboProductType.setPromptText("-- Selecciona un tipo de producto --");

        comboBrand.getItems().add(null);
        comboBrand.getItems().addAll(Brands.values());
        comboBrand.setPromptText("-- Selecciona una marca --");

        listViewProducts.setVisible(false);

        listViewProducts.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox cell = new HBox(10);
                    cell.getStyleClass().add("custom-list-cell");

                    Label name = new Label(item.getName());
                    name.getStyleClass().add("product-name");
                    name.setPrefWidth(200);

                    Label type = new Label(item.getType());
                    type.getStyleClass().add("product-info");
                    type.setPrefWidth(100);

                    Label brand = new Label(item.getBrand());
                    brand.getStyleClass().add("product-info");
                    brand.setPrefWidth(100);

                    HBox statusBox = getHBox(item);

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    Button viewButton = new Button("Ver detalles");
                    viewButton.setStyle("-fx-background-color: #00c000; -fx-text-fill: white;");
                    viewButton.setOnAction(e -> viewManager.loadWithProduct("/com/adama_ui/Product/ProductDetail.fxml", item));

                    cell.getChildren().addAll(name, type, brand, statusBox, spacer, viewButton);
                    setGraphic(cell);
                }
            }
        });
    }

    private static HBox getHBox(Product item) {
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
        return statusBox;
    }

    @FXML
    private void onSearchByIdOrTag() {
        String idOrTag = fieldIdOrTag.getText().trim();
        if (idOrTag.isEmpty()) return;

        try {
            Product product = productService.getProductById(idOrTag);
            if (product != null) {
                viewManager.loadWithProduct("/com/adama_ui/Product/ProductDetail.fxml", product);
            } else {
                showAlert("Sin resultados", "No se encontró un producto con ese ID o Tag.");
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
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
            if (type == null && brand == null) {
                showAlert("Filtros vacíos", "Por favor, selecciona al menos un filtro.");
                return;
            }

            List<Product> filteredProducts = productService.getProductsByFilters(type, brand);

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


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
