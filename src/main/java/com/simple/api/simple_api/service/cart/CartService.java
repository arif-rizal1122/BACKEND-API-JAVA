package com.simple.api.simple_api.service.cart;

import java.math.BigDecimal;


import org.springframework.stereotype.Service;

import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.repository.CartItemRepository;
import com.simple.api.simple_api.repository.CartRepository;
import com.simple.api.simple_api.repository.model.Cart;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
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
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Long initializeNewCart() {
        Cart newCart = new Cart();
        return cartRepository.save(newCart).getId(); 
    }
    

    @Override
    public Cart getCartByUserId(Long userId){
        Cart cart = cartRepository.findByUserId(userId);
        return cart;
    }

}
