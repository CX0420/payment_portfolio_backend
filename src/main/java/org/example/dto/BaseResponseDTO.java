package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Base Response DTO - shared across all services
 * Contains common response fields including audit information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseDTO {

    private Long id;

    private String mobileUserId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDatetime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedDateTime;

    private String createdBy;

    private String modifiedBy;

    private String status;

    // Getters and Setters (Lombok @Data generates these)
}
