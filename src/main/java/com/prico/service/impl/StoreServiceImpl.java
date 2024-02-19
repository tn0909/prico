package com.prico.service.impl;

import com.prico.dto.StoreRequestDto;
import com.prico.dto.StoreResponseDto;
import com.prico.entity.Store;
import com.prico.exception.EntityNotFoundException;
import com.prico.repository.StoreRepository;
import com.prico.service.StoreService;
import com.prico.util.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository repository;

    @Override
    public List<StoreResponseDto> getAll() {
        return repository
            .findAll()
            .stream()
            .map(x -> ObjectMapper.toDto(x))
            .collect(Collectors.toList());
    }

    @Override
    public StoreResponseDto getById(Long id) {
        Optional<Store> optionalStore = repository.findById(id);

        if (optionalStore.isPresent()) {
            return ObjectMapper.toDto(optionalStore.get());
        }

        throw new EntityNotFoundException("Store not found with id: " + id);
    }

    @Override
    public Store create(StoreRequestDto storeRequest) {
        Store store = new Store();
        store.setName(storeRequest.getName());
        store.setLocation(storeRequest.getLocation());
        store.setWebsite(storeRequest.getWebsite());

        return repository.save(store);
    }

    @Override
    public Store update(Long id, StoreRequestDto storeRequest) {
        Store existingStore = repository.findById(id).orElse(null);

        if (existingStore != null) {
            existingStore.setName(storeRequest.getName());
            existingStore.setLocation(storeRequest.getLocation());
            existingStore.setWebsite(storeRequest.getWebsite());

            return repository.save(existingStore);
        }

        throw new EntityNotFoundException("Store not found with id: " + id);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return;
        }

        throw new EntityNotFoundException("Store not found with id: " + id);
    }
}
