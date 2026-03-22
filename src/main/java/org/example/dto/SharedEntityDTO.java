package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Unified Shared Entity DTO - single model structure for all services
 * This is the primary DTO that all services should use
 * Flexible structure to accommodate different entity types
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedEntityDTO {

    // ===== Audit Fields (from BasicTable) =====
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDatetime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedDateTime;

    private String createdBy;

    private String modifiedBy;

    // ===== Common Entity Fields =====
    private String entityType; // Type of entity: MOBILE_USER, TRANSACTION, MID, TID, BATCH, etc.

    private String status;

    // ===== User/Authentication Related =====
    private String mobileUserId;

    private String mobileUserName;

    private String email;

    private String password;

    // ===== Transaction Related =====
    private Long amountAuthorized;

    private String pan;

    private String expiryDate;

    private String cardHolderName;

    private String cardType;

    // ===== MID/TID/Batch Related =====
    private String mid;

    private String tid;

    private String batchId;

    private String batchStatus;

    // ===== Settings Related =====
    private String settingKey;

    private String settingValue;

    private Boolean isNotificationOn;

    private Boolean isDarkModeOn;

    private String selectedLanguage;

    // ===== Flexible Generic Data (for extensibility) =====
    private Map<String, Object> additionalData = new HashMap<>();

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

    // Getters and Setters (Lombok @Data generates these)
}
