package com.adama_ui.util;

import com.fasterxml.jackson.annotation.JsonCreator;


public enum ProductStatus {
    EN_STOCK("En stock", "#007bff"),            // Azul
    EN_REPARACION("En reparación", "#dc3545"),  // Rojo
    PENDIENTE_DE_RECIBIR("Pendiente de recibir", "#ffc107"), // Amarillo
    ASIGNADO("Asignado", "#28a745");            // Verde

    private final String label;
    private final String colorHex;

    ProductStatus(String label, String colorHex) {
        this.label = label;
        this.colorHex = colorHex;
    }

    public String getLabel() {
        return label;
    }

    public String getColorHex() {
        return colorHex;
    }

    @JsonCreator
    public static ProductStatus fromText(String input) {
        if (input == null) return null;
        for (ProductStatus status : values()) {
            if (status.name().equalsIgnoreCase(input.trim()) ||
                    status.getLabel().equalsIgnoreCase(input.trim())) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return label;
    }
}
