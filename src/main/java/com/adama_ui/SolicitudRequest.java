package com.adama_ui;

import com.adama_ui.util.SolicitudStatus;

import java.util.UUID;

public class SolicitudRequest {

    private String id;
    private String userId;
    private String supervisorId;
    private String detailOrder;
    private SolicitudStatus status;

    // ✅ Constructor vacío requerido por Jackson
    public SolicitudRequest() {}

    // ✅ Constructor de uso normal
    public SolicitudRequest(String userId, String supervisorId, String detailOrder) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.supervisorId = supervisorId;
        this.detailOrder = detailOrder;
        this.status = SolicitudStatus.NUEVA;
    }

    // ✅ Getters y setters públicos para Jackson
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

    public SolicitudStatus getStatus() {
        return status;
    }

    public void setStatus(SolicitudStatus status) {
        this.status = status;
    }
}
