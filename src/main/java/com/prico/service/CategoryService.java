package com.prico.service;

import com.prico.dto.crud.CategoryRequestDto;
import com.prico.dto.crud.CategoryResponseDto;
import com.prico.model.Category;

import java.util.List;

public interface CategoryService {

    List<CategoryResponseDto> getAll();

    CategoryResponseDto getById(Long id);

    Category create(CategoryRequestDto productCategory);

    Category update(Long id, CategoryRequestDto productCategory);

    void delete(Long id);
}
