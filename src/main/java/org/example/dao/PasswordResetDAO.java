package org.example.dao;

import org.example.model.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetDAO {
    PasswordReset findActiveByMobileUserId(String mobileUserId);

     void create(PasswordReset reset);

     void update(PasswordReset reset);
}
