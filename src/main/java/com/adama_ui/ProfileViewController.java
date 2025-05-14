package com.adama_ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ProfileViewController {

    @FXML private StackPane contentArea;
    @FXML private VBox profileMenu;
    @FXML private Button btnOrder;
    @FXML private Button btnHistory;


    @FXML
    public void initialize() {
        if (btnOrder != null) {
            btnOrder.setOnAction(event -> {
                OrderViewController controller = ViewManager.loadViewAndReturnController("/com/adama_ui/OrderView.fxml");
                if (controller != null) {
                    controller.loadInStockProducts(); // Solo carga cuando haces clic en ORDER
                }
                highlightMenuButton(btnOrder);
            });
        }
        if (btnHistory != null) {
            btnHistory.setOnAction(event -> {
                ViewManager.loadView("/com/adama_ui/SolicitudHistoryView.fxml");
                highlightMenuButton(btnHistory);
            });
        }
    }

    private void highlightMenuButton(Button activeButton) {
        for (var node : profileMenu.getChildren()) {
            if (node instanceof Button button) {
                button.setStyle("");
            }
        }
        if (activeButton != null) {
            activeButton.setStyle("-fx-border-color: green; -fx-border-width: 2;");
        }
    }

    @FXML
    private void onBack() {
        ViewManager.loadView("/com/adama_ui/HomeView.fxml");
    }
}
