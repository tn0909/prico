package com.prico.util;

import com.prico.dto.ProductRequestDto;
import com.prico.dto.ProductResponseDto;
import com.prico.entity.Product;

public class ObjectMapper {

    public static ProductResponseDto toDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        return dto;
    }

    public static Product toEntity(ProductRequestDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        return product;
    }
}
