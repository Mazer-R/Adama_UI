package com.adama_ui.util;

public enum OrderStatusLocal {
    NEW("Pedido", "#f1c40f"),         // amarillo
    ACCEPTED("Aceptada", "#2ecc71"),  // verde
    REJECTED("Rechazada", "#e74c3c"),  // rojo
    DELIVERED("Entregada", "#bdc3c7"); // gris

    private final String label;
    private final String colorHex;

    OrderStatusLocal(String label, String colorHex) {
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
