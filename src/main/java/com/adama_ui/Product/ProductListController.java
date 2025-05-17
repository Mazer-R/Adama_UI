package com.adama_ui.Product;

import com.adama_ui.Product.DTO.Product;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class ProductListController {

    @FXML
    private TableView<Product> tableProducts;

    @FXML
    private TableColumn<Product, String> colId;

    @FXML
    private TableColumn<Product, String> colName;

    @FXML
    private TableColumn<Product, String> colType;

    @FXML
    private TableColumn<Product, String> colBrand;

    public void setProductList(List<Product> products) {
        tableProducts.getItems().clear();
        tableProducts.getItems().addAll(products);
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
    }
}
