package com.simple.api.simple_api.service.cart;

import java.math.BigDecimal;

import com.simple.api.simple_api.model.Cart;

public interface ICartService {
    
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Long initializeNewCart();
}
