package com.adama_ui;

import com.adama_ui.auth.SessionManager;
import com.adama_ui.Reloadable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ProfileViewController {

    @FXML private StackPane contentArea;
    @FXML private VBox profileMenu;
    @FXML private Button btnOrder;
    @FXML private Button btnTracking;
    @FXML private Button btnManage;
    @FXML private Button btnHistory;

    private static String currentSubview = null;

    public void initialize() {
        boolean isManagerOrAdmin = SessionManager.getInstance().isAdminOrManager();

        if (btnOrder != null) {
            btnOrder.setOnAction(event -> {
                ViewManager.loadInto("/com/adama_ui/OrderView.fxml", contentArea, () -> {
                    currentSubview = "ORDER";
                    highlightMenuButton(btnOrder);

                    // 🔄 Forzar recarga si el controlador implementa Reloadable
                    Object controller = ViewManager.getCurrentController();
                    if (controller instanceof Reloadable reloadable) {
                        reloadable.onReload();
                    }
                });
            });
        }

        if (btnTracking != null) {
            btnTracking.setOnAction(event -> {
                ViewManager.loadInto("/com/adama_ui/TrackingView.fxml", contentArea, () -> {
                    currentSubview = "TRACKING";
                    highlightMenuButton(btnTracking);
                });
            });
        }

        if (btnManage != null) {
            btnManage.setVisible(isManagerOrAdmin);
            btnManage.setManaged(isManagerOrAdmin);

            if (isManagerOrAdmin) {
                btnManage.setOnAction(event -> {
                    ViewManager.loadInto("/com/adama_ui/ManageOrdersView.fxml", contentArea, () -> {
                        currentSubview = "MANAGE";
                        highlightMenuButton(btnManage);
                    });
                });
            }
        }

        if (btnHistory != null) {
            btnHistory.setVisible(isManagerOrAdmin);
            btnHistory.setManaged(isManagerOrAdmin);

            if (isManagerOrAdmin) {
                btnHistory.setOnAction(event -> {
                    ViewManager.loadInto("/com/adama_ui/HistoryView.fxml", contentArea, () -> {
                        currentSubview = "HISTORY";
                        highlightMenuButton(btnHistory);
                    });
                });
            }
        }

        // Cargar la subvista por defecto o la última usada
        if (currentSubview == null) {
            btnOrder.fire();
        } else {
            switch (currentSubview) {
                case "ORDER" -> btnOrder.fire();
                case "TRACKING" -> btnTracking.fire();
                case "MANAGE" -> {
                    if (btnManage.isVisible()) btnManage.fire();
                }
                case "HISTORY" -> {
                    if (btnHistory.isVisible()) btnHistory.fire();
                }
            }
        }
    }

    private void highlightMenuButton(Button activeButton) {
        for (var node : profileMenu.getChildren()) {
            if (node instanceof Button button) {
                button.getStyleClass().remove("active-button");
            }
        }
        if (activeButton != null && !activeButton.getStyleClass().contains("active-button")) {
            activeButton.getStyleClass().add("active-button");
        }
    }

    @FXML
    private void onBack() {
        ViewManager.load("/com/adama_ui/HomeView.fxml");
        currentSubview = null;
    }

    public void loadManageOrders() {
        if (btnManage != null && btnManage.isVisible()) {
            btnManage.fire();
        }
    }
}
