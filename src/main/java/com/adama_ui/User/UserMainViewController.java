package com.adama_ui.User;

import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.awt.*;

public class UserMainViewController {
    @FXML public javafx.scene.control.Button btnManageUser;
    @FXML private StackPane contentPane;
    @FXML private VBox userMainMenu;
    public javafx.scene.control.Button btnAddUser;
    @FXML
    private void initialize() {
        // Cargar vista inicial (opcional)
        ViewManager.loadInto("/com/adama_ui/User/AddUser.fxml", contentPane);
    }

    @FXML
    private void onAddUserClick() {
        ViewManager.loadInto("/com/adama_ui/User/AddUser.fxml", contentPane);
    }

    @FXML
    private void onManageUserClick() {
        ViewManager.loadInto("/com/adama_ui/User/UserManagement.fxml", contentPane);
    }

    @FXML
    private void onBackClick() {
        ViewManager.load("/com/adama_ui/HomeView.fxml");
    }

}
