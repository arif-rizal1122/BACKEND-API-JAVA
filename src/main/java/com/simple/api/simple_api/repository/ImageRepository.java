package com.simple.api.simple_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.api.simple_api.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    
}
