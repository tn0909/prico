package com.prico.controller;

import com.prico.dto.ProductRequest;
import com.prico.dto.ProductResponse;
import com.prico.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetById() throws Exception {
        ProductResponse productDto = new ProductResponse();
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("This is a test product");

        when(productService.getById(anyLong())).thenReturn(productDto);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("This is a test product"));
    }

    @Test
    public void testCreate() throws Exception {
        ProductRequest productDto = new ProductRequest();
        productDto.setName("New Product");
        productDto.setDescription("This is a test product");

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Product\",\"description\":\"This is a test product\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product has been created successfully"));

        verify(productService).create(any(ProductRequest.class));
    }

    @Test
    public void testCreateProduct_WithEmptyName() throws Exception {
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
}

