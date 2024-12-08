package com.recargapay.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(LocalDateTime.now(), 200, message, data);
    }

    public static <T> ApiResponse<T> error(T data, String message, int status) {
        return new ApiResponse<>(LocalDateTime.now(), status, message, data);
    }
}
