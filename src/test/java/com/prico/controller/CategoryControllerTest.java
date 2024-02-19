package com.prico.controller;

import com.prico.dto.CategoryRequestDto;
import com.prico.dto.CategoryResponseDto;
import com.prico.entity.Category;
import com.prico.exception.EntityNotFoundException;
import com.prico.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @MockBean
    private CategoryService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception {
        CategoryResponseDto category1 = CategoryResponseDto
                .builder()
                .id(1L)
                .name("Test Category 1")
                .description("This is the 1st test category")
                .build();

        CategoryResponseDto category2 = CategoryResponseDto
                .builder()
                .id(2L)
                .name("Test Category 2")
                .description("This is the 2nd test category")
                .build();

        when(service
                .getAll())
                .thenReturn(Arrays.asList(category1, category2));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Test Category 1"))
                .andExpect(jsonPath("$.[0].description").value("This is the 1st test category"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].name").value("Test Category 2"))
                .andExpect(jsonPath("$.[1].description").value("This is the 2nd test category"));
    }

    @Test
    public void testGetById() throws Exception {
        CategoryResponseDto categoryDto = new CategoryResponseDto();
        categoryDto.setId(1L);
        categoryDto.setName("Test Category");
        categoryDto.setDescription("This is a test category");

        when(service
                .getById(anyLong()))
                .thenReturn(categoryDto);

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"))
                .andExpect(jsonPath("$.description").value("This is a test category"));
    }

    @Test
    public void testGetById_WithNonExistentId() throws Exception {
        long nonExistentId = 100L;
        when(service
                .getById(eq(nonExistentId)))
                .thenThrow(new EntityNotFoundException("Invalid category"));

        mockMvc.perform(get("/categories/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid category"));
    }

    @Test
    public void testCreate() throws Exception {
        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Category\",\"description\":\"This is a test category\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product Category has been created successfully"));

        verify(service).create(any(CategoryRequestDto.class));
    }

    @Test
    public void testCreate_WithEmptyName() throws Exception {
        String invalidJsonInput = "{\"name\":\"\",\"description\":\"This is a test category\"}";

        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].message").value("Name should not be NULL or EMPTY"));
    }

    @Test
    public void testUpdate() throws Exception {
        long categoryId = 1L;
        Category updatedCategory = Category
                .builder()
                .id(categoryId)
                .name("Updated Category")
                .description("Updated description")
                .build();
        when(service
                .update(eq(categoryId), any(CategoryRequestDto.class)))
                .thenReturn(updatedCategory);

        mockMvc.perform(put("/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Category\",\"description\":\"Updated description\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product Category has been updated successfully"));

        verify(service).update(eq(categoryId), any(CategoryRequestDto.class));
    }

    @Test
    public void testUpdate_WithNonExistentId() throws Exception {
        long nonExistentId = 100L;
        when(service
                .update(eq(nonExistentId), any()))
                .thenThrow(new EntityNotFoundException("Invalid category"));

        mockMvc.perform(put("/categories/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Category\",\"description\":\"Updated description\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid category"));
    }

    @Test
    public void testUpdate_WithEmptyName() throws Exception {
        String invalidJsonInput = "{\"name\":\"\",\"description\":\"This is a test category\"}";

        mockMvc.perform(put("/categories/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].message").value("Name should not be NULL or EMPTY"));
    }

    @Test
    public void testDelete() throws Exception {
        long categoryId = 1L;
        doNothing().when(service).delete(categoryId);

        mockMvc.perform(delete("/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product Category has been deleted successfully"));

        verify(service).delete(categoryId);
    }

    @Test
    public void testDelete_WithNonExistentId() throws Exception {
        long nonExistentId = 100L;
        doThrow(new EntityNotFoundException("Invalid category"))
                .when(service)
                .delete(nonExistentId);

        mockMvc.perform(delete("/categories/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid category"));
    }
}

