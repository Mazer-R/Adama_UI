package com.adama_ui.Order;

import com.adama_ui.util.Reloadable;
import com.adama_ui.auth.SessionManager;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class OrderMainViewController {

    @FXML
    private StackPane contentArea;
    @FXML
    private VBox profileMenu;
    @FXML
    private Button btnOrder;
    @FXML
    private Button btnTracking;
    @FXML
    private Button btnManage;
    @FXML
    private Button btnHistory;
    ViewManager viewManager = ViewManager.getInstance();

    private static String currentSubview = null;

    public void initialize() {
        boolean isManagerOrAdmin = SessionManager.getInstance().isAdminOrManager();
        if (btnOrder != null) {
            btnOrder.setOnAction(event -> {
                viewManager.loadInto("/com/adama_ui/Order/OrderView.fxml", contentArea, () -> {
                    currentSubview = "ORDER";
                    highlightMenuButton(btnOrder);

                    Object controller = viewManager.getCurrentController();
                    if (controller instanceof Reloadable reloadable) {
                        reloadable.onReload();
                    }
                });
            });
        }

        if (btnTracking != null) {
            btnTracking.setOnAction(event -> {
                viewManager.loadInto("/com/adama_ui/Order/OrderTrackingView.fxml", contentArea, () -> {
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
                    viewManager.loadInto("/com/adama_ui/Order/ManageOrdersView.fxml", contentArea, () -> {
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
                    viewManager.loadInto("/com/adama_ui/Order/OrderHistoryView.fxml", contentArea, () -> {
                        currentSubview = "HISTORY";
                        highlightMenuButton(btnHistory);
                    });
                });
            }
        }

        if (currentSubview == null) {
            btnOrder.fire();
        } else {
            switch (currentSubview) {
                case "ORDER" -> btnOrder.fire();
                case "TRACKING" -> btnTracking.fire();
                case "MANAGE" -> btnManage.fire();
                case "HISTORY" -> btnHistory.fire();
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

}
