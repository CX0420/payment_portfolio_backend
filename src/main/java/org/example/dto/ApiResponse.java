package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Generic API Response Wrapper - shared across all services
 * Wraps all service responses for consistency
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private static final long serialVersionUID = 1L;

    private Boolean success;

    private Integer statusCode;

    private String message;

    private T data;

    private List<String> errors;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private LocalDateTime timestamp;

    /**
     * Constructor for successful responses with data
     */
    public ApiResponse(Boolean success, Integer statusCode, String message, T data) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor for responses without data
     */
    public ApiResponse(Boolean success, Integer statusCode, String message) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor for error responses with errors list
     */
    public ApiResponse(Boolean success, Integer statusCode, String message, List<String> errors) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    // Utility methods
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, 200, message, data);
    }

    public static <T> ApiResponse<T> success(T data, String message, Integer statusCode) {
        return new ApiResponse<>(true, statusCode, message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, 200, message);
    }

    public static <T> ApiResponse<T> error(String message, Integer statusCode) {
        return new ApiResponse<>(false, statusCode, message);
    }

    public static <T> ApiResponse<T> error(String message, Integer statusCode, List<String> errors) {
        return new ApiResponse<>(false, statusCode, message, errors);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(false, 400, message);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(false, 401, message);
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(false, 403, message);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(false, 404, message);
    }

    public static <T> ApiResponse<T> internalServerError(String message) {
        return new ApiResponse<>(false, 500, message);
    }

    // Getters and Setters (Lombok @Data generates these)
}
