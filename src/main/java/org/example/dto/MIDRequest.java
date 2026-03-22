package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MIDRequest {

    @NotBlank(message = "MID is required")
    @Size(min = 1, max = 50, message = "MID must be between 1 and 50 characters")
    private String mid;

    @NotBlank(message = "Card Type is required")
    @Size(min = 1, max = 50, message = "Card Type must be between 1 and 50 characters")
    private String cardType;

    // Getters and Setters
    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
