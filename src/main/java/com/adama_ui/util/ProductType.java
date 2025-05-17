package com.adama_ui.util;

public enum ProductType {
    Computer("Ordenador"),
    Peripheral("Periférico"),
    Phone("Teléfono"),
    Software("Software");

    private final String label;

    ProductType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
