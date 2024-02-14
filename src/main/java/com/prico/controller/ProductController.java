package com.prico.controller;

import com.prico.dto.ApiSuccessResponse;
import com.prico.dto.ProductRequest;
import com.prico.entity.Product;
import com.prico.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping()
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = service.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        Product product = service.getById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping()
    public ResponseEntity<ApiSuccessResponse<Product>> create(@Valid @RequestBody ProductRequest product) {
        service.create(product);

        ApiSuccessResponse<Product> response = new ApiSuccessResponse<>();
        response.setMessage("Product has been created successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<Product>> update(@PathVariable Long id, @Valid @RequestBody ProductRequest product) {
        service.update(id, product);

        ApiSuccessResponse<Product> response = new ApiSuccessResponse<>();
        response.setMessage("Product has been updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<Product>> delete(@PathVariable Long id) {
        service.delete(id);

        ApiSuccessResponse<Product> response = new ApiSuccessResponse<>();
        response.setMessage("Product has been deleted successfully");

        return ResponseEntity.ok(response);
    }
}
