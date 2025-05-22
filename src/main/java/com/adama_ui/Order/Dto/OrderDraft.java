package com.adama_ui.Order.Dto;

import com.adama_ui.util.OrderStatusLocal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderDraft {

    private String id;
    private String userId;
    private String supervisorId;
    private String detailOrder;
    private OrderStatusLocal status;


    public OrderDraft(String userId, String supervisorId, String detailOrder) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.supervisorId = supervisorId;
        this.detailOrder = detailOrder;
        this.status = OrderStatusLocal.NEW; // Pending rename
    }

}
