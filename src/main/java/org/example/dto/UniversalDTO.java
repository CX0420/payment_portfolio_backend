package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.example.enumConstant.PaymentType;
import org.example.model.Batch;
import org.example.model.TransactionData;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Universal DTO - Single DTO for ALL services including login
 * Differentiated by serviceName flag
 *
 * Supported Service Types:
 * - LOGIN: Authentication service
 * - SIGNUP: User registration
 * - USER: User management operations
 * - TRANSACTION: Transaction operations
 * - SETTING: User settings operations
 * - MID: Merchant ID operations
 * - TID: Terminal ID operations
 * - BATCH: Batch operations
 * - FORGOT_PASSWORD: Password recovery
 * - RESET_PASSWORD: Password reset
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversalDTO {

    // ===== Service Differentiation =====
    @NotBlank(message = "Service name is required")
    private String serviceName;  // LOGIN, SIGNUP, USER, TRANSACTION, etc.

    // ===== Authentication/Login Fields =====
    private String mobileUserId;

    private String password;

    private String token;

    private String mobileUserName;

    @Email(message = "Email should be valid")
    private String email;

    // ===== Transaction Fields =====
    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private Long amountAuthorized;

//    private String pan;
//
//    private String expiryDate;
//
//    private String cardHolderName;

    private String cardData;
    private String cardType;

    // ===== MID/TID/Batch Fields =====
    private String mid;

    private String tid;

    private Boolean isNotificationOn;

    private Boolean isDarkModeOn;

    private String selectedLanguage;

    // ===== Flexible Generic Data (for extensibility) =====
    private Map<String, Object> additionalData = new HashMap<>();
    private List<TransactionData> transactionDataList;
    private Long salesTrxId;
    private Long voidTrxId;
    private Boolean isFirstTimeLogin;
    private String newPassword;
    private PaymentType paymentType;
    private Batch batch;

    // ===== Helper Methods =====

    /**
     * Add custom field to additionalData
     */
    public void addAttribute(String key, Object value) {
        if (this.additionalData == null) {
            this.additionalData = new HashMap<>();
        }
        this.additionalData.put(key, value);
    }

    /**
     * Get custom field from additionalData
     */
    public Object getAttribute(String key) {
        if (this.additionalData == null) {
            return null;
        }
        return this.additionalData.get(key);
    }

    /**
     * Check if attribute exists
     */
    public boolean hasAttribute(String key) {
        return this.additionalData != null && this.additionalData.containsKey(key);
    }

}
