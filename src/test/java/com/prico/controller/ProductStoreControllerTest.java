package com.prico.controller;

import com.prico.dto.crud.ProductRequestDto;
import com.prico.dto.crud.ProductStoreRequestDto;
import com.prico.dto.crud.ProductStoreResponseDto;
import com.prico.exception.ResourceNotFoundException;
import com.prico.model.ProductStore;
import com.prico.security.JwtDecoderUtil;
import com.prico.service.ProductStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductStoreController.class)
public class ProductStoreControllerTest {

    @MockBean
    private ProductStoreService service;

    @MockBean
    private JwtDecoderUtil jwtDecoderUtil;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetById_ReturnsItem() throws Exception {
        ProductStoreResponseDto dto = ProductStoreResponseDto
                .builder()
                .id(1L)
                .name("Test Product 1")
                .url("www.store1.com/product1")
                .build();

        when(service
                .getById(anyLong()))
                .thenReturn(dto);

        mockMvc.perform(get("/product-stores/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product 1"))
                .andExpect(jsonPath("$.url").value("www.store1.com/product1"));
    }

    @Test
    public void testGetById_WithNonExistentId_ReturnsNotFound() throws Exception {
        long nonExistentId = 100L;
        when(service
                .getById(eq(nonExistentId)))
                .thenThrow(new ResourceNotFoundException("Invalid product variation"));

        mockMvc.perform(get("/product-stores/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid product variation"));
    }

    @Test
    public void testCreate_ReturnsCreated() throws Exception {
        mockMvc.perform(post("/product-stores")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Product\",\"url\":\"www.store1.com/product1\",\"imageUrl\":\"image\",\"price\":5500,\"productId\":99,\"storeId\":66}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product variation has been created successfully"));

