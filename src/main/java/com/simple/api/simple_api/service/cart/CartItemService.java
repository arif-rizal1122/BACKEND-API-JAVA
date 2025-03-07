package com.simple.api.simple_api.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.repository.CartItemRepository;
import com.simple.api.simple_api.repository.CartRepository;
import com.simple.api.simple_api.repository.model.Cart;
import com.simple.api.simple_api.repository.model.CartItem;
import com.simple.api.simple_api.repository.model.Product;
import com.simple.api.simple_api.service.product.IProductService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{


    private final IProductService productService;

    private final ICartService cartService;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    @Transactional
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // 1. Get the cart
        Cart cart = cartService.getCart(cartId);
        if (cart == null) {
            throw new ResponseNotFoundException("Cart not found with id: " + cartId);
        }
    
        // 2. Get the product
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new ResponseNotFoundException("Product not found with id: " + productId);
        }
    
        // 3. Check if the product already exists in the cart
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(new CartItem());
    
        // 4. If the product is not in the cart, initialize a new CartItem
        if (cartItem.getId() == null) {
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            // 5. If the product is already in the cart, update the 
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
    
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }


    @Transactional
    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
       Cart cart = cartService.getCart(cartId);
       CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);    
        cartRepository.save(cart);
    }


    @Transactional
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
            });  
        BigDecimal totalAmount = cart.getItems().stream()
                          .map(CartItem ::getTotalPrice)
                          .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);    
        cartRepository.save(cart);  
    }


    @Transactional
    @Override
    public CartItem getCartItem(Long cartId, Long productId){
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
        .stream()
        .filter(item -> item.getProduct().getId().equals(productId))
        .findFirst().orElseThrow(() -> new ResponseNotFoundException("product not found"));
    }
}
