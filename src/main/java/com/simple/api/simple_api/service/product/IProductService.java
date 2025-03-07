package com.simple.api.simple_api.service.product;

import java.util.List;

import com.simple.api.simple_api.dto.request.CreateProductRequest;
import com.simple.api.simple_api.dto.request.UpdateProductRequest;
import com.simple.api.simple_api.dto.response.ProductDto;
import com.simple.api.simple_api.repository.model.Product;

public interface IProductService {

    Product getProductById(Long id);

    Product addProduct(CreateProductRequest request);

    void deleteProductById(Long id);

    Product updateProduct(UpdateProductRequest request, Long productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);
 
    List<Product> getProductsByName(String name);    

    List<Product> getProductsByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
