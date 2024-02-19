package com.prico.service.impl;

import com.prico.dto.CategoryRequestDto;
import com.prico.dto.CategoryResponseDto;
import com.prico.entity.Category;
import com.prico.exception.EntityNotFoundException;
import com.prico.repository.CategoryRepository;
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

public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl productCategoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        // Given
        Category category1 = new Category(1L, "ProductCategory 1", "ProductCategory 1 description");
        Category category2 = new Category(2L, "ProductCategory 2", "ProductCategory 2 description");
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        // When
        List<CategoryResponseDto> results = productCategoryService.getAll();

        // Then
        assertThat(results).isNotNull();
        assertEquals(2, results.size());
        assertThat(results.get(0).getId()).isEqualTo(1L);
        assertThat(results.get(0).getName()).isEqualTo("ProductCategory 1");
        assertThat(results.get(0).getDescription()).isEqualTo("ProductCategory 1 description");
        assertThat(results.get(1).getId()).isEqualTo(2L);
        assertThat(results.get(1).getName()).isEqualTo("ProductCategory 2");
        assertThat(results.get(1).getDescription()).isEqualTo("ProductCategory 2 description");
    }

    @Test
    public void testGetById() {
        // Given
        long productCategoryId = 1L;
        Category mockCategory = new Category(productCategoryId, "ProductCategory 1", "ProductCategory description");
        when(categoryRepository.findById(productCategoryId)).thenReturn(Optional.of(mockCategory));

        // When
        CategoryResponseDto result = productCategoryService.getById(productCategoryId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productCategoryId);
        assertThat(result.getName()).isEqualTo("ProductCategory 1");
        assertThat(result.getDescription()).isEqualTo("ProductCategory description");
    }

    @Test
    public void testGetById_WhenProductCategoryNotFound_ThrowNotFoundException() {
        // Given
        long productCategoryId = 1L;
        when(categoryRepository.findById(productCategoryId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> productCategoryService.getById(productCategoryId));
    }

    @Test
    public void testCreate() {
        // Given
        CategoryRequestDto newProductCategory = new CategoryRequestDto("New ProductCategory", "ProductCategory description");
        Category savedCategory = new Category(1L, "New ProductCategory", "ProductCategory description");
        when(categoryRepository.save(any())).thenReturn(savedCategory);

        // When
        Category result = productCategoryService.create(newProductCategory);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("New ProductCategory");
        assertThat(result.getDescription()).isEqualTo("ProductCategory description");
    }

    @Test
    public void testUpdate() {
        // Given
        long productCategoryId = 1L;
        CategoryRequestDto updatedProductCategory = new CategoryRequestDto("Updated ProductCategory", "ProductCategory description");
        Category retrievedCategory = new Category(1L, "Original ProductCategory", "Original description");
        Category savedCategory = new Category(1L, "Updated ProductCategory", "ProductCategory description");

        when(categoryRepository.findById(productCategoryId)).thenReturn(Optional.of(retrievedCategory));
        when(categoryRepository.save(any())).thenReturn(savedCategory);

        // When
        Category result = productCategoryService.update(productCategoryId, updatedProductCategory);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productCategoryId);
        assertThat(result.getName()).isEqualTo("Updated ProductCategory");
        assertThat(result.getDescription()).isEqualTo("ProductCategory description");
    }

    @Test
    public void testUpdate_WhenProductCategoryNotFound_ThrowNotFoundException() {
        // Given
        long productCategoryId = 1L;
        CategoryRequestDto updatedProductCategory = new CategoryRequestDto("Updated ProductCategory", "ProductCategory description");
        when(categoryRepository.findById(productCategoryId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> productCategoryService.update(productCategoryId, updatedProductCategory));
    }

    @Test
    public void testDelete() {
        // Given
        long productCategoryId = 1L;
        when(categoryRepository.existsById(productCategoryId)).thenReturn(true);

        // When
        productCategoryService.delete(productCategoryId);

        // Then
        verify(categoryRepository).deleteById(productCategoryId);
    }

    @Test
    public void testDelete_WhenProductCategoryNotFound_ThrowNotFoundException() {
        // Given
        long productCategoryId = 1L;
        when(categoryRepository.existsById(productCategoryId)).thenReturn(false);

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> productCategoryService.delete(productCategoryId));
    }

}
