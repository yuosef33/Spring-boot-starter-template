package com.yuosef.springbootstartertemplate.Models.Dtos;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
    /// for success responses with data
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /// for success responses with no data
    public static <T> ApiResponse<T> ok(String message) {
        return new ApiResponse<>(true, message, null);
    }

    /// for error responses
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}