package com.adama_ui;

import com.adama_ui.style.AppTheme;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class MainScreenController {

    private String authToken;

    @FXML private BorderPane mainContainer;
    @FXML private ToggleButton themeToggleButton;

    private final Image sunIcon = new Image(getClass().getResourceAsStream("/icons/light-mode.png"));
    private final Image moonIcon = new Image(getClass().getResourceAsStream("/icons/night-mode.png"));

    private boolean darkMode = true;

    @FXML
    public void initialize() {
        ViewManager.setMainContainer(mainContainer);
        ViewManager.loadView("/com/adama_ui/HomeView.fxml");

        // Listener para aplicar tema al cambiar escena
        mainContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    stage.setUserData(this);
                    stage.setMaximized(true);
                }
                applyThemeToScene(newScene);
            }
        });

        // Estilo del botón de tema
        if (!themeToggleButton.getStyleClass().contains("theme-toggle")) {
            themeToggleButton.getStyleClass().add("theme-toggle");
        }

        updateThemeToggleIcon();
    }

    // Métodos del tema
    private void applyThemeToScene(Scene scene) {
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(AppTheme.class.getResource(AppTheme.getThemePath(darkMode)).toExternalForm());
            updateThemeToggleIcon();
        }
    }

    private void updateThemeToggleIcon() {
        Image icon = darkMode ? moonIcon : sunIcon;
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(16);
        iconView.setFitHeight(16);
        themeToggleButton.setGraphic(iconView);
        themeToggleButton.setSelected(!darkMode);
    }

    @FXML
    private void onToggleTheme() {
        darkMode = !darkMode;
        applyThemeToScene(themeToggleButton.getScene());
    }
    public void setAuthToken(String token) {
        this.authToken = token;
    }

    // Métodos de carga de vistas (de ambas ramas)
    @FXML private void loadHomeView() { ViewManager.loadView("/com/adama_ui/HomeView.fxml"); }
    @FXML private void loadProfileView() { ViewManager.loadView("/com/adama_ui/ProfileView.fxml"); }
    @FXML private void loadSettingsView() { ViewManager.loadView("/com/adama_ui/SettingsView.fxml"); }
    @FXML private void loadMessagesView() { ViewManager.loadView("/com/adama_ui/MessagesMainView.fxml"); }
    @FXML public void loadWarehouseView() { ViewManager.loadView("/com/adama_ui/ProductManagement.fxml"); }

    public void setCenterContent(javafx.scene.Node node) {
        mainContainer.setCenter(node);
    }
}