package com.adama_ui;

import com.adama_ui.style.AppTheme;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ProfileViewController {

    @FXML private StackPane contentArea;
    @FXML private VBox profileMenu;

    @FXML private Button btnOrder;
    @FXML private Button btnTracking;
    @FXML private Button btnManage;
    @FXML private Button btnHistory;

    @FXML
    public void initialize() {
        if (btnOrder != null) {
            btnOrder.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/adama_ui/OrderView.fxml"));
                    Node view = loader.load();
                    OrderViewController controller = loader.getController();
                    controller.loadInStockProducts();
                    contentArea.getChildren().setAll(view);
                    AppTheme.applyThemeTo(contentArea);
                    highlightMenuButton(btnOrder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        if (btnTracking != null) {
            btnTracking.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/adama_ui/TrackingView.fxml"));
                    Node view = loader.load();
                    contentArea.getChildren().setAll(view);
                    AppTheme.applyThemeTo(contentArea);
                    highlightMenuButton(btnTracking);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        if (btnManage != null) {
            btnManage.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/adama_ui/ManageOrdersView.fxml"));
                    Node view = loader.load();
                    contentArea.getChildren().setAll(view);
                    AppTheme.applyThemeTo(contentArea);
                    highlightMenuButton(btnManage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        if (btnHistory != null) {
            btnHistory.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/adama_ui/HistoryView.fxml")); // ✅ Corregido
                    Node view = loader.load();
                    contentArea.getChildren().setAll(view);
                    AppTheme.applyThemeTo(contentArea);
                    highlightMenuButton(btnHistory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
