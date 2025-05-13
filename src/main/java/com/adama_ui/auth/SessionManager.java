package com.adama_ui.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

public class SessionManager {
    private static SessionManager instance;
    private String authToken;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setAuthToken(String fullResponseToken) {
        try {
            // Creamos un ObjectMapper para convertir el String a un JsonNode
            ObjectMapper objectMapper = new ObjectMapper();

            // Parseamos el JSON
            JsonNode rootNode = objectMapper.readTree(fullResponseToken);

            // Extraemos el token del JSON
            this.authToken = rootNode.path("token").asText();  // "token" es la clave en el JSON
        } catch (Exception e) {
            // Manejo de errores en caso de que el JSON no sea válido o el campo no exista
            e.printStackTrace();
        }
    }

    public String getAuthToken() {
        return authToken;
    }

    public void clearSession() {
        this.authToken = null;
    }

    public String getAuthHeader(){
        if (authToken != null && !authToken.isEmpty()) {
            return "Bearer " + authToken;
        } else {
            throw new IllegalStateException("Authorization token is missing.");
        }
    }
}