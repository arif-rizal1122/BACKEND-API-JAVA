package com.simple.api.simple_api.dto.response;

import java.math.BigDecimal;
import java.util.Set;

import lombok.Data;

@Data
public class CartDto {

    private Long cartId;

    private BigDecimal totalAmount;
    
    private Set<CartItemDto> items;
}
