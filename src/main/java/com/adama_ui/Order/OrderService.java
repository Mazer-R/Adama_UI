package com.adama_ui.Order;

import com.adama_ui.auth.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static com.adama_ui.auth.SessionManager.API_BASE_URL;
import static com.adama_ui.auth.SessionManager.HTTP_CLIENT;

public class OrderService {

    private static final String BASE_URL = (API_BASE_URL+"/orders");
    private final ObjectMapper mapper = new ObjectMapper();

    public void createOrder(OrderRequest order) throws Exception {
        String body = mapper.writeValueAsString(order);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) {
            throw new RuntimeException("❌ Error al crear la solicitud: " + response.statusCode() + " - " + response.body());
        }
    }

    public List<Order> getAllOrders() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<>() {});
        } else {
            throw new RuntimeException("❌ Error al obtener todas las órdenes: " + response.statusCode());
        }
    }

    public List<Order> getOrdersByUser(String userId) throws Exception {
        String url = BASE_URL + "/user/" + userId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<>() {});
        } else {
            throw new RuntimeException("❌ Error al obtener órdenes del usuario: " + response.statusCode());
        }
    }

    public List<Order> getOrdersByStatus(String status) throws Exception {
        String url = BASE_URL + "/status/" + status.toLowerCase();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<>() {});
        } else {
            throw new RuntimeException("❌ Error al obtener órdenes por estado (" + status + "): " + response.statusCode());
        }
    }

    public void validateOrder(String orderId) throws Exception {
        String url = BASE_URL + "/" + orderId + "/validate";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

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
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("❌ Error al rechazar la orden: " + response.statusCode());
        }
    }

    public void fulfillOrder(String orderId) throws Exception {
        String url = BASE_URL + "/" + orderId + "/fulfill";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("❌ Error al completar la orden: " + response.statusCode());
        }
    }

    public List<Order> getValidatedOrder() throws Exception {
        String url = BASE_URL + "/status/validated"; // ✅ SOLO esto

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<List<Order>>() {});
        } else {
            throw new RuntimeException("❌ Error al obtener órdenes validadas: " + response.statusCode());
        }
    }


}
