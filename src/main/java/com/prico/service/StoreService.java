package com.prico.service;

import com.prico.dto.StoreRequestDto;
import com.prico.dto.StoreResponseDto;
import com.prico.entity.Store;

import java.util.List;

public interface StoreService {

    List<StoreResponseDto> getAll();

    StoreResponseDto getById(Long id);

    Store create(StoreRequestDto store);

    Store update(Long id, StoreRequestDto store);

    void delete(Long id);
}
