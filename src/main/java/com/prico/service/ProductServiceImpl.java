package com.prico.service;

import com.prico.exception.ProductNotFoundException;
import com.prico.model.Product;
import com.prico.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product getProductById(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }

//    @Override
//    public List<ProductComparisonResult> comparePrices(List<Long> productIds) {
//        // Implement price comparison logic here
//        return Collections.emptyList();
//    }
//
//    // Other method implementations for CRUD operations, etc.
}
