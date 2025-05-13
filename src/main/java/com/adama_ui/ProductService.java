package com.adama_ui;

import java.net.URI;
import java.net.http.*;
import java.util.Arrays;
import java.util.List;

import com.adama_ui.auth.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductService {

    private static final String BASE_URL = "http://localhost:8080/products";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Product getProductById(String id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Product.class);
        } else {
            throw new Exception("Producto no encontrado");
        }
    }

    public List<Product> getProductsByType(String type) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "?type=" + type))
                .GET()
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return Arrays.asList(objectMapper.readValue(response.body(), Product[].class));
    }

    public List<Product> getProductsByBrand(String brand) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "?brand=" + brand))
                .GET()
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return Arrays.asList(objectMapper.readValue(response.body(), Product[].class));
    }

    public List<Product> getProductsByFilters(String type, String brand) throws Exception {
        String uri = BASE_URL + "?type=" + type + "&brand=" + brand;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .header("Authorization", SessionManager.getInstance().getAuthHeader())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return Arrays.asList(objectMapper.readValue(response.body(), Product[].class));
    }
}
