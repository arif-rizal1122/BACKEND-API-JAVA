package com.simple.api.simple_api.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.model.Cart;
import com.simple.api.simple_api.model.CartItem;
import com.simple.api.simple_api.model.Product;
import com.simple.api.simple_api.repository.CartItemRepository;
import com.simple.api.simple_api.repository.CartRepository;
import com.simple.api.simple_api.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{

    private final CartItemRepository cartItemRepository;

    private final IProductService productService;

    private final ICartService cartService;

    private final CartRepository cartRepository;

    @Override
    public void addItemToCart(Long cartId, Long productId, Integer quantity) {
        //1. get the cart
        //2. get the product
        //3. check if the product already in the cart
        //4. if yes, the increase the quantity with the requested quantity
        //5. if no, the initiate a new cardItem entry

        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getId()
                .equals(productId)).findFirst()
                .orElse(new CartItem());

        if (cartItem.getId() == null) {
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }



    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
       Cart cart = cartService.getCart(cartId);
       CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);    
        cartRepository.save(cart);
    }


    @Override
    public void updateItemQuantity(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
            });  
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);    
        cartRepository.save(cart);  
    }


    @Override
    public CartItem getCartItem(Long cartId, Long productId){
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
        .stream()
        .filter(item -> item.getProduct().getId().equals(productId))
        .findFirst().orElseThrow(() -> new ResponseNotFoundException("product not found"));
    }

    
}
