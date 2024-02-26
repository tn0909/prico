package com.prico.controller;

import com.prico.dto.ApiResponse;
import com.prico.dto.comparison.ProductVariationResponseDto;
import com.prico.dto.crud.ProductRequestDto;
import com.prico.dto.crud.ProductResponseDto;
import com.prico.dto.SearchRequestDto;
import com.prico.model.Product;
import com.prico.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping()
    public ResponseEntity<List<ProductResponseDto>> getAll() {
        List<ProductResponseDto> products = service.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
        ProductResponseDto product = service.getById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody ProductRequestDto product) {
        service.create(product);

        ApiResponse<Product> response = new ApiResponse<>();
        response.setMessage("Product has been created successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable Long id,
                                                 @Valid @RequestBody ProductRequestDto product) {
        service.update(id, product);

        ApiResponse<Product> response = new ApiResponse<>();
        response.setMessage("Product has been updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        service.delete(id);

        ApiResponse<Product> response = new ApiResponse<>();
        response.setMessage("Product has been deleted successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<List<ProductResponseDto>> search(@Valid @RequestBody SearchRequestDto search) {
        List<ProductResponseDto> products = service.search(search);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}/variations")
    public ResponseEntity<ProductVariationResponseDto> getVariationsByProduct(@PathVariable Long id) {
        ProductVariationResponseDto response = service.getVariationsByProduct(id);
        return ResponseEntity.ok(response);
    }
}
