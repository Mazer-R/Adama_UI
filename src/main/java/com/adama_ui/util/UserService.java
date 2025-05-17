package com.adama_ui.util;

import com.adama_ui.auth.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class UserService {

    private static final String BASE_URL = "https://touching-deadly-reindeer.ngrok-free.app/users";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String getUsernameById(String userId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + userId))
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Map<String, Object> userData = mapper.readValue(response.body(), new TypeReference<>() {});
            return userData.getOrDefault("username", "Desconocido").toString();
        } else {
            throw new RuntimeException("Error al obtener username: " + response.statusCode());
        }
    }
}