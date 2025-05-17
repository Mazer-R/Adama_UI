package com.adama_ui;

import com.adama_ui.style.AppTheme;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    @FXML
    public void initialize() {
        // Establecer contenedor principal en ViewManager
        ViewManager.setMainContainer(mainContainer);

        // Cargar vista inicial
        ViewManager.load("/com/adama_ui/HomeView.fxml");

        // Escuchar el cambio de escena para aplicar tema
        mainContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    stage.setUserData(this);
                    stage.setMaximized(true);
                }
                AppTheme.applyTheme(newScene); // Aplica el tema global a la escena
            }
        });

        // Estilo del botón toggle
        if (!themeToggleButton.getStyleClass().contains("theme-toggle")) {
            themeToggleButton.getStyleClass().add("theme-toggle");
        }

        updateThemeToggleIcon();
    }

    /**
     * Actualiza el icono del botón según el modo actual.
     */
    private void updateThemeToggleIcon() {
        boolean isDark = AppTheme.isDarkMode();
        Image icon = isDark ? moonIcon : sunIcon;
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(16);
        iconView.setFitHeight(16);
        themeToggleButton.setGraphic(iconView);
        themeToggleButton.setSelected(!isDark); // True si está en modo claro
    }

    /**
     * Cambia entre modo oscuro y claro.
     */
    @FXML
    private void onToggleTheme() {
        boolean nuevoModo = !AppTheme.isDarkMode();
        AppTheme.setDarkMode(nuevoModo);

        // Aplicar tema a la vista actual y a la escena
        ViewManager.refreshCurrentView(); // Recarga sin usar caché
        updateThemeToggleIcon();
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    // Métodos para navegación entre vistas
    @FXML
    private void loadHomeView() {
        ViewManager.load("/com/adama_ui/HomeView.fxml");
    }

    @FXML
    private void loadOrderView() {ViewManager.load("/com/adama_ui/OrderMainView.fxml");}

    @FXML
    private void loadSettingsView() {
        ViewManager.load("/com/adama_ui/SettingsView.fxml");
    }

    @FXML
    private void loadMessagesView() {
        ViewManager.load("/com/adama_ui/MessagesMainView.fxml");
    }

    @FXML
    public void loadWarehouseView() {
        ViewManager.load("/com/adama_ui/Product/ProductManagement.fxml");
    }

    @FXML
    private void loadUserView() {
        ViewManager.load("/com/adama_ui/User/AddUser.fxml");
    }

    public void setCenterContent(Node node) {
        mainContainer.setCenter(node);
    }
}