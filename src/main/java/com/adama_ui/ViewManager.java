package com.adama_ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewManager {
    private static final Map<String, Pane> viewCache = new HashMap<>();
    private static BorderPane mainContainer;

    public static void setMainContainer(BorderPane container) {
        mainContainer = container;
    }

    // Versión básica (de ambas ramas)
    public static void loadView(String fxmlPath) {
        loadView(fxmlPath, true);
    }

    // Versión mejorada con caché (de search-update-product-pages)
    public static void loadView(String fxmlPath, boolean cache) {
        try {
            Pane view;

            if (cache && viewCache.containsKey(fxmlPath)) {
                view = viewCache.get(fxmlPath);
            } else {
                FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
                view = loader.load();
                if (cache) viewCache.put(fxmlPath, view);
            }

            mainContainer.setCenter(view);
        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // Métodos nuevos para productos (de search-update-product-pages)
    public static void loadView(String fxmlPath, Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
            Pane view = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ProductDetailController) {
                ((ProductDetailController) controller).setProduct(product);
            }

            mainContainer.setCenter(view);
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
            } else {
                FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
                view = loader.load();

                Object controller = loader.getController();
                if (controller instanceof ProductListController) {
                    ((ProductListController) controller).setProductList(productList);
                }

                if (cache) viewCache.put(fxmlPath, view);
            }

            mainContainer.setCenter(view);
        } catch (IOException e) {
            System.err.println("Error loading view with product list: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // Métodos para escenas completas (de search-update-product-pages)
    public static Parent loadViewForScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
            return loader.load();
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

            Object controller = loader.getController();
            if (controller instanceof ProductDetailController) {
                ((ProductDetailController) controller).setProduct(product);
            }

            return view;
        } catch (IOException e) {
            System.err.println("Error loading view for scene with product: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }

    public static void clearCache() {
        viewCache.clear();
    }
}