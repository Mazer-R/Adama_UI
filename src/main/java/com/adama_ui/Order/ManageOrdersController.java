package com.adama_ui.Order;

import com.adama_ui.Product.DTO.Product;
import com.adama_ui.Product.ProductService;
import com.adama_ui.style.AppTheme;
import com.adama_ui.util.UserService;
import com.adama_ui.util.ViewManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageOrdersController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private ListView<Order> listViewManage;

    @FXML
    private Label emptyLabel;

    private final OrderService orderService = new OrderService();
    private final ProductService productService = new ProductService();
    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        AppTheme.applyThemeTo(rootPane);
        loadOrderedRequests();
    }

    public void loadOrderedRequests() {
        new Thread(() -> {
            try {
                List<Order> orders = orderService.getOrdersByStatus("ordered");
                Map<String, String> userIdToUsername = new HashMap<>();

                for (Order order : orders) {
                    // Obtener datos del producto
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

                    // Obtener username a partir de userId
                    try {
                        String userId = order.getUserId();
                        if (userIdToUsername.containsKey(userId)) {
                            order.setUsername(userIdToUsername.get(userId));
                        } else {
                            String username = userService.getUsernameById(userId);
                            order.setUsername(username);
                            userIdToUsername.put(userId, username);
                        }
                    } catch (Exception e) {
                        System.err.println("No se pudo obtener usuario con ID: " + order.getUserId() + " → " + e.getMessage());
                        order.setUsername("Desconocido");
                    }
                }

                Platform.runLater(() -> {
                    if (orders == null || orders.isEmpty()) {
                        listViewManage.getItems().clear();
                        emptyLabel.setVisible(true);
                        emptyLabel.setManaged(true);
                    } else {
                        emptyLabel.setVisible(false);
                        emptyLabel.setManaged(false);
                        listViewManage.getItems().setAll(orders);
                        listViewManage.setCellFactory(list -> new ListCell<>() {
                            @Override
                            protected void updateItem(Order item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                    setGraphic(null);
                                } else {
                                    HBox cell = new HBox(10);
                                    cell.getStyleClass().add("custom-list-cell");

                                    Label name = new Label(safe(item.getProductName()));
                                    name.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                                    name.setPrefWidth(200);

                                    Label type = new Label(safe(item.getProductType()));
                                    type.setStyle("-fx-text-fill: white;");
                                    type.setPrefWidth(100);

                                    Label brand = new Label(safe(item.getBrand()));
                                    brand.setStyle("-fx-text-fill: white;");
                                    brand.setPrefWidth(100);

                                    Label user = new Label("Solicitado por: " + safe(item.getUsername()));
                                    user.setStyle("-fx-text-fill: white;");
                                    user.setPrefWidth(200);

                                    Region spacer = new Region();
                                    HBox.setHgrow(spacer, Priority.ALWAYS);

                                    HBox statusBox = buildStatusBox(item.getStatus());

                                    Button detailButton = new Button("Detalle");
                                    detailButton.setStyle("-fx-background-color: #00A651; -fx-text-fill: white;");
                                    detailButton.setOnAction(e -> {
                                        OrderDetailController.setCurrentOrder(item);
                                        ViewManager.load("/com/adama_ui/Order/OrderDetailView.fxml");
                                    });

                                    cell.getChildren().addAll(name, type, brand, user, spacer, statusBox, detailButton);
                                    setGraphic(cell);
                                }
                            }
                        });
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    listViewManage.getItems().clear();
                    emptyLabel.setText("Error al cargar órdenes.");
                    emptyLabel.setVisible(true);
                    emptyLabel.setManaged(true);
                });
            }
        }).start();
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

    private String safe(String val) {
        return val != null ? val : "(no disponible)";
    }

    public void loadPendingOrders() {
        loadOrderedRequests();
    }
}