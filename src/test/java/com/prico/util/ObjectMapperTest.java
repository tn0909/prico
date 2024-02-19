package com.prico.util;

import com.prico.dto.*;
import com.prico.entity.Brand;
import com.prico.entity.Product;
import com.prico.entity.ProductCategory;
import com.prico.entity.Store;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;

public class ObjectMapperTest {

    @Test
    public void testToDto_Brand() {
        // Given
        Brand entity = new Brand(1L, "Product 1", "Product description");

        // When
        BrandResponseDto dto = ObjectMapper.toDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getDescription(), dto.getDescription());
    }

    @Test
    public void testToEntity_Brand() {
        // Given
        BrandRequestDto dto = new BrandRequestDto("Product 1", "Product description");

        // When
        Brand entity = ObjectMapper.toEntity(dto);

        // Then
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getDescription(), entity.getDescription());
    }


    @Test
    public void testToDto_Product() {
        // Given
        Product entity = new Product(1L, "Product 1", "Product description");

        // When
        ProductResponseDto dto = ObjectMapper.toDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getDescription(), dto.getDescription());
    }

    @Test
    public void testToEntity_Product() {
        // Given
        ProductRequestDto dto = new ProductRequestDto("Product 1", "Product description");

        // When
        Product entity = ObjectMapper.toEntity(dto);

        // Then
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getDescription(), entity.getDescription());
    }

    @Test
    public void testToDto_ProductCategory() {
        // Given
        ProductCategory entity = new ProductCategory(1L, "Product category 1", "Product category description");

        // When
        CategoryResponseDto dto = ObjectMapper.toDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getDescription(), dto.getDescription());
    }

    @Test
    public void testToEntity_ProductCategory() {
        // Given
        CategoryRequestDto dto = new CategoryRequestDto("Product category 1", "Product category description");

        // When
        ProductCategory entity = ObjectMapper.toEntity(dto);

        // Then
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getDescription(), entity.getDescription());
    }


    @Test
    public void testToDto_Store() {
        // Given
        Store entity = new Store(1L, "Store 1", "Store description", "store.com");

        // When
        StoreResponseDto dto = ObjectMapper.toDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getLocation(), dto.getLocation());
    }

    @Test
    public void testToEntity_Store() {
        // Given
        StoreRequestDto dto = new StoreRequestDto("Store 1", "Store description", "store.com");

        // When
        Store entity = ObjectMapper.toEntity(dto);

        // Then
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getLocation(), entity.getLocation());
        assertEquals(dto.getWebsite(), entity.getWebsite());
    }
}
