package com.adama_ui.util;

public record LabeledValue(String value, String label) {

    @Override
    public String toString() {
        return label;
    }
}