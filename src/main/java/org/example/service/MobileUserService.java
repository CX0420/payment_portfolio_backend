package org.example.service;

import jakarta.annotation.Resource;
import org.example.dao.MIDDAO;
import org.example.dao.MobileUserDAO;
import org.example.dao.PasswordResetDAO;
import org.example.dao.TIDDAO;
import org.example.dto.ApiResponse;
import org.example.dto.UniversalDTO;
import org.example.model.*;
import org.example.session.RedisSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MobileUserService {

    @Autowired
    MobileUserDAO mobileUserDAO;

    @Autowired
    PasswordResetDAO passwordResetDAO;

    @Autowired
    MIDDAO midDAO;

    @Autowired
    TIDDAO tidDAO;

    @Autowired
    EmailService emailService;

    @Autowired
    MIDService midService;

    @Autowired
    TIDService tidService;

    @Autowired
    MobileUserSettingService mobileUserSettingService;

    @Resource
    private RedisSessionService redisSessionService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<UniversalDTO> processSignup(UniversalDTO dto) {
        try {

            // Validate input
            if (dto.getMobileUserId() == null || dto.getEmail() == null ||
                    dto.getMobileUserName() == null ||
                    dto.getMid() == null || dto.getTid() == null) {
                return ApiResponse.badRequest("All fields are required for signup");
            }

            if (mobileUserDAO.existsByMobileUserId(dto.getMobileUserId())) {
                return ApiResponse.badRequest("Mobile User ID already exists");
            }

            MobileUserSetting mobileUserSetting = new MobileUserSetting();
            mobileUserSetting.setCreatedBy("SYSTEM");
            mobileUserSetting.setIsDarkModeOn(false);
            mobileUserSetting.setIsNotificationOn(false);
            mobileUserSetting.setSelectedLanguage("EN");
            mobileUserSetting = mobileUserSettingService.create(mobileUserSetting);

            // Create new user
            MobileUser newUser = new MobileUser();
            newUser.setMobileUserId(dto.getMobileUserId());
            newUser.setMobileUserName(dto.getMobileUserName());
            newUser.setEmail(dto.getEmail());
            newUser.setPin(null);
            newUser.setWrongPasswordCount(0);
            newUser.setStatus("ACTIVE");
            newUser.setCreatedBy("SYSTEM");
            newUser.setMobileUserSetting(mobileUserSetting);
            newUser = mobileUserDAO.create(newUser);


            String validatedMid = midService.validateAndGetMid(dto.getMid());
            if(midDAO.existsByMid(validatedMid)){
                return ApiResponse.badRequest("MID already exists");
            }

            MID newMID = new MID();
            newMID.setMid(validatedMid);
            newMID.setCreatedBy(dto.getMobileUserId());
            newMID.setIsMastercardEnabled(true);
            newMID.setIsVisaEnabled(true);
            newMID.setIsTngEnabled(true);
            newMID.setIsDuitnowEnabled(true);
            newMID.setIsBoostEnabled(true);
            newMID.setIsGrabEnabled(true);
            newMID.setCreatedBy("SYSTEM");
            newMID = midDAO.create(newMID);

            String validatedTid = tidService.validateAndGetTid(dto.getTid());
            if(tidDAO.existsByTid(validatedTid)){
                return ApiResponse.badRequest("TID already exists");
            }

            TID newTid = new TID();
            newTid.setTid(validatedTid);
            newTid.setCreatedBy(dto.getMobileUserId());
            newTid.setMobileUser(newUser);
            newTid.setMid(newMID);
            newTid.setTrace(0L);
            newTid.setStan(0L);
            newTid.setCreatedBy("SYSTEM");
            tidDAO.create(newTid);

            // Retrieve created user
            MobileUser user = mobileUserDAO.findByMobileUserId(dto.getMobileUserId());
            if (user == null) {
                return ApiResponse.internalServerError("Failed to create user");
            }

            // Generate random 6-digit PIN
            String randomPin = generateRandomPin();

            // Create password reset entry
            PasswordReset reset = new PasswordReset();
            reset.setPin(passwordEncoder.encode(randomPin)); // Hash the PIN
            reset.setMobileUser(user);
            reset.setMobileUserId(user.getMobileUserId());
            reset.setStatus("ACTIVE");
            reset.setCreatedBy("SYSTEM");
            reset.setExpiresAt(LocalDateTime.now().plusHours(24));
            reset.setUsedAt(null);

            // Save to database
            passwordResetDAO.create(reset);

            emailService.sendSimpleEmail(user.getEmail(),
                    "Welcome to Payment Portfolio",
                    "Hello " + user.getMobileUserName() + ",\n\n" +
                            "Welcome to Payment Portfolio! Your account has been successfully created.\n\n" +
                            "Your Mobile User ID: " + user.getMobileUserId() + "\n" +
                            "Your Email: " + user.getEmail() + "\n" +
                            "Your Temporary Pin: " + randomPin + "\n\n" +
                            "Please login and update your password within 24 hours.\n\n" +
                            "If you have any questions, please contact our support team.\n\n" +
                            "Best regards,\nPayment Portfolio Team");

            return ApiResponse.success(dto, "User created successfully", 201);

        } catch (Exception e) {
            return ApiResponse.internalServerError("Signup failed: " + e.getMessage());
        }
    }

    private static String generateRandomPin() {
        SecureRandom random = new SecureRandom();
        int pin = 100000 + random.nextInt(900000); // 100000 to 999999
        return String.valueOf(pin);
    }

    public ApiResponse<UniversalDTO> processForgotPassword(UniversalDTO dto) {
        try {
            String muid = dto.getMobileUserId();
            if (muid == null) {
                return ApiResponse.badRequest("Username is required");
            }

            MobileUser user = mobileUserDAO.findByMobileUserId(muid);
            if (user == null) {
                return ApiResponse.badRequest("User not found");
            }

            // Generate random temporary password
            String temporaryPassword = generateRandomPin(); // 6-digit PIN or random string

            // Create or update password reset record
            PasswordReset reset = new PasswordReset();
            reset.setPin(passwordEncoder.encode(temporaryPassword));
            reset.setMobileUser(user);
            reset.setMobileUserId(user.getMobileUserId());
            reset.setStatus("ACTIVE");
            reset.setCreatedBy("SYSTEM");
            reset.setExpiresAt(LocalDateTime.now().plusHours(24)); // 24 hours expiry
            reset.setUsedAt(null);

            passwordResetDAO.create(reset);

            // Send email with temporary password
            emailService.sendSimpleEmail(user.getEmail(),
                    "Your Temporary Password",
                    "Hello " + user.getMobileUserName() + ",\n\n" +
                            "You requested a password reset. Here is your temporary password:\n\n" +
                            "Temporary Password: " + temporaryPassword + "\n\n" +
                            "Please login with this temporary password. You will be prompted to set a new password.\n\n" +
                            "This temporary password will expire in 24 hours.\n\n" +
                            "If you did not request this, please ignore this email.\n\n" +
                            "Best regards,\nPayment Portfolio Team");

            return ApiResponse.success(dto, "Temporary password sent to your email");

        } catch (Exception e) {
            return ApiResponse.internalServerError("Forgot password failed: " + e.getMessage());
        }
    }

     /**
     * Process reset password request using UniversalDTO
     */
     public ApiResponse<UniversalDTO> processResetPassword(UniversalDTO request) {
         try {
             String muid = request.getMobileUserId();
             String newPassword = request.getNewPassword();

             if (muid == null || newPassword == null) {
                 return ApiResponse.badRequest("User ID and new password are required");
             }

             // Validate new password strength (optional)
             if (newPassword.length() < 6) {
                 return ApiResponse.badRequest("Password must be at least 6 characters long");
             }

             MobileUser user = mobileUserDAO.findByMobileUserId(muid);
             if (user == null) {
                 return ApiResponse.badRequest("User not found");
             }

             // Find the active password reset record (temporary password)
             PasswordReset reset = passwordResetDAO.findActiveByMobileUserId(muid);

             if (reset == null) {
                 return ApiResponse.badRequest("No active password reset request found. Please request a new one.");
             }

             // Check if the temporary password is expired
             if (reset.getExpiresAt() != null && reset.getExpiresAt().isBefore(LocalDateTime.now())) {
                 reset.setStatus("EXPIRED");
                 passwordResetDAO.update(reset);
                 return ApiResponse.badRequest("Temporary password has expired. Please request a new one.");
             }

             // Hash the new password
             String hashedNewPassword = passwordEncoder.encode(newPassword);

             // Update user's password
             user.setPin(hashedNewPassword);
             mobileUserDAO.update(user);

             // Mark the temporary password as used
             reset.setStatus("USED");
             reset.setUsedAt(LocalDateTime.now());
             passwordResetDAO.update(reset);

             // Auto login with new password - create new session
             String newSessionId = redisSessionService.create(user);

             // Prepare response
             UniversalDTO response = new UniversalDTO();
             response.setMobileUserId(user.getMobileUserId());
             response.setMobileUserName(user.getMobileUserName());
             response.setEmail(user.getEmail());
             response.addAttribute("redisSessionId", newSessionId);
             response.addAttribute("passwordChanged", true);
             response.addAttribute("isFirstTimeLogin", false);

             return ApiResponse.success(response, "Password reset successful. You are now logged in with your new password.");

         } catch (Exception e) {
             return ApiResponse.internalServerError("Reset password failed: " + e.getMessage());
         }
     }


}