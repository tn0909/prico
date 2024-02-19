package com.prico.controller;

import com.prico.dto.CategoryRequestDto;
import com.prico.dto.CategoryResponseDto;
import com.prico.entity.ProductCategory;
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
        CategoryResponseDto productCategory1 = CategoryResponseDto
                .builder()
                .id(1L)
                .name("Test ProductCategory 1")
                .description("This is the 1st test productCategory")
                .build();

        CategoryResponseDto productCategory2 = CategoryResponseDto
                .builder()
                .id(2L)
                .name("Test ProductCategory 2")
                .description("This is the 2nd test productCategory")
                .build();

        when(service
                .getAll())
                .thenReturn(Arrays.asList(productCategory1, productCategory2));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Test ProductCategory 1"))
                .andExpect(jsonPath("$.[0].description").value("This is the 1st test productCategory"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].name").value("Test ProductCategory 2"))
                .andExpect(jsonPath("$.[1].description").value("This is the 2nd test productCategory"));
    }

    @Test
    public void testGetById() throws Exception {
        CategoryResponseDto productCategoryDto = new CategoryResponseDto();
        productCategoryDto.setId(1L);
        productCategoryDto.setName("Test ProductCategory");
        productCategoryDto.setDescription("This is a test productCategory");

        when(service
                .getById(anyLong()))
                .thenReturn(productCategoryDto);

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test ProductCategory"))
                .andExpect(jsonPath("$.description").value("This is a test productCategory"));
    }

    @Test
    public void testGetById_WithNonExistentId() throws Exception {
        long nonExistentId = 100L;
        when(service
                .getById(eq(nonExistentId)))
                .thenThrow(new EntityNotFoundException("Invalid productCategory"));

        mockMvc.perform(get("/categories/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid productCategory"));
    }

    @Test
    public void testCreate() throws Exception {
        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New ProductCategory\",\"description\":\"This is a test productCategory\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product Category has been created successfully"));

        verify(service).create(any(CategoryRequestDto.class));
    }

    @Test
    public void testCreate_WithEmptyName() throws Exception {
        String invalidJsonInput = "{\"name\":\"\",\"description\":\"This is a test productCategory\"}";

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
        long productCategoryId = 1L;
        ProductCategory updatedProductCategory = ProductCategory
                .builder()
                .id(productCategoryId)
                .name("Updated ProductCategory")
                .description("Updated description")
                .build();
        when(service
                .update(eq(productCategoryId), any(CategoryRequestDto.class)))
                .thenReturn(updatedProductCategory);

        mockMvc.perform(put("/categories/{id}", productCategoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated ProductCategory\",\"description\":\"Updated description\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product Category has been updated successfully"));

        verify(service).update(eq(productCategoryId), any(CategoryRequestDto.class));
    }

    @Test
    public void testUpdate_WithNonExistentId() throws Exception {
        long nonExistentId = 100L;
        when(service
                .update(eq(nonExistentId), any()))
                .thenThrow(new EntityNotFoundException("Invalid productCategory"));

        mockMvc.perform(put("/categories/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated ProductCategory\",\"description\":\"Updated description\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid productCategory"));
    }

    @Test
    public void testUpdate_WithEmptyName() throws Exception {
        String invalidJsonInput = "{\"name\":\"\",\"description\":\"This is a test productCategory\"}";

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
        long productCategoryId = 1L;
        doNothing().when(service).delete(productCategoryId);

        mockMvc.perform(delete("/categories/{id}", productCategoryId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product Category has been deleted successfully"));

        verify(service).delete(productCategoryId);
    }

    @Test
    public void testDelete_WithNonExistentId() throws Exception {
        long nonExistentId = 100L;
        doThrow(new EntityNotFoundException("Invalid productCategory"))
                .when(service)
                .delete(nonExistentId);

        mockMvc.perform(delete("/categories/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid productCategory"));
    }
}

