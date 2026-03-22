package org.example.dto;

import org.example.model.*;
import lombok.experimental.UtilityClass;

/**
 * Shared Entity Mapper - maps between models and SharedEntityDTO
 * Use this for converting any model to the shared DTO format
 */
@UtilityClass
public class SharedEntityMapper {

    /**
     * Convert MobileUser model to SharedEntityDTO
     */
    public static SharedEntityDTO toSharedDTO(MobileUser mobileUser) {
        if (mobileUser == null) {
            return null;
        }

        SharedEntityDTO dto = new SharedEntityDTO();
        dto.setId(mobileUser.getId());
        dto.setCreatedDatetime(mobileUser.getCreatedDateTime());
        dto.setModifiedDateTime(mobileUser.getModifiedDateTime());
        dto.setCreatedBy(mobileUser.getCreatedBy());
        dto.setModifiedBy(mobileUser.getModifiedBy());

        // Map entity-specific fields
        dto.setEntityType("MOBILE_USER");
        dto.setMobileUserId(mobileUser.getMobileUserId());
        dto.setMobileUserName(mobileUser.getMobileUserName());
        dto.setEmail(mobileUser.getEmail());
        dto.setStatus(mobileUser.getStatus());

        return dto;
    }

    /**
     * Convert TransactionData model to SharedEntityDTO
     */
    public static SharedEntityDTO toSharedDTO(TransactionData transaction) {
        if (transaction == null) {
            return null;
        }

        SharedEntityDTO dto = new SharedEntityDTO();
        dto.setId(transaction.getId());
        dto.setCreatedDatetime(transaction.getCreatedDateTime());
        dto.setModifiedDateTime(transaction.getModifiedDateTime());
        dto.setCreatedBy(transaction.getCreatedBy());
        dto.setModifiedBy(transaction.getModifiedBy());

        // Map entity-specific fields
        dto.setEntityType("TRANSACTION");
        dto.setMobileUserId(transaction.getMobileUserId());
        dto.setAmountAuthorized(transaction.getAmountAuthorized());
        dto.setPan(transaction.getPan());
        dto.setExpiryDate(transaction.getExpiryDate());
        dto.setCardHolderName(transaction.getCardHolderName());
        dto.setCardType(transaction.getCardType());
        dto.setStatus(transaction.getStatus());
        dto.setMid(transaction.getMid());
        dto.setTid(transaction.getTid());

        return dto;
    }

    /**
     * Convert MobileUserSetting model to SharedEntityDTO
     */
    public static SharedEntityDTO toSharedDTO(MobileUserSetting setting) {
        if (setting == null) {
            return null;
        }

        SharedEntityDTO dto = new SharedEntityDTO();
        dto.setId(setting.getId());
        dto.setCreatedDatetime(setting.getCreatedDateTime());
        dto.setModifiedDateTime(setting.getModifiedDateTime());
        dto.setCreatedBy(setting.getCreatedBy());
        dto.setModifiedBy(setting.getModifiedBy());

        // Map entity-specific fields
        dto.setEntityType("SETTING");
        dto.setIsNotificationOn(setting.getIsNotificationOn());
        dto.setIsDarkModeOn(setting.getIsDarkModeOn());
        dto.setSelectedLanguage(setting.getSelectedLanguage());

        // Get mobile user ID from the relationship if available
        if (setting.getMobileUser() != null) {
            dto.setMobileUserId(setting.getMobileUser().getMobileUserId());
        }

        return dto;
    }

    /**
     * Convert MID model to SharedEntityDTO
     */
    public static SharedEntityDTO toSharedDTO(MID mid) {
        if (mid == null) {
            return null;
        }

        SharedEntityDTO dto = new SharedEntityDTO();
        dto.setId(mid.getId());
        dto.setCreatedDatetime(mid.getCreatedDateTime());
        dto.setModifiedDateTime(mid.getModifiedDateTime());
        dto.setCreatedBy(mid.getCreatedBy());
        dto.setModifiedBy(mid.getModifiedBy());

        // Map entity-specific fields
        dto.setEntityType("MID");
        dto.setMid(mid.getMid());

        return dto;
    }

