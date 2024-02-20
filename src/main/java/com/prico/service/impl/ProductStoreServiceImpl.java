package com.prico.service.impl;

import com.prico.dto.SearchRequestDto;
import com.prico.dto.comparison.ProductVariationResponseDto;
import com.prico.dto.comparison.StoreDto;
import com.prico.dto.crud.ProductRequestDto;
import com.prico.dto.crud.ProductResponseDto;
import com.prico.dto.crud.ProductStoreRequestDto;
import com.prico.dto.crud.ProductStoreResponseDto;
import com.prico.exception.ResourceNotFoundException;
import com.prico.model.*;
import com.prico.repository.*;
import com.prico.service.ProductService;
import com.prico.service.ProductStoreService;
import com.prico.util.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductStoreServiceImpl implements ProductStoreService {

    @Autowired
    private ProductStoreRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public List<ProductStoreResponseDto> getAll() {
        return repository
            .findAll()
            .stream()
            .map(x -> ObjectMapper.toDto(x))
            .collect(Collectors.toList());
    }

    @Override
    public ProductStoreResponseDto getById(Long id) {
        Optional<ProductStore> optionalProduct = repository.findById(id);

        if (optionalProduct.isPresent()) {
            return ObjectMapper.toDto(optionalProduct.get());
        }

        throw new ResourceNotFoundException("Product not found with id: " + id);
    }

    @Override
    public ProductStore create(ProductStoreRequestDto productStoreRequest) {
        ProductStore productStore = ObjectMapper.toEntity(productStoreRequest);

        Optional<Product> product = productRepository.findById(productStoreRequest.getProductId());

        if (!product.isPresent()) {
            throw new ResourceNotFoundException("Product not found with id: " + productStoreRequest.getProductId());
        }

        productStore.setProduct(product.get());

        Optional<Store> store = storeRepository.findById(productStoreRequest.getStoreId());

        if (!store.isPresent()) {
            throw new ResourceNotFoundException("Store not found with id: " + productStoreRequest.getStoreId());
        }

        productStore.setStore(store.get());

        return repository.save(productStore);
    }

    @Override
    public ProductStore update(Long id, ProductStoreRequestDto productStoreRequest) {
        ProductStore existingProductStore = repository.findById(id).orElse(null);

        if (existingProductStore == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        existingProductStore.setName(productStoreRequest.getName());
        existingProductStore.setUrl(productStoreRequest.getUrl());
        existingProductStore.setImageUrl(productStoreRequest.getImageUrl());
        existingProductStore.setPrice(productStoreRequest.getPrice());

        Optional<Product> product = productRepository.findById(productStoreRequest.getProductId());

        if (!product.isPresent()) {
            throw new ResourceNotFoundException("Product not found with id: " + productStoreRequest.getProductId());
        }

        existingProductStore.setProduct(product.get());

        Optional<Store> store = storeRepository.findById(productStoreRequest.getStoreId());

        if (!store.isPresent()) {
            throw new ResourceNotFoundException("Store not found with id: " + productStoreRequest.getStoreId());
        }

        existingProductStore.setStore(store.get());

        return repository.save(existingProductStore);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return;
        }

        throw new ResourceNotFoundException("Product Store not found with id: " + id);
    }

}
