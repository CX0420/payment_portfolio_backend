package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base Request DTO - shared across all services
 * Contains common fields that every request might need
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseRequestDTO {

    private String mobileUserId;

    private String requestSource;

    private String remarks;

    // Getters and Setters (Lombok @Data generates these)
}
