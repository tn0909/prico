package com.prico.dto;

import com.prico.validation.AtLeastOneNotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AtLeastOneNotBlank
public class SearchRequestDto {

    private String name;

    private String brand;

    private String category;
}
