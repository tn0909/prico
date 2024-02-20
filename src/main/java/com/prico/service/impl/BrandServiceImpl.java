package com.prico.service.impl;

import com.prico.dto.BrandRequestDto;
import com.prico.dto.BrandResponseDto;
import com.prico.model.Brand;
import com.prico.exception.ResourceNotFoundException;
import com.prico.repository.BrandRepository;
import com.prico.service.BrandService;
import com.prico.util.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository repository;

    @Override
    public List<BrandResponseDto> getAll() {
        return repository
            .findAll()
            .stream()
            .map(x -> ObjectMapper.toDto(x))
            .collect(Collectors.toList());
    }

    @Override
    public BrandResponseDto getById(Long id) {
        Optional<Brand> optionalBrand = repository.findById(id);

        if (optionalBrand.isPresent()) {
            return ObjectMapper.toDto(optionalBrand.get());
        }

        throw new ResourceNotFoundException("Brand not found with id: " + id);
    }

    @Override
    public Brand create(BrandRequestDto brandRequest) {
        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brand.setDescription(brandRequest.getDescription());

        return repository.save(brand);
    }

    @Override
    public Brand update(Long id, BrandRequestDto brandRequest) {
        Brand existingBrand = repository.findById(id).orElse(null);

        if (existingBrand != null) {
            existingBrand.setName(brandRequest.getName());
            existingBrand.setDescription(brandRequest.getDescription());

            return repository.save(existingBrand);
        }

        throw new ResourceNotFoundException("Brand not found with id: " + id);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return;
        }

        throw new ResourceNotFoundException("Brand not found with id: " + id);
    }
}