    /**
     * Convert TID model to SharedEntityDTO
     */
    public static SharedEntityDTO toSharedDTO(TID tid) {
        if (tid == null) {
            return null;
        }

        SharedEntityDTO dto = new SharedEntityDTO();
        dto.setId(tid.getId());
        dto.setCreatedDatetime(tid.getCreatedDateTime());
        dto.setModifiedDateTime(tid.getModifiedDateTime());
        dto.setCreatedBy(tid.getCreatedBy());
        dto.setModifiedBy(tid.getModifiedBy());

        // Map entity-specific fields
        dto.setEntityType("TID");
        dto.setTid(tid.getTid());

        // Get MID string from the relationship if available
        if (tid.getMid() != null) {
            dto.setMid(tid.getMid().getMid());
        }

        // Get mobile user ID from the relationship if available
        if (tid.getMobileUser() != null) {
            dto.setMobileUserId(tid.getMobileUser().getMobileUserId());
        }

        return dto;
    }

    /**
     * Convert Batch model to SharedEntityDTO
     */
    public static SharedEntityDTO toSharedDTO(Batch batch) {
        if (batch == null) {
            return null;
        }

        SharedEntityDTO dto = new SharedEntityDTO();
        dto.setId(batch.getId());
        dto.setCreatedDatetime(batch.getCreatedDateTime());
        dto.setModifiedDateTime(batch.getModifiedDateTime());
        dto.setCreatedBy(batch.getCreatedBy());
        dto.setModifiedBy(batch.getModifiedBy());

        // Map entity-specific fields
        dto.setEntityType("BATCH");
        dto.setBatchStatus(batch.getStatus());
        dto.setStatus(batch.getStatus());

        // Get TID from the relationship if available
        if (batch.getTid() != null) {
            dto.setTid(batch.getTid().getTid());
            if (batch.getTid().getMid() != null) {
                dto.setMid(batch.getTid().getMid().getMid());
            }
            if (batch.getTid().getMobileUser() != null) {
                dto.setMobileUserId(batch.getTid().getMobileUser().getMobileUserId());
            }
        }

        return dto;
    }

    /**
     * Convert SharedEntityDTO to MobileUser model
     */
    public static MobileUser fromSharedDTO(SharedEntityDTO dto, MobileUser mobileUser) {
        if (dto == null) {
            return null;
        }

        if (mobileUser == null) {
            mobileUser = new MobileUser();
        }

        mobileUser.setMobileUserId(dto.getMobileUserId());
        mobileUser.setMobileUserName(dto.getMobileUserName());
        mobileUser.setEmail(dto.getEmail());
        mobileUser.setStatus(dto.getStatus());

        return mobileUser;
    }

    /**
     * Convert SharedEntityDTO to TransactionData model
     */
    public static TransactionData fromSharedDTO(SharedEntityDTO dto, TransactionData transaction) {
        if (dto == null) {
            return null;
        }

        if (transaction == null) {
            transaction = new TransactionData();
        }

        transaction.setMobileUserId(dto.getMobileUserId());
        transaction.setAmountAuthorized(dto.getAmountAuthorized());
        transaction.setPan(dto.getPan());
        transaction.setExpiryDate(dto.getExpiryDate());
        transaction.setCardHolderName(dto.getCardHolderName());
        transaction.setCardType(dto.getCardType());
        transaction.setStatus(dto.getStatus());
        transaction.setMid(dto.getMid());
        transaction.setTid(dto.getTid());

        return transaction;
    }

    /**
     * Convert SharedEntityDTO to MobileUserSetting model
     */
    public static MobileUserSetting fromSharedDTO(SharedEntityDTO dto, MobileUserSetting setting) {
        if (dto == null) {
            return null;
        }

        if (setting == null) {
            setting = new MobileUserSetting();
        }

        setting.setIsNotificationOn(dto.getIsNotificationOn());
        setting.setIsDarkModeOn(dto.getIsDarkModeOn());
        setting.setSelectedLanguage(dto.getSelectedLanguage());

        return setting;
    }

    // Getters and Setters handled by Lombok
}
