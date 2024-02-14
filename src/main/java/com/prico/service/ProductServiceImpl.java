package com.prico.service;

import com.prico.dto.ProductRequest;
import com.prico.exception.ProductNotFoundException;
import com.prico.entity.Product;
import com.prico.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public Product getById(Long id) {
        Optional<Product> optionalProduct = repository.findById(id);
        return optionalProduct.orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Override
    public Product create(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());

        return repository.save(product);
    }

    @Override
    public Product update(Long id, ProductRequest productRequest) {
        Product existingProduct = repository.findById(id).orElse(null);

        if (existingProduct != null) {
            existingProduct.setName(productRequest.getName());
            existingProduct.setDescription(productRequest.getDescription());

            return repository.save(existingProduct);
        }

        throw new ProductNotFoundException("Product not found with id: " + id);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }

        throw new ProductNotFoundException("Product not found with id: " + id);
    }
}
