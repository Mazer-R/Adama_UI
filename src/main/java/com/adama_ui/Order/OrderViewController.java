
package com.adama_ui.Order;

import com.adama_ui.Order.OrderRequest;
import com.adama_ui.Order.OrderService;
import com.adama_ui.Product.DTO.Product;
import com.adama_ui.Product.ProductService;
import com.adama_ui.Reloadable;
import com.adama_ui.auth.SessionManager;
import com.adama_ui.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.util.*;
import java.util.stream.Collectors;

public class OrderViewController implements Reloadable {

    @FXML private ComboBox<LabeledValue> comboProductType;
    @FXML private ComboBox<LabeledValue> comboBrand;
    @FXML private ListView<Product> listViewProducts;

    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    private List<Product> allStockProducts = new ArrayList<>();

    @FXML
    public void initialize() {
        setupComboBoxes();
        setupListView();
        loadInStockProducts();
    }

    private void setupComboBoxes() {
        comboProductType.setPromptText("Todos los tipos");
        comboBrand.setPromptText("Todas las marcas");

        comboProductType.setCellFactory(cb -> new ListCell<>() {
            @Override protected void updateItem(LabeledValue item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Todos los tipos" : item.label());
            }
        });
        comboProductType.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(LabeledValue item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Todos los tipos" : item.label());
            }
        });

        comboBrand.setCellFactory(cb -> new ListCell<>() {
            @Override protected void updateItem(LabeledValue item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Todas las marcas" : item.label());
            }
        });
        comboBrand.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(LabeledValue item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Todas las marcas" : item.label());
            }
        });

        comboProductType.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) onSearchByFilters(); });
        comboBrand.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) onSearchByFilters(); });
    }

    private void setupListView() {
        listViewProducts.setVisible(false);
        listViewProducts.setCellFactory(list -> new ListCell<>() {
            @Override protected void updateItem(Product item, boolean empty) {
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

                    ProductStatus status = item.getStatus();
                    Label statusDot = new Label();
                    statusDot.setPrefSize(12, 12);
                    statusDot.setStyle("-fx-background-radius: 6em; -fx-background-color: " +
                            (status != null ? status.getColorHex() : "gray") + ";");

                    Label statusLabel = new Label(" " + (status != null ? status.getLabel() : "Desconocido"));
                    statusLabel.getStyleClass().add("status-label");

                    HBox statusBox = new HBox(5, statusDot, statusLabel);
                    statusBox.setPrefWidth(150);

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    Button requestButton = new Button("Solicitar");
                    requestButton.getStyleClass().add("action-button");
                    requestButton.setOnAction(e -> saveRequestToBackend(item));

                    cell.getChildren().addAll(name, type, brand, statusBox, spacer, requestButton);
                    setGraphic(cell);
                }
            }
        });
    }

    public void loadInStockProducts() {
        try {
            allStockProducts = productService.getAllProducts().stream()
                    .filter(p -> p.getStatus() == ProductStatus.STOCK)
                    .collect(Collectors.toList());

            if (allStockProducts.isEmpty()) {
                listViewProducts.setVisible(false);
                listViewProducts.getItems().clear();
                return;
            }

            listViewProducts.getItems().setAll(allStockProducts);
            listViewProducts.setVisible(true);
            updateComboBoxFilters(allStockProducts);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudieron cargar los productos disponibles.");
        }
    }

    private void saveRequestToBackend(Product product) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Motivo de la solicitud");
        dialog.setHeaderText("Introduce el motivo de tu solicitud:");
        dialog.getDialogPane().getButtonTypes().addAll(
                new ButtonType("Enviar", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);

        TextArea motivoArea = new TextArea();
        motivoArea.setPromptText("Escribe el motivo aquí...");
        motivoArea.setWrapText(true);
        dialog.getDialogPane().setContent(motivoArea);

        dialog.setResultConverter(button -> button.getButtonData() == ButtonBar.ButtonData.OK_DONE ? motivoArea.getText() : null);

        dialog.showAndWait().ifPresent(motivo -> {
            if (motivo.isBlank()) {
                showAlert("Campo vacío", "Debes introducir un motivo.");
                return;
            }

            SessionManager session = SessionManager.getInstance();
            String manager = Optional.ofNullable(session.getUsername()).orElse("admin");

            try {
                orderService.createOrder(new OrderRequest(product.getId(), session.getUserId(), manager, motivo));
                showAlert("Solicitud enviada", "Producto solicitado correctamente.");

                allStockProducts.remove(product);
                listViewProducts.getItems().remove(product);
                updateComboBoxFilters(listViewProducts.getItems());

                if (listViewProducts.getItems().isEmpty()) {
                    listViewProducts.setVisible(false);
                }

                onReload();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "No se pudo enviar la solicitud.");
            }
        });
    }

    private void updateComboBoxFilters(List<Product> products) {
        Set<String> types = products.stream()
                .map(Product::getType)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));

        Set<String> brands = products.stream()
                .map(Product::getBrand)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));

        List<LabeledValue> typeItems = new ArrayList<>();
        typeItems.add(null);
        for (String type : types) {
            ProductType mapped = EnumMapper.fromProductTypeString(type);
            String label = mapped != null ? mapped.getLabel() : type;
            typeItems.add(new LabeledValue(type, label));
        }

        List<LabeledValue> brandItems = new ArrayList<>();
        brandItems.add(null);
        for (String brand : brands) {
            brandItems.add(new LabeledValue(brand, brand));
        }

        comboProductType.getItems().setAll(typeItems);
        comboBrand.getItems().setAll(brandItems);
    }

    @FXML
    private void onSearchByFilters() {
        LabeledValue selectedType = comboProductType.getValue();
        LabeledValue selectedBrand = comboBrand.getValue();

        List<Product> filtered = allStockProducts.stream()
                .filter(p -> selectedType == null || selectedType.value().equalsIgnoreCase(p.getType()))
                .filter(p -> selectedBrand == null || selectedBrand.value().equalsIgnoreCase(p.getBrand()))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            listViewProducts.setVisible(false);
            listViewProducts.getItems().clear();
            showAlert("Sin resultados", "No hay productos disponibles con esos filtros.");
        } else {
            listViewProducts.getItems().setAll(filtered);
            listViewProducts.setVisible(true);
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @Override
    public void onReload() {
        Platform.runLater(this::loadInStockProducts);
    }
}