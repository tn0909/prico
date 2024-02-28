package com.prico.controller;

import com.prico.dto.crud.BrandRequestDto;
import com.prico.dto.crud.BrandResponseDto;
import com.prico.model.Brand;
import com.prico.exception.ResourceNotFoundException;
import com.prico.security.JwtDecoderUtil;
import com.prico.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BrandController.class)
public class BrandControllerTest {

    @MockBean
    private BrandService brandService;

    @MockBean
    private JwtDecoderUtil jwtDecoderUtil;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAll_ReturnsList() throws Exception {
        BrandResponseDto brand1 = BrandResponseDto
                .builder()
                .id(1L)
                .name("Test Brand 1")
                .description("This is the 1st test brand")
                .build();

        BrandResponseDto brand2 = BrandResponseDto
                .builder()
                .id(2L)
                .name("Test Brand 2")
                .description("This is the 2nd test brand")
                .build();

        when(brandService
                .getAll())
                .thenReturn(Arrays.asList(brand1, brand2));

        mockMvc.perform(get("/brands"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Test Brand 1"))
                .andExpect(jsonPath("$.[0].description").value("This is the 1st test brand"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].name").value("Test Brand 2"))
                .andExpect(jsonPath("$.[1].description").value("This is the 2nd test brand"));
    }

    @Test
    public void testGetById_ReturnsItem() throws Exception {
        BrandResponseDto brandDto = new BrandResponseDto();
        brandDto.setId(1L);
        brandDto.setName("Test Brand");
        brandDto.setDescription("This is a test brand");

        when(brandService
                .getById(anyLong()))
                .thenReturn(brandDto);

        mockMvc.perform(get("/brands/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Brand"))
                .andExpect(jsonPath("$.description").value("This is a test brand"));
    }

    @Test
    public void testGetById_WithNonExistentId_ReturnsNotFound() throws Exception {
        long nonExistentId = 100L;
        when(brandService
                .getById(eq(nonExistentId)))
                .thenThrow(new ResourceNotFoundException("Invalid brand"));

        mockMvc.perform(get("/brands/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid brand"));
    }

    @Test
    public void testCreate_ReturnsCreated() throws Exception {
        mockMvc.perform(post("/brands")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Brand\",\"description\":\"This is a test brand\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Brand has been created successfully"));

        verify(brandService).create(any(BrandRequestDto.class));
    }

    @Test
    public void testCreate_Unauthorised_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Brand\",\"description\":\"This is a test brand\"}"))
                .andExpect(status().isUnauthorized());

        verify(brandService, never()).create(any(BrandRequestDto.class));
    }

    @Test
    public void testCreate_WithEmptyName_ReturnsNotFound() throws Exception {
        String invalidJsonInput = "{\"name\":\"\",\"description\":\"This is a test brand\"}";

        mockMvc.perform(post("/brands")
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
    public void testUpdate_ReturnsOk() throws Exception {
        long brandId = 1L;
        Brand updatedBrand = Brand
                .builder()
                .id(brandId)
                .name("Updated Brand")
                .description("Updated description")
                .build();
        when(brandService
                .update(eq(brandId), any(BrandRequestDto.class)))
                .thenReturn(updatedBrand);

        mockMvc.perform(put("/brands/{id}", brandId)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Brand\",\"description\":\"Updated description\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Brand has been updated successfully"));

        verify(brandService).update(eq(brandId), any(BrandRequestDto.class));
    }

    @Test
    public void testUpdate_Unauthorised_ReturnsUnauthorised() throws Exception {
        long brandId = 1L;

        mockMvc.perform(put("/brands/{id}", brandId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Brand\",\"description\":\"Updated description\"}"))
                .andExpect(status().isUnauthorized());

        verify(brandService, never()).update(eq(brandId), any(BrandRequestDto.class));
    }

    @Test
    public void testUpdate_WithNonExistentId_ReturnsNotFound() throws Exception {
        long nonExistentId = 100L;
        when(brandService
                .update(eq(nonExistentId), any()))
                .thenThrow(new ResourceNotFoundException("Invalid brand"));

        mockMvc.perform(put("/brands/{id}", nonExistentId)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Brand\",\"description\":\"Updated description\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid brand"));
    }

    @Test
    public void testUpdate_WithEmptyName_ReturnsBadRequest() throws Exception {
        String invalidJsonInput = "{\"name\":\"\",\"description\":\"This is a test brand\"}";

        mockMvc.perform(put("/brands/{id}", 1L)
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
    public void testDelete_ReturnsOk() throws Exception {
        long brandId = 1L;
        doNothing().when(brandService).delete(brandId);

        mockMvc.perform(delete("/brands/{id}", brandId)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Brand has been deleted successfully"));

        verify(brandService).delete(brandId);
    }

    @Test
    public void testDelete_Unauthorised_ReturnsUnauthorized() throws Exception {
        long brandId = 1L;
        doNothing().when(brandService).delete(brandId);

        mockMvc.perform(delete("/brands/{id}", brandId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(brandService, never()).delete(brandId);
    }

    @Test
    public void testDelete_WithNonExistentId_ReturnsNotFound() throws Exception {
        long nonExistentId = 100L;
        doThrow(new ResourceNotFoundException("Invalid brand"))
                .when(brandService)
                .delete(nonExistentId);

        mockMvc.perform(delete("/brands/{id}", nonExistentId)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid brand"));
    }
}

