package com.adama_ui;

import com.adama_ui.style.AppTheme;
import com.adama_ui.util.ButtonCreator;
import com.adama_ui.util.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainScreenController {

    private ButtonCreator buttonCreator;
    @FXML private Button Order;
    @FXML private Button Home;
    @FXML private Button Messages;
    @FXML private Button Products;
    @FXML private Button Users;
    @FXML private Button Delivery;
    @FXML private BorderPane mainContainer;
    @FXML private ToggleButton themeToggleButton;

    private final Image sunIcon = new Image(getClass().getResourceAsStream("/icons/light-mode.png"));
    private final Image moonIcon = new Image(getClass().getResourceAsStream("/icons/night-mode.png"));

    @FXML
    public void initialize() {
        buttonCreator = new ButtonCreator();
        ViewManager.setMainContainer(mainContainer);
        ViewManager.load("/com/adama_ui/HomeView.fxml");
        configureButtons();


        mainContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    stage.setUserData(this);
                    stage.setMaximized(true);
                }
                AppTheme.applyTheme(newScene);
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
        themeToggleButton.setSelected(!isDark); }


    @FXML
    private void onToggleTheme() {
        boolean nuevoModo = !AppTheme.isDarkMode();
        AppTheme.setDarkMode(nuevoModo);


        ViewManager.refreshCurrentView(); // Recarga sin usar caché
        updateThemeToggleIcon();
    }
    @FXML
    private void loadHomeView() {ViewManager.load("/com/adama_ui/HomeView.fxml");
    }

    @FXML
    private void loadOrderView() {ViewManager.load("/com/adama_ui/Order/OrderMainView.fxml");}


    @FXML
    private void loadMessagesView() {
        ViewManager.load("/com/adama_ui/Message/MessagesMainView.fxml");
    }

    @FXML
    public void loadProductView() {
        ViewManager.load("/com/adama_ui/Product/ProductMainView.fxml");
    }

    @FXML
    private void loadUserView() {
        ViewManager.load("/com/adama_ui/User/AddUser.fxml");
    }
    @FXML
    private void loadSettingsView() {
        ViewManager.load("/com/adama_ui/TestView.fxml");

        javafx.application.Platform.runLater(() -> {
            var node = mainContainer.lookup("#contentPane");
            if (node instanceof StackPane contentPane) {
                ViewManager.loadInto("/com/adama_ui/Product/AddProductView.fxml", contentPane);
            } else {
                System.err.println("⚠️ No se encontró el StackPane con fx:id=\"contentPane\" en ProductMainView.fxml");
            }
        });
    }

    public void setCenterContent(Node node) {
        mainContainer.setCenter(node);
    }

    public void loadDeliveryView(ActionEvent actionEvent) {
    }
    private void configureButtons() {
        buttonCreator.configureIconButton(Home, "/ExternalResources/HomeIcon.png", 40);
        buttonCreator.configureIconButton(Order, "/ExternalResources/OrderIcon.png", 40);
        buttonCreator.configureIconButton(Messages, "/ExternalResources/MessageIcon.png", 40);
        buttonCreator.configureIconButton(Products, "/ExternalResources/ProductIcon.png", 40);
        buttonCreator.configureIconButton(Delivery, "/ExternalResources/DeliveryIcon.png", 40);
    }
}