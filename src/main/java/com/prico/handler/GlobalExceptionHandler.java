package com.prico.handler;

import com.prico.dto.ApiError;
import com.prico.dto.ApiResponse;
import com.prico.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleProductNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<?> response = ApiResponse
            .<List<String>>builder()
            .status("error")
            .message(ex.getMessage())
            .build();

        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ApiError> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = error instanceof FieldError ? ((FieldError) error).getField() : "";
            String errorMessage = error.getDefaultMessage();
            errors.add(new ApiError(fieldName, errorMessage));
        });

        ApiResponse<List<String>> response = ApiResponse
            .<List<String>>builder()
            .status("error")
            .errors(errors)
            .message("Validation failed")
            .build();

        return response;
    }
}
