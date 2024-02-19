package com.prico.service.impl;

import com.prico.dto.ProductRequestDto;
import com.prico.dto.ProductResponseDto;
import com.prico.entity.Product;
import com.prico.exception.EntityNotFoundException;
import com.prico.repository.ProductRepository;
import com.prico.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        // Given
        Product product1 = new Product(1L, "Product 1", "Product 1 description");
        Product product2 = new Product(2L, "Product 2", "Product 2 description");
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // When
        List<ProductResponseDto> results = productService.getAll();

        // Then
        assertThat(results).isNotNull();
        assertEquals(2, results.size());
        assertThat(results.get(0).getId()).isEqualTo(1L);
        assertThat(results.get(0).getName()).isEqualTo("Product 1");
        assertThat(results.get(0).getDescription()).isEqualTo("Product 1 description");
        assertThat(results.get(1).getId()).isEqualTo(2L);
        assertThat(results.get(1).getName()).isEqualTo("Product 2");
        assertThat(results.get(1).getDescription()).isEqualTo("Product 2 description");
    }

    @Test
    public void testGetById() {
        // Given
        long productId = 1L;
        Product mockProduct = new Product(productId, "Product 1", "Product description");
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        // When
        ProductResponseDto result = productService.getById(productId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getName()).isEqualTo("Product 1");
        assertThat(result.getDescription()).isEqualTo("Product description");
    }

    @Test
    public void testGetById_WhenProductNotFound_ThrowNotFoundException() {
        // Given
        long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> productService.getById(productId));
    }

    @Test
    public void testCreate() {
        // Given
        ProductRequestDto newProduct = new ProductRequestDto("New Product", "Product description");
        Product savedProduct = new Product(1L, "New Product", "Product description");
        when(productRepository.save(any())).thenReturn(savedProduct);

        // When
        Product result = productService.create(newProduct);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("New Product");
        assertThat(result.getDescription()).isEqualTo("Product description");
    }

    @Test
    public void testUpdate() {
        // Given
        long productId = 1L;
        ProductRequestDto updatedProduct = new ProductRequestDto("Updated Product", "Product description");
        Product retrievedProduct = new Product(1L, "Original Product", "Original description");
        Product savedProduct = new Product(1L, "Updated Product", "Product description");

        when(productRepository.findById(productId)).thenReturn(Optional.of(retrievedProduct));
        when(productRepository.save(any())).thenReturn(savedProduct);

        // When
        Product result = productService.update(productId, updatedProduct);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getName()).isEqualTo("Updated Product");
        assertThat(result.getDescription()).isEqualTo("Product description");
    }

    @Test
    public void testUpdate_WhenProductNotFound_ThrowNotFoundException() {
        // Given
        long productId = 1L;
        ProductRequestDto updatedProduct = new ProductRequestDto("Updated Product", "Product description");
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> productService.update(productId, updatedProduct));
    }

    @Test
    public void testDelete() {
        // Given
        long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(true);

        // When
        productService.delete(productId);

        // Then
        verify(productRepository).deleteById(productId);
    }

    @Test
    public void testDelete_WhenProductNotFound_ThrowNotFoundException() {
        // Given
        long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(false);

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> productService.delete(productId));
    }

}
