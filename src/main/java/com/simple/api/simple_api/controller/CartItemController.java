package com.simple.api.simple_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simple.api.simple_api.dto.response.ApiResponse;
import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.model.Cart;
import com.simple.api.simple_api.model.CartItem;
import com.simple.api.simple_api.model.User;
import com.simple.api.simple_api.service.cart.ICartItemService;
import com.simple.api.simple_api.service.cart.ICartService;
import com.simple.api.simple_api.service.user.IUserService;

import lombok.RequiredArgsConstructor;
 

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    
    private final ICartItemService cartItemService;

    private final ICartService cartService;
    
    private final IUserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(
                                                    @RequestParam Long productId,
                                                    @RequestParam Integer quantity) {
        try {
            User user = userService.getByUserId(2L);
            Cart cart = cartService.initializeNewCart(user);
              
            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Add Item Success", null));
        } catch (ResponseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse("Failed to add item to cart: " + e.getMessage(), null));
                                 
        }
    }



    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart (@PathVariable("cartId") Long cartId, 
                                                          @PathVariable("itemId") Long itemId){

        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("success remove item from cart", null));
        } catch (ResponseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("failed remove item from cart", e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse("failed remove item from cart", "Internal Server Error"));
}                                                
    }

    

    @PutMapping("/cart/{cartId}/item/{itemId}/update")   
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable("cartId") Long cartId,
                                                          @PathVariable("itemId") Long itemId, 
                                                          @RequestParam Integer quantity){
        try {
            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("success update item quantity", quantity));
        } catch (ResponseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("failed update item quantity", HttpStatus.INTERNAL_SERVER_ERROR));
        }  
    }



    public ResponseEntity<ApiResponse> getCartItem(@PathVariable("cartId") Long cartId, 
                                                   @PathVariable("productId") Long productId){

        try {
            CartItem item = cartItemService.getCartItem(cartId, productId);                                            
            return ResponseEntity.ok(new ApiResponse("get cart item success", item));
        } catch (ResponseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("failed to get cart item", HttpStatus.INTERNAL_SERVER_ERROR));
        }  
    }






}
