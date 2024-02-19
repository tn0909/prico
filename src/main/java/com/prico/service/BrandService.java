package com.prico.service;

import com.prico.dto.BrandRequestDto;
import com.prico.dto.BrandResponseDto;
import com.prico.dto.ProductRequestDto;
import com.prico.dto.ProductResponseDto;
import com.prico.entity.Brand;
import com.prico.entity.Product;

import java.util.List;

public interface BrandService {

    List<BrandResponseDto> getAll();

    BrandResponseDto getById(Long id);

    Brand create(BrandRequestDto brand);

    Brand update(Long id, BrandRequestDto brand);

    void delete(Long id);
}
