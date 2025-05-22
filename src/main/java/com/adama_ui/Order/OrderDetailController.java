package com.adama_ui.Order;

import com.adama_ui.Order.Dto.Order;
import com.adama_ui.Order.Dto.OrderService;
import com.adama_ui.style.AppTheme;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;

public class OrderDetailController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField fieldProductType;
    @FXML
    private TextField fieldBrand;
    @FXML
    private TextField fieldUsername;
    @FXML
    private TextArea fieldMotivo;

    @Setter
    private static Order currentOrder;
    private final OrderService orderService = new OrderService();

    @FXML
    public void initialize() {
        AppTheme.applyThemeTo(rootPane);

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
    private void onAccept() {
        if (currentOrder == null) return;

        try {
            orderService.validateOrder(currentOrder.getId());
            showInfoAlert("Orden aceptada", "La orden se ha validado correctamente.");
            ViewManager.getInstance().goBack();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error", "No se pudo validar la orden.");
        }
    }

    @FXML
    private void onDeny() {
        if (currentOrder == null) return;

        try {
            orderService.denyOrder(currentOrder.getId());
            showInfoAlert("Orden rechazada", "La orden ha sido rechazada correctamente.");
            ViewManager.getInstance().goBack(); // Volver a Profile + Manage
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error", "No se pudo rechazar la orden.");
        }
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}