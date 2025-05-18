package com.adama_ui.Message;

import com.adama_ui.auth.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static com.adama_ui.auth.SessionManager.API_BASE_URL;

public class ComposeMessageViewController {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private static List<String> cachedUsernames; // ✅ Lista estática compartida

    @FXML private ComboBox<String> recipientCombo;
    @FXML private TextField subjectField;
    @FXML private TextArea messageArea;
    @FXML private Button sendButton;

    @FXML
    public void initialize() {
        sendButton.setOnAction(e -> onSendMessage());

        if (cachedUsernames != null && !cachedUsernames.isEmpty()) {
            Platform.runLater(() -> {
                recipientCombo.setItems(FXCollections.observableArrayList(cachedUsernames));
            });
        } else {
            fetchUsernames();
        }
    }

    private void onSendMessage() {
        String destinatario = recipientCombo.getValue();
        String asunto = subjectField.getText();
        String contenido = messageArea.getText();

        if (destinatario == null || asunto.isBlank()) {
            showAlert("Faltan datos", "Debes seleccionar un destinatario y escribir un asunto.");
            return;
        }

        sendMessageToBackend(destinatario, asunto, contenido);
    }

    private void fetchUsernames() {
        new Thread(() -> {
            try {
                String endpoint = API_BASE_URL + "/users/usernames";
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint))
                        .GET()
                        .header("Accept", "application/json")
                        .header("Authorization", SessionManager.getInstance().getAuthHeader())
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    ObjectMapper mapper = new ObjectMapper();
                    List<String> usernames = mapper.readValue(response.body(), new TypeReference<>() {});

                    // Excluir al usuario actual
                    String currentUser = SessionManager.getInstance().getUsername();
                    usernames.removeIf(user -> user.equalsIgnoreCase(currentUser));

                    cachedUsernames = usernames;

                    Platform.runLater(() -> recipientCombo.setItems(FXCollections.observableArrayList(cachedUsernames)));
                } else {
                    showAlert("Error", "Error al obtener usernames: " + response.statusCode());
                }

            } catch (Exception e) {
                showAlert("Excepción", "Error al obtener usernames:\n" + e.getMessage());
            }
        }).start();
    }

    private void sendMessageToBackend(String receiverUsername, String subject, String content) {
        new Thread(() -> {
            try {
                String endpoint = API_BASE_URL + "/messages";

                ObjectMapper mapper = new ObjectMapper();
                var message = new java.util.HashMap<String, Object>();
                message.put("receiverUsername", receiverUsername);
                message.put("subject", subject);
                if (content != null && !content.isBlank()) {
                    message.put("content", content);
                }

                String requestBody = mapper.writeValueAsString(message);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint))
                        .header("Content-Type", "application/json")
                        .header("Authorization", SessionManager.getInstance().getAuthHeader())
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 201) {
                    Platform.runLater(() -> {
                        showAlertInfo("Mensaje enviado", "Tu mensaje ha sido enviado correctamente.");

                        subjectField.clear();
                        messageArea.clear();
                        recipientCombo.getSelectionModel().clearSelection();
                        recipientCombo.setPromptText("Selecciona destinatario");

                        // 🔄 Restaurar lista si por algún motivo se perdió
                        if (recipientCombo.getItems() == null || recipientCombo.getItems().isEmpty()) {
                            recipientCombo.setItems(FXCollections.observableArrayList(cachedUsernames));
                        }
                    });
                } else {
                    showAlert("Error al enviar", "Código: " + response.statusCode() + "\n" + response.body());
                }

            } catch (Exception e) {
                showAlert("Excepción", "Error al enviar el mensaje:\n" + e.getMessage());
            }
        }).start();
    }

    private void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    private void showAlertInfo(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}