package com.simple.api.simple_api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;


@Data
public class OrderDto {
    

    private Long id;

    private Long userId;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    private String status;

    private List<OrderItemDto> items;




}
