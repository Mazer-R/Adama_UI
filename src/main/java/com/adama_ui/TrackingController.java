package com.adama_ui;

import com.adama_ui.auth.SessionManager;
import com.adama_ui.style.AppTheme;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.List;

public class TrackingController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private ListView<Order> listViewSolicitudes;

    private final OrderService orderService = new OrderService();
    private final ProductService productService = new ProductService();

    @FXML
    public void initialize() {
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                AppTheme.applyTheme(newScene);
            }
        });
        loadOrders();
    }

    private void loadOrders() {
        new Thread(() -> {
            try {
                SessionManager session = SessionManager.getInstance();
                String userId = session.getUserId();

                if (userId == null || userId.isBlank()) {
                    Platform.runLater(() -> showError("No se ha podido identificar tu sesión. Cierra y vuelve a iniciar sesión."));
                    return;
                }

                List<Order> orders = orderService.getOrdersByUser(userId);

                for (Order order : orders) {
                    try {
                        Product product = productService.getProductById(order.getProductId());
                        if (product != null) {
                            order.setProductName(product.getName());
                            order.setProductType(product.getType());
                            order.setBrand(product.getBrand());
                        }
                    } catch (Exception e) {
                        System.err.println("No se pudo obtener producto con ID: " + order.getProductId());
                    }
                }

                Platform.runLater(() -> {
                    listViewSolicitudes.getItems().setAll(orders);
                    listViewSolicitudes.setCellFactory(list -> new ListCell<>() {
                        @Override
                        protected void updateItem(Order item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setGraphic(null);
                            } else {
                                HBox cell = new HBox(10);
                                cell.getStyleClass().add("custom-list-cell");

                                Label name = new Label(safe(item.getProductName()));
                                name.getStyleClass().add("product-name");
                                name.setPrefWidth(200);

                                Label type = new Label(safe(item.getProductType()));
                                type.getStyleClass().add("product-info");
                                type.setPrefWidth(100);

                                Label brand = new Label(safe(item.getBrand()));
                                brand.getStyleClass().add("product-info");
                                brand.setPrefWidth(100);

                                Region spacer = new Region();
                                HBox.setHgrow(spacer, Priority.ALWAYS);

                                HBox statusBox = buildStatusBox(item.getStatus());

                                cell.getChildren().addAll(name, type, brand, spacer, statusBox);
                                setGraphic(cell);
                            }
                        }
                    });
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showError("Ocurrió un error al cargar tus solicitudes. Intenta más tarde."));
            }
        }).start();
    }

    private void showError(String message) {
        listViewSolicitudes.getItems().clear();
        Label error = new Label(message);
        error.getStyleClass().add("product-name"); // texto blanco en fondo oscuro
        error.setStyle("-fx-text-fill: red; -fx-padding: 10;");
        listViewSolicitudes.setPlaceholder(error);
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
        text.getStyleClass().add("status-label");

        HBox box = new HBox(5, dot, text);
        box.setPrefWidth(150);
        return box;
    }

    private String safe(String val) {
        return val != null ? val : "(no disponible)";
    }
}