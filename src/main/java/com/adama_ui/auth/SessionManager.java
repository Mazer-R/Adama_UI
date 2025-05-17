package com.adama_ui.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;

public class SessionManager {
    public static final String API_BASE_URL = "https://touching-deadly-reindeer.ngrok-free.app";
    public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();


    private static SessionManager instance;
    private String authToken;
    private String userId;
    private String role;
    private String managerId;
    private String managerUsername;
    private String username;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setAuthToken(String fullResponseToken) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(fullResponseToken);

            this.authToken = rootNode.path("token").asText();
            this.userId = rootNode.path("userId").asText(null);

            JsonNode roleNode = rootNode.path("role");
            this.role = roleNode.isArray() ? roleNode.get(0).asText() : roleNode.asText(null);

            this.managerId = rootNode.path("managerId").asText(null);
            this.managerUsername = rootNode.path("managerUsername").asText(null);
            this.username = rootNode.path("username").asText(null);

        } catch (Exception e) {
            System.err.println("Error al procesar el token JSON:");
            e.printStackTrace();
        }
    }

    public void setUserData(String userId, String role, String username) {
        this.userId = userId;
        this.role = role;
        this.username = username;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getAuthHeader() {
        if (authToken != null && !authToken.isEmpty()) {
            return "Bearer " + authToken;
        } else {
            throw new IllegalStateException("Authorization token is missing.");
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public String getManagerId() {
        return managerId;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdminOrManager() {
        return role != null && (role.contains("ROLE_ADMIN") || role.contains("ROLE_MANAGER"));
    }

    public void clearSession() {
        this.authToken = null;
        this.userId = null;
        this.role = null;
        this.managerId = null;
        this.managerUsername = null;
        this.username = null;
        System.out.println("Sesión limpiada correctamente.");
    }
}
