package com.adama_ui.Product.DTO;

import lombok.Data;
import lombok.ToString;


@Data
public class ProductResponseDTO {
    private String id;
    private String name;
    private String description;
    private String type;
    private String brand;
    private String status;
    private String userId;
    private String created;
    private String lastModified;
}