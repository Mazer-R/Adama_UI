package com.adama_ui;

import com.adama_ui.Message.DTO.MessageService;
import com.adama_ui.auth.SessionManager;
import com.adama_ui.style.AppTheme;
import com.adama_ui.util.ButtonCreator;
import com.adama_ui.util.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.management.relation.Role;
import java.util.Optional;

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
    @FXML private Button logoutButton;

    private final Image sunIcon = new Image(getClass().getResourceAsStream("/icons/light-mode.png"));
    private final Image moonIcon = new Image(getClass().getResourceAsStream("/icons/night-mode.png"));

    @FXML
    public void initialize() {
        buttonCreator = new ButtonCreator();
        ViewManager.setMainContainer(mainContainer);
        ViewManager.load("/com/adama_ui/HomeView.fxml");
        configureButtons(SessionManager.getInstance().getRole());
        buttonCreator.configureIconButton(logoutButton, "/ExternalResources/LogoutIcon.png", 40);



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
    public void loadDeliveryView() {
        ViewManager.load("/com/adama_ui/Delivery/DeliveryMainView.fxml");
    }

    @FXML
    private void loadUserView() {
        ViewManager.load("/com/adama_ui/User/UserMainView.fxml");

    }
    @FXML
    private void logout() {
        ButtonType confirmButton = new ButtonType("Cerrar sesión", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Permanece aquí", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Estás seguro que deseas cerrar sesión?",
                confirmButton,
                cancelButton
        );

        alert.setTitle("Confirmar cierre de sesión");
        alert.setHeaderText("¡Hasta pronto!");
        alert.setContentText("Se perderá cualquier cambio no guardado");

        Optional<ButtonType> result = alert.showAndWait();

        result.ifPresent(buttonType -> {
            if (buttonType == confirmButton) {
                try {
                    SessionManager.getInstance().clearSession();

                    Stage loginStage = new Stage();
                    Parent root = ViewManager.loadForScene("/com/adama_ui/LoginToApp.fxml");
                    loginStage.setScene(new Scene(root));
                    loginStage.setTitle("Login - Adama");
                    loginStage.show();
                    loginStage.setMaximized(true);

                    Stage currentStage = (Stage) mainContainer.getScene().getWindow();
                    currentStage.close();

                } catch (Exception e) {
                    System.err.println("Error al cerrar sesión:");
                    MessageService.showError("Error al cargar la vista de login");
                }
            }
        });
    }

    private void configureButtons(String role) {
        switch (role) {
            case "ROLE_USER" ->{
                buttonCreator.configureIconButton(Home, "/ExternalResources/HomeIcon.png", 40);
                buttonCreator.configureIconButton(Order, "/ExternalResources/OrderIcon.png", 40);
                buttonCreator.configureIconButton(Messages, "/ExternalResources/MessageIcon.png", 40);
                Products.setVisible(false);
                Delivery.setVisible(false);
                Users.setVisible(false);
            }
            case "ROLE_WAREHOUSE" ->{
                buttonCreator.configureIconButton(Home, "/ExternalResources/HomeIcon.png", 40);
                buttonCreator.configureIconButton(Order, "/ExternalResources/OrderIcon.png", 40);
                buttonCreator.configureIconButton(Messages, "/ExternalResources/MessageIcon.png", 40);
                buttonCreator.configureIconButton(Products, "/ExternalResources/ProductIcon.png", 40);
                buttonCreator.configureIconButton(Delivery, "/ExternalResources/DeliveryIcon.png", 40);
                Users.setVisible(false);
            }
            case "ROLE_MANAGER"->{
                buttonCreator.configureIconButton(Home, "/ExternalResources/HomeIcon.png", 40);
                buttonCreator.configureIconButton(Order, "/ExternalResources/OrderIcon.png", 40);
                buttonCreator.configureIconButton(Messages, "/ExternalResources/MessageIcon.png", 40);
                Products.setVisible(false);
                Delivery.setVisible(false);
                Users.setVisible(false);
            }
            case "ROLE_ADMIN" ->{
                buttonCreator.configureIconButton(Home, "/ExternalResources/HomeIcon.png", 40);
                buttonCreator.configureIconButton(Order, "/ExternalResources/OrderIcon.png", 40);
                buttonCreator.configureIconButton(Messages, "/ExternalResources/MessageIcon.png", 40);
                buttonCreator.configureIconButton(Users, "/ExternalResources/UserIcon.png", 40);
                buttonCreator.configureIconButton(Products, "/ExternalResources/ProductIcon.png", 40);
                buttonCreator.configureIconButton(Delivery, "/ExternalResources/DeliveryIcon.png", 40);
            }
        }
    }
}