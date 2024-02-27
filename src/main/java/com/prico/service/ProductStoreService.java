package com.prico.service;

import com.prico.dto.crud.ProductStoreRequestDto;
import com.prico.dto.crud.ProductStoreResponseDto;
import com.prico.model.ProductStore;

import java.util.List;

public interface ProductStoreService {

    List<ProductStoreResponseDto> getAll();

    ProductStoreResponseDto getById(Long id);

    ProductStore create(ProductStoreRequestDto product);

    ProductStore update(Long id, ProductStoreRequestDto product);

    void delete(Long id);
}
