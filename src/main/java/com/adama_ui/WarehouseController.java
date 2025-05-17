package com.adama_ui;

import com.adama_ui.auth.SessionManager;
import com.adama_ui.Reloadable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class WarehouseController {

    @FXML private StackPane contentPane;
    @FXML private VBox warehouseMenu;
    @FXML private Button btnAddProduct;
    @FXML private Button btnManageProduct;
    @FXML private Button btnBack;

    private static String currentSubview = null;

    @FXML
    public void initialize() {
        if (btnAddProduct != null) {
            btnAddProduct.setOnAction(event -> {
                ViewManager.loadInto("/com/adama_ui/AddProductView.fxml", contentPane, () -> {
                    currentSubview = "ADD";
                    highlightMenuButton(btnAddProduct);
                    Object controller = ViewManager.getCurrentController();
                    if (controller instanceof Reloadable reloadable) {
                        reloadable.onReload();
                    }
                });
            });
        }

        if (btnManageProduct != null) {
            btnManageProduct.setOnAction(event -> {
                ViewManager.loadInto("/com/adama_ui/ProductManagement.fxml", contentPane, () -> {
                    currentSubview = "MANAGE";
                    highlightMenuButton(btnManageProduct);
                    Object controller = ViewManager.getCurrentController();
                    if (controller instanceof Reloadable reloadable) {
                        reloadable.onReload();
                    }
                });
            });
        }

        if (btnBack != null) {
            btnBack.setOnAction(e -> {
                ViewManager.load("/com/adama_ui/HomeView.fxml");
                currentSubview = null;
            });
        }

        // Subvista por defecto o persistida
        if (currentSubview == null) {
            btnAddProduct.fire();
        } else {
            switch (currentSubview) {
                case "ADD" -> btnAddProduct.fire();
                case "MANAGE" -> btnManageProduct.fire();
            }
        }
    }

    private void highlightMenuButton(Button activeButton) {
        for (var node : warehouseMenu.getChildren()) {
            if (node instanceof Button button) {
                button.getStyleClass().remove("active-button");
            }
        }
        if (activeButton != null && !activeButton.getStyleClass().contains("active-button")) {
            activeButton.getStyleClass().add("active-button");
        }
    }
}