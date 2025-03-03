package com.simple.api.simple_api.service.cart;

import com.simple.api.simple_api.model.CartItem;

public interface ICartItemService {
 
    void addCartItem(Long cartId, Long productId, int quantity);

    void removeItemFromCart(Long cartId, Long productId);

    void updateItemQuantity(Long cartId, Long productId, int quantity);
    
    CartItem getCartItem(Long cartId, Long productId);
}
