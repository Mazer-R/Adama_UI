package com.adama_ui.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionManager {

    private static SessionManager instance;
    private String authToken;
    private String userId;
    private String role;
    private String managerId;
    private String managerUsername;
    private String username; // ✅ NUEVO

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
            this.role = rootNode.path("role").asText(null);
            this.managerId = rootNode.path("managerId").asText(null);
            this.managerUsername = rootNode.path("managerUsername").asText(null);
            this.username = rootNode.path("username").asText(null); // ✅ Extrae el username del token

            System.out.println("🔐 Token recibido: " + this.authToken);
            System.out.println("👤 userId extraído: " + this.userId);
            System.out.println("🎭 Rol extraído: " + this.role);
            System.out.println("🧑 Username: " + this.username);
            System.out.println("🧑‍💼 ManagerUsername extraído: " + this.managerUsername);

        } catch (Exception e) {
            System.err.println("❌ Error al procesar el token JSON:");
            e.printStackTrace();
        }
    }

    public void setUserData(String userId, String role, String username) {
        this.userId = userId;
        this.role = role;
        this.username = username;

        System.out.println("📌 Datos de sesión establecidos manualmente:");
        System.out.println("👤 userId: " + userId);
        System.out.println("🎭 role: " + role);
        System.out.println("🧑 username: " + username);
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
        System.out.println("📌 Manager ID: " + managerId);
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
        System.out.println("📌 Manager username: " + managerUsername);
    }

    public void setUsername(String username) {
        this.username = username;
        System.out.println("📌 Username: " + username);
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
        return "ADMIN".equalsIgnoreCase(role) || "MANAGER".equalsIgnoreCase(role);
    }

    public void clearSession() {
        this.authToken = null;
        this.userId = null;
        this.role = null;
        this.managerId = null;
        this.managerUsername = null;
        this.username = null;
        System.out.println("🔓 Sesión limpiada correctamente.");
    }
}
