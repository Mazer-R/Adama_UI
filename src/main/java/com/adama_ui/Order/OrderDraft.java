package com.adama_ui.Order;

import com.adama_ui.util.OrderStatusLocal;

import java.util.UUID;

public class OrderDraft {

    private String id;
    private String userId;
    private String supervisorId;
    private String detailOrder;
    private OrderStatusLocal status;

    // ✅ Default constructor for Jackson
    public OrderDraft() {}

    // ✅ Constructor for normal use
    public OrderDraft(String userId, String supervisorId, String detailOrder) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.supervisorId = supervisorId;
        this.detailOrder = detailOrder;
        this.status = OrderStatusLocal.NEW; // Pending rename
    }

    // ✅ Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getDetailOrder() {
        return detailOrder;
    }

    public void setDetailOrder(String detailOrder) {
        this.detailOrder = detailOrder;
    }

    public OrderStatusLocal getStatus() {
        return status;
    }

    public void setStatus(OrderStatusLocal status) {
        this.status = status;
    }
}
