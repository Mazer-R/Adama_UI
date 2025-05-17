package com.adama_ui.Product;

import java.net.URI;
import java.net.http.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.adama_ui.auth.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductService {

    private static final String BASE_URL = "https://touching-deadly-reindeer.ngrok-free.app";
    private static final String PRODUCTS_PATH = "/products";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Product getProductById(String id) throws Exception {
        String url = BASE_URL + PRODUCTS_PATH + "/" + id;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Product.class);
        } else {
            System.err.println("Error al obtener producto por ID: " + response.body());
            throw new Exception("Producto no encontrado");
        }
    }

    public List<Product> getProductsByFilters(String type, String brand) throws Exception {
        StringBuilder uriBuilder = new StringBuilder(BASE_URL + PRODUCTS_PATH);
        boolean hasParams = false;

        if (type != null && !type.isEmpty()) {
            uriBuilder.append(hasParams ? "&" : "?").append("type=").append(type);
            hasParams = true;
        }

        if (brand != null && !brand.isEmpty()) {
            uriBuilder.append(hasParams ? "&" : "?").append("brand=").append(brand);
        }

        String url = uriBuilder.toString();
        System.out.println(">>> Petición a: " + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return Arrays.asList(objectMapper.readValue(response.body(), Product[].class));
        } else {
            System.err.println("Error al obtener productos por filtros: " + response.body());
            return Collections.emptyList();
        }
    }

    public List<Product> getAllProducts() throws Exception {
        return getProductsByFilters(null, null);
    }
}
