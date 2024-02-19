package com.prico.util;

import com.prico.dto.*;
import com.prico.entity.Brand;
import com.prico.entity.Product;
import com.prico.entity.Category;
import com.prico.entity.Store;

public class ObjectMapper {

    public static BrandResponseDto toDto(Brand entity) {
        BrandResponseDto dto = new BrandResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public static Brand toEntity(BrandRequestDto dto) {
        Brand entity = new Brand();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

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

    public static CategoryResponseDto toDto(Category entity) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public static Category toEntity(CategoryRequestDto dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public static StoreResponseDto toDto(Store entity) {
        StoreResponseDto dto = new StoreResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLocation(entity.getLocation());
        dto.setWebsite(entity.getWebsite());
        return dto;
    }

    public static Store toEntity(StoreRequestDto dto) {
        Store entity = new Store();
        entity.setName(dto.getName());
        entity.setLocation(dto.getLocation());
        entity.setWebsite(dto.getWebsite());
        return entity;
    }
}
