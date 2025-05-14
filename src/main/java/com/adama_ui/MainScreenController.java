package com.adama_ui;

import com.adama_ui.style.AppTheme;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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

        if (!themeToggleButton.getStyleClass().contains("theme-toggle")) {
            themeToggleButton.getStyleClass().add("theme-toggle");
        }

        updateThemeToggleIcon();
    }

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

    // Carga vistas en el centro
    @FXML private void loadHomeView() {
        clearLeftPane();
        ViewManager.loadView("/com/adama_ui/HomeView.fxml");
    }

    @FXML
    private void loadProfileView() {
        clearLeftPane(); // ← Asegura que no quede el menú anterior cargado
        ViewManager.loadView("/com/adama_ui/ProfileView.fxml");
    }

    @FXML private void loadSettingsView() {
        clearLeftPane();
        ViewManager.loadView("/com/adama_ui/SettingsView.fxml");
    }

    @FXML private void loadMessagesView() {
        clearLeftPane();
        ViewManager.loadView("/com/adama_ui/MessagesMainView.fxml");
    }

    @FXML public void loadWarehouseView() {
        clearLeftPane();
        ViewManager.loadView("/com/adama_ui/ProductManagement.fxml");
    }

    public void setCenterContent(Node node) {
        mainContainer.setCenter(node);
    }

    private void loadLeftPane(String fxmlPath) {
        try {
            Parent leftContent = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainContainer.setLeft(leftContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearLeftPane() {
        mainContainer.setLeft(null);
    }
}
