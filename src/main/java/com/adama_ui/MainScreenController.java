package com.adama_ui;

import com.adama_ui.Message.DTO.MessageService;
import com.adama_ui.auth.SessionManager;
import com.adama_ui.style.AppTheme;
import com.adama_ui.util.ButtonCreator;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Optional;

public class MainScreenController {

    private ButtonCreator buttonCreator;
    @FXML
    private Button Order;
    @FXML
    private Button Home;
    @FXML
    private Button Messages;
    @FXML
    private Button Products;
    @FXML
    private Button Users;
    @FXML
    private Button Delivery;
    @FXML
    private BorderPane mainContainer;
    @FXML
    private ToggleButton themeToggleButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button backButton;
    private ViewManager viewManager = ViewManager.getInstance();

    private final Image sunIcon = new Image(getClass().getResourceAsStream("/icons/light-mode.png"));
    private final Image moonIcon = new Image(getClass().getResourceAsStream("/icons/night-mode.png"));

    @FXML
    public void initialize() {
        buttonCreator = new ButtonCreator();
        viewManager.setMainContainer(mainContainer);
        viewManager.load("/com/adama_ui/HomeView.fxml");
        configureButtonsByRole(SessionManager.getInstance().getRole());
        buttonCreator.configureIconButton(logoutButton, "/ExternalResources/LogoutIcon.png");
        buttonCreator.configureLongButton(backButton, "/ExternalResources/BackIcon.png");


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
        boolean isDark = AppTheme.isDark();
        Image icon = isDark ? moonIcon : sunIcon;
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(16);
        iconView.setFitHeight(16);
        themeToggleButton.setGraphic(iconView);
        themeToggleButton.setSelected(!isDark);
    }

    @FXML
    private void onToggleTheme() {
        boolean nuevoModo = !AppTheme.isDark();
        AppTheme.setDark(nuevoModo);

        Scene scene = mainContainer.getScene();
        if (scene != null) {
            AppTheme.applyTheme(scene);
        } else {
            AppTheme.applyThemeTo(mainContainer);
        }

        updateThemeToggleIcon();
        viewManager.reload();
    }

    @FXML
    private void loadHomeView() {
        viewManager.load("/com/adama_ui/HomeView.fxml");
    }

    @FXML
    private void loadOrderView() {
        viewManager.load("/com/adama_ui/Order/OrderMainView.fxml");
    }


    @FXML
    private void loadMessagesView() {
        viewManager.load("/com/adama_ui/Message/MessagesMainView.fxml");
    }

    @FXML
    public void loadProductView() {
        viewManager.load("/com/adama_ui/Product/ProductMainView.fxml");
    }

    @FXML
    public void loadDeliveryView() {
        viewManager.load("/com/adama_ui/Delivery/DeliveryMainView.fxml");
    }

    @FXML
    private void loadUserView() {
        viewManager.load("/com/adama_ui/User/UserMainView.fxml");

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
                    Parent root = viewManager.loadForScene("/com/adama_ui/LoginToApp.fxml");
                    loginStage.setScene(new Scene(root));
                    loginStage.setTitle("Login - Adama");
                    loginStage.show();
                    loginStage.setMaximized(true);

                    Stage currentStage = (Stage) mainContainer.getScene().getWindow();
                    currentStage.close();
                    viewManager.clearHistory();

                } catch (Exception e) {
                    System.err.println("Error al cerrar sesión:");
                    MessageService.showError("Error al cargar la vista de login");
                }
            }
        });
    }

    @FXML
    private void goBack() {
        System.out.println("BACKBUTTON PRESSED");
        viewManager.goBack();
    }

    private void configureButton(Button button, String iconPath) {
        buttonCreator.configureIconButton(button, iconPath);
        button.setVisible(true);
    }

    private void configureButtonsByRole(String role) {
        configureButton(Home, "/ExternalResources/HomeIcon.png");
        configureButton(Order, "/ExternalResources/OrderIcon.png");
        configureButton(Messages, "/ExternalResources/MessageIcon.png");

        Products.setVisible(false);
        Delivery.setVisible(false);
        Users.setVisible(false);

        switch (role) {
            case "ROLE_WAREHOUSE" -> {
                configureButton(Products, "/ExternalResources/ProductIcon.png");
                configureButton(Delivery, "/ExternalResources/DeliveryIcon.png");
            }
            case "ROLE_ADMIN" -> {
                configureButton(Users, "/ExternalResources/UserIcon.png");
                configureButton(Products, "/ExternalResources/ProductIcon.png");
                configureButton(Delivery, "/ExternalResources/DeliveryIcon.png");
            }
        }
    }
}