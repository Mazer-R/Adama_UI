package com.example.adama_ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class ProductManagementController {

    @FXML
    private TextField fieldIdOrTag;

    @FXML
    private ComboBox<String> comboProductType;

    @FXML
    private ComboBox<String> comboBrand;

    private final ProductService productService = new ProductService();


    @FXML
    public void initialize() {
        // Cargar valores en los combos (pueden provenir del backend o ser listas locales)
        comboProductType.getItems().addAll("Electronics", "Computing", "Mechanics");
        comboBrand.getItems().addAll("Samsung", "Sony", "HP", "Bosch");
    }

    @FXML
    private void onSearchByIdOrTag() {
        String idOrTag = fieldIdOrTag.getText().trim();
        if (idOrTag.isEmpty()) return;

        try {
            Product product = productService.getProductById(idOrTag);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/adama_ui/ProductDetail.fxml"));
            BorderPane detailRoot = loader.load();

            ProductDetailController controller = loader.getController();
            controller.setProduct(product);

            Stage stage = new Stage();
            stage.setTitle("Product Detail");
            stage.setScene(new Scene(detailRoot));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace(); // Puedes reemplazar por un Alert si quieres
        }
    }


    @FXML
    private void onSearchByFilters() {
        String type = comboProductType.getValue();
        String brand = comboBrand.getValue();

        if (type != null && brand != null) {
            System.out.println("Searching by filters: " + type + ", " + brand);
            // Agregar lógica para consultar el backend con ambos filtros
        }
    }

    @FXML
    private void onBack() {
        System.out.println("Back button clicked");
        // Lógica para regresar o cerrar la vista actual
    }
}