        verify(service).create(any(ProductStoreRequestDto.class));
    }

    @Test
    public void testCreate_Unauthorised_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/product-stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Product\",\"url\":\"www.store1.com/product1\",\"imageUrl\":\"image\",\"price\":5500,\"productId\":99,\"storeId\":66}"))
                .andExpect(status().isUnauthorized());

        verify(service, never()).create(any(ProductStoreRequestDto.class));
    }

    @Test
    public void testCreate_WithEmptyName_ReturnsBadRequest() throws Exception {
        String invalidJsonInput = "{\"name\":\"\",\"url\":\"www.store1.com/product1\",\"imageUrl\":\"image\",\"price\":5500,\"productId\":99,\"storeId\":66}";

        mockMvc.perform(post("/product-stores")
                .with(jwt())
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
    public void testCreate_WithEmptyUrl_ReturnsBadRequest() throws Exception {
        String invalidJsonInput = "{\"name\":\"Product 1\",\"url\":\"\",\"imageUrl\":\"image\",\"price\":5500,\"productId\":99,\"storeId\":66}";

        mockMvc.perform(post("/product-stores")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("url"))
                .andExpect(jsonPath("$.errors[0].message").value("Url should not be NULL or EMPTY"));
    }

    @Test
    public void testCreate_WithoutPrice_ReturnsBadRequest() throws Exception {
        String invalidJsonInput = "{\"name\":\"Product 1\",\"url\":\"product.html\",\"imageUrl\":\"image\",\"productId\":99,\"storeId\":66}";

        mockMvc.perform(post("/product-stores")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("price"))
                .andExpect(jsonPath("$.errors[0].message").value("Price should not be NULL"));
    }

    @Test
    public void testCreate_WithNegativePrice_ReturnsBadRequest() throws Exception {
        String invalidJsonInput = "{\"name\":\"Product 1\",\"url\":\"product.html\",\"imageUrl\":\"image\",\"price\":-5500,\"productId\":99,\"storeId\":66}";

        mockMvc.perform(post("/product-stores")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("price"))
                .andExpect(jsonPath("$.errors[0].message").value("Price should be greater than 0"));
    }

    @Test
    public void testCreate_WithoutProduct_ReturnsBadRequest() throws Exception {
        String invalidJsonInput = "{\"name\":\"Product 1\",\"url\":\"product.html\",\"imageUrl\":\"image\",\"price\":5500,\"storeId\":66}";

        mockMvc.perform(post("/product-stores")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("productId"))
                .andExpect(jsonPath("$.errors[0].message").value("Product should not be NULL"));
    }

    @Test
    public void testCreate_WithoutStore_ReturnsBadRequest() throws Exception {
        String invalidJsonInput = "{\"name\":\"Product 1\",\"url\":\"product.html\",\"imageUrl\":\"image\",\"price\":5500,\"productId\":99}";

        mockMvc.perform(post("/product-stores")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("storeId"))
                .andExpect(jsonPath("$.errors[0].message").value("Store should not be NULL"));
    }

    @Test
    public void testUpdate_ReturnsOk() throws Exception {
        long id = 1L;
        ProductStore updatedProduct = ProductStore
                .builder()
                .id(id)
                .name("Updated Product")
                .url("product.html")
                .build();
        when(service
                .update(eq(id), any(ProductStoreRequestDto.class)))
                .thenReturn(updatedProduct);

        mockMvc.perform(put("/product-stores/{id}", id)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Product\",\"url\":\"www.store1.com/product1\",\"imageUrl\":\"image\",\"price\":5500,\"productId\":99,\"storeId\":66}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product variation has been updated successfully"));

        verify(service).update(eq(id), any(ProductStoreRequestDto.class));
    }

    @Test
    public void testUpdate_Unauthorised_ReturnsUnauthorized() throws Exception {
        long productId = 1L;

        mockMvc.perform(put("/product-stores/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Product\",\"url\":\"www.store1.com/product1\",\"imageUrl\":\"image\",\"price\":5500,\"productId\":99,\"storeId\":66}"))
                .andExpect(status().isUnauthorized());

        verify(service, never()).update(any(), any());
    }

    @Test
    public void testUpdate_WithNonExistentId_ReturnsNotFound() throws Exception {
        long nonExistentId = 100L;
        when(service
                .update(eq(nonExistentId), any()))
                .thenThrow(new ResourceNotFoundException("Invalid product variation"));

        mockMvc.perform(put("/product-stores/{id}", nonExistentId)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Product\",\"url\":\"www.store1.com/product1\",\"imageUrl\":\"image\",\"price\":5500,\"productId\":99,\"storeId\":66}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid product variation"));
    }

    @Test
    public void testUpdate_WithEmptyName_ReturnsBadRequest() throws Exception {
        String invalidJsonInput = "{\"name\":\"\",\"url\":\"www.store1.com/product1\",\"imageUrl\":\"image\",\"price\":5500,\"productId\":99,\"storeId\":66}";

        mockMvc.perform(put("/product-stores/{id}", 1L)
                .with(jwt())
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
    public void testUpdate_WithEmptyUrl_ReturnsBadRequest() throws Exception {
        String invalidJsonInput = "{\"name\":\"New Product\",\"url\":\"\",\"imageUrl\":\"image\",\"price\":5500,\"productId\":99,\"storeId\":66}";

        mockMvc.perform(put("/product-stores/{id}", 1L)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("url"))
                .andExpect(jsonPath("$.errors[0].message").value("Url should not be NULL or EMPTY"));
    }

    @Test
    public void testUpdate_WithoutPrice_ReturnsBadRequest() throws Exception {
        String invalidJsonInput = "{\"name\":\"New Product\",\"url\":\"www.store1.com/product1\",\"imageUrl\":\"image\",\"productId\":99,\"storeId\":66}";

        mockMvc.perform(put("/product-stores/{id}", 1L)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("price"))
                .andExpect(jsonPath("$.errors[0].message").value("Price should not be NULL"));
    }

    @Test
    public void testUpdate_WithNegativePrice_ReturnsBadRequest() throws Exception {
        String invalidJsonInput = "{\"name\":\"New Product\",\"url\":\"www.store1.com/product1\",\"imageUrl\":\"image\",\"price\":-5500,\"productId\":99,\"storeId\":66}";

        mockMvc.perform(put("/product-stores/{id}", 1L)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("price"))
                .andExpect(jsonPath("$.errors[0].message").value("Price should be greater than 0"));
    }

    @Test
    public void testUpdate_WithoutProduct_ReturnsBadRequest() throws Exception {
        String invalidJsonInput = "{\"name\":\"New Product\",\"url\":\"www.store1.com/product1\",\"imageUrl\":\"image\",\"price\":5500,\"productId\":null,\"storeId\":66}";

        mockMvc.perform(put("/product-stores/{id}", 1L)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("productId"))
                .andExpect(jsonPath("$.errors[0].message").value("Product should not be NULL")) ;
    }

    @Test
    public void testUpdate_WithoutStore_ReturnsBadRequest() throws Exception {
        String invalidJsonInput = "{\"name\":\"New Product\",\"url\":\"www.store1.com/product1\",\"imageUrl\":\"image\",\"price\":5500,\"productId\":99,\"storeId\":null}";

        mockMvc.perform(put("/product-stores/{id}", 1L)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("storeId"))
                .andExpect(jsonPath("$.errors[0].message").value("Store should not be NULL"));
    }

    @Test
    public void testDelete_ReturnsOk() throws Exception {
        long id = 1L;
        doNothing().when(service).delete(id);

        mockMvc.perform(delete("/product-stores/{id}", id)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product variation has been deleted successfully"));

        verify(service).delete(id);
    }

    @Test
    public void testDelete_Unauthorised_ReturnsUnauthorized() throws Exception {
        long id = 1L;

        mockMvc.perform(delete("/product-stores/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(service, never()).delete(any());
    }

    @Test
    public void testDelete_WithNonExistentId_ReturnsNotFound() throws Exception {
        long nonExistentId = 100L;
        doThrow(new ResourceNotFoundException("Invalid product variation"))
                .when(service)
                .delete(nonExistentId);

        mockMvc.perform(delete("/product-stores/{id}", nonExistentId)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid product variation"));
    }

}

