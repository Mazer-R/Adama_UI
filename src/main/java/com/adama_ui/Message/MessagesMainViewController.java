package com.adama_ui.Message;

import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MessagesMainViewController {

    @FXML private StackPane contentArea;
    @FXML private Button btnCompose;
    @FXML private Button btnInbox;
    @FXML private Button btnSent;
    @FXML private Button btnHistory;

    private Runnable currentViewLoader;

    @FXML
    public void initialize() {
        // Enlaces de botones
        btnCompose.setOnAction(e -> loadCompose());
        btnInbox.setOnAction(e -> loadInbox());
        btnSent.setOnAction(e -> loadSent());
        btnHistory.setOnAction(e -> loadHistory());

        // Si ya había una vista activa (por cambio de tema), se recarga esa
        if (currentViewLoader == null) {
            loadCompose(); // Vista por defecto solo si no había ninguna cargada
        } else {
            currentViewLoader.run();
        }
    }

    private void loadCompose() {
        ViewManager.loadInto("/com/adama_ui/Message/ComposeMessageView.fxml", contentArea);
        currentViewLoader = this::loadCompose;
    }

    private void loadInbox() {
        ViewManager.loadInto("/com/adama_ui/Message/InboxMessagesView.fxml", contentArea);
        currentViewLoader = this::loadInbox;
    }

    private void loadSent() {
        ViewManager.loadInto("/com/adama_ui/Message/SentMessagesView.fxml", contentArea);
        currentViewLoader = this::loadSent;
    }

    private void loadHistory() {
        ViewManager.loadInto("/com/adama_ui/Message/MessageHistoryView.fxml", contentArea);
        currentViewLoader = this::loadHistory;
    }

    @FXML
    private void onBack() {
        ViewManager.load("/com/adama_ui/HomeView.fxml");
    }
}