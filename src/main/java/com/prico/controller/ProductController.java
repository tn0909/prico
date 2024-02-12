package com.prico.controller;

import com.prico.exception.ProductNotFoundException;
import com.prico.model.Product;
import com.prico.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//
//    @GetMapping("/compare")
//    public ResponseEntity<List<ProductComparisonResult>> comparePrices(
//            @RequestParam("productIds") List<Long> productIds) {
//        List<ProductComparisonResult> comparisonResults = productService.comparePrices(productIds);
//        return ResponseEntity.ok(comparisonResults);
//    }
//
//    @PostMapping("/purchase")
//    public ResponseEntity<String> purchaseProduct(@RequestBody PurchaseRequest request) {
//        // Process purchase request and return appropriate response
//        return ResponseEntity.ok("Purchase successful");
//    }

    // Exception handling
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
