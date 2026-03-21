package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.dto.*;
import org.example.model.MobileUser;
import org.example.service.MobileUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/mobileUser")
public class MobileUserController {

    @Resource
    MobileUserService mobileUserService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UniversalDTO>> signup(@Valid @RequestBody UniversalDTO dto) {
        ApiResponse<UniversalDTO> response = (ApiResponse<UniversalDTO>) mobileUserService.processSignup(dto);
        if (response.getSuccess()) {
            if (response.getStatusCode() == 201) {
                return ResponseEntity.status(201).body(response);
            }
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<UniversalDTO>> forgotPassword(@Valid @RequestBody UniversalDTO dto) {

        ApiResponse<UniversalDTO> response = (ApiResponse<UniversalDTO>) mobileUserService.processForgotPassword(dto);
        if (response.getSuccess()) {
            if (response.getStatusCode() == 201) {
                return ResponseEntity.status(201).body(response);
            }
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }

//    @PostMapping("/reset-password")
//    public Result<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
//        try {
//            mobileUserService.resetPassword(request.getResetToken(), request.getNewPassword());
//            return Result.success(null);
//        } catch (IllegalArgumentException e) {
//            return new Result<>(400, e.getMessage(), null);
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }
}