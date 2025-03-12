package com.simple.api.simple_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simple.api.simple_api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
