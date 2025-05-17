package com.adama_ui.User.DTO;

import com.adama_ui.auth.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static com.adama_ui.auth.SessionManager.API_BASE_URL;
import static com.adama_ui.auth.SessionManager.HTTP_CLIENT;

public class UserService {


    private final ObjectMapper mapper = new ObjectMapper();

    public String getUsernameById(String userId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/" + userId))
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .GET()
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Map<String, Object> userData = mapper.readValue(response.body(), new TypeReference<>() {});
            return userData.getOrDefault("username", "Desconocido").toString();
        } else {
            throw new RuntimeException("Error al obtener username: " + response.statusCode());
        }
    }
}
