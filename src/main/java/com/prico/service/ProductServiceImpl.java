package com.prico.service;

import com.prico.dto.ProductRequestDto;
import com.prico.dto.ProductResponseDto;
import com.prico.exception.ProductNotFoundException;
import com.prico.entity.Product;
import com.prico.repository.ProductRepository;
import com.prico.util.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    public List<ProductResponseDto> getAll() {
        return repository
            .findAll()
            .stream()
            .map(x -> ObjectMapper.toDto(x))
            .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getById(Long id) {
        Optional<Product> optionalProduct = repository.findById(id);

        if (optionalProduct.isPresent()) {
            return ObjectMapper.toDto(optionalProduct.get());
        }

        throw new ProductNotFoundException("Product not found with id: " + id);
    }

    @Override
    public Product create(ProductRequestDto productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());

        return repository.save(product);
    }

    @Override
    public Product update(Long id, ProductRequestDto productRequest) {
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
            return;
        }

        throw new ProductNotFoundException("Product not found with id: " + id);
    }
}
