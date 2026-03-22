package org.example.dao;

import org.example.model.MobileUser;

public interface LoginDAO {
    /**
     * Check if a mobile user ID already exists
     */
    boolean existsByMobileUserId(String mobileUserId);

    /**
     * Check if an email already exists
     */
    boolean existsByEmail(String email);

    /**
     * Create a new mobile user (for signup)
     */
    MobileUser create(MobileUser user);

    /**
     * Update password/pin for a user
     */
    void updatePassword(String mobileUserId, String hashedPassword);

    String getPasswordByMobileUserId(String mobileUserId);

    public MobileUser findByMobileUserId(String mobileUserId);
}
