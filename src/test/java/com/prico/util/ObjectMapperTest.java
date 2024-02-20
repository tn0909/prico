package com.prico.util;

import com.prico.dto.*;
import com.prico.model.Brand;
import com.prico.model.Product;
import com.prico.model.Category;
import com.prico.model.Store;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

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
        Product entity = Product
                .builder()
                .id(1L)
                .name("Product 1")
                .description("Product description")
                .category(new Category(2L, "Category 1", "Category description"))
                .brand(new Brand(3L, "Brand 1", "Brand description"))
                .build();

        // When
        ProductResponseDto dto = ObjectMapper.toDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getDescription(), dto.getDescription());

        assertNotNull(entity.getBrand());
        assertEquals(entity.getBrand().getId(), dto.getBrand().getId());
        assertEquals(entity.getBrand().getName(), dto.getBrand().getName());
        assertEquals(entity.getBrand().getDescription(), dto.getBrand().getDescription());

        assertNotNull(entity.getCategory());
        assertEquals(entity.getCategory().getId(), dto.getCategory().getId());
        assertEquals(entity.getCategory().getName(), dto.getCategory().getName());
        assertEquals(entity.getCategory().getDescription(), dto.getCategory().getDescription());
    }

    @Test
    public void testToDto_ProductWithoutCategory() {
        // Given
        Product entity = Product
                .builder()
                .id(1L)
                .name("Product 1")
                .description("Product description")
                .brand(new Brand(3L, "Brand 1", "Brand description"))
                .build();

        // When
        ProductResponseDto dto = ObjectMapper.toDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getDescription(), dto.getDescription());

        assertNotNull(entity.getBrand());
        assertEquals(entity.getBrand().getId(), dto.getBrand().getId());
        assertEquals(entity.getBrand().getName(), dto.getBrand().getName());
        assertEquals(entity.getBrand().getDescription(), dto.getBrand().getDescription());

        assertNull(entity.getCategory());
    }

    @Test
    public void testToDto_ProductWithoutBrand() {
        // Given
        Product entity = Product
                .builder()
                .id(1L)
                .name("Product 1")
                .description("Product description")
                .category(new Category(2L, "Category 1", "Category description"))
                .build();

        // When
        ProductResponseDto dto = ObjectMapper.toDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getDescription(), dto.getDescription());

        assertNull(entity.getBrand());

        assertNotNull(entity.getCategory());
        assertEquals(entity.getCategory().getId(), dto.getCategory().getId());
        assertEquals(entity.getCategory().getName(), dto.getCategory().getName());
        assertEquals(entity.getCategory().getDescription(), dto.getCategory().getDescription());
    }

    @Test
    public void testToEntity_Product() {
        // Given
        ProductRequestDto dto = ProductRequestDto
                .builder()
                .name("Product 1")
                .description("Product description")
                .build();

        // When
        Product entity = ObjectMapper.toEntity(dto);

        // Then
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getDescription(), entity.getDescription());
    }

    @Test
    public void testToDto_ProductCategory() {
        // Given
        Category entity = new Category(1L, "Product category 1", "Product category description");

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
        Category entity = ObjectMapper.toEntity(dto);

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
