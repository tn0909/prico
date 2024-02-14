package com.prico.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiSuccessResponse<T> {
    private String status;
    private T data;
    private String message;
}

