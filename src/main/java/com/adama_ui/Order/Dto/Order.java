package com.adama_ui.Order.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {

    private String id;
    private String productId;
    private String userId;
    private String managerId;
    private String managerUsername;
    private String status;
    private String orderDate;
    private String validationDate;
    private String fulfillmentDate;
    private String details;
    private String productType;
    private String brand;
    private String username;
    private String productName;
}