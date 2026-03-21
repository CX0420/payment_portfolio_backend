package org.example.service;

import jakarta.annotation.Resource;
import org.example.dao.MobileUserDAO;
import org.example.dto.ApiResponse;
import org.example.dto.UniversalDTO;
import org.example.model.MobileUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MobileUserService {

    @Resource
    MobileUserDAO mobileUserDAO;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ApiResponse<UniversalDTO> processSignup(UniversalDTO dto) {
        try {

            // Validate input
            if (dto.getMobileUserId() == null || dto.getEmail() == null ||
                    dto.getPassword() == null || dto.getMobileUserName() == null) {
                return ApiResponse.badRequest("All fields are required for signup");
            }

            if (mobileUserDAO.existsByMobileUserId(dto.getMobileUserId())) {
                return ApiResponse.badRequest("Mobile User ID already exists");
            }

            // Create new user
            MobileUser newUser = new MobileUser();
            newUser.setMobileUserId(dto.getMobileUserId());
            newUser.setMobileUserName(dto.getMobileUserName());
            newUser.setEmail(dto.getEmail());
            newUser.setPin(passwordEncoder.encode(dto.getPassword()));
            newUser.setWrongPasswordCount(0);
            newUser.setStatus("ACTIVE");

            mobileUserDAO.create(newUser);

            // Retrieve created user
            MobileUser user = mobileUserDAO.findByMobileUserId(dto.getMobileUserId());
            if (user == null) {
                return ApiResponse.internalServerError("Failed to create user");
            }

            return ApiResponse.success(dto, "User created successfully", 201);

        } catch (Exception e) {
            return ApiResponse.internalServerError("Signup failed: " + e.getMessage());
        }
    }

    public ApiResponse<UniversalDTO> processForgotPassword(UniversalDTO dto) {
        try {

            String muid = dto.getMobileUserId() != null ? dto.getMobileUserId() : dto.getEmail();
            if (muid == null) {
                return ApiResponse.badRequest("Email or username is required");
            }

            MobileUser user = mobileUserDAO.findByMobileUserId(muid);
            if (user == null) {
                return ApiResponse.badRequest("User not found");
            }

            // // Generate reset token
            // String resetToken = UUID.randomUUID().toString();
            //
            // // Store token (you might want to store this in database/cache)
            // passwordResetTokenService.createToken(user.getId(), resetToken);

            // In a real application, send email here
            // emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

            // dto.addAttribute("resetToken", resetToken);

            return ApiResponse.success(dto, "Password reset email sent");

        } catch (Exception e) {
            return ApiResponse.internalServerError("Forgot password failed: " + e.getMessage());
        }
    }

    // /**
    // * Process reset password request using UniversalDTO
    // */
    // public ApiResponse<UniversalDTO> processResetPassword(UniversalDTO request) {
    // try {
    // if (!request.isResetPassword()) {
    // return ApiResponse.badRequest("Invalid service type for reset password");
    // }
    //
    // if (request.getEmail() == null || request.getResetToken() == null ||
    // request.getNewPassword() == null) {
    // return ApiResponse.badRequest("Email, reset token, and new password are
    // required");
    // }
    //
    // MobileUser user = loginDAO.findByIdentifier(request.getEmail());
    // if (user == null) {
    // return ApiResponse.badRequest("User not found");
    // }
    //
    // // Validate token
    // boolean isValidToken = passwordResetTokenService.validateToken(user.getId(),
    // request.getResetToken());
    // if (!isValidToken) {
    // return ApiResponse.badRequest("Invalid or expired reset token");
    // }
    //
    // // Hash new password
    // String hashedPassword = passwordEncoder.encode(request.getNewPassword());
    //
    // // Update password
    // user.setPin(hashedPassword);
    // mobileUserDAO.update(user);
    //
    // // Remove used token
    // passwordResetTokenService.removeToken(user.getId());
    //
    // UniversalDTO response = UniversalMapper.toUniversalDTO(user,
    // "RESET_PASSWORD");
    // return ApiResponse.success(response, "Password reset successful");
    //
    // } catch (Exception e) {
    // return ApiResponse.internalServerError("Reset password failed: " +
    // e.getMessage());
    // }
    // }
    //

}