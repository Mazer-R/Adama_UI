package com.adama_ui.User;

import com.adama_ui.util.Reloadable;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UserMainViewController implements Reloadable {
    @FXML
    public Button btnManageUser;
    @FXML
    private VBox userMainMenu;
    @FXML
    private StackPane contentPane;
    @FXML
    public Button btnAddUser;
    private static String currentSubview = null;
    @FXML
    public Button btnBack;
    ViewManager viewManager = ViewManager.getInstance();

    @FXML
    private void initialize() {

        btnAddUser.setOnAction(event -> {
            viewManager.loadInto("/com/adama_ui/User/AddUser.fxml", contentPane, () -> {
                currentSubview = "ADD";
                highlightMenuButton(btnAddUser);
                Object controller = viewManager.getCurrentController();
            });
        });

        btnManageUser.setOnAction(event -> {
            viewManager.loadInto("/com/adama_ui/User/UserManagement.fxml", contentPane, () -> {
                currentSubview = "MANAGE";
                highlightMenuButton(btnManageUser);
                Object controller = viewManager.getCurrentController();
                if (controller instanceof Reloadable reloadable) {
                    reloadable.onReload();
                }
            });
        });

        if (currentSubview == null) {
            btnAddUser.fire();
        } else {
            switch (currentSubview) {
                case "ADD" -> btnAddUser.fire();
                case "MANAGE" -> btnManageUser.fire();
            }
        }
    }


    private void highlightMenuButton(Button activeButton) {
        for (var node : userMainMenu.getChildren()) {
            if (node instanceof Button button) {
                button.getStyleClass().remove("active-button");
            }
        }

        if (activeButton != null && !activeButton.getStyleClass().contains("active-button")) {
            activeButton.getStyleClass().add("active-button");
        }
    }

    @Override
    public void onReload() {
        if (currentSubview != null) {
            switch (currentSubview) {
                case "ADD" -> btnAddUser.fire();
                case "MANAGE" -> btnManageUser.fire();
            }
        }
    }
}

