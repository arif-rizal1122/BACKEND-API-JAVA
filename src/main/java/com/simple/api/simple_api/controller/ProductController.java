package com.simple.api.simple_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simple.api.simple_api.dto.request.CreateProductRequest;
import com.simple.api.simple_api.dto.request.UpdateProductRequest;
import com.simple.api.simple_api.dto.response.ApiResponse;
import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.model.Product;
import com.simple.api.simple_api.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("get all products success!", products));
    }


    @GetMapping("/product/{productId}/get")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable("productId") Long productId){
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(new ApiResponse("get product by Id success!!", product));
        } catch (ResponseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("get product failed", null));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("get product failed", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody CreateProductRequest request){
        try {
            Product product = productService.addProduct(request);
            return ResponseEntity.ok(new ApiResponse("add product success!", product));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("add product failed", e.getMessage()));
        }
    }


    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(
        @RequestBody UpdateProductRequest request, 
        @PathVariable("productId") Long productId) {
    
        try {
            Product updatedProduct = productService.updateProduct(request, productId);
            if (updatedProduct == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Update product failed: Product not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Updated product success!", updatedProduct));
        } catch (ResponseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Update product failed: Product not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update product failed: " + e.getMessage(), null));
        }
    }


    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("productId") Long productId){
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("delete product success", productId));
        } catch (ResponseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("product not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("delete product failed", e.getMessage()));
        }
    }



    @GetMapping("/product/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(
        @RequestParam String brandName,
        @RequestParam String productName) {
        
        try {
            List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
            if (products == null || products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse("No products found for the given brand and name.", null));
            }
            return ResponseEntity.ok(new ApiResponse("Successfully retrieved products by brand and name!", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to get products by brand and name: " + e.getMessage(), null));
        }
    }
     



    
    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(
        @RequestParam String category,
        @RequestParam String brand) {
    
        try {
            if (category == null || category.trim().isEmpty() || brand == null || brand.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Category and brand parameters are required.", null));
            }
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            if (products == null || products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No products found for the given category and brand.", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products retrieved successfully!", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to get products: " + e.getMessage(), null));
        }
    }
    

    @GetMapping("/{name}/products")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable("name") String name){
       try {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest()
            .body(new ApiResponse("product category parameter are required", null));
        }
        List<Product> products = productService.getProductsByName(name);
        if (products == null || products.isEmpty()) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No products found by name.", null));
        }
             return ResponseEntity.ok(new ApiResponse("Success get products by name products", products));
         } catch(Exception e){
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("failed get product by name", e.getMessage()));
      } 
    }



    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand){
        try {
            if (brand == null || brand.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                .body(new ApiResponse("parameter are required", null));
            }
            List<Product> products = productService.getProductsByBrand(brand);
            if (products == null || products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                           .body(new ApiResponse("No products found by brand.", null));
           }
           return ResponseEntity.ok(new ApiResponse("Success get products by brand", products));               
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("failed get product by brand", e.getMessage())); 
        }
    }



    @GetMapping("/product/{category}/by-category")
    public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable("category") String category){
        try {
            if (category == null || category.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                .body(new ApiResponse("parameter are required", null));
            }
            List<Product> products = productService.getProductsByCategory(category);
            if (products == null || products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                           .body(new ApiResponse("No products found by category.", null));
           }
           return ResponseEntity.ok(new ApiResponse("Success get products by category", products));               
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("failed get product by category", e.getMessage())); 
        }
    }


    @GetMapping("/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name){
        try {
            var productCount = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("product count:",  productCount));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }

    }



}
