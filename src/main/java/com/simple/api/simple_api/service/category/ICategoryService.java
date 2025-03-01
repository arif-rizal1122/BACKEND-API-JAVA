package com.simple.api.simple_api.service.category;

import java.util.List;

import com.simple.api.simple_api.model.Category;

public interface ICategoryService {
    
    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    List<Category> getAllCategories();

    Category addCategory(Category category);

    Category updateCategory(Category category, Long id);

    boolean deleteCategoryById(Long id);

}
