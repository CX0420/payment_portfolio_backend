package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BatchRequest {

    @NotBlank(message = "Status is required")
    @Size(min = 1, max = 50, message = "Status must be between 1 and 50 characters")
    private String status;

    @NotNull(message = "TID ID is required")
    private Long tidId;

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTidId() {
        return tidId;
    }

    public void setTidId(Long tidId) {
        this.tidId = tidId;
    }
}
