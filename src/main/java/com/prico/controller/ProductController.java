package com.prico.controller;

import com.prico.dto.ApiResponse;
import com.prico.dto.ProductRequest;
import com.prico.dto.ProductResponse;
import com.prico.entity.Product;
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
    public ResponseEntity<List<ProductResponse>> getAll() {
        List<ProductResponse> products = service.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        ProductResponse product = service.getById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody ProductRequest product) {
        log.debug("TEST: controller.create");
        service.create(product);

        ApiResponse<Product> response = new ApiResponse<>();
        response.setMessage("Product has been created successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable Long id,
                                                 @Valid @RequestBody ProductRequest product) {
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
}
