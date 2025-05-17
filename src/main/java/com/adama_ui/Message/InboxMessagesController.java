package com.adama_ui.Message;

import com.adama_ui.auth.SessionManager;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static com.adama_ui.auth.SessionManager.API_BASE_URL;

public class InboxMessagesController {

    @FXML private ListView<MessageResponse> inboxListView;
    @FXML private Label emptyLabel;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @FXML
    public void initialize() {
        fetchInboxMessages();
    }

    private void fetchInboxMessages() {
        new Thread(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE_URL + "/messages/my-messages/inbox"))
                        .GET()
                        .header("Authorization", SessionManager.getInstance().getAuthHeader())
                        .header("Accept", "application/json")
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    ObjectMapper mapper = new ObjectMapper();
                    List<MessageResponse> messages = mapper.readValue(response.body(), new TypeReference<>() {});

                    Platform.runLater(() -> {
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
                            inboxListView.setCellFactory(list -> new MessageCard());
                        }
                    });
                } else {
                    showError("Error al obtener mensajes: " + response.statusCode());
                }

            } catch (Exception e) {
                showError("Error al cargar mensajes:\n" + e.getMessage());
            }
        }).start();
    }

    private void showError(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }

    private static class MessageCard extends ListCell<MessageResponse> {
        @Override
        protected void updateItem(MessageResponse msg, boolean empty) {
            super.updateItem(msg, empty);
            if (empty || msg == null) {
                setGraphic(null);
            } else {
                Label title = new Label(msg.getSubject());
                title.getStyleClass().add("card-title");
                Label from = new Label("De: " + msg.getSenderUsername());
                from.getStyleClass().add("card-subtitle");

                VBox leftContent = new VBox(title, from);
                leftContent.setSpacing(4);
                VBox.setVgrow(leftContent, Priority.ALWAYS);

                Button btn = new Button("Detalle");
                btn.getStyleClass().add("green-button");
                btn.setOnAction(e -> {
                    // Mostrar contenido
                    Alert detail = new Alert(Alert.AlertType.INFORMATION);
                    detail.setTitle("Mensaje recibido");
                    detail.setHeaderText(msg.getSubject());
                    detail.setContentText("De: " + msg.getSenderUsername() + "\n\n" +
                            (msg.getContent() != null ? msg.getContent() : "Sin contenido"));
                    detail.showAndWait();

                    // Marcar como leído
                    new Thread(() -> {
                        try {
                            String endpoint = API_BASE_URL + "/messages/" + msg.getId();

                            String jsonBody = "{\"isRead\": true}";

                            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create(endpoint))
                                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody))
                                    .header("Authorization", SessionManager.getInstance().getAuthHeader())
                                    .header("Content-Type", "application/json")
                                    .build();

                            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                            if (response.statusCode() == 200) {
                                Platform.runLater(() -> getListView().getItems().remove(msg));
                            } else {
                                System.err.println("⚠️ No se pudo marcar como leído: " + response.statusCode());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                });

                BorderPane pane = new BorderPane();
                pane.setLeft(leftContent);
                pane.setRight(btn);
                pane.getStyleClass().add("card-item");
                setGraphic(pane);
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MessageResponse {
        private String id;
        private String senderUsername;
        private String subject;
        private String content;

        public String getId() { return id; }
        public String getSenderUsername() { return senderUsername; }
        public String getSubject() { return subject; }
        public String getContent() { return content; }
    }
}