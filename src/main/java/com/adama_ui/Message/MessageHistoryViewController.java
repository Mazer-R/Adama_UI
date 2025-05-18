package com.adama_ui.Message;

import com.adama_ui.Message.DTO.MessageResponse;
import com.adama_ui.Message.DTO.MessageService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class MessageHistoryViewController {

        @FXML private ListView<MessageResponse> historyListView;
        @FXML private Label emptyLabel;

        private final MessageService messageService = new MessageService();

        @FXML
        public void initialize() {
            configureListView();
            loadMessages();
        }

        private void configureListView() {
            historyListView.setCellFactory(list -> new com.adama_ui.Message.DTO.MessageCard());
        }

        private void loadMessages() {
            messageService.fetchMessages(
                    MessageService.MessageType.RECEIVED,
                    this::updateMessageList,
                    MessageService::showError,
                    historyListView,
                    emptyLabel
            );
        }

        private void updateMessageList(List<MessageResponse> messages) {
            if (messages.isEmpty()) {
                emptyLabel.setVisible(true);
                emptyLabel.setManaged(true);
                historyListView.setVisible(false);
                historyListView.setManaged(false);
            } else {
                emptyLabel.setVisible(false);
                emptyLabel.setManaged(false);
                historyListView.setVisible(true);
                historyListView.setManaged(true);
                historyListView.setItems(FXCollections.observableArrayList(messages));
            }
        }
    }