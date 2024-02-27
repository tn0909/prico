package com.prico.service.impl;

import com.prico.dto.comparison.ProductVariationResponseDto;
import com.prico.dto.comparison.StoreDto;
import com.prico.dto.crud.ProductRequestDto;
import com.prico.dto.crud.ProductResponseDto;
import com.prico.dto.SearchRequestDto;
import com.prico.model.*;
import com.prico.exception.ResourceNotFoundException;
import com.prico.repository.BrandRepository;
import com.prico.repository.CategoryRepository;
import com.prico.repository.ProductRepository;
import com.prico.repository.ProductStoreRepository;
import com.prico.service.ProductService;
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

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductStoreRepository productStoreRepository;

    @Override
    public List<ProductResponseDto> getAll() {
        return repository
            .findAll()
            .stream()
            .map(x -> ObjectMapper.toDto(x))
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> search(SearchRequestDto searchRequest) {
        return repository
                .search(
                        searchRequest.getName().toLowerCase(),
                        searchRequest.getCategory().toLowerCase(),
                        searchRequest.getBrand().toLowerCase())
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

        throw new ResourceNotFoundException("Product not found with id: " + id);
    }

    @Override
    public Product create(ProductRequestDto productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setImageUrl(productRequest.getImageUrl());

        if (productRequest.getBrandId() != null) {
            Optional<Brand> brand = brandRepository.findById(productRequest.getBrandId());

            if (!brand.isPresent()) {
                throw new ResourceNotFoundException("Brand not found with id: " + productRequest.getBrandId());
            }

            product.setBrand(brand.get());
        }

        if (productRequest.getCategoryId() != null) {
            Optional<Category> category = categoryRepository.findById(productRequest.getCategoryId());

            if (!category.isPresent()) {
                throw new ResourceNotFoundException("Category not found with id: " + productRequest.getCategoryId());
            }

            product.setCategory(category.get());
        }

        return repository.save(product);
    }

    @Override
    public Product update(Long id, ProductRequestDto productRequest) {
        Product existingProduct = repository.findById(id).orElse(null);

        if (existingProduct == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());

        existingProduct.setImageUrl(productRequest.getImageUrl());

        if (productRequest.getBrandId() != null) {
            Optional<Brand> brand = brandRepository.findById(productRequest.getBrandId());

            if (!brand.isPresent()) {
                throw new ResourceNotFoundException("Brand not found with id: " + productRequest.getBrandId());
            }

            existingProduct.setBrand(brand.get());
        } else {
            existingProduct.setBrand(null);
        }

        if (productRequest.getCategoryId() != null) {
            Optional<Category> category = categoryRepository.findById(productRequest.getCategoryId());

            if (!category.isPresent()) {
                throw new ResourceNotFoundException("Category not found with id: " + productRequest.getCategoryId());
            }

            existingProduct.setCategory(category.get());
        }
        else {
            existingProduct.setCategory(null);
        }

        return repository.save(existingProduct);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return;
        }

        throw new ResourceNotFoundException("Product not found with id: " + id);
    }

    @Override
    public ProductVariationResponseDto getVariationsByProduct(Long productId) {
        Product product = repository.findById(productId).orElse(null);

        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        ProductVariationResponseDto response = ProductVariationResponseDto
                .builder()
                 .productId(product.getId())
                .productName(product.getName())
                .productImageUrl(product.getImageUrl())
                .build();

        List<ProductStore> productStores = productStoreRepository.findAllByProduct(product);

        if (productStores.isEmpty()) {
            return response;
        }

        List<StoreDto> stores = productStores
                .stream()
                .collect(Collectors.groupingBy(ProductStore::getStore))
                .entrySet()
                .stream()
                .map(x -> ObjectMapper.toDto(x))
                .collect(Collectors.toList());

        response.setStores(stores);

        return response;
    }
}
