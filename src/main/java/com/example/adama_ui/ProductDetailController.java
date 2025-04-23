package com.example.adama_ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProductDetailController {

    @FXML private Label labelId;
    @FXML private Label labelName;
    @FXML private Label labelDescription;
    @FXML private Label labelType;
    @FXML private Label labelBrand;
    @FXML private Label labelStatus;
    @FXML private Label labelUser;
    @FXML private Label labelCreated;
    @FXML private Label labelModified;

    public void setProduct(Product product) {
        labelId.setText("ID: " + product.getId());
        labelName.setText("Name: " + product.getName());
        labelDescription.setText("Description: " + product.getDescription());
        labelType.setText("Type: " + product.getType());
        labelBrand.setText("Brand: " + product.getBrand());
        labelStatus.setText("Status: " + product.getStatus());
        labelUser.setText("User ID: " + product.getUserId());
        labelCreated.setText("Created: " + product.getCreated());
        labelModified.setText("Last Modified: " + product.getLastModified());
    }
}
