package com.prico.controller;

import com.prico.dto.crud.StoreRequestDto;
import com.prico.dto.crud.StoreResponseDto;
import com.prico.model.Store;
import com.prico.exception.ResourceNotFoundException;
import com.prico.service.StoreService;
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

@WebMvcTest(StoreController.class)
public class StoreControllerTest {

    @MockBean
    private StoreService storeService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception {
        StoreResponseDto store1 = StoreResponseDto
                .builder()
                .id(1L)
                .name("Test Store 1")
                .location("Test Store 1 location")
                .website("store1.com")
                .build();

        StoreResponseDto store2 = StoreResponseDto
                .builder()
                .id(2L)
                .name("Test Store 2")
                .location("Test Store 2 location")
                .website("store2.com")
                .build();

        when(storeService
                .getAll())
                .thenReturn(Arrays.asList(store1, store2));

        mockMvc.perform(get("/stores"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Test Store 1"))
                .andExpect(jsonPath("$.[0].location").value("Test Store 1 location"))
                .andExpect(jsonPath("$.[0].website").value("store1.com"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].name").value("Test Store 2"))
                .andExpect(jsonPath("$.[1].location").value("Test Store 2 location"))
                .andExpect(jsonPath("$.[1].website").value("store2.com"));
    }

    @Test
    public void testGetById() throws Exception {
        StoreResponseDto storeDto = StoreResponseDto
                .builder()
                .id(1L)
                .name("Test Store")
                .location("Test Store location")
                .website("store.com")
                .build();

        when(storeService
                .getById(anyLong()))
                .thenReturn(storeDto);

        mockMvc.perform(get("/stores/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Store"))
                .andExpect(jsonPath("$.location").value("Test Store location"))
                .andExpect(jsonPath("$.website").value("store.com"));
    }

    @Test
    public void testGetById_WithNonExistentId() throws Exception {
        long nonExistentId = 100L;
        when(storeService
                .getById(eq(nonExistentId)))
                .thenThrow(new ResourceNotFoundException("Invalid store"));

        mockMvc.perform(get("/stores/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid store"));
    }

    @Test
    public void testCreate() throws Exception {
        mockMvc.perform(post("/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Store\",\"location\":\"Store Location\",\"website\":\"store.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Store has been created successfully"));

        verify(storeService).create(any(StoreRequestDto.class));
    }

    @Test
    public void testCreate_WithEmptyName() throws Exception {
        String invalidJsonInput = "{\"name\":\"\",\"location\":\"Store location\",\"website\":\"store.com\"}";

        mockMvc.perform(post("/stores")
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
        long storeId = 1L;
        Store updatedStore = Store
                .builder()
                .id(storeId)
                .name("Updated Store")
                .location("Updated Store location")
                .website("updated-store.com")
                .build();
        when(storeService
                .update(eq(storeId), any(StoreRequestDto.class)))
                .thenReturn(updatedStore);

        mockMvc.perform(put("/stores/{id}", storeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Store\",\"location\":\"Updated location\",\"website\":\"updated-com.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Store has been updated successfully"));

        verify(storeService).update(eq(storeId), any(StoreRequestDto.class));
    }

    @Test
    public void testUpdate_WithNonExistentId() throws Exception {
        long nonExistentId = 100L;
        when(storeService
                .update(eq(nonExistentId), any()))
                .thenThrow(new ResourceNotFoundException("Invalid store"));

        mockMvc.perform(put("/stores/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Store\",\"location\":\"Updated location\",\"website\":\"updated-com.com\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid store"));
    }

    @Test
    public void testUpdate_WithEmptyName() throws Exception {
        String invalidJsonInput = "{\"name\":\"\",\"location\":\"Updated location\",\"website\":\"updated-com.com\"}";

        mockMvc.perform(put("/stores/{id}", 1L)
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
        long storeId = 1L;
        doNothing().when(storeService).delete(storeId);

        mockMvc.perform(delete("/stores/{id}", storeId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Store has been deleted successfully"));

        verify(storeService).delete(storeId);
    }

    @Test
    public void testDelete_WithNonExistentId() throws Exception {
        long nonExistentId = 100L;
        doThrow(new ResourceNotFoundException("Invalid store"))
                .when(storeService)
                .delete(nonExistentId);

        mockMvc.perform(delete("/stores/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid store"));
    }
}

