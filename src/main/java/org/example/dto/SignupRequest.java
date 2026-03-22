package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {

    @NotBlank(message = "Mobile User ID is required")
    @Size(min = 3, max = 50, message = "Mobile User ID must be between 3 and 50 characters")
    private String mobileUserId;

    @NotBlank(message = "Mobile User Name is required")
    @Size(min = 2, max = 100, message = "Mobile User Name must be between 2 and 100 characters")
    private String mobileUserName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;

    // Getters and Setters
    public String getMobileUserId() {
        return mobileUserId;
    }

    public void setMobileUserId(String mobileUserId) {
        this.mobileUserId = mobileUserId;
    }

    public String getMobileUserName() {
        return mobileUserName;
    }

    public void setMobileUserName(String mobileUserName) {
        this.mobileUserName = mobileUserName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
