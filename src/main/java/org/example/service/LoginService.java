package org.example.service;

import jakarta.annotation.Resource;
import org.example.dao.LoginDAO;
import org.example.dao.MobileUserDAO;
import org.example.dao.PasswordResetDAO;
import org.example.dto.ApiResponse;
import org.example.dto.UniversalDTO;
import org.example.enumConstant.ServiceNames;
import org.example.model.MobileUser;
import org.example.model.PasswordReset;
import org.example.session.RedisSessionService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LoginService {

    @Resource
    private LoginDAO loginDAO;

    @Resource
    private MobileUserDAO mobileUserDAO;

    @Resource
    private PasswordResetDAO passwordResetDAO;

    @Resource
    private RedisSessionService redisSessionService;

    @Resource
    private PasswordResetTokenService passwordResetTokenService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Process login request using UniversalDTO
     */
    public ApiResponse<UniversalDTO> processLogin(UniversalDTO dto) {
        try {
            String muid = dto.getMobileUserId();
            String password = dto.getPassword();

            if (muid == null || password == null) {
                return ApiResponse.badRequest("Username and password are required");
            }

            MobileUser user = mobileUserDAO.findByMobileUserId(muid);
            if (user == null) {
                return ApiResponse.unauthorized("This username is not registered. Please sign up to login.");
            }

            // First, check if there's an active temporary password (password reset request)
            PasswordReset activeReset = passwordResetDAO.findActiveByMobileUserId(muid);

            // If there's an active temporary password, ONLY allow login with that
            if (activeReset != null) {
                // Check if temporary password is expired
                if (activeReset.getExpiresAt() != null && activeReset.getExpiresAt().isBefore(LocalDateTime.now())) {
                    activeReset.setStatus("EXPIRED");
                    passwordResetDAO.update(activeReset);
                    return ApiResponse.badRequest("Temporary password has expired. Please request a new one.");
                }

                // Verify the temporary password
                if (!passwordEncoder.matches(password, activeReset.getPin())) {
                    return ApiResponse.unauthorized("Invalid temporary password.");
                }

                // Check if this is for first-time signup or forgot password
                boolean hasPermanentPassword = user.getPin() != null && !user.getPin().isEmpty();

                if (hasPermanentPassword) {
                    // This is a forgot password scenario (user already had a password)
                    // Allow them to set a new password
                    dto.setIsFirstTimeLogin(false); // Not first time, just forgot password
                    dto.addAttribute("isForgotPassword", true);
                    dto.addAttribute("mobileUserId", user.getMobileUserId());
                    dto.addAttribute("mobileUserName", user.getMobileUserName());
                    dto.addAttribute("email", user.getEmail());
                    return ApiResponse.success(dto, "Forgot password. Please set a new password.");
                } else {
                    // First time login (no permanent password yet)
                    dto.setIsFirstTimeLogin(true);
                    dto.addAttribute("mobileUserId", user.getMobileUserId());
                    dto.addAttribute("mobileUserName", user.getMobileUserName());
                    dto.addAttribute("email", user.getEmail());
                    return ApiResponse.success(dto, "First time login. Please set your password.");
                }
            }

            // No active temporary password - normal login flow
            String storedPasswordHash = user.getPin();

            // User must have a permanent password at this point
            if (storedPasswordHash == null || storedPasswordHash.isEmpty()) {
                return ApiResponse.badRequest("Account not properly set up. Please request a new password.");
            }

            // Verify permanent password
            if (!passwordEncoder.matches(password, storedPasswordHash)) {
                return ApiResponse.unauthorized("Invalid credentials. Wrong password.");
            }

            // Regular login - create session
            String sessionId = redisSessionService.create(user);
            dto.addAttribute("redisSessionId", sessionId);
            return ApiResponse.success(dto, "Login successful");

        } catch (Exception e) {
            return ApiResponse.internalServerError("Login failed: " + e.getMessage());
        }
    }

    /**
     * Retrieve the currently logged-in user based on session ID.
     */
    public MobileUser me(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return null;
        }
        return redisSessionService.get(sessionId);
    }

    /**
     * Invalidate the current session by sessionId string.
     */
    public void logout(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return;
        }
        redisSessionService.delete(sessionId);
    }

    /**
     * Invalidate the current session using UniversalDTO (support router header path).
     */
    public void logout(UniversalDTO request) {
        if (request == null) {
            return;
        }
        Object idFromAttr = request.getAttribute("sessionId");
        if (idFromAttr == null) {
            idFromAttr = request.getAttribute("x-session-id");
        }
        if (idFromAttr instanceof String sessionId) {
            logout(sessionId);
        }
    }
}
