package com.prico.service.impl;

import com.prico.dto.CategoryRequestDto;
import com.prico.dto.CategoryResponseDto;
import com.prico.entity.ProductCategory;
import com.prico.exception.EntityNotFoundException;
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
        Optional<ProductCategory> optionalProductCategory = repository.findById(id);

        if (optionalProductCategory.isPresent()) {
            return ObjectMapper.toDto(optionalProductCategory.get());
        }

        throw new EntityNotFoundException("ProductCategory not found with id: " + id);
    }

    @Override
    public ProductCategory create(CategoryRequestDto productCategoryRequest) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(productCategoryRequest.getName());
        productCategory.setDescription(productCategoryRequest.getDescription());

        return repository.save(productCategory);
    }

    @Override
    public ProductCategory update(Long id, CategoryRequestDto productCategoryRequest) {
        ProductCategory existingProductCategory = repository.findById(id).orElse(null);

        if (existingProductCategory != null) {
            existingProductCategory.setName(productCategoryRequest.getName());
            existingProductCategory.setDescription(productCategoryRequest.getDescription());

            return repository.save(existingProductCategory);
        }

        throw new EntityNotFoundException("ProductCategory not found with id: " + id);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return;
        }

        throw new EntityNotFoundException("ProductCategory not found with id: " + id);
    }
}
