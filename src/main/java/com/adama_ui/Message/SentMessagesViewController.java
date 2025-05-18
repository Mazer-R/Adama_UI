package com.adama_ui.Message;

import com.adama_ui.Message.DTO.MessageResponse;
import com.adama_ui.Message.DTO.MessageService;
import com.adama_ui.util.ViewManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.List;

public class SentMessagesViewController {

    @FXML private ListView<MessageResponse> sentListView;
    @FXML private Label emptyLabel;

    private final MessageService messageService = new MessageService();
    private StackPane contentArea;

    @FXML
    public void initialize() {
        configureListView();
        loadMessages();
    }

    private void configureListView() {
        sentListView.setCellFactory(listView -> new javafx.scene.control.ListCell<>() {
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

                Label receiver = new Label("Para: " + msg.getReceiverUsername());
                receiver.getStyleClass().add("product-name");
                receiver.setPrefWidth(150);

                Label subject = new Label(msg.getSubject());
                subject.getStyleClass().add("product-info");
                subject.setPrefWidth(250);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button btnDetalle = new Button("Detalle");
                btnDetalle.getStyleClass().add("small-action-button");
                btnDetalle.setOnAction(e -> {
                    ViewManager.setCurrentMessage(msg);

                    if (contentArea != null) {
                        ViewManager.loadInto("/com/adama_ui/Message/MessageDetailView.fxml", contentArea, () -> {
                            var ctrl = ViewManager.getCurrentControllerAs(MessageDetailController.class);
                            if (ctrl != null) {
                                ctrl.setContentArea(contentArea);
                                ctrl.setMessage(msg);
                            }
                        });
                    } else {
                        System.err.println("❌ contentArea no está inicializado (null).");
                    }
                });

                cell.getChildren().addAll(receiver, subject, spacer, btnDetalle);
                setText(null);
                setGraphic(cell);
            }
        });
    }

    private void loadMessages() {
        messageService.fetchMessages(
                MessageService.MessageType.SENT,
                this::updateMessageList,
                MessageService::showError,
                sentListView,
                emptyLabel
        );
    }

    private void updateMessageList(List<MessageResponse> messages) {
        Platform.runLater(() -> {
            boolean hasMessages = messages != null && !messages.isEmpty();

            emptyLabel.setVisible(!hasMessages);
            emptyLabel.setManaged(!hasMessages);

            sentListView.setVisible(hasMessages);
            sentListView.setManaged(hasMessages);

            if (hasMessages) {
                sentListView.setItems(FXCollections.observableArrayList(messages));
            } else {
                sentListView.getItems().clear();
            }
        });
    }

    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }
}