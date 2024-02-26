package com.prico.dto.comparison;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductStoreDto {

    private Long id;

    private String name;

    private String url;

    private String imageUrl;

    private Float price;
}
