package com.prico.service.impl;

import com.prico.dto.crud.BrandRequestDto;
import com.prico.dto.crud.BrandResponseDto;
import com.prico.model.Brand;
import com.prico.exception.ResourceNotFoundException;
import com.prico.repository.BrandRepository;
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

public class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandServiceImpl brandService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        // Given
        Brand brand1 = new Brand(1L, "Brand 1", "Brand 1 description");
        Brand brand2 = new Brand(2L, "Brand 2", "Brand 2 description");
        when(brandRepository.findAll()).thenReturn(Arrays.asList(brand1, brand2));

        // When
        List<BrandResponseDto> results = brandService.getAll();

        // Then
        assertThat(results).isNotNull();
        assertEquals(2, results.size());
        assertThat(results.get(0).getId()).isEqualTo(1L);
        assertThat(results.get(0).getName()).isEqualTo("Brand 1");
        assertThat(results.get(0).getDescription()).isEqualTo("Brand 1 description");
        assertThat(results.get(1).getId()).isEqualTo(2L);
        assertThat(results.get(1).getName()).isEqualTo("Brand 2");
        assertThat(results.get(1).getDescription()).isEqualTo("Brand 2 description");
    }

    @Test
    public void testGetById() {
        // Given
        long brandId = 1L;
        Brand mockBrand = new Brand(brandId, "Brand 1", "Brand description");
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(mockBrand));

        // When
        BrandResponseDto result = brandService.getById(brandId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(brandId);
        assertThat(result.getName()).isEqualTo("Brand 1");
        assertThat(result.getDescription()).isEqualTo("Brand description");
    }

    @Test
    public void testGetById_WhenBrandNotFound_ThrowNotFoundException() {
        // Given
        long brandId = 1L;
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> brandService.getById(brandId));
    }

    @Test
    public void testCreate() {
        // Given
        BrandRequestDto newBrand = new BrandRequestDto("New Brand", "Brand description");
        Brand savedBrand = new Brand(1L, "New Brand", "Brand description");
        when(brandRepository.save(any())).thenReturn(savedBrand);

        // When
        Brand result = brandService.create(newBrand);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("New Brand");
        assertThat(result.getDescription()).isEqualTo("Brand description");
    }

    @Test
    public void testUpdate() {
        // Given
        long brandId = 1L;
        BrandRequestDto updatedBrand = new BrandRequestDto("Updated Brand", "Brand description");
        Brand retrievedBrand = new Brand(1L, "Original Brand", "Original description");
        Brand savedBrand = new Brand(1L, "Updated Brand", "Brand description");

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(retrievedBrand));
        when(brandRepository.save(any())).thenReturn(savedBrand);

        // When
        Brand result = brandService.update(brandId, updatedBrand);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(brandId);
        assertThat(result.getName()).isEqualTo("Updated Brand");
        assertThat(result.getDescription()).isEqualTo("Brand description");
    }

    @Test
    public void testUpdate_WhenBrandNotFound_ThrowNotFoundException() {
        // Given
        long brandId = 1L;
        BrandRequestDto updatedBrand = new BrandRequestDto("Updated Brand", "Brand description");
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> brandService.update(brandId, updatedBrand));
    }

    @Test
    public void testDelete() {
        // Given
        long brandId = 1L;
        when(brandRepository.existsById(brandId)).thenReturn(true);

        // When
        brandService.delete(brandId);

        // Then
        verify(brandRepository).deleteById(brandId);
    }

    @Test
    public void testDelete_WhenBrandNotFound_ThrowNotFoundException() {
        // Given
        long brandId = 1L;
        when(brandRepository.existsById(brandId)).thenReturn(false);

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> brandService.delete(brandId));
    }

}
