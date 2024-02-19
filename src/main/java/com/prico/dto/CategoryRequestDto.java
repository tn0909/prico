package com.prico.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequestDto {

    @NotBlank(message = "Name should not be NULL or EMPTY")
    private String name;

    private String description;
}