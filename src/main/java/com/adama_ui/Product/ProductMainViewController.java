package com.adama_ui.Product;

import com.adama_ui.util.Reloadable;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ProductMainViewController implements Reloadable {

    @FXML
    private StackPane contentPane;
    @FXML
    private VBox warehouseMenu;
    @FXML
    private Button btnAddProduct;
    @FXML
    private Button btnManageProduct;


    private String currentSubview = null;
    ViewManager viewManager = ViewManager.getInstance();

    @FXML
    public void initialize() {
        btnAddProduct.setOnAction(event -> {
            viewManager.loadInto("/com/adama_ui/Product/AddProductView.fxml", contentPane, () -> {
                currentSubview = "ADD";
                highlightMenuButton(btnAddProduct);
                Object controller = viewManager.getCurrentController();
            });
        });

        btnManageProduct.setOnAction(event -> {
            viewManager.loadInto("/com/adama_ui/Product/ProductManagement.fxml", contentPane, () -> {
                currentSubview = "MANAGE";
                highlightMenuButton(btnManageProduct);
                Object controller = viewManager.getCurrentController();
                if (controller instanceof Reloadable reloadable) {
                    reloadable.onReload();
                }
            });
        });

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

    @Override
    public void onReload() {
        if (currentSubview != null) {
            switch (currentSubview) {
                case "ADD" -> btnAddProduct.fire();
                case "MANAGE" -> btnManageProduct.fire();
            }
        }
    }
}