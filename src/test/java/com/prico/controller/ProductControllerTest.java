package com.prico.controller;

import com.prico.dto.ProductRequestDto;
import com.prico.dto.ProductResponseDto;
import com.prico.entity.Product;
import com.prico.exception.ProductNotFoundException;
import com.prico.service.ProductService;
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

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception {
        ProductResponseDto product1 = ProductResponseDto
                .builder()
                .id(1L)
                .name("Test Product 1")
                .description("This is the 1st test product")
                .build();

        ProductResponseDto product2 = ProductResponseDto
                .builder()
                .id(2L)
                .name("Test Product 2")
                .description("This is the 2nd test product")
                .build();

        when(productService
                .getAll())
                .thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Test Product 1"))
                .andExpect(jsonPath("$.[0].description").value("This is the 1st test product"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].name").value("Test Product 2"))
                .andExpect(jsonPath("$.[1].description").value("This is the 2nd test product"));
    }

    @Test
    public void testGetById() throws Exception {
        ProductResponseDto productDto = new ProductResponseDto();
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("This is a test product");

        when(productService
                .getById(anyLong()))
                .thenReturn(productDto);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("This is a test product"));
    }

    @Test
    public void testGetById_WithNonExistentId() throws Exception {
        long nonExistentId = 100L;
        when(productService
                .getById(eq(nonExistentId)))
                .thenThrow(new ProductNotFoundException("Invalid product"));

        mockMvc.perform(get("/products/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid product"));
    }

    @Test
    public void testCreate() throws Exception {
        ProductRequestDto productDto = new ProductRequestDto();
        productDto.setName("New Product");
        productDto.setDescription("This is a test product");

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Product\",\"description\":\"This is a test product\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product has been created successfully"));

        verify(productService).create(any(ProductRequestDto.class));
    }

    @Test
    public void testCreate_WithEmptyName() throws Exception {
        String invalidJsonInput = "{\"name\":\"\",\"description\":\"This is a test product\"}";

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].message").value("Product name should not be NULL or EMPTY"));
    }

    @Test
    public void testUpdate() throws Exception {
        long productId = 1L;
        Product updatedProduct = new Product(productId, "Updated Product", "Updated description");
        when(productService
                .update(eq(productId), any(ProductRequestDto.class)))
                .thenReturn(updatedProduct);

        mockMvc.perform(put("/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Product\",\"description\":\"Updated description\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product has been updated successfully"));

        verify(productService).update(eq(productId), any(ProductRequestDto.class));
    }

    @Test
    public void testUpdate_WithNonExistentId() throws Exception {
        long nonExistentId = 100L;
        when(productService
                .update(eq(nonExistentId), any()))
                .thenThrow(new ProductNotFoundException("Invalid product"));

        mockMvc.perform(put("/products/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Product\",\"description\":\"Updated description\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid product"));
    }

    @Test
    public void testUpdate_WithEmptyName() throws Exception {
        String invalidJsonInput = "{\"name\":\"\",\"description\":\"This is a test product\"}";

        mockMvc.perform(put("/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].message").value("Product name should not be NULL or EMPTY"));
    }

    @Test
    public void testDelete() throws Exception {
        long productId = 1L;
        doNothing().when(productService).delete(productId);

        mockMvc.perform(delete("/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product has been deleted successfully"));

        verify(productService).delete(productId);
    }

    @Test
    public void testDelete_WithNonExistentId() throws Exception {
        long nonExistentId = 100L;
        doThrow(new ProductNotFoundException("Invalid product"))
                .when(productService)
                .delete(nonExistentId);

        mockMvc.perform(delete("/products/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid product"));
    }
}

