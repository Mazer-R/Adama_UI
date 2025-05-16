package com.adama_ui;

public class Order {

    private String id;
    private String productId;
    private String userId;
    private String managerId;
    private String managerUsername;
    private String status;
    private String orderDate;
    private String validationDate;
    private String fulfillmentDate;
    private String details;
    private String productType;
    private String brand;
    private String username;

    // ✅ Nuevo campo
    private String productName;

    // ====== Getters y Setters ======

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getManagerId() { return managerId; }
    public void setManagerId(String managerId) { this.managerId = managerId; }

    public String getManagerUsername() { return managerUsername; }
    public void setManagerUsername(String managerUsername) { this.managerUsername = managerUsername; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getValidationDate() { return validationDate; }
    public void setValidationDate(String validationDate) { this.validationDate = validationDate; }

    public String getFulfillmentDate() { return fulfillmentDate; }
    public void setFulfillmentDate(String fulfillmentDate) { this.fulfillmentDate = fulfillmentDate; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    // ✅ Getter y setter para el nombre del producto
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
}
