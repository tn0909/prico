package com.prico.util;

import com.prico.dto.comparison.ProductStoreDto;
import com.prico.dto.comparison.StoreDto;
import com.prico.dto.crud.ProductStoreResponseDto;
import com.prico.dto.crud.*;
import com.prico.model.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        dto.setImageUrl(product.getImageUrl());

        if (product.getBrand() != null) {
            dto.setBrand(toDto(product.getBrand()));
        }

        if (product.getCategory() != null) {
            dto.setCategory(toDto(product.getCategory()));
        }

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


    public static ProductStoreResponseDto toDto(ProductStore entity) {
        return ProductStoreResponseDto
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .url(entity.getUrl())
                .imageUrl(entity.getImageUrl())
                .price(entity.getPrice())
                .store(toDto(entity.getStore()))
                .product(toDto(entity.getProduct()))
                .build();
    }

    public static ProductStore toEntity(ProductStoreRequestDto dto) {
        return ProductStore
                .builder()
                .name(dto.getName())
                .url(dto.getUrl())
                .imageUrl(dto.getImageUrl())
                .price(dto.getPrice())
                .build();
    }

    public static StoreDto toDto(Map.Entry<Store, List<ProductStore>> storeProductStoreEntry) {
        Store store = storeProductStoreEntry.getKey();
        List<ProductStore> variations = storeProductStoreEntry.getValue();

        List<ProductStoreDto> variationDtos = variations.stream()
                .map(variation -> ProductStoreDto
                        .builder()
                        .id(variation.getId())
                        .name(variation.getName())
                        .url(variation.getUrl())
                        .imageUrl(variation.getImageUrl())
                        .price(variation.getPrice())
                        .build()
                )
                .collect(Collectors.toList());

        return StoreDto
                .builder()
                .id(store.getId())
                .name(store.getName())
                .website(store.getWebsite())
                .variations(variationDtos)
                .build();
    }
}
