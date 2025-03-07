package com.simple.api.simple_api.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.simple.api.simple_api.enums.OrderStatus;
import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.model.Cart;
import com.simple.api.simple_api.model.Order;
import com.simple.api.simple_api.model.OrderItem;
import com.simple.api.simple_api.model.Product;
import com.simple.api.simple_api.repository.OrderRepository;
import com.simple.api.simple_api.repository.ProductRepository;
import com.simple.api.simple_api.service.cart.ICartService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final ICartService cartService;
    

    @Override
    public Order placeOrder(Long userId) {
         Cart cart = cartService.getCartByUserId(userId);
         Order order = createOrder(cart);
         List<OrderItem> orderItemList = createOrderItems(order, cart);
         order.setOrderItems(new HashSet<>(orderItemList));
         order.setTotalAmount(calculateTotalAmount(orderItemList));

         Order savedOrder = orderRepository.save(order);
         cartService.clearCart(cart.getId());

        return savedOrder;
    }




    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
        .orElseThrow(() -> new ResponseNotFoundException("order id not found"));
    }



    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser()); 
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }



    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                order,
                product,
                cartItem.getQuantity(),
                cartItem.getUnitPrice()
            );
        }).toList();
    }

    
    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList.stream()
                .map(item -> item.getPrice()
                .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Order> getUserOrders(Long userId){
        return orderRepository.findByUserId(userId);
    }

}
