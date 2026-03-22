//package org.example.controller;
//
//import jakarta.annotation.Resource;
//import jakarta.validation.Valid;
//import org.example.dto.Result;
//import org.example.dto.SettingUpdateRequest;
//import org.example.model.MobileUserSetting;
//import org.example.service.MobileUserSettingService;
//import org.springframework.web.bind.annotation.*;
//
//@CrossOrigin
//@RestController
//@RequestMapping("/api/v1/settings")
//public class SettingController {
//
//    @Resource
//    private MobileUserSettingService mobileUserSettingService;
//
//    @GetMapping("/user/{mobileUserId}")
//    public Result<MobileUserSetting> getByMobileUserId(@PathVariable String mobileUserId) {
//        try {
//            MobileUserSetting setting = mobileUserSettingService.findByMobileUserId(mobileUserId);
//            if (setting == null) {
//                return new Result<>(404, "Setting not found", null);
//            }
//            return Result.success(setting);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @GetMapping("/{id}")
//    public Result<MobileUserSetting> getById(@PathVariable Long id) {
//        try {
//            MobileUserSetting setting = mobileUserSettingService.findById(id);
//            if (setting == null) {
//                return new Result<>(404, "Setting not found", null);
//            }
//            return Result.success(setting);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @PostMapping
//    public Result<MobileUserSetting> create(@Valid @RequestBody SettingUpdateRequest request) {
//        try {
//            MobileUserSetting setting = new MobileUserSetting();
//            setting.setIsNotificationOn(request.getIsNotificationOn() != null ? request.getIsNotificationOn() : true);
//            setting.setIsDarkModeOn(request.getIsDarkModeOn() != null ? request.getIsDarkModeOn() : false);
//            setting.setSelectedLanguage(request.getSelectedLanguage() != null ? request.getSelectedLanguage() : "en");
//
//            MobileUserSetting created = mobileUserSettingService.create(setting);
//            return Result.success(created);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @PutMapping("/{id}")
//    public Result<MobileUserSetting> update(
//            @PathVariable Long id,
//            @Valid @RequestBody SettingUpdateRequest request) {
//        try {
//            MobileUserSetting existing = mobileUserSettingService.findById(id);
//            if (existing == null) {
//                return new Result<>(404, "Setting not found", null);
//            }
//
//            if (request.getIsNotificationOn() != null) {
//                existing.setIsNotificationOn(request.getIsNotificationOn());
//            }
//            if (request.getIsDarkModeOn() != null) {
//                existing.setIsDarkModeOn(request.getIsDarkModeOn());
//            }
//            if (request.getSelectedLanguage() != null) {
//                existing.setSelectedLanguage(request.getSelectedLanguage());
//            }
//
//            MobileUserSetting updated = mobileUserSettingService.update(existing);
//            return Result.success(updated);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
//}
