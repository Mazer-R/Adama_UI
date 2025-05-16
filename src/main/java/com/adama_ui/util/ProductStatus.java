package com.adama_ui.util;

import com.fasterxml.jackson.annotation.JsonCreator;


public enum ProductStatus {
    STOCK("En stock", "#007bff"),            // Azul
    ON_REPAIR("En reparacion", "#c700ff"),  // Rojo
    PENDING("Pendiente de recibir", "#ffc107"), // Amarillo
    ASSIGNED("Asignado", "#28a745"),
    INACTIVE ("Inactivo", "#dc3545");            // Verde

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
