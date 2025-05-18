package com.adama_ui.Product.DTO;
import com.adama_ui.util.ProductStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductMapper {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(ProductRequestDTO dto) throws Exception {
        return mapper.writeValueAsString(dto);
    }

    public static ProductResponseDTO fromJson(String json) throws Exception {
        return mapper.readValue(json, ProductResponseDTO.class);
    }

    public static Product toProduct(ProductResponseDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setType(dto.getType());
        product.setBrand(dto.getBrand());
        product.setStatus(ProductStatus.valueOf(dto.getStatus()));
        product.setUserId(dto.getUserId());
        return product;
    }
}