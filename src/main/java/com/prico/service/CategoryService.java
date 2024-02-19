package com.prico.service;

import com.prico.dto.CategoryRequestDto;
import com.prico.dto.CategoryResponseDto;
import com.prico.entity.ProductCategory;

import java.util.List;

public interface CategoryService {

    List<CategoryResponseDto> getAll();

    CategoryResponseDto getById(Long id);

    ProductCategory create(CategoryRequestDto productCategory);

    ProductCategory update(Long id, CategoryRequestDto productCategory);

    void delete(Long id);
}
