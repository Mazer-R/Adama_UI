package com.adama_ui.Message.DTO;

import com.adama_ui.auth.SessionManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MessageCard extends ListCell<MessageResponse> {

    private final MessageService messageService = new MessageService();

    @Override
    protected void updateItem(MessageResponse message, boolean empty) {
        super.updateItem(message, empty);
        if (empty || message == null) {
            setGraphic(null);
            return;
        }

        BorderPane card = createCard(message);
        setGraphic(card);
    }

    private BorderPane createCard(MessageResponse message) {
        // Componentes UI
        Label titleLabel = new Label(message.getSubject());
        titleLabel.getStyleClass().add("card-title");

        Label senderLabel = new Label("De: " + message.getSenderUsername());
        senderLabel.getStyleClass().add("card-subtitle");

        // Contenedor izquierdo
        VBox leftContent = new VBox(4, titleLabel, senderLabel);
        VBox.setVgrow(leftContent, Priority.ALWAYS);

        // Botones
        Button detailButton = createDetailButton(message);


        // Layout final
        BorderPane card = new BorderPane();
        card.setLeft(leftContent);
        card.setRight(new VBox(5, detailButton));
        card.getStyleClass().add("card-item");

        return card;
    }

    private Button createDetailButton(MessageResponse message) {
        Button button = new Button("Detalles");
        button.getStyleClass().add("green-button");
        button.setOnAction(e -> showMessageDetails(message));
        return button;
    }

    private void showMessageDetails(MessageResponse message) {
        if (!message.isRead() && !(message.getSenderUsername().equals(SessionManager.getInstance().getUsername()))) {
            handleMarkAsRead(message);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles del mensaje");
        alert.setHeaderText(message.getSubject());
        alert.setContentText(createDetailContent(message));
        alert.showAndWait();
    }

    private String createDetailContent(MessageResponse message) {
        return String.format(
                "De: %s\n\n%s",
                message.getSenderUsername(),
                message.getContent() != null ? message.getContent() : "Sin contenido"
        );
    }

    private void handleMarkAsRead(MessageResponse message) {
        if (!message.isRead()) {
            messageService.markAsRead(
                    message.getId(),
                    () -> Platform.runLater(() -> getListView().getItems().remove(message)),
                    MessageService::showError
            );
        }
    }
}