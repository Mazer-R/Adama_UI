package com.adama_ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewManager {
    private static final Map<String, Pane> viewCache = new HashMap<>();
    private static BorderPane mainContainer;

    public static void setMainContainer(BorderPane container) {
        mainContainer = container;
    }

    public static void loadView(String fxmlPath) {
        try {
            Pane view = viewCache.get(fxmlPath);

            if (view == null) {
                FXMLLoader loader = new FXMLLoader(
                        ViewManager.class.getResource(fxmlPath)
                );
                view = loader.load();
                viewCache.put(fxmlPath, view);
            }

            mainContainer.setCenter(view);
        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void clearCache() {
        viewCache.clear();
    }
}