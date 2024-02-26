package com.prico.dto.crud;

import com.prico.dto.crud.ProductResponseDto;
import com.prico.dto.crud.StoreResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductStoreResponseDto {

    private Long id;

    private String name;

    private String url;

    private String imageUrl;

    private Float price;

    private ProductResponseDto product;

    private StoreResponseDto store;
}
