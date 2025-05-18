package com.adama_ui.Message.DTO;

import java.util.List;

import com.adama_ui.auth.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import lombok.Getter;

import java.net.URI;
import java.net.http.*;
import java.util.function.Consumer;

import static com.adama_ui.auth.SessionManager.HTTP_CLIENT;

public class MessageService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

@Getter
    public enum MessageType {
        INBOX("inbox"),
        SENT("sent"),
        RECEIVED("");
        private final String path;
        MessageType(String path) {
            this.path = path;
        }
    }
    public void fetchMessages(MessageType type,
                              Consumer<List<MessageResponse>> onSuccess,
                              Consumer<String> onError,
                              ListView<MessageResponse> listView,
                              Label emptyLabel) {
        new Thread(() -> {
            try {
                HttpRequest request = null;
                if (type == MessageType.RECEIVED) {
                     request = HttpRequest.newBuilder()
                            .uri(URI.create(SessionManager.API_BASE_URL + "/messages/my-messages"))
                            .header("Authorization", SessionManager.getInstance().getAuthHeader())
                            .header("Accept", "application/json")
                            .GET()
                            .build();
                }else{
                     request = HttpRequest.newBuilder()
                            .uri(URI.create(SessionManager.API_BASE_URL + "/messages/my-messages/" + type.getPath()))
                            .header("Authorization", SessionManager.getInstance().getAuthHeader())
                            .header("Accept", "application/json")
                            .GET()
                            .build();
                }
                HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

                Platform.runLater(() -> {
                    try {
                        if (response.statusCode() == 200) {
                            List<MessageResponse> messages = MAPPER.readValue(
                                    response.body(),
                                    new TypeReference<>() {}
                            );
                            handleSuccess(messages, listView, emptyLabel);
                            if (onSuccess != null) onSuccess.accept(messages);
                        } else {
                            handleError(response.statusCode(), onError);
                        }
                    } catch (Exception e) {
                        handleException(e, onError);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> handleException(e, onError));
            }
        }).start();
    }

    public void markAsRead(String messageId, Runnable onSuccess, Consumer<String> onError) {
        new Thread(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(SessionManager.API_BASE_URL + "/messages/" + messageId))
                        .method("PATCH", HttpRequest.BodyPublishers.ofString("{\"isRead\": true}"))
                        .header("Authorization", SessionManager.getInstance().getAuthHeader())
                        .header("Content-Type", "application/json")
                        .build();

                HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

                Platform.runLater(() -> {
                    if (response.statusCode() == 200) {
                        onSuccess.run();
                    } else {
                        onError.accept("Error al marcar como leído: " + response.statusCode());
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> onError.accept("Error: " + e.getMessage()));
            }
        }).start();
    }

    private void handleSuccess(List<MessageResponse> messages,
                               ListView<MessageResponse> listView,
                               Label emptyLabel) {
        if (messages.isEmpty()) {
            emptyLabel.setVisible(true);
            emptyLabel.setManaged(true);
            listView.setVisible(false);
            listView.setManaged(false);
        } else {
            emptyLabel.setVisible(false);
            emptyLabel.setManaged(false);
            listView.setVisible(true);
            listView.setManaged(true);
            listView.setItems(FXCollections.observableArrayList(messages));
        }
    }

    private void handleError(int statusCode, Consumer<String> onError) {
        onError.accept("Error " + statusCode + ": No se pudieron obtener los mensajes");
    }

    private void handleException(Exception e, Consumer<String> onError) {
        onError.accept("Error: " + e.getMessage());
    }

    public static void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}