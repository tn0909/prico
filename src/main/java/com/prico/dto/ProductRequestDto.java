package com.prico.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Product name should not be NULL or EMPTY")
    private String name;

    private String description;
}
