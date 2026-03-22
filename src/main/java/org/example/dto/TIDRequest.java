package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TIDRequest {

    @NotBlank(message = "TID is required")
    @Size(min = 1, max = 50, message = "TID must be between 1 and 50 characters")
    private String tid;

    @NotNull(message = "Mobile User ID is required")
    private String mobileUserId;

    @NotNull(message = "MID ID is required")
    private Long midId;

    // Getters and Setters
    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getMobileUserId() {
        return mobileUserId;
    }

    public void setMobileUserId(String mobileUserId) {
        this.mobileUserId = mobileUserId;
    }

    public Long getMidId() {
        return midId;
    }

    public void setMidId(Long midId) {
        this.midId = midId;
    }
}
