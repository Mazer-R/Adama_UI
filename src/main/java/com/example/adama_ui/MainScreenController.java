package com.example.adama_ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class MainScreenController {
    @FXML
    private BorderPane mainContainer;

    @FXML
    public void initialize()  {
       try{ FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/adama_ui/HomeView.fxml"));
        Pane view = loader.load();
        mainContainer.setCenter(view);
       }
       catch (IOException e){
           e.printStackTrace();
       }
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane view = loader.load();
            mainContainer.setCenter(view);
        } catch (IOException e) {
            System.err.println("Error cargando " + fxmlFile);
            e.printStackTrace();
        }
    }

    @FXML
    private void loadHomeView() { loadView("/com/example/adama_ui/HomeView.fxml"); }

    @FXML
    private void loadProfileView() { loadView("/com/example/adama_ui/ProfileView.fxml"); }

    @FXML
    private void loadSettingsView() { loadView("/com/example/adama_ui/SettingsView.fxml"); }

    @FXML
    private void loadMessagesView() { loadView("/com/example/adama_ui/MessagesMainView.fxml"); }

    @FXML
    private void loadWarehouseView() {
        loadView("/com/example/adama_ui/ProductManagement.fxml");
    }
}