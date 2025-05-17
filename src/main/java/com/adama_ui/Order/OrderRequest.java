package com.adama_ui.Order;

public class OrderRequest {
    private String details ="";
    private String productId;
    private String userId;
    private String managerUsername;

    public OrderRequest() {}

    public OrderRequest(String productId, String userId, String managerUsername,String details) {
        this.productId = productId;
        this.userId = userId;
        this.managerUsername = managerUsername;
        this.details = details;
    }

    // Getters y Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details != null ? details : "";
    }
}