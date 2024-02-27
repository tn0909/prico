package com.prico.service;

import com.prico.dto.crud.BrandRequestDto;
import com.prico.dto.crud.BrandResponseDto;
import com.prico.model.Brand;

import java.util.List;

public interface BrandService {

    List<BrandResponseDto> getAll();

    BrandResponseDto getById(Long id);

    Brand create(BrandRequestDto brand);

    Brand update(Long id, BrandRequestDto brand);

    void delete(Long id);
}
