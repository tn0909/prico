package com.prico.validation;

import com.prico.dto.SearchRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SearchRequestValidator implements ConstraintValidator<AtLeastOneNotBlank, SearchRequestDto> {

    @Override
    public boolean isValid(SearchRequestDto request, ConstraintValidatorContext context) {
        return request != null
                && (request.getName() != null && request.getName().trim() != ""
                    || request.getBrand() != null && request.getBrand().trim() != ""
                    || request.getCategory() != null && request.getCategory().trim() != "");
    }
}
