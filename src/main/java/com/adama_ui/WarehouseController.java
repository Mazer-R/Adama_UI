package com.adama_ui;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class WarehouseController {

    @FXML private StackPane contentPane;

    @FXML
    public void initialize() {
        // Cargar la subvista que estaba activa antes (por defecto es AddProduct)
        String subview = ViewManager.getCurrentSubView();
        ViewManager.loadInto(subview, contentPane);
    }

    @FXML
    private void loadAddProduct() {
        ViewManager.setCurrentSubView("/com/adama_ui/AddProductView.fxml");
        ViewManager.loadInto("/com/adama_ui/AddProductView.fxml", contentPane);
    }

    @FXML
    private void loadProductManagement() {
        ViewManager.setCurrentSubView("/com/adama_ui/ProductManagement.fxml");
        ViewManager.loadInto("/com/adama_ui/ProductManagement.fxml", contentPane);
    }

    @FXML
    private void onBack() {
        ViewManager.load("/com/adama_ui/HomeView.fxml");
    }
}
