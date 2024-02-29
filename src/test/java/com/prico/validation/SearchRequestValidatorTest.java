package com.prico.validation;

import com.prico.dto.SearchRequestDto;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SearchRequestValidatorTest {

    private final SearchRequestValidator validator = new SearchRequestValidator();

    @Test
    public void testValidation_WithAllSpecified_ReturnsSuccess() {
        SearchRequestDto searchRequestDto = new SearchRequestDto("name", "brand", "category");
        assertTrue(validator.isValid(searchRequestDto, null));
    }

    @Test
    public void testValidation_WithNameSpecified_ReturnsSuccess() {
        SearchRequestDto searchRequestDto = new SearchRequestDto("name", "", null);
        assertTrue(validator.isValid(searchRequestDto, null));
    }

    @Test
    public void testValidation_WithBrandSpecified_ReturnsSuccess() {
        SearchRequestDto searchRequestDto = new SearchRequestDto(null, "brand", " ");
        assertTrue(validator.isValid(searchRequestDto, null));
    }

    @Test
    public void testValidation_WithCategorySpecified_ReturnsSuccess() {
        SearchRequestDto searchRequestDto = new SearchRequestDto("", null, "category");
        assertTrue(validator.isValid(searchRequestDto, null));
    }

    @Test
    public void testValidation_AllEmpty_ReturnsFailure() {
        SearchRequestDto searchRequestDto = new SearchRequestDto("", "", "");
        assertFalse(validator.isValid(searchRequestDto, null));
    }

    @Test
    public void testValidation_AllNull_ReturnsFailure() {
        SearchRequestDto searchRequestDto = new SearchRequestDto(null, null, null);
        assertFalse(validator.isValid(searchRequestDto, null));
    }

    @Test
    public void testValidation_Null_ReturnsFailure() {
        assertFalse(validator.isValid(null, null));
    }

}
