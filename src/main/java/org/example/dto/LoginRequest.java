package org.example.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    /**
     * Accepts either email or mobileUserId.
     */
    @NotBlank
    private String identifier;

    /**
     * For your current schema this maps to `mobile_user.pin`.
     */
    @NotBlank
    private String password;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

