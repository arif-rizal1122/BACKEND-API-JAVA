package com.simple.api.simple_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simple.api.simple_api.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
}
