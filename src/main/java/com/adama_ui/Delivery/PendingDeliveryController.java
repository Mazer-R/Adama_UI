package com.adama_ui.Delivery;

import com.adama_ui.Order.Dto.Order;
import com.adama_ui.Order.Dto.OrderService;
import com.adama_ui.Product.DTO.Product;
import com.adama_ui.Product.ProductService;
import com.adama_ui.auth.SessionManager;
import com.adama_ui.util.ViewManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.List;

public class PendingDeliveryController {

    @FXML
    private ListView<Order> contenido;

    private final OrderService orderService = new OrderService();
    private final ProductService productService = new ProductService();

    @FXML
    public void initialize() {
        loadValidatedOrders();
    }

    private void loadValidatedOrders() {
        new Thread(() -> {
            try {
                // Validamos sesión primero
                SessionManager session = SessionManager.getInstance();
                String userId = session.getUserId();

                if (userId == null || userId.isBlank()) {
                    Platform.runLater(() -> showError("No se ha podido identificar tu sesión."));
                    return;
                }

                // Obtenemos las órdenes validadas
                List<Order> orders = orderService.getValidatedOrder();

                // Enriquecemos cada orden con datos del producto
                for (Order order : orders) {
                    try {
                        Product product = productService.getProductById(order.getProductId());
                        if (product != null) {
                            order.setProductName(product.getName());
                            order.setProductType(product.getType());
                            order.setBrand(product.getBrand());
                        }
                    } catch (Exception e) {
                        System.err.println("⚠️ Error obteniendo producto con ID: " + order.getProductId());
                    }
                }

                Platform.runLater(() -> {
                    contenido.getItems().setAll(orders);
                    contenido.setCellFactory(list -> new ListCell<>() {
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

                                // Botón Detalle
                                Button btnDetalle = new Button("Detalle");
                                btnDetalle.setOnAction(e -> {
                                    openDetailView(item);
                                });

                                cell.getChildren().addAll(name, type, brand, spacer, statusBox, btnDetalle);
                                setGraphic(cell);
                            }
                        }
                    });
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showError("Error al cargar entregas pendientes."));
            }
        }).start();
    }

    private void showError(String msg) {
        contenido.getItems().clear();
        Label label = new Label(msg);
        label.setStyle("-fx-text-fill: red; -fx-padding: 10;");
        contenido.setPlaceholder(label);
    }

    private String safe(String value) {
        return value != null ? value : "(sin datos)";
    }

    private HBox buildStatusBox(String status) {
        String label = switch (status.toUpperCase()) {
            case "VALIDATED" -> "Aceptada";
            case "ORDERED" -> "Solicitada";
            case "FULFILLED" -> "Entregada";
            case "DENIED" -> "Rechazada";
            default -> "Desconocida";
        };

        String color = switch (status.toUpperCase()) {
            case "VALIDATED" -> "#2ecc71";   // verde
            case "ORDERED" -> "#f1c40f";     // amarillo
            case "FULFILLED" -> "#bdc3c7";   // gris
            case "DENIED" -> "#e74c3c";      // rojo
            default -> "#ffffff";            // blanco
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

    @FXML
    private void openDetailView(Order order) {
        DeliveryDetailController.setCurrentOrder(order);
        ViewManager.getInstance().load("/com/adama_ui/Delivery/DeliveryDetailView.fxml");

    }
}
