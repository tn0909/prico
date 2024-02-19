package com.prico.controller;

import com.prico.dto.ApiResponse;
import com.prico.dto.CategoryRequestDto;
import com.prico.dto.CategoryResponseDto;
import com.prico.entity.ProductCategory;
import com.prico.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping()
    public ResponseEntity<List<CategoryResponseDto>> getAll() {
        List<CategoryResponseDto> productCategories = service.getAll();
        return ResponseEntity.ok(productCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable Long id) {
        CategoryResponseDto productCategory = service.getById(id);
        return ResponseEntity.ok(productCategory);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody CategoryRequestDto productCategory) {
        service.create(productCategory);

        ApiResponse<ProductCategory> response = new ApiResponse<>();
        response.setMessage("Product Category has been created successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable Long id,
                                                 @Valid @RequestBody CategoryRequestDto productCategory) {
        service.update(id, productCategory);

        ApiResponse<ProductCategory> response = new ApiResponse<>();
        response.setMessage("Product Category has been updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        service.delete(id);

        ApiResponse<ProductCategory> response = new ApiResponse<>();
        response.setMessage("Product Category has been deleted successfully");

        return ResponseEntity.ok(response);
    }
}
