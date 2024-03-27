package com.prico.controller;

import com.prico.dto.ApiResponse;
import com.prico.dto.SearchRequestDto;
import com.prico.dto.comparison.ProductVariationResponseDto;
import com.prico.dto.crud.ProductRequestDto;
import com.prico.dto.crud.ProductResponseDto;
import com.prico.dto.crud.ProductStoreRequestDto;
import com.prico.dto.crud.ProductStoreResponseDto;
import com.prico.model.Product;
import com.prico.service.ProductService;
import com.prico.service.ProductStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/product-stores")
public class ProductStoreController {

    @Autowired
    private ProductStoreService service;

    @GetMapping("/{id}")
    public ResponseEntity<ProductStoreResponseDto> getById(@PathVariable Long id) {
        ProductStoreResponseDto productStore = service.getById(id);
        return ResponseEntity.ok(productStore);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody ProductStoreRequestDto productStore) {
        service.create(productStore);

        ApiResponse<Product> response = new ApiResponse<>();
        response.setMessage("Product variation has been created successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable Long id,
                                                 @Valid @RequestBody ProductStoreRequestDto productStore) {
        service.update(id, productStore);

        ApiResponse<Product> response = new ApiResponse<>();
        response.setMessage("Product variation has been updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        service.delete(id);

        ApiResponse<Product> response = new ApiResponse<>();
        response.setMessage("Product variation has been deleted successfully");

        return ResponseEntity.ok(response);
    }
}
