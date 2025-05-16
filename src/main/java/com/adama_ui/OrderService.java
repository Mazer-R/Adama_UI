package com.adama_ui;

import com.adama_ui.auth.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class OrderService {

    private static final String BASE_URL = "https://touching-deadly-reindeer.ngrok-free.app/orders";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // ✅ Crear una nueva orden
    public void createOrder(OrderRequest order) throws Exception {
        String body = mapper.writeValueAsString(order);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) {
            throw new RuntimeException("❌ Error al crear la solicitud: " + response.statusCode() + " - " + response.body());
        }
    }

    // ✅ Obtener todas las órdenes
    public List<Order> getAllOrders() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<>() {});
        } else {
            throw new RuntimeException("❌ Error al obtener todas las órdenes: " + response.statusCode());
        }
    }

    // ✅ Obtener órdenes por usuario
    public List<Order> getOrdersByUser(String userId) throws Exception {
        String url = BASE_URL + "/user/" + userId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<>() {});
        } else {
            throw new RuntimeException("❌ Error al obtener órdenes del usuario: " + response.statusCode());
        }
    }

    // ✅ Obtener órdenes por estado
    public List<Order> getOrdersByStatus(String status) throws Exception {
        String url = BASE_URL + "/status/" + status.toLowerCase();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<>() {});
        } else {
            throw new RuntimeException("❌ Error al obtener órdenes por estado (" + status + "): " + response.statusCode());
        }
    }

    // ✅ Validar una orden (POST)
    public void validateOrder(String orderId) throws Exception {
        String url = BASE_URL + "/" + orderId + "/validate";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .POST(HttpRequest.BodyPublishers.noBody()) // CORREGIDO
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("❌ Error al validar la orden: " + response.statusCode());
        }
    }

    // ✅ Rechazar una orden (POST)
    public void denyOrder(String orderId) throws Exception {
        String url = BASE_URL + "/" + orderId + "/deny";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .POST(HttpRequest.BodyPublishers.noBody()) // CORREGIDO
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("❌ Error al rechazar la orden: " + response.statusCode());
        }
    }
}
