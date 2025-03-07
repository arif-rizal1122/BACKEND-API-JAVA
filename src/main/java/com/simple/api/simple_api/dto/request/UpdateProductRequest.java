package com.simple.api.simple_api.dto.request;

import java.math.BigDecimal;

import com.simple.api.simple_api.repository.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {
    
    private String name;

    private String brand;
    
    private BigDecimal price;

    private int inventory;

    private String description;

    private Category category;

}
