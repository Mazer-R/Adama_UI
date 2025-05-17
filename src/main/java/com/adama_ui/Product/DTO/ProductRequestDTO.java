package com.adama_ui.Product.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor

public class ProductRequestDTO {
    private String name;
    private String description;
    private String type;
    private String brand;
}