package com.prico.controller;

import com.prico.dto.ApiResponse;
import com.prico.dto.crud.BrandRequestDto;
import com.prico.dto.crud.BrandResponseDto;
import com.prico.model.Brand;
import com.prico.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService service;

    @GetMapping()
    public ResponseEntity<List<BrandResponseDto>> getAll() {
        List<BrandResponseDto> brands = service.getAll();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDto> getById(@PathVariable Long id) {
        BrandResponseDto brand = service.getById(id);
        return ResponseEntity.ok(brand);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody BrandRequestDto brand) {
        service.create(brand);

        ApiResponse<Brand> response = new ApiResponse<>();
        response.setMessage("Brand has been created successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable Long id,
                                                 @Valid @RequestBody BrandRequestDto brand) {
        service.update(id, brand);

        ApiResponse<Brand> response = new ApiResponse<>();
        response.setMessage("Brand has been updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        service.delete(id);

        ApiResponse<Brand> response = new ApiResponse<>();
        response.setMessage("Brand has been deleted successfully");

        return ResponseEntity.ok(response);
    }
}
