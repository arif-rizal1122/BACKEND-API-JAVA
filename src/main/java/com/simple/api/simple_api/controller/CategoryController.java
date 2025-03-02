package com.simple.api.simple_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.api.simple_api.dto.response.ApiResponse;
import com.simple.api.simple_api.exception.AlreadyExistException;
import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.model.Category;
import com.simple.api.simple_api.service.category.ICategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    
    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAll(){
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok().body(new ApiResponse("get all categories success", categories));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("get all categories is failed", e.getMessage()));
        }
    }


    @PostMapping("/add")    
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name)
   {
       try {
        if (name == null || name.getName() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("category name is required", null));
        }

        Category theCategory = categoryService.addCategory(name);
        return ResponseEntity.ok(new ApiResponse("created category success", theCategory));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
   } 


   @GetMapping("/category/{categoryId}/getId")
   public ResponseEntity<ApiResponse> getCategoryById(@PathVariable("categoryId") Long categoryId){
     
      try {
           Category category = categoryService.getCategoryById(categoryId);
         if (category != null) {
           return ResponseEntity.ok(new ApiResponse("get category by id success!", category)); 
         }
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("failed get category by id", HttpStatus.NOT_FOUND));
    } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("failed get category id", e.getMessage()));        
    }
   }



   @GetMapping("/category/{name}/getName")
   public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable("name") String name){
     
      try {
           Category category = categoryService.getCategoryByName(name);
         if (category != null) {
           return ResponseEntity.ok(new ApiResponse("get category by name success!", category)); 
         }
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("failed get category by name", HttpStatus.NOT_FOUND));
    } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("failed get category name", e.getMessage()));        
    }
   }


   @DeleteMapping("/category/{categoryId}/delete")
   public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryId") Long categoryId){
       try {
        categoryService.deleteCategoryById(categoryId);
         return ResponseEntity.ok(new ApiResponse("delete category success!!", categoryId));
      } catch (ResponseNotFoundException e) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("category not found!!", null));
      } catch (Exception e){
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("delete category failed", e.getMessage()));
      }
   }

   

   @PutMapping("/category/{categoryId}/update")
   public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category, 
                                                     @PathVariable("categoryId") Long categoryId) {
       try {
           Category updatedCategory = categoryService.updateCategory(category, categoryId);
           if (updatedCategory == null) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body(new ApiResponse("Category ID not found", null));
           }
           if (!updatedCategory.getId().equals(categoryId)) {
               return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                       .body(new ApiResponse("Update failed, category ID mismatch!", null));
           }
           return ResponseEntity.ok(new ApiResponse("Update success!!", updatedCategory));
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(new ApiResponse("Update category failed!!", e.getMessage()));
       }
   }

   
   


}
