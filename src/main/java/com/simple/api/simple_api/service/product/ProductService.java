package com.simple.api.simple_api.service.product;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.simple.api.simple_api.dto.request.CreateProductRequest;
import com.simple.api.simple_api.dto.request.UpdateProductRequest;
import com.simple.api.simple_api.dto.response.ImageDto;
import com.simple.api.simple_api.dto.response.ProductDto;
import com.simple.api.simple_api.exception.AlreadyExistException;
import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.model.Category;
import com.simple.api.simple_api.model.Image;
import com.simple.api.simple_api.model.Product;
import com.simple.api.simple_api.repository.CategoryRepository;
import com.simple.api.simple_api.repository.ImageRepository;
import com.simple.api.simple_api.repository.ProductRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;

    private final ImageRepository imageRepository;

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
        if (productExists(request.getName(), request.getBrand())) {
            throw new AlreadyExistException(request.getBrand() +" "+ request.getName() + " already exists, you may update this product");
        }

        Category category = categoryRepository.findByName(request.getCategory().getName())
            .orElseGet(() -> {
                Category newCategory = new Category(request.getCategory().getName());
                return categoryRepository.save(newCategory);
            });
    
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }



    private boolean productExists(String name, String brand){
         return productRepository.existsByNameAndBrand(name, brand);
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

        Category category = categoryRepository.findByName(request.getCategory().getName()).orElseGet(() -> {
            Category newCategory = new Category(request.getCategory().getName());
            return categoryRepository.save(newCategory);
        });
        existingProduct.setCategory(category);
        return existingProduct;
    }


    // 
    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }


    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }


    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
        .map(image -> modelMapper.map(image, ImageDto.class))
        .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }



}
