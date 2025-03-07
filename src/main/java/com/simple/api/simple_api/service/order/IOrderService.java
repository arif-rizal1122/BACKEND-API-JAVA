package com.simple.api.simple_api.service.order;

import java.util.List;

import com.simple.api.simple_api.dto.response.OrderDto;
import com.simple.api.simple_api.repository.model.Order;

public interface IOrderService {
    
    Order placeOrder(Long userId);

    OrderDto getOrder(Long orderId);
     
    List<OrderDto> getUserOrders(Long userId);
}
