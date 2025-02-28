package com.simple.api.simple_api.service.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.simple.api.simple_api.dto.request.CreateProductRequest;
import com.simple.api.simple_api.dto.request.UpdateProductRequest;
import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.model.Category;
import com.simple.api.simple_api.model.Product;
import com.simple.api.simple_api.repository.CategoryRepository;
import com.simple.api.simple_api.repository.ProductRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    // 
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
        .orElseThrow(() -> new ResponseNotFoundException("product is not found!!"));
    }

    
    // 
    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
        .ifPresentOrElse(productRepository::delete, 
        () -> {
            throw new ResponseNotFoundException("product is not found");
             });
    }


    // 
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    // 
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }


    // 
    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

   
    // 
    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }


    // 
    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }
    


    //
    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }


    //
    @Override
    public Product addProduct(CreateProductRequest request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
        .orElseGet(() -> {
            Category newCategory = new Category(request.getCategory().getName());
            return categoryRepository.save(newCategory);
        }); 
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(CreateProductRequest request, Category category){
        return new Product(
            request.getName(),
            request.getBrand(),
            request.getPrice(),
            request.getInventory(),
            request.getDescription(),
            category
        );
    }
    


    // 
    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
         return productRepository.findById(productId)
         .map(existingProduct -> updateExistingProduct(existingProduct, request))
         .map(productRepository :: save)
         .orElseThrow(() -> new ResponseNotFoundException("product not found!!"));        
    }


    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }


    // 
    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }


        

}
