package com.prico.controller;

import com.prico.dto.ApiResponse;
import com.prico.dto.crud.StoreRequestDto;
import com.prico.dto.crud.StoreResponseDto;
import com.prico.model.Store;
import com.prico.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreService service;

    @GetMapping()
    public ResponseEntity<List<StoreResponseDto>> getAll() {
        List<StoreResponseDto> brands = service.getAll();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDto> getById(@PathVariable Long id) {
        StoreResponseDto brand = service.getById(id);
        return ResponseEntity.ok(brand);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody StoreRequestDto brand) {
        service.create(brand);

        ApiResponse<Store> response = new ApiResponse<>();
        response.setMessage("Store has been created successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable Long id,
                                                 @Valid @RequestBody StoreRequestDto brand) {
        service.update(id, brand);

        ApiResponse<Store> response = new ApiResponse<>();
        response.setMessage("Store has been updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        service.delete(id);

        ApiResponse<Store> response = new ApiResponse<>();
        response.setMessage("Store has been deleted successfully");

        return ResponseEntity.ok(response);
    }
}
