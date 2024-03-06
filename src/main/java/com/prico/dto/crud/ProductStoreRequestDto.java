package com.prico.dto.crud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductStoreRequestDto {

    @NotBlank(message = "Name should not be NULL or EMPTY")
    private String name;

    @NotBlank(message = "Url should not be NULL or EMPTY")
    private String url;

    private String imageUrl;

    @NotNull(message = "Price should not be NULL")
    @Positive(message = "Price should be greater than 0")
    private Long price;

    @NotNull(message = "Product should not be NULL")
    private Long productId;

    @NotNull(message = "Store should not be NULL")
    private Long storeId;
}
