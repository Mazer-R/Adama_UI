package com.adama_ui.Delivery;

import com.adama_ui.util.Reloadable;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DeliveryViewController {

    @FXML
    private StackPane contentArea;
    @FXML
    private VBox menuBox;
    @FXML
    private Button btnPendientes;
    @FXML
    private Button btnHistorico;

    private static String currentSubview = null;
    ViewManager viewManager = ViewManager.getInstance();

    public void initialize() {

        btnPendientes.setOnAction(event -> {
            viewManager.loadInto("/com/adama_ui/Delivery/PendingDeliveryView.fxml", contentArea, () -> {
                currentSubview = "PENDIENTES";
                highlightMenuButton(btnPendientes);

                Object controller = viewManager.getCurrentController();
                if (controller instanceof Reloadable reloadable) {
                    reloadable.onReload();
                }
            });
        });


        // Carga la subvista activa o por defecto
        if (currentSubview == null) {
            btnPendientes.fire(); // carga pendiente por defecto
        } else {
            switch (currentSubview) {
                case "PENDIENTES" -> btnPendientes.fire();
                case "HISTORICO" -> btnHistorico.fire();
            }
        }
    }

    private void highlightMenuButton(Button activeButton) {
        for (var node : menuBox.getChildren()) {
            if (node instanceof Button button) {
                button.getStyleClass().remove("active-button");
            }
        }
        if (activeButton != null && !activeButton.getStyleClass().contains("active-button")) {
            activeButton.getStyleClass().add("active-button");
        }
    }


}
