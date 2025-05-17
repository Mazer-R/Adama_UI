package com.adama_ui.Order;

import com.adama_ui.Product.Product;
import com.adama_ui.Product.ProductService;
import com.adama_ui.util.*;
import com.adama_ui.style.AppTheme;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.*;
import java.util.stream.Collectors;

public class OrderHistoryController {

    @FXML private ListView<Order> listViewOrders;
    @FXML private ComboBox<LabeledValue> filterStatus;
    @FXML private ComboBox<LabeledValue> filterType;
    @FXML private ComboBox<LabeledValue> filterBrand;
    @FXML private TextField filterUser;

    private final OrderService orderService = new OrderService();
    private final ProductService productService = new ProductService();
    private final UserService userService = new UserService();

    private List<Order> allOrders = new ArrayList<>();

    @FXML
    public void initialize() {
        listViewOrders.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) AppTheme.applyTheme(newScene);
        });
        loadAllOrders();
    }

    private void loadAllOrders() {
        new Thread(() -> {
            try {
                allOrders = orderService.getAllOrders();

                for (Order order : allOrders) {
                    try {
                        Product product = productService.getProductById(order.getProductId());
                        if (product != null) {
                            order.setProductName(product.getName());
                            order.setProductType(product.getType());
                            order.setBrand(product.getBrand());
                        }

                        String username = userService.getUsernameById(order.getUserId());
                        order.setUsername(username);

                    } catch (Exception e) {
                        System.err.println("Error al enriquecer orden ID: " + order.getId());
                    }
                }

                Platform.runLater(() -> {
                    populateFilters(allOrders);
                    updateList(allOrders);
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    listViewOrders.getItems().clear();
                    Label error = new Label("Error al cargar las órdenes.");
                    error.getStyleClass().add("estado-rechazada");
                    listViewOrders.setPlaceholder(error);
                });
            }
        }).start();
    }

    private void populateFilters(List<Order> orders) {
        Set<String> statuses = orders.stream()
                .map(Order::getStatus)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));

        Set<String> types = orders.stream()
                .map(Order::getProductType)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));

        Set<String> brands = orders.stream()
                .map(Order::getBrand)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));

        List<LabeledValue> statusItems = new ArrayList<>();
        statusItems.add(null);
        for (String status : statuses) {
            OrderStatusLocal mapped = EnumMapper.fromOrderStatusString(status);
            String label = mapped != null ? mapped.getLabel() : status;
            statusItems.add(new LabeledValue(status, label));
        }

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

        filterStatus.getItems().setAll(statusItems);
        filterType.getItems().setAll(typeItems);
        filterBrand.getItems().setAll(brandItems);

        filterStatus.setPromptText("Todos los estados");
        filterType.setPromptText("Todos los tipos");
        filterBrand.setPromptText("Todas las marcas");
    }

    @FXML
    private void applyFilters() {
        LabeledValue selectedStatus = filterStatus.getValue();
        LabeledValue selectedType = filterType.getValue();
        LabeledValue selectedBrand = filterBrand.getValue();
        String user = filterUser.getText() != null ? filterUser.getText().trim().toLowerCase() : "";

        String status = selectedStatus != null ? selectedStatus.value() : null;
        String type = selectedType != null ? selectedType.value() : null;
        String brand = selectedBrand != null ? selectedBrand.value() : null;

        List<Order> filtered = allOrders.stream()
                .filter(o -> status == null || status.equalsIgnoreCase(o.getStatus()))
                .filter(o -> type == null || type.equalsIgnoreCase(o.getProductType()))
                .filter(o -> brand == null || brand.equalsIgnoreCase(o.getBrand()))
                .filter(o -> user.isEmpty() || (o.getUsername() != null && o.getUsername().toLowerCase().contains(user)))
                .collect(Collectors.toList());

        updateList(filtered);
    }

    private void updateList(List<Order> orders) {
        listViewOrders.getItems().setAll(orders);
        listViewOrders.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Order item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox cell = new HBox(10);
                    cell.getStyleClass().add("custom-list-cell");

                    Label name = new Label("Producto: " + safe(item.getProductName()));
                    name.getStyleClass().add("product-name");

                    Label type = new Label("Tipo: " + safeLabel(item.getProductType(), ProductType.values()));
                    type.getStyleClass().add("product-info");

                    Label brand = new Label("Marca: " + safe(item.getBrand()));
                    brand.getStyleClass().add("product-info");

                    Label user = new Label("Usuario: " + safe(item.getUsername()));
                    user.getStyleClass().add("product-info");

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    HBox statusBox = buildStatusBox(item.getStatus());

                    cell.getChildren().addAll(name, type, brand, user, spacer, statusBox);
                    setGraphic(cell);
                }
            }
        });
    }

    private HBox buildStatusBox(String statusStr) {
        OrderStatusLocal status = EnumMapper.fromOrderStatusString(statusStr);

        String label = status != null ? status.getLabel() : "Desconocido";
        String color = status != null ? status.getColorHex() : "#888888";

        Label dot = new Label();
        dot.setPrefSize(12, 12);
        dot.setStyle("-fx-background-radius: 6em; -fx-background-color: " + color + ";");

        Label text = new Label(" " + label);
        text.getStyleClass().add("status-label");

        HBox box = new HBox(5, dot, text);
        box.setPrefWidth(150);
        return box;
    }

    private String safe(String val) {
        return val != null ? val : "(no disponible)";
    }

    private String safeLabel(String enumValue, ProductType[] values) {
        for (ProductType p : values) {
            if (p.name().equalsIgnoreCase(enumValue)) return p.getLabel();
        }
        return "(tipo desconocido)";
    }

    @FXML
    private void onBack() {
        ViewManager.load("/com/adama_ui/OrderMainView.fxml");
    }
}