package com.prico.service;

import com.prico.dto.ProductRequestDto;
import com.prico.dto.ProductResponseDto;
import com.prico.entity.Product;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getAll();

    ProductResponseDto getById(Long id);

    Product create(ProductRequestDto product);

    Product update(Long id, ProductRequestDto product);

    void delete(Long id);
}
