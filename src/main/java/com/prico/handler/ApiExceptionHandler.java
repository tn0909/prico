package com.prico.handler;

import com.prico.dto.ApiErrorResponse;
import com.prico.dto.ApiSuccessResponse;
import com.prico.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse<?> handleProductNotFoundException(ProductNotFoundException ex) {
        ApiErrorResponse<?> response = ApiErrorResponse
            .<List<String>>builder()
            .status("error")
            .message(ex.getMessage())
            .build();

        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName + ": " + errorMessage);
        });

        ApiErrorResponse<List<String>> response = ApiErrorResponse
            .<List<String>>builder()
            .status("error")
            .data(errors)
            .message("Validation failed")
            .build();

        return response;
    }
}
