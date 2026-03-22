package org.example.service;

import jakarta.annotation.Resource;
import org.example.dao.MobileUserSettingDAO;
import org.example.dto.ApiResponse;
import org.example.dto.UniversalDTO;
import org.example.model.MobileUserSetting;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileUserSettingService {

    @Resource
    private MobileUserSettingDAO mobileUserSettingDAO;
//
//    /**
//     * Process setting request using UniversalDTO
//     */
//    public ApiResponse<UniversalDTO> processSetting(UniversalDTO request) {
//        try {
//            if (!request.isSetting()) {
//                return ApiResponse.badRequest("Invalid service type for setting");
//            }
//
//            // Validate required fields
//            if (request.getMobileUserId() == null) {
//                return ApiResponse.badRequest("Mobile User ID is required for settings");
//            }
//
//            // Find existing setting or create new one
//            MobileUserSetting setting = mobileUserSettingDAO.findByMobileUserId(request.getMobileUserId());
//            if (setting == null) {
//                setting = new MobileUserSetting();
//            }
//
//            // Update setting from DTO
//            setting = UniversalMapper.fromUniversalDTO(request, setting);
//
//            MobileUserSetting saved = mobileUserSettingDAO.create(setting);
//
//            UniversalDTO response = UniversalMapper.toUniversalDTO(saved, "SETTING");
//            return ApiResponse.success(response, "Setting processed successfully");
//
//        } catch (Exception e) {
//            return ApiResponse.internalServerError("Setting processing failed: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Get setting by ID
//     */
//    public ApiResponse<UniversalDTO> getSetting(Long id) {
//        try {
//            MobileUserSetting setting = mobileUserSettingDAO.findById(id).orElse(null);
//            if (setting == null) {
//                return ApiResponse.notFound("Setting not found");
//            }
//
//            UniversalDTO response = UniversalMapper.toUniversalDTO(setting, "SETTING");
//            return ApiResponse.success(response, "Setting retrieved successfully");
//
//        } catch (Exception e) {
//            return ApiResponse.internalServerError("Failed to get setting: " + e.getMessage());
//        }
//    }
//
//    /**
//     * List settings for a user
//     */
//    public ApiResponse<List<UniversalDTO>> listSettings(String mobileUserId) {
//        try {
//            List<MobileUserSetting> settings;
//            if (mobileUserId != null) {
//                MobileUserSetting setting = mobileUserSettingDAO.findByMobileUserId(mobileUserId);
//                settings = setting != null ? List.of(setting) : List.of();
//            } else {
//                // If no mobileUserId provided, return empty list (or implement full list if needed)
//                settings = List.of();
//            }
//
//            List<UniversalDTO> dtos = settings.stream()
//                .map(setting -> UniversalMapper.toUniversalDTO(setting, "SETTING"))
//                .collect(Collectors.toList());
//
//            return ApiResponse.success(dtos, "Settings retrieved successfully");
//
//        } catch (Exception e) {
//            return ApiResponse.internalServerError("Failed to list settings: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Update setting
//     */
//    public ApiResponse<UniversalDTO> updateSetting(Long id, UniversalDTO request) {
//        try {
//            MobileUserSetting existingSetting = mobileUserSettingDAO.findById(id).orElse(null);
//            if (existingSetting == null) {
//                return ApiResponse.notFound("Setting not found");
//            }
//
//            // Update setting from DTO
//            MobileUserSetting updatedSetting = UniversalMapper.fromUniversalDTO(request, existingSetting);
//            updatedSetting.setId(id); // Ensure ID is preserved
//            updatedSetting.setModifiedDateTime(LocalDateTime.now());
//
//            mobileUserSettingDAO.update(updatedSetting);
//
//            UniversalDTO response = UniversalMapper.toUniversalDTO(updatedSetting, "SETTING");
//            return ApiResponse.success(response, "Setting updated successfully");
//
//        } catch (Exception e) {
//            return ApiResponse.internalServerError("Failed to update setting: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Delete setting
//     */
//    public void deleteSetting(Long id) {
//        // Note: You might want to implement delete in DAO if needed
//        // mobileUserSettingDAO.delete(id);
//    }
//
//    // Legacy methods for backward compatibility
//    public MobileUserSetting findByMobileUserId(String mobileUserId) {
//        return mobileUserSettingDAO.findByMobileUserId(mobileUserId);
//    }
//
//    public MobileUserSetting findById(Long id) {
//        return mobileUserSettingDAO.findById(id).orElse(null);
//    }
//
    public MobileUserSetting create(MobileUserSetting setting) {
        return mobileUserSettingDAO.create(setting);
    }
//
//    public MobileUserSetting update(MobileUserSetting setting) {
//        mobileUserSettingDAO.update(setting);
//        return setting;
//    }
}
