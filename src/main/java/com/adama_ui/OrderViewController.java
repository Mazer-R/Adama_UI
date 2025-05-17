package com.adama_ui;

import com.adama_ui.auth.SessionManager;
import com.adama_ui.util.Brands;
import com.adama_ui.util.ProductStatus;
import com.adama_ui.util.ProductType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.List;

public class OrderViewController implements Reloadable {

    @FXML private ComboBox<ProductType> comboProductType;
    @FXML private ComboBox<Brands> comboBrand;
    @FXML private ListView<Product> listViewProducts;

    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    @FXML
    public void initialize() {
        comboProductType.getItems().add(null);
        comboProductType.getItems().addAll(ProductType.values());
        comboProductType.setValue(null);
        comboProductType.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(ProductType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Tipo de producto" : item.getLabel());
            }
        });

        comboProductType.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ProductType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Tipo de producto" : item.getLabel());
            }
        });

        comboBrand.getItems().add(null);
        comboBrand.getItems().addAll(Brands.values());
        comboBrand.setPromptText("Marca del producto");

        comboProductType.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onSearchByFilters();
            }
        });

        comboBrand.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onSearchByFilters();
            }
        });

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
                    name.setPrefWidth(200);
                    name.getStyleClass().add("product-name");

                    Label type = new Label(item.getType());
                    type.setPrefWidth(100);
                    type.getStyleClass().add("product-info");

                    Label brand = new Label(item.getBrand());
                    brand.setPrefWidth(100);
                    brand.getStyleClass().add("product-info");

                    ProductStatus statusEnum = item.getStatus();
                    String statusLabelText = statusEnum != null ? statusEnum.getLabel() : "Desconocido";
                    String color = statusEnum != null ? statusEnum.getColorHex() : "gray";

                    Label statusDot = new Label();
                    statusDot.setPrefSize(12, 12);
                    statusDot.setStyle("-fx-background-radius: 6em; -fx-background-color: " + color + ";");

                    Label statusLabel = new Label(" " + statusLabelText);
                    statusLabel.getStyleClass().add("status-label");

                    HBox statusBox = new HBox(5, statusDot, statusLabel);
                    statusBox.setPrefWidth(150);

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    Button requestButton = new Button("Solicitar");
                    requestButton.setOnAction(e -> saveRequestToBackend(item));
                    requestButton.getStyleClass().add("action-button");

                    cell.getChildren().addAll(name, type, brand, statusBox, spacer, requestButton);
                    setGraphic(cell);
                }
            }
        });
    }

    private void saveRequestToBackend(Product product) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Motivo de la solicitud");
        dialog.setHeaderText("Introduce el motivo por el cual solicitas este producto:");

        ButtonType sendButtonType = new ButtonType("Enviar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sendButtonType, ButtonType.CANCEL);

        TextArea motivoArea = new TextArea();
        motivoArea.setPromptText("Escribe el motivo aquí...");
        motivoArea.setWrapText(true);
        motivoArea.setPrefRowCount(6);
        motivoArea.setPrefColumnCount(40);

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

            SessionManager session = SessionManager.getInstance();
            String productId = product.getId();
            String userId = session.getUserId();
            String managerUsername = session.getUsername();

            if (managerUsername == null || managerUsername.isBlank()) {
                managerUsername = "admin";
            }

            OrderRequest order = new OrderRequest(productId, userId, managerUsername, motivo);

            try {
                orderService.createOrder(order);
                showAlert("Solicitud enviada", "El producto ha sido solicitado con éxito.");
                loadInStockProducts();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "No se pudo enviar la solicitud.");
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
            List<Product> filtered = productService.getProductsByFilters(type, brand);
            List<String> orderedProductIds = orderService.getOrdersByStatus("ORDERED")
                    .stream().map(Order::getProductId).toList();

            List<Product> available = filtered.stream()
                    .filter(p -> p.getStatus() == ProductStatus.STOCK)
                    .filter(p -> !orderedProductIds.contains(p.getId()))
                    .toList();

            if (available.isEmpty()) {
                showAlert("Sin resultados", "No hay productos en stock con esos filtros.");
                listViewProducts.setVisible(false);
            } else {
                listViewProducts.getItems().setAll(available);
                listViewProducts.setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Ocurrió un error al filtrar productos.");
        }
    }

    public void loadInStockProducts() {
        try {
            List<Product> allProducts = productService.getAllProducts();
            List<String> orderedProductIds = orderService.getOrdersByStatus("ORDERED")
                    .stream().map(Order::getProductId).toList();

            List<Product> available = allProducts.stream()
                    .filter(p -> p.getStatus() == ProductStatus.STOCK)
                    .filter(p -> !orderedProductIds.contains(p.getId()))
                    .toList();

            if (available.isEmpty()) {
                showAlert("Sin productos", "No hay productos en stock disponibles.");
                listViewProducts.setVisible(false);
            } else {
                listViewProducts.getItems().setAll(available);
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

    @Override
    public void onReload() {
        System.out.println("🔄 onReload() ejecutado en OrderViewController");
        javafx.application.Platform.runLater(() -> {
            System.out.println("✅ Ejecutando loadInStockProducts()");
            loadInStockProducts();
        });
    }
}