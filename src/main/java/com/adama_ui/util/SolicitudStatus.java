package com.adama_ui.util;

public enum SolicitudStatus {
    NUEVA("Nueva", "#f1c40f"),        // amarillo
    ACEPTADA("Aceptada", "#2ecc71"),  // verde
    RECHAZADA("Rechazada", "#e74c3c"),// rojo
    ENTREGADA("Entregada", "#bdc3c7");// gris

    private final String label;
    private final String colorHex;

    SolicitudStatus(String label, String colorHex) {
        this.label = label;
        this.colorHex = colorHex;
    }

    public String getLabel() {
        return label;
    }

    public String getColorHex() {
        return colorHex;
    }
}