package com.simple.api.simple_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.simple.api.simple_api.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
   Optional<Category> findByName(String name);
   boolean existsByName(String name);
}
