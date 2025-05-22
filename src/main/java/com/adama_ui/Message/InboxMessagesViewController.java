package com.adama_ui.Message;

import com.adama_ui.Message.DTO.MessageResponse;
import com.adama_ui.Message.DTO.MessageService;
import com.adama_ui.style.AppTheme;
import com.adama_ui.util.Reloadable;
import com.adama_ui.util.ViewManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import lombok.Setter;

import java.util.List;
import java.util.stream.IntStream;

public class InboxMessagesViewController implements Reloadable {

    @FXML
    private BorderPane rootPane;
    @FXML
    private ListView<MessageResponse> inboxListView;
    @FXML
    private Label emptyLabel;

    private List<MessageResponse> currentMessages;
    private final MessageService messageService = new MessageService();
    @Setter
    private StackPane contentArea;
    ViewManager viewManager = ViewManager.getInstance();

    @FXML
    public void initialize() {
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                AppTheme.applyTheme(newScene);
            }
        });

        configureListView();
        loadMessages();
    }

    private void configureListView() {
        inboxListView.setCellFactory(listView -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(MessageResponse msg, boolean empty) {
                super.updateItem(msg, empty);
                if (empty || msg == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                HBox cell = new HBox(10);
                cell.getStyleClass().add("custom-list-cell");

                Label sender = new Label("De: " + msg.getSenderUsername());
                sender.getStyleClass().add("product-name");
                sender.setPrefWidth(150);

                Label subject = new Label(msg.getSubject());
                subject.getStyleClass().add("product-info");
                subject.setPrefWidth(250);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox actionBox = new HBox();
                actionBox.setPrefWidth(100);

                if (!msg.isRead()) {
                    Button btnLeer = new Button("Leer");
                    btnLeer.getStyleClass().add("small-action-button");

                    btnLeer.setOnAction(e -> {
                        messageService.markAsRead(
                                msg.getId(),
                                () -> Platform.runLater(() -> {
                                    inboxListView.getItems().remove(msg);
                                    viewManager.setCurrentMessage(msg); // ya lo tienes

                                    if (contentArea != null) {
                                        viewManager.loadInto("/com/adama_ui/Message/MessageDetailView.fxml", contentArea, () -> {
                                            var ctrl = viewManager.getCurrentControllerAs(MessageDetailController.class);
                                            if (ctrl != null) {
                                                ctrl.setContentArea(contentArea);
                                                ctrl.setMessage(msg);  // 🟢 Esto es crucial (nuevo cambio)
                                            }
                                        });
                                    } else {
                                        System.err.println("❌ contentArea no está inicializado (null).");
                                    }
                                }),
                                errorMsg -> Platform.runLater(() ->
                                        System.err.println("Error al marcar como leído: " + errorMsg)
                                )
                        );
                    });


                    actionBox.getChildren().add(btnLeer);
                }

                cell.getChildren().addAll(sender, subject, spacer, actionBox);
                setText(null);
                setGraphic(cell);
            }
        });
    }

    private void loadMessages() {
        messageService.fetchMessages(
                MessageService.MessageType.INBOX,
                this::updateMessageList,
                MessageService::showError,
                inboxListView,
                emptyLabel
        );
    }

    private void updateMessageList(List<MessageResponse> messages) {
        Platform.runLater(() -> {
            currentMessages = messages;

            boolean hasMessages = messages != null && !messages.isEmpty();

            emptyLabel.setVisible(!hasMessages);
            emptyLabel.setManaged(!hasMessages);

            inboxListView.setVisible(hasMessages);
            inboxListView.setManaged(hasMessages);

            if (hasMessages) {
                inboxListView.setItems(FXCollections.observableArrayList(messages));
            } else {
                inboxListView.getItems().clear();
            }
        });
    }

    @Override
    public void onReload() {
        messageService.fetchMessages(
                MessageService.MessageType.INBOX,
                newMessages -> {
                    if (shouldReload(newMessages))
                        updateMessageList(newMessages);
                },
                MessageService::showError,
                inboxListView,
                emptyLabel
        );
    }

    private boolean shouldReload(List<MessageResponse> newMessages) {
        if (currentMessages == null) return true;
        if (newMessages == null) return false;
        if (newMessages.size() != currentMessages.size()) return true;
        return IntStream.range(0, newMessages.size())
                .anyMatch(i -> !newMessages.get(i).getId().equals(currentMessages.get(i).getId()));

    }
}