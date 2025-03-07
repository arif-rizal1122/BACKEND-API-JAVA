package com.simple.api.simple_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.api.simple_api.repository.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    
    void deleteAllByCartId(Long id);

}
