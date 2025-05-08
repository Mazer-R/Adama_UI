package com.adama_ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
public class MainScreenController {
    @FXML
    private BorderPane mainContainer;

    @FXML
    public void initialize() {
        ViewManager.setMainContainer(mainContainer);
        ViewManager.loadView("/com/adama_ui/HomeView.fxml");
    }

    @FXML
    private void loadHomeView() {
        ViewManager.loadView("/com/adama_ui/HomeView.fxml");
    }

    @FXML
    private void loadProfileView() {
        ViewManager.loadView("/com/adama_ui/ProfileView.fxml");
    }
    @FXML
    private void loadSettingsView(){
        ViewManager.loadView("/com/adama_ui/SettingsView.fxml");
    }
    @FXML
    private void loadMessagesView(){
        ViewManager.loadView("/com/adama_ui/MessagesMainView.fxml");
    }

}