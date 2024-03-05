package com.prico.service.impl;

import com.prico.dto.crud.ProductStoreRequestDto;
import com.prico.dto.crud.ProductStoreResponseDto;
import com.prico.exception.ResourceNotFoundException;
import com.prico.model.Product;
import com.prico.model.ProductStore;
import com.prico.model.Store;
import com.prico.repository.ProductRepository;
import com.prico.repository.ProductStoreRepository;
import com.prico.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductStoreServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ProductStoreRepository productStoreRepository;

    @InjectMocks
    private ProductStoreServiceImpl productStoreService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        // Given
        Product product1 = new Product(1L, "Product 1", "Product 1 description");
        Product product2 = new Product(2L, "Product 2", "Product 2 description");

        Store store1 = new Store(11L, "Store 1", "Store 1 location", "www.store1.com");
        Store store2 = new Store(22L, "Store 2", "Store 2 location", "www.store2.com");

        ProductStore ps1 = new ProductStore(111L, "Product 1 - store 1", "www.store1.com/product1", "image1", 1111L, product1, store1);
        ProductStore ps2 = new ProductStore(222L, "Product 2 - store 2", "www.store2.com/product2", "image2", 2222L, product2, store2);

        when(productStoreRepository
                .findAll())
                .thenReturn(Arrays.asList(ps1, ps2));

        // When
        List<ProductStoreResponseDto> results = productStoreService.getAll();

        // Then
        assertThat(results).isNotNull();
        assertEquals(2, results.size());

        ProductStoreResponseDto psDto1 = results.get(0);

        assertEquals(111L, psDto1.getId());
        assertEquals("Product 1 - store 1", psDto1.getName());
        assertEquals("www.store1.com/product1", psDto1.getUrl());
        assertEquals("image1", psDto1.getImageUrl());
        assertEquals(1111L, psDto1.getPrice());
        assertEquals(1L, psDto1.getProduct().getId());
        assertEquals("Product 1", psDto1.getProduct().getName());
        assertEquals("Product 1 description", psDto1.getProduct().getDescription());
        assertEquals(11L, psDto1.getStore().getId());
        assertEquals("Store 1", psDto1.getStore().getName());
        assertEquals("Store 1 location", psDto1.getStore().getLocation());
        assertEquals("www.store1.com", psDto1.getStore().getWebsite());

        ProductStoreResponseDto psDto2 = results.get(1);

        assertEquals(222L, psDto2.getId());
        assertEquals("Product 2 - store 2", psDto2.getName());
        assertEquals("www.store2.com/product2", psDto2.getUrl());
        assertEquals("image2", psDto2.getImageUrl());
        assertEquals(2222L, psDto2.getPrice());
        assertEquals(2L, psDto2.getProduct().getId());
        assertEquals("Product 2", psDto2.getProduct().getName());
        assertEquals("Product 2 description", psDto2.getProduct().getDescription());
        assertEquals(22L, psDto2.getStore().getId());
        assertEquals("Store 2", psDto2.getStore().getName());
        assertEquals("Store 2 location", psDto2.getStore().getLocation());
        assertEquals("www.store2.com", psDto2.getStore().getWebsite());
    }

    @Test
    public void testGetById() {
        // Given
        long productStoreId = 111L;
        Product product1 = new Product(1L, "Product 1", "Product 1 description");
        Store store1 = new Store(11L, "Store 1", "Store 1 location", "www.store1.com");
        ProductStore ps1 = new ProductStore(productStoreId, "Product 1 - store 1", "www.store1.com/product1", "image1", 1111L, product1, store1);
        when(productStoreRepository.findById(productStoreId)).thenReturn(Optional.of(ps1));

        // When
        ProductStoreResponseDto result = productStoreService.getById(productStoreId);

        // Then
        assertThat(result).isNotNull();

        assertEquals(productStoreId, result.getId());
        assertEquals("Product 1 - store 1", result.getName());
        assertEquals("www.store1.com/product1", result.getUrl());
        assertEquals("image1", result.getImageUrl());
        assertEquals(1111L, result.getPrice());

        assertEquals(1L, result.getProduct().getId());
        assertEquals("Product 1", result.getProduct().getName());
        assertEquals("Product 1 description", result.getProduct().getDescription());

        assertEquals(11L, result.getStore().getId());
        assertEquals("Store 1", result.getStore().getName());
        assertEquals("Store 1 location", result.getStore().getLocation());
        assertEquals("www.store1.com", result.getStore().getWebsite());
    }

    @Test
    public void testGetById_WhenProductStoreNotFound_ThrowNotFoundException() {
        // Given
        long productStoreId = 1L;
        when(productStoreRepository.findById(productStoreId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> productStoreService.getById(productStoreId));
    }

    @Test
    public void testCreate_WithProductAndStore_CreatesEntity() {
        // Given
        ProductStoreRequestDto newProductStore = ProductStoreRequestDto
                .builder()
                .name("New Product")
                .url("www.store1.com/product1")
                .imageUrl("image1.jpg")
                .storeId(11L)
                .productId(1L)
                .build();

        Product product1 = new Product(1L, "Product 1", "Product 1 description");
        Store store1 = new Store(11L, "Store 1", "Store 1 location", "www.store1.com");
        ProductStore savedProductStore = new ProductStore(111L, "Product 1 - store 1", "www.store1.com/product1", "image1", 1111L, product1, store1);

        when(productStoreRepository.save(any())).thenReturn(savedProductStore);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(storeRepository.findById(11L)).thenReturn(Optional.of(store1));

        // When
        ProductStore result = productStoreService.create(newProductStore);

        // Then
        assertThat(result).isNotNull();

        assertEquals(111L, result.getId());
        assertEquals("Product 1 - store 1", result.getName());
        assertEquals("www.store1.com/product1", result.getUrl());
        assertEquals("image1", result.getImageUrl());
        assertEquals(1111L, result.getPrice());

        assertEquals(1L, result.getProduct().getId());
        assertEquals("Product 1", result.getProduct().getName());
        assertEquals("Product 1 description", result.getProduct().getDescription());

        assertEquals(11L, result.getStore().getId());
        assertEquals("Store 1", result.getStore().getName());
        assertEquals("Store 1 location", result.getStore().getLocation());
        assertEquals("www.store1.com", result.getStore().getWebsite());
    }

    @Test
    public void testCreate_WithNonExisingProduct_ThowsNotFoundException() {
        // Given
        ProductStoreRequestDto newProductStore = ProductStoreRequestDto
                .builder()
                .name("New Product")
                .url("www.store1.com/product1")
                .imageUrl("image1.jpg")
                .storeId(11L)
                .productId(1L)
                .build();

        when(productRepository.findById(11L)).thenReturn(Optional.empty());

        Store store1 = new Store(11L, "Store 1", "Store 1 location", "www.store1.com");
        when(storeRepository.findById(11L)).thenReturn(Optional.of(store1));

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> productStoreService.create(newProductStore));
    }

    @Test
    public void testCreate_WithNonExisingStore_ThowsNotFoundException() {
        // Given
        ProductStoreRequestDto newProductStore = ProductStoreRequestDto
                .builder()
                .name("New Product")
                .url("www.store1.com/product1")
                .imageUrl("image1.jpg")
                .storeId(11L)
                .productId(1L)
                .build();

        Product product1 = new Product(1L, "Product 1", "Product 1 description");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(storeRepository.findById(11L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> productStoreService.create(newProductStore));
    }

    @Test
    public void testUpdate_WithProductAndStore_UpdatesProduct() {
        // Given
        ProductStoreRequestDto updatedProductStore = ProductStoreRequestDto
                .builder()
                .name("Updated Product")
                .url("www.store1.com/product1")
                .imageUrl("image1.jpg")
                .storeId(11L)
                .productId(1L)
                .build();

        Product product1 = new Product(1L, "Product 1", "Product 1 description");
        Store store1 = new Store(11L, "Store 1", "Store 1 location", "www.store1.com");
        ProductStore ps1 = new ProductStore(111L, "Product 1 - store 1", "www.store1.com/product1", "image1", 1111L, product1, store1);

        when(productStoreRepository.findById(1L)).thenReturn(Optional.of(ps1));

        ProductStore savedProductStore = new ProductStore(111L, "Product 1 - store 1", "www.store1.com/product1", "image1", 1111L, product1, store1);
        when(productStoreRepository.save(any())).thenReturn(savedProductStore);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(storeRepository.findById(11L)).thenReturn(Optional.of(store1));

        // When
        ProductStore result = productStoreService.update(1L, updatedProductStore);

        // Then
        assertThat(result).isNotNull();

        assertEquals(111L, result.getId());
        assertEquals("Product 1 - store 1", result.getName());
        assertEquals("www.store1.com/product1", result.getUrl());
        assertEquals("image1", result.getImageUrl());
        assertEquals(1111L, result.getPrice());

        assertEquals(1L, result.getProduct().getId());
        assertEquals("Product 1", result.getProduct().getName());
        assertEquals("Product 1 description", result.getProduct().getDescription());

        assertEquals(11L, result.getStore().getId());
        assertEquals("Store 1", result.getStore().getName());
        assertEquals("Store 1 location", result.getStore().getLocation());
        assertEquals("www.store1.com", result.getStore().getWebsite());
    }

    @Test
    public void testUpdate_WhenProductStoreNotFound_ThrowsNotFoundException() {
        // Given
        long productStoreId = 1L;
        ProductStoreRequestDto updatedProductStore = ProductStoreRequestDto
                .builder()
                .name("Updated Product")
                .url("www.store1.com/product1")
                .imageUrl("image1.jpg")
                .storeId(11L)
                .productId(1L)
                .build();

        when(productRepository.findById(productStoreId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> productStoreService.update(productStoreId, updatedProductStore));
    }

    @Test
    public void testUpdate_WithNonExistingProduct_ThrowsNotFoundException() {
        // Given
        long productStoreId = 111L;
        ProductStoreRequestDto updatedProductStore = ProductStoreRequestDto
                .builder()
                .name("New Product")
                .url("www.store1.com/product1")
                .imageUrl("image1.jpg")
                .storeId(11L)
                .productId(2L)
                .build();

        Product product1 = new Product(1L, "Product 1", "Product 1 description");
        Store store1 = new Store(11L, "Store 1", "Store 1 location", "www.store1.com");
        ProductStore ps1 = new ProductStore(productStoreId, "Product 1 - store 1", "www.store1.com/product1", "image1", 1111L, product1, store1);

        when(productStoreRepository.findById(productStoreId)).thenReturn(Optional.of(ps1));
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        when(storeRepository.findById(11L)).thenReturn(Optional.of(store1));

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> productStoreService.update(productStoreId, updatedProductStore));
    }

    @Test
    public void testUpdate_WithNonExistingStore_ThrowsNotFoundException() {
        // Given
        long productStoreId = 111L;
        ProductStoreRequestDto updatedProductStore = ProductStoreRequestDto
                .builder()
                .name("New Product")
                .url("www.store1.com/product1")
                .imageUrl("image1.jpg")
                .storeId(22L)
                .productId(1L)
                .build();

        Product product1 = new Product(1L, "Product 1", "Product 1 description");
        Store store1 = new Store(11L, "Store 1", "Store 1 location", "www.store1.com");
        ProductStore ps1 = new ProductStore(productStoreId, "Product 1 - store 1", "www.store1.com/product1", "image1", 1111L, product1, store1);

        when(productStoreRepository.findById(productStoreId)).thenReturn(Optional.of(ps1));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(storeRepository.findById(22L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> productStoreService.update(productStoreId, updatedProductStore));
    }

    @Test
    public void testDelete() {
        // Given
        long productStoreId = 1L;
        when(productStoreRepository.existsById(productStoreId)).thenReturn(true);

        // When
        productStoreService.delete(productStoreId);

        // Then
        verify(productStoreRepository).deleteById(productStoreId);
    }

    @Test
    public void testDelete_WhenProductNotFound_ThrowsNotFoundException() {
        // Given
        long productStoreId = 1L;
        when(productStoreRepository.existsById(productStoreId)).thenReturn(false);

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> productStoreService.delete(productStoreId));
    }

}
