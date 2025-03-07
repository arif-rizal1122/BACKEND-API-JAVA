package com.simple.api.simple_api.service.order;

import java.util.List;

import com.simple.api.simple_api.model.Order;

public interface IOrderService {
    
    Order placeOrder(Long userId);

    Order getOrder(Long orderId);
     
    List<Order> getUserOrders(Long userId);
}
