package com.prico.util;

import com.prico.dto.ProductRequest;
import com.prico.dto.ProductResponse;
import com.prico.entity.Product;

public class ObjectMapper {

    public static ProductResponse toDto(Product product) {
        ProductResponse dto = new ProductResponse();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        return dto;
    }

    public static Product toEntity(ProductRequest dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        return product;
    }
}
