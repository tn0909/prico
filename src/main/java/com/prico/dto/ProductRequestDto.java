package com.prico.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestDto {

    @NotBlank(message = "Name should not be NULL or EMPTY")
    private String name;

    private String description;

    private String imageUrl;

    private Long brandId;

    private Long categoryId;
}
