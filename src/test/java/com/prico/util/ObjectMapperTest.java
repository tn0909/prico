package com.prico.util;

import com.prico.dto.ProductRequest;
import com.prico.dto.ProductResponse;
import com.prico.entity.Product;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;

public class ObjectMapperTest {

    @Test
    public void testToDto_Product() {
        // Given
        Product product = new Product(1L, "Product 1", "Product description");

        // When
        ProductResponse productDto = ObjectMapper.toDto(product);

        // Then
        assertEquals(product.getId(), productDto.getId());
        assertEquals(product.getName(), productDto.getName());
        assertEquals(product.getDescription(), productDto.getDescription());
    }

    @Test
    public void testToEntity_Product() {
        // Given
        ProductRequest productDto = new ProductRequest("Product 1", "Product description");

        // When
        Product product = ObjectMapper.toEntity(productDto);

        // Then
        assertEquals(productDto.getName(), product.getName());
        assertEquals(productDto.getDescription(), product.getDescription());
    }
}
