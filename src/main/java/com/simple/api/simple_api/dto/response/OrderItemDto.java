package com.simple.api.simple_api.dto.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDto {
    
    private Long productId;

    private String productName;

    private int quantity;

    private BigDecimal price;

}
