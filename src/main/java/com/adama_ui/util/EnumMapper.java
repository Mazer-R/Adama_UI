package com.adama_ui.util;

public class EnumMapper {

    /**
     * Mapea un string del backend a un enum OrderStatusLocal, manejando mayúsculas/minúsculas.
     */
    public static OrderStatusLocal fromOrderStatusString(String status) {
        if (status == null) return null;

        return switch (status.toUpperCase()) {
            case "ORDERED" -> OrderStatusLocal.NEW;
            case "VALIDATED" -> OrderStatusLocal.ACCEPTED;
            case "DENIED" -> OrderStatusLocal.REJECTED;
            case "FULFILLED" -> OrderStatusLocal.DELIVERED;
            default -> null;
        };
    }

    /**
     * Mapea un string a ProductType, si existe una coincidencia exacta con el nombre del enum.
     */
    public static ProductType fromProductTypeString(String type) {
        if (type == null) return null;

        for (ProductType pt : ProductType.values()) {
            if (pt.name().equalsIgnoreCase(type)) {
                return pt;
            }
        }

        return null;
    }
}