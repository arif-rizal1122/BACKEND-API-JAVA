package com.simple.api.simple_api.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.model.Cart;
import com.simple.api.simple_api.repository.CartItemRepository;
import com.simple.api.simple_api.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{

    private final CartRepository cartRepository; 

    private final CartItemRepository cartItemRepository;



    @Override
    public Cart getCart(Long id) {
      Cart cart = cartRepository.findById(id)
      .orElseThrow(() -> new ResponseNotFoundException("cart id not found!!"));
      
      BigDecimal totalAmount = cart.getTotalAmount(); 
      cart.setTotalAmount(totalAmount);
      return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }
    

    
}
