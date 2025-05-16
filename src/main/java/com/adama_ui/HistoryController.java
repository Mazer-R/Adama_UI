package com.adama_ui;

import com.adama_ui.auth.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HistoryController {

    @FXML private ListView<Order> listViewOrders;
    @FXML private ComboBox<String> filterStatus;
    @FXML private ComboBox<String> filterType;
    @FXML private ComboBox<String> filterBrand;
    @FXML private TextField filterUser;

    private final OrderService orderService = new OrderService();
    private final ProductService productService = new ProductService();
    private final UserService userService = new UserService();

    private List<Order> allOrders = new ArrayList<>();

    @FXML
    public void initialize() {
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
                        System.err.println("⚠️ Error al enriquecer orden ID: " + order.getId());
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
                    Label error = new Label("⚠ Error loading orders.");
                    error.setStyle("-fx-text-fill: red; -fx-padding: 10;");
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

        // Insertar opción por defecto (sin filtro)
        List<String> statusList = new ArrayList<>();
        statusList.add(null);
        statusList.addAll(statuses);

        List<String> typeList = new ArrayList<>();
        typeList.add(null);
        typeList.addAll(types);

        List<String> brandList = new ArrayList<>();
        brandList.add(null);
        brandList.addAll(brands);

        filterStatus.getItems().setAll(statusList);
        filterStatus.setPromptText("Todos los estados");

        filterType.getItems().setAll(typeList);
        filterType.setPromptText("Todos los tipos");

        filterBrand.getItems().setAll(brandList);
        filterBrand.setPromptText("Todas las marcas");
    }

    @FXML
    private void applyFilters() {
        String status = filterStatus.getValue();
        String type = filterType.getValue();
        String brand = filterBrand.getValue();
        String user = filterUser.getText() != null ? filterUser.getText().trim().toLowerCase() : "";

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

                    Label name = new Label("Product: " + safe(item.getProductName()));
                    Label type = new Label("Type: " + safe(item.getProductType()));
                    Label brand = new Label("Brand: " + safe(item.getBrand()));
                    Label user = new Label("User: " + safe(item.getUsername()));

                    Stream.of(name, type, brand, user)
                            .forEach(label -> label.setStyle("-fx-text-fill: white;"));

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    HBox statusBox = buildStatusBox(item.getStatus());

                    cell.getChildren().addAll(name, type, brand, user, spacer, statusBox);
                    setGraphic(cell);
                }
            }
        });
    }

    private HBox buildStatusBox(String status) {
        String label = switch (status.toUpperCase()) {
            case "ORDERED" -> "Solicitada";
            case "VALIDATED" -> "Aceptada";
            case "FULFILLED" -> "Entregada";
            case "DENIED" -> "Rechazada";
            default -> "Desconocida";
        };

        String color = switch (status.toUpperCase()) {
            case "ORDERED" -> "#f1c40f";
            case "VALIDATED" -> "#2ecc71";
            case "FULFILLED" -> "#bdc3c7";
            case "DENIED" -> "#e74c3c";
            default -> "#ffffff";
        };

        Label dot = new Label();
        dot.setPrefSize(12, 12);
        dot.setStyle("-fx-background-radius: 6em; -fx-background-color: " + color + ";");

        Label text = new Label(" " + label);
        text.setStyle("-fx-text-fill: white;");

        HBox box = new HBox(5, dot, text);
        box.setPrefWidth(150);
        return box;
    }

    @FXML
    private void onBack() {
        ViewManager.loadView("/com/adama_ui/ProfileView.fxml");
    }

    private String safe(String val) {
        return val != null ? val : "(not available)";
    }
}
