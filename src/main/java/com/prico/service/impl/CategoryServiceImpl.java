package com.prico.service.impl;

import com.prico.dto.CategoryRequestDto;
import com.prico.dto.CategoryResponseDto;
import com.prico.model.Category;
import com.prico.exception.ResourceNotFoundException;
import com.prico.repository.CategoryRepository;
import com.prico.service.CategoryService;
import com.prico.util.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Override
    public List<CategoryResponseDto> getAll() {
        return repository
            .findAll()
            .stream()
            .map(x -> ObjectMapper.toDto(x))
            .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        Optional<Category> optionalProductCategory = repository.findById(id);

        if (optionalProductCategory.isPresent()) {
            return ObjectMapper.toDto(optionalProductCategory.get());
        }

        throw new ResourceNotFoundException("ProductCategory not found with id: " + id);
    }

    @Override
    public Category create(CategoryRequestDto productCategoryRequest) {
        Category category = new Category();
        category.setName(productCategoryRequest.getName());
        category.setDescription(productCategoryRequest.getDescription());

        return repository.save(category);
    }

    @Override
    public Category update(Long id, CategoryRequestDto productCategoryRequest) {
        Category existingCategory = repository.findById(id).orElse(null);

        if (existingCategory != null) {
            existingCategory.setName(productCategoryRequest.getName());
            existingCategory.setDescription(productCategoryRequest.getDescription());

            return repository.save(existingCategory);
        }

        throw new ResourceNotFoundException("ProductCategory not found with id: " + id);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return;
        }

        throw new ResourceNotFoundException("ProductCategory not found with id: " + id);
    }
}
