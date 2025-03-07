package com.simple.api.simple_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.api.simple_api.repository.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
   Category findByName(String name);
   boolean existsByName(String name);
}
