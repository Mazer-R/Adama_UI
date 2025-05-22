package com.adama_ui.Product;

import com.adama_ui.Product.DTO.Product;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ProductListCellController {
    @FXML
    private Label labelName;
    @FXML
    private Label labelType;
    @FXML
    private Label labelBrand;

    private Product product;

    public void setProduct(Product product) {
        this.product = product;
        labelName.setText(product.getName());
        labelType.setText("Type: " + product.getType());
        labelBrand.setText("Brand: " + product.getBrand());
    }

    @FXML
    private void handleClick(MouseEvent event) {
        ViewManager.getInstance().loadWithProduct("/com/adama_ui/Product/ProductDetail.fxml", product);
    }
}
