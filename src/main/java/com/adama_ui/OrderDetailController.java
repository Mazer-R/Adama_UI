package com.adama_ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class OrderDetailController {

    @FXML private TextField fieldProductType;
    @FXML private TextField fieldBrand;
    @FXML private TextField fieldUsername;
    @FXML private TextArea fieldMotivo;

    private static Order currentOrder;
    private final OrderService orderService = new OrderService();

    public static void setCurrentOrder(Order order) {
        currentOrder = order;
    }

    @FXML
    public void initialize() {
        if (currentOrder != null) {
            fieldProductType.setText(safe(currentOrder.getProductType()));
            fieldBrand.setText(safe(currentOrder.getBrand()));
            fieldUsername.setText(safe(currentOrder.getUsername()));
            fieldMotivo.setText(currentOrder.getDetails() != null ? currentOrder.getDetails() : "(Sin motivo)");
        }
    }

    private String safe(String val) {
        return val != null ? val : "N/A";
    }

    @FXML
    private void onBack() {
        ViewManager.loadView("/com/adama_ui/ManageOrdersView.fxml");
    }

    @FXML
    private void onAccept() {
        if (currentOrder == null) return;

        try {
            orderService.validateOrder(currentOrder.getId());
            showAlert("Orden aceptada", "La orden se ha validado correctamente.");
            onBack();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo validar la orden.");
        }
    }

    @FXML
    private void onDeny() {
        if (currentOrder == null) return;

        try {
            orderService.denyOrder(currentOrder.getId());
            showAlert("Orden rechazada", "La orden ha sido rechazada correctamente.");
            onBack();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo rechazar la orden.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
