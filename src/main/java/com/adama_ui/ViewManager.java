package com.adama_ui;

import com.adama_ui.style.AppTheme;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewManager {
    private static final Map<String, Pane> viewCache = new HashMap<>();
    private static BorderPane mainContainer;
    private static Object currentController;

    public static void setMainContainer(BorderPane container) {
        mainContainer = container;
    }

    public static void clearCache() {
        viewCache.clear();
    }

    public static Object getCurrentController() {
        return currentController;
    }

    public static void loadView(String fxmlPath) {
        loadView(fxmlPath, true);
    }

    public static void loadView(String fxmlPath, boolean cache) {
        try {
            Pane view;

            if (cache && viewCache.containsKey(fxmlPath)) {
                view = viewCache.get(fxmlPath);
                currentController = null;
            } else {
                FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
                view = loader.load();
                currentController = loader.getController();
                if (cache) viewCache.put(fxmlPath, view);
            }

            mainContainer.setCenter(view);
            applyThemeIfAvailable();

        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void loadView(String fxmlPath, Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
            Pane view = loader.load();
            currentController = loader.getController();

            if (currentController instanceof ProductDetailController controller) {
                controller.setProduct(product);
            }

            mainContainer.setCenter(view);
            applyThemeIfAvailable();

        } catch (IOException e) {
            System.err.println("Error loading view with product: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void loadView(String fxmlPath, List<Product> productList, boolean cache) {
        try {
            Pane view;

            if (cache && viewCache.containsKey(fxmlPath)) {
                view = viewCache.get(fxmlPath);
                currentController = null;
            } else {
                FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
                view = loader.load();
                currentController = loader.getController();

                if (currentController instanceof ProductListController controller) {
                    controller.setProductList(productList);
                }

                if (cache) viewCache.put(fxmlPath, view);
            }

            mainContainer.setCenter(view);
            applyThemeIfAvailable();

        } catch (IOException e) {
            System.err.println("Error loading view with product list: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static Parent loadViewForScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
            Parent view = loader.load();
            currentController = loader.getController();
            return view;
        } catch (IOException e) {
            System.err.println("Error loading view for scene: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }

    public static Parent loadViewForScene(String fxmlPath, Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
            Parent view = loader.load();
            currentController = loader.getController();

            if (currentController instanceof ProductDetailController controller) {
                controller.setProduct(product);
            }

            return view;
        } catch (IOException e) {
            System.err.println("Error loading view for scene with product: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T loadViewAndReturnController(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
            Pane view = loader.load();
            T controller = loader.getController();
            currentController = controller;
            mainContainer.setCenter(view);
            applyThemeIfAvailable();
            return controller;
        } catch (IOException e) {
            System.err.println("Error loading view and returning controller: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }

    // ✅ Aplica tema si hay una escena
    private static void applyThemeIfAvailable() {
        Scene scene = mainContainer.getScene();
        if (scene != null) {
            AppTheme.applyTheme(scene);
        }
    }
}
