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
import com.simple.api.simple_api.model.CartItem;
import com.simple.api.simple_api.service.cart.ICartItemService;

import lombok.RequiredArgsConstructor;
 

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    
    private final ICartItemService cartItemService;

    @PostMapping("/item/add-to-cart")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long cartId,
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity){
        try {
            cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("success add cart to item cart", null));
        } catch (ResponseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("cart not found", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("failed add cart to item cart", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }



    @DeleteMapping("/{cartId}/item/{productId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart (@PathVariable("cartId") Long cartId, 
                                                          @PathVariable("productId") Long productId){

        try {
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("success remove item from cart", null));
        } catch (ResponseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("failed remove item from cart", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("failed remove item from cart", HttpStatus.INTERNAL_SERVER_ERROR));
        }                                                
    }

    

    @PutMapping("/cart/{cartId}/item/{productId}/update")   
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable("cartId") Long cartId,
                                                          @PathVariable("productId") Long productId, 
                                                          @RequestParam Integer quantity){
        try {
            cartItemService.updateItemQuantity(cartId, productId, quantity);
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
