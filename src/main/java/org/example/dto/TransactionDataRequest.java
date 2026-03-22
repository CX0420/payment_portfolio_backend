package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class TransactionDataRequest {

    @NotNull(message = "Amount Authorized is required")
    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private Long amountAuthorized;

    @NotBlank(message = "PAN is required")
    private String pan;

    @NotBlank(message = "Expiry Date is required")
    private String expiryDate;

    @NotBlank(message = "Card Holder Name is required")
    private String cardHolderName;

    private String status = "PENDING";

    @NotBlank(message = "Card Type is required")
    private String cardType;

    @NotBlank(message = "Mobile User ID is required")
    private String mobileUserId;

    private String mid;

    private String tid;

    // Getters and Setters
    public Long getAmountAuthorized() {
        return amountAuthorized;
    }

    public void setAmountAuthorized(Long amountAuthorized) {
        this.amountAuthorized = amountAuthorized;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getMobileUserId() {
        return mobileUserId;
    }

    public void setMobileUserId(String mobileUserId) {
        this.mobileUserId = mobileUserId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
