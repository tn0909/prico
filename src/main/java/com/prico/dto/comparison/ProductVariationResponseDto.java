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
public class ProductVariationResponseDto {

    private Long productId;

    private String productName;

    private String productImageUrl;

    private List<StoreDto> stores;
}
