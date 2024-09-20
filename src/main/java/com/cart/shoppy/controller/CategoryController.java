package com.cart.shoppy.controller;

import com.cart.shoppy.exceptions.AlreadyExistsException;
import com.cart.shoppy.exceptions.EntityNotFoundException;
import com.cart.shoppy.model.Category;
import com.cart.shoppy.response.ApiResponse;
import com.cart.shoppy.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found!", categories));
        } catch (Exception exception) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        try {
            Category newCategory = categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Added the Category:", newCategory));
        } catch (AlreadyExistsException exception) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(exception.getMessage(), null));
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found!", category));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(exception.getMessage(), null));
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found!", category));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(exception.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, id);
            return ResponseEntity.ok(new ApiResponse("Updated the Category:", updatedCategory));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(exception.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted!", null));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(exception.getMessage(), null));
        }
    }
}
