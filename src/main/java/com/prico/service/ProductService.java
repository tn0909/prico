package com.prico.service;

import com.prico.dto.crud.ProductRequestDto;
import com.prico.dto.crud.ProductResponseDto;
import com.prico.dto.comparison.ProductVariationResponseDto;
import com.prico.dto.SearchRequestDto;
import com.prico.model.Product;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getAll();

    ProductResponseDto getById(Long id);

    Product create(ProductRequestDto product);

    Product update(Long id, ProductRequestDto product);

    void delete(Long id);

    List<ProductResponseDto> search(SearchRequestDto searchRequest);

    ProductVariationResponseDto getProductVariation(Long productId);
}
