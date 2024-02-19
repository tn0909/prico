package com.prico.service.impl;

import com.prico.dto.StoreRequestDto;
import com.prico.dto.StoreResponseDto;
import com.prico.entity.Store;
import com.prico.exception.EntityNotFoundException;
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

public class StoreServiceImplTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreServiceImpl storeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        // Given
        Store store1 = new Store(1L, "Store 1", "Store 1 location", "store1.com");
        Store store2 = new Store(2L, "Store 2", "Store 2 location", "store2.com");
        when(storeRepository.findAll()).thenReturn(Arrays.asList(store1, store2));

        // When
        List<StoreResponseDto> results = storeService.getAll();

        // Then
        assertThat(results).isNotNull();
        assertEquals(2, results.size());

        assertThat(results.get(0).getId()).isEqualTo(1L);
        assertThat(results.get(0).getName()).isEqualTo("Store 1");
        assertThat(results.get(0).getLocation()).isEqualTo("Store 1 location");
        assertThat(results.get(0).getWebsite()).isEqualTo("store1.com");

        assertThat(results.get(1).getId()).isEqualTo(2L);
        assertThat(results.get(1).getName()).isEqualTo("Store 2");
        assertThat(results.get(1).getLocation()).isEqualTo("Store 2 location");
        assertThat(results.get(1).getWebsite()).isEqualTo("store2.com");
    }

    @Test
    public void testGetById() {
        // Given
        long storeId = 1L;
        Store mockStore = new Store(storeId, "Store 1", "Store 1 location", "store1.com");
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(mockStore));

        // When
        StoreResponseDto result = storeService.getById(storeId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(storeId);
        assertThat(result.getName()).isEqualTo("Store 1");
        assertThat(result.getLocation()).isEqualTo("Store 1 location");
        assertThat(result.getWebsite()).isEqualTo("store1.com");
    }

    @Test
    public void testGetById_WhenStoreNotFound_ThrowNotFoundException() {
        // Given
        long storeId = 1L;
        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> storeService.getById(storeId));
    }

    @Test
    public void testCreate() {
        // Given
        StoreRequestDto newStore = new StoreRequestDto("New Store", "Store 1 location", "store1.com");
        Store savedStore = new Store(1L, "New Store", "Store 1 location", "store1.com");
        when(storeRepository.save(any())).thenReturn(savedStore);

        // When
        Store result = storeService.create(newStore);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("New Store");
        assertThat(result.getLocation()).isEqualTo("Store 1 location");
        assertThat(result.getWebsite()).isEqualTo("store1.com");
    }

    @Test
    public void testUpdate() {
        // Given
        long storeId = 1L;
        StoreRequestDto updatedStore = new StoreRequestDto("Updated Store", "Updated Store location", "updated-store1.com");
        Store retrievedStore = new Store(1L, "Original Store", "Original Store location", "original-store1.com");
        Store savedStore = new Store(1L, "Updated Store", "Updated Store location", "updated-store1.com");

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(retrievedStore));
        when(storeRepository.save(any())).thenReturn(savedStore);

        // When
        Store result = storeService.update(storeId, updatedStore);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(storeId);
        assertThat(result.getName()).isEqualTo("Updated Store");
        assertThat(result.getLocation()).isEqualTo("Updated Store location");
        assertThat(result.getWebsite()).isEqualTo("updated-store1.com");
    }

    @Test
    public void testUpdate_WhenStoreNotFound_ThrowNotFoundException() {
        // Given
        long storeId = 1L;
        StoreRequestDto updatedStore = new StoreRequestDto("Updated Store", "Updated Store location", "updated-store1.com");
        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> storeService.update(storeId, updatedStore));
    }

    @Test
    public void testDelete() {
        // Given
        long storeId = 1L;
        when(storeRepository.existsById(storeId)).thenReturn(true);

        // When
        storeService.delete(storeId);

        // Then
        verify(storeRepository).deleteById(storeId);
    }

    @Test
    public void testDelete_WhenStoreNotFound_ThrowNotFoundException() {
        // Given
        long storeId = 1L;
        when(storeRepository.existsById(storeId)).thenReturn(false);

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> storeService.delete(storeId));
    }

}
