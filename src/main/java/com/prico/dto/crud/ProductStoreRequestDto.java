package com.prico.dto.crud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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

    @NotBlank(message = "Price should not be NULL or 0")
    private Float price;

    @NotBlank(message = "Product should not be NULL")
    private Long productId;

    @NotBlank(message = "Store should not be NULL")
    private Long storeId;
}
