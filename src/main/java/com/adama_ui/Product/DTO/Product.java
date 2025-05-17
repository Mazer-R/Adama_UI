package com.adama_ui.Product.DTO;

import com.adama_ui.util.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Data
@ToString(includeFieldNames = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    @ToString.Exclude
    private String id;
    private String name;
    private String description;
    private String type;
    private String brand;

    @ToString.Exclude
    private ProductStatus status;
    @ToString.Exclude
    private String userId;
    @ToString.Exclude
    private String created;
    @ToString.Exclude
    private String lastModified;
    @ToString.Exclude
    private String modifiedBy;

    public String toPrettyJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.toString());
    }

}
