package com.adama_ui.util;

import com.adama_ui.Message.DTO.MessageResponse;

import com.adama_ui.Product.DTO.Product;
import com.adama_ui.Product.ProductDetailController;
import com.adama_ui.User.DTO.UserResponse;
import com.adama_ui.User.UserDetailsController;
import com.adama_ui.style.AppTheme;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

@Slf4j
@Getter
@Setter
public class ViewManager {

    private static ViewManager instance;
    private final Stack<ViewState> viewHistory = new Stack<>();
    private final Map<String, Pane> viewCache = new HashMap<>();
    private List<String> forbiddenList = new ArrayList<>();

    private BorderPane mainContainer;
    private Object currentController;
    private String fxmlPath;
    private String currentSubViewPath = "/com/adama_ui/Product/AddProductView.fxml";
    private MessageResponse currentMessage;

    private ViewManager() {
        fillForbiddenList();
    }

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    @FunctionalInterface
    public interface ControllerCallback {
        void handle(Object controller);
    }

    private <T extends Node> T loadView(
            String fxmlPath,
            boolean cache,
            boolean saveHistory,
            Class<T> nodeType,
            ControllerCallback callback
    ) throws IOException {
        if (saveHistory) saveCurrentToHistory();
        this.fxmlPath = fxmlPath;

        T view;

        if (cache && viewCache.containsKey(fxmlPath) && nodeType == Pane.class) {
            view = nodeType.cast(viewCache.get(fxmlPath));
            this.currentController = null;
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            view = loader.load();
            this.currentController = loader.getController();

            if (callback != null) {
                callback.handle(currentController);
            }

            if (currentController instanceof Reloadable reloadable) {
                reloadable.onReload();
            }

            if (cache && view instanceof Pane paneView) {
                viewCache.put(fxmlPath, paneView);
            }
        }

        AppTheme.applyThemeTo(view);
        return view;
    }

    public void load(String fxmlPath) {
        load(fxmlPath, false);
    }

    public void load(String fxmlPath, boolean cache) {
        try {
            Pane view = loadView(fxmlPath, false, true, Pane.class, null);
            mainContainer.setCenter(view);
        } catch (IOException e) {
            log.error("❌ Error al cargar la vista: " + fxmlPath, e);
        }
    }

    public void loadWithProduct(String fxmlPath, Product product) {
        try {
            Pane view = loadView(fxmlPath, false, true, Pane.class, controller -> {
                if (controller instanceof ProductDetailController ctrl) {
                    ctrl.setProduct(product);
                }
            });
            mainContainer.setCenter(view);
        } catch (IOException e) {
            log.error("❌ Error al cargar vista con producto: " + fxmlPath, e);
        }
    }

    public Parent loadForScene(String fxmlPath) {
        try {
            return loadView(fxmlPath, false, true, Parent.class, null);
        } catch (IOException e) {
            log.error("❌ Error al cargar vista para escena: " + fxmlPath, e);
            return null;
        }
    }

    public Parent loadForSceneWithUser(String fxmlPath, UserResponse user) {
        try {
            return loadView(fxmlPath, false, true, Parent.class, controller -> {
                if (controller instanceof UserDetailsController ctrl) {
                    ctrl.showUserDetails(user);
                }
            });
        } catch (IOException e) {
            log.error("❌ Error al cargar vista de usuario: " + fxmlPath, e);
            return null;
        }
    }


//    public void loadInto(String fxmlPath, Pane container) {
//        loadInto(fxmlPath, container, null);
//    }

    public void loadInto(String fxmlPath, Pane container, Runnable onLoadCallback) {
        try {
            if (currentSubViewPath != null && isNotForbidden(currentSubViewPath)) {
                viewHistory.push(new ViewState(currentSubViewPath, true, container, onLoadCallback));
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            this.currentController = loader.getController();
            this.currentSubViewPath = fxmlPath;

            if (currentController instanceof Reloadable reloadable) {
                reloadable.onReload();
            }

            AppTheme.applyThemeTo(view);
            container.getChildren().setAll(view);

            if (onLoadCallback != null) {
                onLoadCallback.run();
            }

        } catch (Exception e) {
            log.error("❌ Error al cargar vista en contenedor: " + fxmlPath, e);
        }
    }


//    public void loadProfileAndManageOrders() {
//        load("/com/adama_ui/Order/OrderMainView.fxml", false);
//        if (fxmlPath != null) {
//            viewHistory.push(fxmlPath);
//        }
//        javafx.application.Platform.runLater(() -> {
//            Node node = mainContainer.lookup("#contentArea");
//            if (node instanceof StackPane contentArea) {
//                loadInto("/com/adama_ui/Order/ManageOrdersView.fxml", contentArea, () -> {
//                    Object controller = getCurrentController();
//                    if (controller instanceof ManageOrdersController ctrl) {
//                        ctrl.initialize();
//                    }
//                });
//            }
//        });
//    }

    public <T> T getCurrentControllerAs(Class<T> clazz) {
        if (clazz.isInstance(currentController)) {
            return clazz.cast(currentController);
        } else {
            throw new IllegalStateException("El controlador actual no es del tipo esperado: " + clazz.getSimpleName());
        }
    }

    public void goBack() {
        if (!viewHistory.isEmpty()) {
            ViewState previousState = viewHistory.pop();

            if (previousState.isSubView && previousState.targetContainer != null) {
                loadInto(previousState.fxmlPath, previousState.targetContainer, null);
            } else {
                load(previousState.fxmlPath, false);
            }

        } else {
            System.out.println("⚠️ No hay vista anterior en el historial.");
        }
    }

    public void clearHistory() {
        viewHistory.clear();
    }

    private boolean isNotForbidden(String fxmlPath) {
        return !forbiddenList.contains(fxmlPath);
    }

    private void fillForbiddenList() {
        forbiddenList.add("/com/adama_ui/LoginToApp.fxml");
        forbiddenList.add("/com/adama_ui/MessageDetailController.fxml");
    }

    public void reload() {
        if (mainContainer == null || fxmlPath == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            this.currentController = loader.getController();

            if (currentController instanceof Reloadable reloadable) {
                reloadable.onReload();
            }

            mainContainer.setCenter(root);

            Scene scene = mainContainer.getScene();
            if (scene != null) {
                AppTheme.applyTheme(scene);
            }

        } catch (IOException e) {
            log.error("❌ Error recargando la vista: " + fxmlPath, e);
        }
    }

    private record ViewState(String fxmlPath, boolean isSubView, Pane targetContainer, Runnable onLoadCallback) {
    }


    private void saveCurrentToHistory() {
        if (this.fxmlPath != null && isNotForbidden(this.fxmlPath)) {
            viewHistory.push(new ViewState(this.fxmlPath, false, null, null));
        }
    }

}

