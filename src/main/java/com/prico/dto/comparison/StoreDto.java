package com.prico.dto.comparison;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDto {

    private Long id;

    private String name;

    private String website;

    private List<ProductStoreDto> variations;
}
