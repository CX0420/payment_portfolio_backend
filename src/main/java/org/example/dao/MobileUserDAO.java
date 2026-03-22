package org.example.dao;

import org.example.model.MobileUser;

import java.util.List;
import java.util.Optional;

public interface MobileUserDAO {
    public List<MobileUser> getMobileUserList();

    boolean existsByMobileUserId(String mobileUserId);

    MobileUser create(MobileUser mobileUser);

    MobileUser findByMobileUserId(String mobileUserId);

    MobileUser findById(Long id);

    void update(MobileUser mobileUser);

    void delete(Long id);
}
