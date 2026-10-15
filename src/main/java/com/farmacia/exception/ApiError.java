package com.farmacia.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/** Formato único de error para toda la API. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(int status, String mensaje, Map<String, List<String>> errors, LocalDateTime timestamp) {

    public static ApiError of(HttpStatus status, String mensaje) {
        return new ApiError(status.value(), mensaje, null, LocalDateTime.now());
    }

    public static ApiError of(HttpStatus status, String mensaje, Map<String, List<String>> errors) {
        return new ApiError(status.value(), mensaje, errors, LocalDateTime.now());
    }
}
