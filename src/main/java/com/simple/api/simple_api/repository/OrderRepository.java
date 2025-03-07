package com.simple.api.simple_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.api.simple_api.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
     List<Order> findByUserId(Long userId);
}
