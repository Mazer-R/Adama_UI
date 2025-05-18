package com.adama_ui.Message;

import com.adama_ui.Message.DTO.MessageResponse;
import com.adama_ui.Message.DTO.MessageService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;

public class SentMessagesViewController {

    @FXML
    private ListView<MessageResponse> sentListView;
    @FXML private Label emptyLabel;

    private final MessageService messageService = new MessageService();

    @FXML
    public void initialize() {
        configureListView();
        loadMessages();
    }

    private void configureListView() {
        sentListView.setCellFactory(list -> new com.adama_ui.Message.DTO.MessageCard());
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
        if (messages.isEmpty()) {
            emptyLabel.setVisible(true);
            emptyLabel.setManaged(true);
            sentListView.setVisible(false);
            sentListView.setManaged(false);
        } else {
            emptyLabel.setVisible(false);
            emptyLabel.setManaged(false);
            sentListView.setVisible(true);
            sentListView.setManaged(true);
            sentListView.setItems(FXCollections.observableArrayList(messages));
        }
    }
}