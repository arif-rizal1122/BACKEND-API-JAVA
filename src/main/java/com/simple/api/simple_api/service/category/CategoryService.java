package com.simple.api.simple_api.service.category;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.simple.api.simple_api.exception.AlreadyExistException;
import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.model.Category;
import com.simple.api.simple_api.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;


    // 
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
        .orElseThrow(() -> new ResponseNotFoundException("category is not found!!"));
    }


    //
    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }



    //
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }



    //
    @Override
    public boolean deleteCategoryById(Long id) {
        AtomicBoolean isDeleted = new AtomicBoolean(false);

        categoryRepository.findById(id).ifPresentOrElse(category -> {
            categoryRepository.delete(category);
            isDeleted.set(true);
        }, () -> {
            throw new ResponseNotFoundException("category not found!!");
        });
        return isDeleted.get();
    }


    

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
        .map(categoryRepository ::save).orElseThrow(() -> new AlreadyExistException(category.getName() + "category is not found!!"));
    }





    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id))
        .map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new ResponseNotFoundException("category is not found!!"));
    }
    

}
