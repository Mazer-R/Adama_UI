package com.adama_ui.Message;

import com.adama_ui.Reloadable;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MessagesMainViewController {

    @FXML private StackPane contentArea;
    @FXML private VBox messageMenu;
    @FXML private Button btnCompose;
    @FXML private Button btnInbox;
    @FXML private Button btnSent;
    @FXML private Button btnHistory;

    private static String currentSubview = null;
    private static boolean showingDetail = false;

    @FXML
    public void initialize() {
        if (showingDetail) {
            return;
        }

        setupButtons();

        if (currentSubview == null) {
            btnCompose.fire();
        } else {
            switch (currentSubview) {
                case "COMPOSE" -> btnCompose.fire();
                case "INBOX" -> btnInbox.fire();
                case "SENT" -> btnSent.fire();
                case "HISTORY" -> btnHistory.fire();
            }
        }
    }

    private void setupButtons() {
        if (btnCompose != null) {
            btnCompose.setOnAction(e -> loadSubview("COMPOSE", "/com/adama_ui/Message/ComposeMessageView.fxml", btnCompose));
        }

        if (btnInbox != null) {
            btnInbox.setOnAction(e -> loadSubview("INBOX", "/com/adama_ui/Message/InboxMessagesView.fxml", btnInbox));
        }

        if (btnSent != null) {
            btnSent.setOnAction(e -> {
                ViewManager.loadInto("/com/adama_ui/Message/SentMessagesView.fxml", contentArea, () -> {
                    currentSubview = "SENT";
                    highlightMenuButton(btnSent);
                    reloadIfNeeded();

                    var controller = ViewManager.getCurrentController();
                    if (controller instanceof InboxMessagesViewController inboxController) {
                        inboxController.setContentArea(contentArea);
                    } else if (controller instanceof SentMessagesViewController sentController) {
                        sentController.setContentArea(contentArea);
                    } else if (controller instanceof MessageHistoryViewController historyController) {
                        historyController.setContentArea(contentArea);
                    }

                    showingDetail = false;
                });
            });
        }

        if (btnHistory != null) {
            btnHistory.setOnAction(e -> loadSubview("HISTORY", "/com/adama_ui/Message/MessageHistoryView.fxml", btnHistory));
        }
    }

    private void loadSubview(String subviewName, String fxmlPath, Button activeButton) {
        ViewManager.loadInto(fxmlPath, contentArea, () -> {
            currentSubview = subviewName;
            highlightMenuButton(activeButton);
            reloadIfNeeded();

            var controller = ViewManager.getCurrentController();
            if (controller instanceof InboxMessagesViewController inboxController) {
                inboxController.setContentArea(contentArea);
            } else if (controller instanceof SentMessagesViewController sentController) {
                sentController.setContentArea(contentArea);
            } else if (controller instanceof MessageHistoryViewController historyController) {
                historyController.setContentArea(contentArea);
            }

            showingDetail = false;
        });
    }

    private void highlightMenuButton(Button activeButton) {
        for (var node : messageMenu.getChildren()) {
            if (node instanceof Button button) {
                button.getStyleClass().remove("active-button");
            }
        }
        if (activeButton != null && !activeButton.getStyleClass().contains("active-button")) {
            activeButton.getStyleClass().add("active-button");
        }
    }

    private void reloadIfNeeded() {
        Object controller = ViewManager.getCurrentController();
        if (controller instanceof Reloadable reloadable) {
            reloadable.onReload();
        }
    }

    @FXML
    private void onBack() {
        ViewManager.load("/com/adama_ui/HomeView.fxml");
        currentSubview = null;
        showingDetail = false;
    }

    public void loadInboxMessages() {
        if (btnInbox != null) {
            btnInbox.fire();
        }
    }

    public static void setShowingDetail(boolean value) {
        showingDetail = value;
    }

    public static boolean isShowingDetail() {
        return showingDetail;
    }
}