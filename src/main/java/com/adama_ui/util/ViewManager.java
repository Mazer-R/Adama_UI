package com.adama_ui.util;

import com.adama_ui.*;
import com.adama_ui.Message.DTO.MessageResponse;
import com.adama_ui.Message.InboxMessagesViewController;
import com.adama_ui.Message.MessageDetailController;
import com.adama_ui.Message.MessagesMainViewController;
import com.adama_ui.Order.ManageOrdersController;
import com.adama_ui.Product.DTO.Product;
import com.adama_ui.Product.ProductDetailController;
import com.adama_ui.Product.ProductListController;
import com.adama_ui.style.AppTheme;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewManager {
    private static final Map<String, Pane> viewCache = new HashMap<>();
    @Setter
    private static BorderPane mainContainer;
    @Getter
    private static Object currentController;
    private static String currentFxmlPath;
    private static String currentSubViewPath = "/com/adama_ui/Product/AddProductView.fxml"; // Valor por defecto en Inventario
    private static MessageResponse currentMessage;

    public static void clearViewCache() {
        viewCache.clear();
    }

    public static void load(String fxmlPath) {
        load(fxmlPath, false);
    }

    public static void load(String fxmlPath, boolean cache) {
        try {
            Pane view;
            currentFxmlPath = fxmlPath;

            if (cache && viewCache.containsKey(fxmlPath)) {
                view = viewCache.get(fxmlPath);
                currentController = null;
            } else {
                FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
                view = loader.load();
                currentController = loader.getController();

                if (currentController instanceof Reloadable reloadable) {
                    reloadable.onReload();
                }

                if (cache) viewCache.put(fxmlPath, view);
            }

            AppTheme.applyThemeTo(view);
            mainContainer.setCenter(view);
            applyThemeToSceneIfAvailable();

        } catch (IOException e) {
            System.err.println("❌ Error al cargar la vista: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void loadWithProduct(String fxmlPath, Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
            Pane view = loader.load();
            currentFxmlPath = fxmlPath;
            currentController = loader.getController();

            if (currentController instanceof ProductDetailController controller) {
                controller.setProduct(product);
            }

            if (currentController instanceof Reloadable reloadable) {
                reloadable.onReload();
            }

            AppTheme.applyThemeTo(view);
            mainContainer.setCenter(view);
            applyThemeToSceneIfAvailable();

        } catch (IOException e) {
            System.err.println("❌ Error al cargar la vista con producto: " + fxmlPath);
            e.printStackTrace();
        }

    }

    public static void loadWithProductList(String fxmlPath, List<Product> productList, boolean cache) {
        try {
            Pane view;
            currentFxmlPath = fxmlPath;

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

                if (currentController instanceof Reloadable reloadable) {
                    reloadable.onReload();
                }

                if (cache) viewCache.put(fxmlPath, view);
            }

            AppTheme.applyThemeTo(view);
            mainContainer.setCenter(view);
            applyThemeToSceneIfAvailable();

        } catch (IOException e) {
            System.err.println("❌ Error al cargar la vista con lista de productos: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static Parent loadForScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
            Parent view = loader.load();
            currentFxmlPath = fxmlPath;
            currentController = loader.getController();

            if (currentController instanceof Reloadable reloadable) {
                reloadable.onReload();
            }

            AppTheme.applyThemeTo(view);
            return view;
        } catch (IOException e) {
            System.err.println("❌ Error al cargar la vista para la escena: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }

    public static Parent loadForSceneWithProduct(String fxmlPath, Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
            Parent view = loader.load();
            currentFxmlPath = fxmlPath;
            currentController = loader.getController();

            if (currentController instanceof ProductDetailController controller) {
                controller.setProduct(product);
            }

            if (currentController instanceof Reloadable reloadable) {
                reloadable.onReload();
            }

            AppTheme.applyThemeTo(view);
            return view;
        } catch (IOException e) {
            System.err.println("❌ Error al cargar la vista para la escena con producto: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T loadAndReturnController(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
            Pane view = loader.load();
            currentFxmlPath = fxmlPath;
            T controller = loader.getController();
            currentController = controller;

            if (controller instanceof Reloadable reloadable) {
                reloadable.onReload();
            }

            AppTheme.applyThemeTo(view);
            mainContainer.setCenter(view);
            applyThemeToSceneIfAvailable();

            return controller;
        } catch (IOException e) {
            System.err.println("❌ Error al cargar la vista y devolver el controlador: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }

    public static void loadInto(String fxmlPath, Pane container) {
        loadInto(fxmlPath, container, null);
    }

    public static void loadInto(String fxmlPath, Pane container, Runnable onLoadCallback) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlPath));
            Node view = loader.load();
            currentController = loader.getController();
            currentSubViewPath = fxmlPath;

            if (currentController instanceof Reloadable reloadable) {
                reloadable.onReload();
            }

            AppTheme.applyThemeTo(view);
            container.getChildren().setAll(view);

            if (onLoadCallback != null) {
                onLoadCallback.run();
            }

        } catch (Exception e) {
            System.err.println("❌ Error al cargar vista en contenedor: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void refreshCurrentView() {
        if (mainContainer != null && currentFxmlPath != null) {
            if (currentFxmlPath.contains("MessagesMainView.fxml")) {
                if (MessagesMainViewController.isShowingDetail()) {
                    AppTheme.applyThemeTo(mainContainer);
                    // 🟢 Asegura que el detalle siempre se recargue correctamente
                    javafx.application.Platform.runLater(() -> {
                        Node node = mainContainer.lookup("#contentArea");
                        if (node instanceof StackPane contentArea) {
                            loadInto("/com/adama_ui/Message/MessageDetailView.fxml", contentArea, () -> {
                                var ctrl = getCurrentControllerAs(MessageDetailController.class);
                                ctrl.setContentArea(contentArea);
                                ctrl.setMessage(getCurrentMessage()); // 🔑 aquí está el fix clave
                            });
                        } else {
                            System.err.println("⚠️ No se encontró StackPane contentArea al recargar detalle mensaje.");
                        }
                    });
                    return;
                }
                // Tu lógica existente si no está mostrando detalle:
                load(currentFxmlPath, false);
                javafx.application.Platform.runLater(() -> {
                    String subview = currentSubViewPath != null
                            ? currentSubViewPath
                            : "/com/adama_ui/Message/ComposeMessageView.fxml";

                    Node node = mainContainer.lookup("#contentArea");
                    if (node instanceof StackPane contentArea) {
                        loadInto(subview, contentArea);
                    } else {
                        System.err.println("⚠️ No se encontró el StackPane contentArea en MessagesMainView.fxml");
                    }
                });
            } else {
                load(currentFxmlPath, false);
            }
        }
    }


    private static void applyThemeToSceneIfAvailable() {
        Scene scene = mainContainer.getScene();
        if (scene != null) {
            AppTheme.applyTheme(scene);
        }
    }

    public static void loadProfileAndManageOrders() {
        load("/com/adama_ui/Order/OrderMainView.fxml", false);
        javafx.application.Platform.runLater(() -> {
            Node node = mainContainer.lookup("#contentArea");
            if (node instanceof StackPane contentArea) {
                loadInto("/com/adama_ui/Order/ManageOrdersView.fxml", contentArea, () -> {
                    Object controller = getCurrentController();
                    if (controller instanceof ManageOrdersController manageOrdersController) {
                        // Asegúrate de que el método exista y esté público
                        manageOrdersController.initialize(); // O un método como reload() o updateOrders() si lo prefieres
                    }
                });
            } else {
                System.err.println("⚠️ No se encontró el StackPane con fx:id=\"contentArea\" en OrderMainView.fxml");
            }
        });
    }

    // Método auxiliar para actualizar la subvista actual de Warehouse
    public static void setCurrentSubView(String subViewPath) {
        currentSubViewPath = subViewPath;
    }

    public static String getCurrentSubView() {
        return currentSubViewPath;
    }

    public static <T> T getCurrentControllerAs(Class<T> clazz) {
        Object controller = getCurrentController();
        if (clazz.isInstance(controller)) {
            return clazz.cast(controller);
        } else {
            throw new IllegalStateException("El controlador actual no es del tipo esperado: " + clazz.getSimpleName());
        }
    }

    public static void setCurrentMessage(MessageResponse message) {
        currentMessage = message;
    }

    public static MessageResponse getCurrentMessage() {
        return currentMessage;
    }

}