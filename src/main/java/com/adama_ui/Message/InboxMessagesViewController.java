package com.adama_ui.Message;


import com.adama_ui.Message.DTO.MessageResponse;
import com.adama_ui.Message.DTO.MessageService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;

public class InboxMessagesViewController {

    @FXML private ListView<MessageResponse> inboxListView;
    @FXML private Label emptyLabel;

    private final MessageService messageService = new MessageService();

    @FXML
    public void initialize() {
        configureListView();
        loadMessages();
    }

    private void configureListView() {
        inboxListView.setCellFactory(list -> new com.adama_ui.Message.DTO.MessageCard());
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
        if (messages.isEmpty()) {
            emptyLabel.setVisible(true);
            emptyLabel.setManaged(true);
            inboxListView.setVisible(false);
            inboxListView.setManaged(false);
        } else {
            emptyLabel.setVisible(false);
            emptyLabel.setManaged(false);
            inboxListView.setVisible(true);
            inboxListView.setManaged(true);
            inboxListView.setItems(FXCollections.observableArrayList(messages));
        }
    }
}