package com.prico.service.impl;

import com.prico.dto.comparison.ProductStoreDto;
import com.prico.dto.comparison.ProductVariationResponseDto;
import com.prico.dto.comparison.StoreDto;
import com.prico.dto.crud.ProductRequestDto;
import com.prico.dto.crud.ProductResponseDto;
import com.prico.dto.SearchRequestDto;
import com.prico.model.Product;
import com.prico.exception.ResourceNotFoundException;
import com.prico.model.ProductStore;
import com.prico.model.Store;
import com.prico.repository.ProductRepository;
import com.prico.repository.ProductStoreRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductStoreRepository productStoreRepository;

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
        when(productRepository
                .findAll())
                .thenReturn(Arrays.asList(product1, product2));

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
        assertThrows(ResourceNotFoundException.class, () -> productService.getById(productId));
    }

    @Test
    public void testCreate() {
        // Given
        ProductRequestDto newProduct = ProductRequestDto
                .builder()
                .name("New Product")
                .description("Product description")
                .build();
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
        ProductRequestDto updatedProduct = ProductRequestDto
                .builder()
                .name("Updated Product")
                .description("Product description")
                .build();
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
        ProductRequestDto updatedProduct = ProductRequestDto
                .builder()
                .name("Updated Product")
                .description("Product description")
                .build();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> productService.update(productId, updatedProduct));
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
        assertThrows(ResourceNotFoundException.class, () -> productService.delete(productId));
    }

    @Test
    public void testSearch() {
        // Given
        Product product1 = new Product(1L, "Product 1", "Product 1 description");
        Product product2 = new Product(2L, "Product 2", "Product 2 description");
        when(productRepository
                .search("product name","category name","brand name"))
                .thenReturn(Arrays.asList(product1, product2));

        // When
        SearchRequestDto searchRequestDto = SearchRequestDto
                .builder()
                .name("Product Name")
                .category("Category Name")
                .brand("Brand Name")
                .build();

        List<ProductResponseDto> results = productService.search(searchRequestDto);

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
    public void testGetVariationsByProduct() {
        // Given
        Product product = Product
                .builder()
                .id(1L)
                .name("Product 1")
                .description("Product 1 description")
                .imageUrl("https://prico.com/images/product1.jpg")
                .build();

        when(productRepository
                .findById(1L))
                .thenReturn(Optional.of(product));

        Store store = Store
                .builder()
                .id(3L)
                .name("Store 1")
                .build();

        ProductStore variation1 = ProductStore
                .builder()
                .name("Product 1 variation")
                .imageUrl("image.jpg")
                .url("https://store1.com/product1-variation")
                .price(5.5F)
                .product(product)
                .store(store)
                .build();

        when(productStoreRepository
                .findAllByProduct(product))
                .thenReturn(Arrays.asList(variation1));

        // When
        ProductVariationResponseDto result = productService.getVariationsByProduct(1L);

        // Then
        assertThat(result).isNotNull();
        assertEquals(1L, result.getProductId());
        assertEquals("Product 1", result.getProductName());
        assertEquals("https://prico.com/images/product1.jpg", result.getProductImageUrl());

        assertEquals(1, result.getStores().size());
        assertEquals(3L, result.getStores().get(0).getId());
        assertEquals("Store 1", result.getStores().get(0).getName());

        assertEquals(1, result.getStores().get(0).getVariations().size());
        assertEquals("Product 1 variation", result.getStores().get(0).getVariations().get(0).getName());
        assertEquals("image.jpg", result.getStores().get(0).getVariations().get(0).getImageUrl());
        assertEquals("https://store1.com/product1-variation", result.getStores().get(0).getVariations().get(0).getUrl());
        assertEquals(5.5F, result.getStores().get(0).getVariations().get(0).getPrice());
    }

    @Test
    public void testGetVariationsByProduct_GroupByStore() {
        // Given
        Product product = Product
                .builder()
                .id(1L)
                .name("Product 1")
                .description("Product 1 description")
                .imageUrl("https://prico.com/images/product1.jpg")
                .build();

        when(productRepository
                .findById(1L))
                .thenReturn(Optional.of(product));

        Store store1 = Store
                .builder()
                .id(11L)
                .name("Store 1")
                .build();

        Store store2 = Store
                .builder()
                .id(22L)
                .name("Store 2")
                .build();

        ProductStore variation1 = ProductStore
                .builder()
                .id(111L)
                .name("Product 1 variation 1")
                .imageUrl("image1.jpg")
                .url("https://store1.com/product1-variation1")
                .price(5.5F)
                .product(product)
                .store(store1)
                .build();

        ProductStore variation2 = ProductStore
                .builder()
                .id(112L)
                .name("Product 1 variation 2")
                .imageUrl("image2.jpg")
                .url("https://store1.com/product1-variation2")
                .price(5.6F)
                .product(product)
                .store(store1)
                .build();

        ProductStore variation3 = ProductStore
                .builder()
                .id(113L)
                .name("Product 1 variation 3")
                .imageUrl("image3.jpg")
                .url("https://store1.com/product1-variation3")
                .price(5.3F)
                .product(product)
                .store(store2)
                .build();

        when(productStoreRepository
                .findAllByProduct(product))
                .thenReturn(Arrays.asList(variation1, variation2, variation3));

        // When
        ProductVariationResponseDto result = productService.getVariationsByProduct(1L);

        // Then
        assertThat(result).isNotNull();
        assertEquals(1L, result.getProductId());
        assertEquals("Product 1", result.getProductName());
        assertEquals("https://prico.com/images/product1.jpg", result.getProductImageUrl());

        assertEquals(2, result.getStores().size());

        StoreDto storeDto1 = result.getStores().get(1);
        StoreDto storeDto2 = result.getStores().get(0);
        ProductStoreDto productStoreDto1 = storeDto1.getVariations().get(0);
        ProductStoreDto productStoreDto2 = storeDto1.getVariations().get(1);
        ProductStoreDto productStoreDto3 = storeDto2.getVariations().get(0);

        assertEquals(11L, storeDto1.getId());
        assertEquals("Store 1", storeDto1.getName());
        assertEquals(2, storeDto1.getVariations().size());

        assertEquals(22L, storeDto2.getId());
        assertEquals("Store 2", storeDto2.getName());

        assertEquals("Product 1 variation 1", productStoreDto1.getName());
        assertEquals("image1.jpg", productStoreDto1.getImageUrl());
        assertEquals("https://store1.com/product1-variation1", productStoreDto1.getUrl());
        assertEquals(5.5F, productStoreDto1.getPrice());

        assertEquals("Product 1 variation 2", productStoreDto2.getName());
        assertEquals("image2.jpg", productStoreDto2.getImageUrl());
        assertEquals("https://store1.com/product1-variation2", productStoreDto2.getUrl());
        assertEquals(5.6F, productStoreDto2.getPrice());

        assertEquals("Product 1 variation 3", productStoreDto3.getName());
        assertEquals("image3.jpg", productStoreDto3.getImageUrl());
        assertEquals("https://store1.com/product1-variation3", productStoreDto3.getUrl());
        assertEquals(5.3F, productStoreDto3.getPrice());
    }


    @Test
    public void testGetVariationsByProduct_WhenProductNotFound_ThrowNotFoundException() {
        // Given
        when(productRepository
                .findById(1L))
                .thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> productService.getVariationsByProduct(1L));
    }
}
